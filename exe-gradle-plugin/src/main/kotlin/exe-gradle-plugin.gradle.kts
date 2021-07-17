import com.fleshgrinder.extensions.kotlin.toLowerCamelCase
import com.fleshgrinder.extensions.kotlin.toUpperCamelCase
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.kotlin.kotlin-dsl")

    id("jacoco")

    id("maven-publish")
}

dependencies {
    implementation(gradleKotlinDsl())

    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(gradleTestKit())
    testImplementation(platform("io.strikt:strikt-bom:0.31.0"))
    testImplementation("io.strikt:strikt-core")
    testImplementation("io.strikt:strikt-jvm")
    testImplementation("io.strikt:strikt-gradle")
}

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/") { mavenContent { snapshotsOnly() } }
    mavenLocal()
}

gradlePlugin.plugins.register(project.name.toLowerCamelCase()) {
    id = "${project.group.toString().substringBeforeLast('.')}.${project.name}"
    displayName = project.name
    description = project.description
    implementationClass = "${project.group}.${project.name.toUpperCamelCase()}Plugin"
}

tasks {
    jacocoTestReport.configure {
        shouldRunAfter(test)
        reports {
            xml.required.set(System.getenv().containsKey("CI"))
        }
    }

    check.configure {
        dependsOn(jacocoTestReport)
    }

    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
        systemProperties(
            "junit.jupiter.execution.parallel.enabled" to "true",
            "junit.jupiter.execution.parallel.config.strategy" to "dynamic",
            "junit.jupiter.execution.parallel.config.dynamic.factor" to "2",
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
            "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent",
            "junit.jupiter.testinstance.lifecycle.default" to "per_class",
            "java.io.tmpdir" to temporaryDir.absolutePath,
        )
    }
}
