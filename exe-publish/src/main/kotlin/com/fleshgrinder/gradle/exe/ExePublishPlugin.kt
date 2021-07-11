package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Arch
import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Os
import com.fleshgrinder.platform.Platform
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.newInstance

public class ExePublishPlugin @Inject constructor(
    @JvmField private val componentFactory: SoftwareComponentFactory,
) : Plugin<Project> {
    override fun apply(project: Project) {
        val objects = project.objects

        project.pluginManager.apply(ExeBasePlugin::class)

        val component = componentFactory.adhoc("exe").also(project.components::add)
        val variants = ExeVariants(project, component)

        val publish = objects.newInstance<ExeExtension>()
        publish.extensions.add("variants", variants)

        val redistribute = objects.newInstance<ExeRedistributionExtension>(project, variants)

        for (os in Os.values()) {
            if (os == Os.UNKNOWN) continue
            val osExt = objects.newInstance<ExeExtension>()
            for (arch in Arch.values()) {
                if (arch.isUnknown) continue
                val platform = Platform(os, arch)
                val archExt = objects.newInstance<ExeVariantHandler>(project, variants, platform)
                for (env in Env.values()) {
                    if (env == Env.UNKNOWN) continue
                    val envExt = objects.newInstance<ExeVariantHandler>(project, variants, platform)
                    envExt.env = env
                    archExt.extensions.add(env.id, envExt)
                }
                osExt.extensions.add(arch.id.replace('-', '_'), archExt)
            }
            publish.extensions.add(os.id, osExt)
        }

        project.extensions.configure<ExeExtension> {
            extensions.add("publish", publish)
            extensions.add("redistribute", redistribute)
        }
    }
}
