pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "gradle-exe-plugin"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("exe-gradle-plugin")
include("exe-base", "exe", "exe-publish", "exe-maven-publish")
