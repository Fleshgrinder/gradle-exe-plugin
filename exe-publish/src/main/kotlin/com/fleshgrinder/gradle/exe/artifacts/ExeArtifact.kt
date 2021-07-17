package com.fleshgrinder.gradle.exe.artifacts

import com.fleshgrinder.platform.Platform
import java.io.File
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.tasks.TaskDependency

public class ExeArtifact(
    name: String,
    private val file: File,
    platform: Platform,
) : AbstractExeArtifact(name, platform) {
    override fun getFile(): File = file
    override fun getBuildDependencies(): TaskDependency = DefaultTaskDependency()
}
