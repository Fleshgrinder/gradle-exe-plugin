package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Platform
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.property

/**
 * The redistribution extension makes it trivial to publish native executables
 * that are already available in a repository to another repository. This can be
 * useful for adding the required metadata for Gradle.
 */
@ExeDsl
public abstract class ExeRedistributionExtension @Inject constructor(
    @JvmField private val project: Project,
    @JvmField private val variants: ExeVariants,
) : ExtensionAware {
    /**
     * Sets the group of the artifact that should be redistributed.
     *
     * This is the only property that must be set and has no default value.
     * Settings this to the group of the project makes little sense because it
     * would mean that the current exe is redistributed to the same coordinates
     * as the current exe.
     */
    public val group: Property<String> = project.objects.property()

    /**
     * Sets the name of the artifact that should be redistributed.
     *
     * Defaults to the [Project.getName].
     */
    public val name: Property<String> = project.objects.property<String>()
        .convention(project.name)

    /**
     * Sets the configuration of the artifact that should be redistributed.
     *
     * Optional and not set by default.
     */
    public val configuration: Property<String> = project.objects.property()

    /**
     * Sets the extension of the artifact that should be redistributed.
     *
     * Defaults to `exe`.
     */
    public val ext: Property<String> = project.objects.property<String>()
        .value("exe")

    /**
     * Sets the version of the artifact that should be redistributed.
     *
     * Defaults to [Project.getVersion].
     */
    public val version: Property<String> = project.objects.property<String>()
        .convention(project.provider { project.version.toString() })

    private val config = project.configurations.register("exeRedistribution") {
        isCanBeConsumed = false
        isCanBeResolved = true
        isTransitive = false
        isVisible = false
    }

    @JvmOverloads
    public fun artifact(
        classifier: String,
        platform: Platform = Platform.parse(classifier),
        env: Env? = null,
    ) {
        project.dependencies {
            add(config.name, create(group.get(), name.get(), version.get(), configuration.orNull, classifier, ext.orNull))
        }

        val file = config.map { config ->
            val filename = "${name.get()}-${version.get()}-$classifier.${ext.get()}"
            config.files.single { it.name == filename }
        }

        variants.add(file, platform, env)
    }
}
