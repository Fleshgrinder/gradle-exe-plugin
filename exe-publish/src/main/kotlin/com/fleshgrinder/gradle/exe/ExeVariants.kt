package com.fleshgrinder.gradle.exe

import com.fleshgrinder.extensions.kotlin.toUpperCamelCase
import com.fleshgrinder.gradle.exe.artifacts.AbstractExeArtifact
import com.fleshgrinder.gradle.exe.artifacts.ExeArtifact
import com.fleshgrinder.gradle.exe.artifacts.ExeArtifactProvider
import com.fleshgrinder.platform.Platform
import java.io.File
import java.net.URI
import java.net.URL
import org.gradle.api.Project
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.register

@ExeDsl
public class ExeVariants(private val project: Project, private val component: AdhocComponentWithVariants) {
    public val filesDir: DirectoryProperty = project.objects.directoryProperty().convention(project.layout.projectDirectory)

    public val allowInsecureDownloads: Property<Boolean> = project.objects.property<Boolean>().convention(false)
    // TODO url builder?!?
    // TODO download authentication!?!

    public val redistGroup: Property<String> = project.objects.property()
    public val redistName: Property<String> = project.objects.property<String>().convention(project.name)
    public val redistConfiguration: Property<String> = project.objects.property()
    public val redistExt: Property<String> = project.objects.property<String>().convention("exe")
    public val redistVersion: Property<String> = project.objects.property<String>().convention(project.provider { project.version.toString() })

    public fun add(variant: AbstractExeArtifact) {
        val configuration = project.configurations.create(variant.id) {
            isCanBeConsumed = true
            isCanBeResolved = false
            artifacts.add(variant)
            with(attributes) {
                attribute(PLATFORM_ATTRIBUTE, variant.platform.toString())
            }
        }

        component.addVariantsFromConfiguration(configuration) {
            mapToMavenScope("runtime")
        }
    }

    public fun download(url: Any, platform: Platform) {
        download(URI(url.toString()), platform)
    }

    public fun download(url: URL, platform: Platform) {
        download(url.toURI(), platform)
    }

    public fun download(uri: URI, platform: Platform) {
        val taskName = buildString {
            append("download")
            append(platform.toString().toUpperCamelCase())
            append("Exe")
        }

        val task = project.tasks.register<ExeDownload>(taskName) {
            this.uri.set(uri)
            this.name.set(project.name)
            this.platform.set(platform)
        }

        file(task.map { it.exe.get().asFile }, platform)
    }

    public fun file(file: Any, platform: Platform) {
        file(filesDir.file(file.toString()), platform)
    }

    public fun file(file: File, platform: Platform) {
        add(ExeArtifact(project.name, file, platform))
    }

    public fun file(file: Provider<File>, platform: Platform) {
        add(ExeArtifactProvider(project.name, file, platform))
    }

    @JvmName("regularFile")
    public fun file(file: RegularFile, platform: Platform) {
        file(file.asFile, platform)
    }

    @JvmName("regularFile")
    public fun file(file: Provider<RegularFile>, platform: Platform) {
        file(file.map { it.asFile }, platform)
    }
}
