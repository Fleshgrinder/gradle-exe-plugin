plugins {
    id("exe-gradle-plugin")
}

dependencies {
    api(projects.exePublish)
    implementation("com.fleshgrinder.kotlin:case-format:0.2.0")
}
