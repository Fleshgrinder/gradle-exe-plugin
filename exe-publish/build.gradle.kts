plugins {
    id("exe-gradle-plugin")
}

dependencies {
    implementation("com.fleshgrinder.kotlin:case-format:0.2.0")
    implementation("org.tukaani:xz:1.9")
    api("org.apache.commons:commons-compress:1.20")
    api(projects.exeBase)
}
