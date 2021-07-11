package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Platform
import java.io.File
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider

@ExeDsl
public abstract class ExeVariantHandler @Inject constructor(
    @JvmField private val project: Project,
    @JvmField private val variants: ExeVariants,
    @JvmField private val platform: Platform,
) : ExtensionAware {
    // We have to use a member here because the dependency injection system of
    // Gradle cannot deal with nullable types.
    @JvmField internal var env: Env? = null

    /**
     * Adds the variant with the configured platform to the variants of this
     * exe, the [path] is resolved against the project directory.
     */
    public fun from(path: String) {
        from(project.layout.projectDirectory.dir(path).asFile)
    }

    /**
     * Adds the variant with the configured platform to the variants of this
     * exe.
     */
    @JvmName("fromFile")
    public fun from(path: File) {
        variants.add(path, platform, env)
    }

    /**
     * Adds the variant with the configured platform to the variants of this
     * exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    @JvmName("fromFile")
    public fun from(path: Provider<File>) {
        variants.add(path, platform, env)
    }

    /**
     * Adds the variant with the configured platform to the variants of this
     * exe.
     */
    public fun from(path: RegularFile) {
        variants.add(path, platform, env)
    }

    /**
     * Adds the variant with the configured platform to the variants of this
     * exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    public fun from(path: Provider<RegularFile>) {
        variants.add(path, platform, env)
    }
}
