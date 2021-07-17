package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Platform
import java.io.File
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider

@ExeDsl
public abstract class ExeVariantHandler @Inject constructor(
    private val project: Project,
    private val variants: ExeVariants,
    private val platform: Platform,
) : ExtensionAware {
    public fun file(file: String) {
        variants.file(project.layout.projectDirectory.file(file).asFile, platform)
    }

    public fun file(file: File) {
        variants.file(file, platform)
    }

    public fun file(file: Provider<File>) {
        variants.file(file, platform)
    }

    @JvmName("regularFile")
    public fun file(file: RegularFile) {
        variants.file(file.asFile, platform)
    }

    @JvmName("regularFile")
    public fun file(file: Provider<RegularFile>) {
        variants.file(file.map { it.asFile }, platform)
    }
}
