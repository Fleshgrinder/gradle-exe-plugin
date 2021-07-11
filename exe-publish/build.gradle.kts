import com.fleshgrinder.extensions.kotlin.toLowerCamelCase
import com.fleshgrinder.extensions.kotlin.toUpperCamelCase

plugins {
    `kotlin-dsl`
    `maven-publish`
}

buildscript {
    dependencies.classpath("com.fleshgrinder.kotlin:case-format:0.2.0")
    repositories.mavenCentral()
}

kotlin.explicitApi()
repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.fleshgrinder.kotlin:case-format:0.2.0")
    implementation("org.tukaani:xz:1.9")
    api("org.apache.commons:commons-compress:1.20")
    api(projects.exeBase)
}

gradlePlugin.plugins.register(project.name.toLowerCamelCase()) {
    id = "${project.group}.${project.name}"
    displayName = project.name
    description = project.description
    implementationClass = "${project.group}.${rootProject.name.substringAfter('-').substringBeforeLast('-').toLowerCamelCase()}.${project.name.toUpperCamelCase()}Plugin"
}