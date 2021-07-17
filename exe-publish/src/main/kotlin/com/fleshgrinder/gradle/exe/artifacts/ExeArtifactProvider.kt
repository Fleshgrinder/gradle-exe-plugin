package com.fleshgrinder.gradle.exe.artifacts

import com.fleshgrinder.platform.Platform
import java.io.File
import org.gradle.api.internal.tasks.AbstractTaskDependency
import org.gradle.api.internal.tasks.TaskDependencyResolveContext
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskDependency

/**
 * @see org.gradle.api.internal.artifacts.dsl.LazyPublishArtifact
 */
public class ExeArtifactProvider(
    name: String,
    private val fileProvider: Provider<File>,
    platform: Platform,
) : AbstractExeArtifact(name, platform) {
    override fun getFile(): File = fileProvider.get()
    override fun getBuildDependencies(): TaskDependency =
        object : AbstractTaskDependency() {
            override fun visitDependencies(context: TaskDependencyResolveContext) {
                context.add(fileProvider)
            }
        }
}
