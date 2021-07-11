package com.fleshgrinder.gradle.exe

import com.fleshgrinder.extensions.kotlin.toUpperCamelCase
import com.fleshgrinder.gradle.exe.artifacts.AbstractExeArtifact
import com.fleshgrinder.gradle.exe.artifacts.ExeArtifact
import com.fleshgrinder.gradle.exe.artifacts.ExeArtifactProvider
import com.fleshgrinder.platform.Arch
import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Os
import com.fleshgrinder.platform.Platform
import java.io.File
import java.net.URI
import org.gradle.api.Project
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.register

/**
 * Used to collect all variants of a native executable that are going to be
 * published.
 */
@ExeDsl
public class ExeVariants(
    @JvmField private val project: Project,
    @JvmField private val component: AdhocComponentWithVariants,
) {
    /**
     * Adds a variant from the given [artifact] to the variants of this exe.
     */
    public fun add(artifact: AbstractExeArtifact) {
        val configuration = project.configurations.create(artifact.id) {
            isCanBeConsumed = true
            isCanBeResolved = false
            artifacts.add(artifact)
            with(attributes) {
                attribute(PLATFORM_ATTRIBUTE, artifact.platform.id)
                artifact.env?.let { attribute(ENV_ATTRIBUTE, it.id) }
            }
        }

        component.addVariantsFromConfiguration(configuration) {
            mapToMavenScope("runtime")
        }
    }

    // region ------------------------------------------------------------------ File

    /**
     * Adds a variant for the given [platform] and optional [env] to the
     * variants of this exe.
     */
    @JvmName("addFile")
    @JvmOverloads
    public fun add(path: File, platform: Platform, env: Env? = null) {
        add(ExeArtifact(project.name, path, platform, env))
    }

    /**
     * Adds a variant for the given [os], [arch], and optional [env] to the
     * variants of this exe.
     */
    @JvmName("addFile")
    @JvmOverloads
    public fun add(path: File, os: Os, arch: Arch, env: Env? = null) {
        add(path, Platform(os, arch), env)
    }

    /**
     * Adds a variant with the [platform being parsed][Platform.parse] from the
     * path's name (stem) and the optional [env] to the variants of this exe.
     */
    @JvmName("addFile")
    @JvmOverloads
    public fun add(path: File, env: Env? = null) {
        add(path, Platform.parse(path.nameWithoutExtension), env)
    }

    /**
     * Adds a variant with the [platform][Platform.parse] and [env][Env.parse]
     * being parsed from the path's name (stem) to the variants of this exe.
     */
    @JvmName("addFileWithEnv")
    public fun addWithEnv(path: File) {
        val stem = path.nameWithoutExtension
        add(path, Platform.parse(stem), Env.parse(stem))
    }

    // endregion --------------------------------------------------------------- File
    // region ------------------------------------------------------------------ Provider<File>

    /**
     * Adds a variant for the given [platform] and optional [env] to the
     * variants of this exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    @JvmName("addFile")
    @JvmOverloads
    public fun add(path: Provider<File>, platform: Platform, env: Env? = null) {
        add(ExeArtifactProvider(project.name, path, platform, env))
    }

    /**
     * Adds a variant for the given [os], [arch], and optional [env] to the
     * variants of this exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    @JvmName("addFile")
    @JvmOverloads
    public fun add(path: Provider<File>, os: Os, arch: Arch, env: Env? = null) {
        add(ExeArtifactProvider(project.name, path, Platform(os, arch), env))
    }

    // endregion --------------------------------------------------------------- Provider<File>
    // region ------------------------------------------------------------------ RegularFile

    /**
     * Adds a variant for the given [platform] and optional [env] to the
     * variants of this exe.
     */
    @JvmOverloads
    public fun add(path: RegularFile, platform: Platform, env: Env? = null) {
        add(path.asFile, platform, env)
    }

    /**
     * Adds a variant for the given [os], [arch], and optional [env] to the
     * variants of this exe.
     */
    @JvmOverloads
    public fun add(path: RegularFile, os: Os, arch: Arch, env: Env? = null) {
        add(path.asFile, os, arch, env)
    }

    /**
     * Adds a variant with the [platform being parsed][Platform.parse] from the
     * path's name (stem) and the optional [env] to the variants of this exe.
     */
    @JvmOverloads
    public fun add(path: RegularFile, env: Env? = null) {
        add(path.asFile, env)
    }

    /**
     * Adds a variant with the [platform][Platform.parse] and [env][Env.parse]
     * being parsed from the path's name (stem) to the variants of this exe.
     */
    public fun addWithEnv(path: RegularFile) {
        add(path.asFile)
    }

    // endregion --------------------------------------------------------------- RegularFile
    // region ------------------------------------------------------------------ Provider<RegularFile>

    /**
     * Adds a variant for the given [os], [arch], and optional [env] to the
     * variants of this exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    @JvmOverloads
    public fun add(path: Provider<RegularFile>, os: Os, arch: Arch, env: Env? = null) {
        add(path.map { it.asFile }, os, arch, env)
    }

    /**
     * Adds a variant for the given [platform] and optional [env] to the
     * variants of this exe.
     *
     * If the given [path] provider is attached to a task then the task is
     * executed automatically when the exe is required.
     */
    @JvmOverloads
    public fun add(path: Provider<RegularFile>, platform: Platform, env: Env? = null) {
        add(path.map { it.asFile }, platform, env)
    }

    // endregion --------------------------------------------------------------- Provider<RegularFile>
    // region ------------------------------------------------------------------ URI
    /**
     * Adds a variant for the given [platform] and optional [env] to the
     * variants of this exe.
     *
     * An [ExeDownload] task is created to download the content from the given
     * [url].
     */
    @JvmOverloads
    public fun download(
        url: String,
        platform: Platform = Platform.parse(url.substringAfterLast('/')),
        env: Env? = null,
    ) {
        val taskName = buildString {
            append("download")
            append(platform.id.toUpperCamelCase())
            if (env != null) append(env.id.toUpperCamelCase())
            append("Exe")
        }

        val task = project.tasks.register<ExeDownload>(taskName) {
            this.uri.set(URI(url))
            this.name.set(project.name)
            this.platform.set(platform)
            if (env != null) this.env.set(env)
        }

        add(task.flatMap { it.exe }, platform, env)
    }
    // endregion --------------------------------------------------------------- URI
}
