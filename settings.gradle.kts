pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "gradle-exe-plugin"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "exe-base",
    "exe",
    "exe-publish",
    "exe-maven-publish",
)
