package com.fleshgrinder.gradle.exe

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.newInstance

/**
 * The [ExeBasePlugin] adds an empty [ExeExtension] to this project that can be
 * extended by the other plugins.
 */
public class ExeBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.add("exe", project.objects.newInstance<ExeExtension>())
    }
}
