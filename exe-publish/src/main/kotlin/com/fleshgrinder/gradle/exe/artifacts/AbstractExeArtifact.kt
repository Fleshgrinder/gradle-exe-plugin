package com.fleshgrinder.gradle.exe.artifacts

import com.fleshgrinder.platform.Env
import com.fleshgrinder.platform.Platform
import java.util.Date
import org.gradle.api.internal.artifacts.PublishArtifactInternal

public abstract class AbstractExeArtifact(
    @JvmField private val name: String,
    @JvmField public val platform: Platform,
    @JvmField public val env: Env?,
) : PublishArtifactInternal {
    private val classifier = buildString {
        append(platform)
        if (env != null) append('.').append(env)
    }

    public val id: String get() = "$name.$classifier.$extension"

    final override fun getClassifier(): String = classifier
    final override fun getName(): String = name
    final override fun getExtension(): String = "exe"
    final override fun getType(): String = extension
    final override fun getDate(): Date? = null
    final override fun shouldBePublished(): Boolean = true
}
