package com.fleshgrinder.gradle.exe.artifacts

import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Platform
import java.io.File
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.tasks.TaskDependency

public class ExeArtifact(
    name: String,
    @JvmField private val file: File,
    platform: Platform,
    env: Env?,
) : AbstractExeArtifact(name, platform, env) {
    override fun getFile(): File = file
    override fun getBuildDependencies(): TaskDependency = DefaultTaskDependency()
}
