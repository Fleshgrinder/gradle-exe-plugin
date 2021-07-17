package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Arch
import com.fleshgrinder.platform.Os
import com.fleshgrinder.platform.Platform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.support.serviceOf

public class ExePublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val objects = project.objects

        project.pluginManager.apply(ExeBasePlugin::class)

        val component = project.serviceOf<SoftwareComponentFactory>().adhoc("exe").also(project.components::add)
        val variants = ExeVariants(project, component)

        val publish = objects.newInstance<ExePublishExtension>()
        publish.extensions.add("variants", variants)

        val downloads = objects.newInstance<ExeDownloadExtension>()
        val redistribute = objects.newInstance<ExeRedistributionExtension>(project, variants)

        for (os in Os.values()) {
            val osExt = objects.newInstance<ExeExtension>()
            for (arch in Arch.values()) {
                val platform = Platform(os, arch)
                val archExt = objects.newInstance<ExeVariantHandler>(project, variants, platform)
                osExt.extensions.add(arch.toString().replace('-', '_'), archExt)
            }
            publish.extensions.add(os.toString(), osExt)
        }

        project.extensions.configure<ExeExtension> {
            extensions.add("downloads", downloads)
            extensions.add("publish", publish)
            extensions.add("redistribute", redistribute)
        }
    }
}
