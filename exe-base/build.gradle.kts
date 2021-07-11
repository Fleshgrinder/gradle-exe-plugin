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
    api("com.fleshgrinder:jvm-platform:1.0.0-SNAPSHOT")
}

gradlePlugin.plugins.register(project.name.toLowerCamelCase()) {
    id = "${project.group}.${project.name}"
    displayName = project.name
    description = project.description
    implementationClass = "${project.group}.${rootProject.name.substringAfter('-').substringBeforeLast('-').toLowerCamelCase()}.${project.name.toUpperCamelCase()}Plugin"
}
