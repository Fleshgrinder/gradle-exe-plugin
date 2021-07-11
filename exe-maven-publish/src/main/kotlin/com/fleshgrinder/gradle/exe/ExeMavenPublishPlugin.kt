package com.fleshgrinder.gradle.exe

import com.fleshgrinder.extensions.kotlin.toLowerCamelCase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.register

/**
 * Registers and preconfigures a [MavenPublication] for the exe software
 * component.
 */
public class ExeMavenPublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(ExePublishPlugin::class)
        project.pluginManager.apply(MavenPublishPlugin::class)

        project.extensions.configure<PublishingExtension> {
            val publication = publications.register<MavenPublication>("${project.name.toLowerCamelCase()}Exe") {
                from(project.components.getByName("exe"))
                suppressAllPomMetadataWarnings()
            }

            project.extensions.getByType<ExeExtension>().extensions.add(
                "publication",
                project.objects.newInstance<ExeMavenPublicationExtension>(publication),
            )
        }
    }
}
