package com.fleshgrinder.gradle.exe.artifacts

import com.fleshgrinder.platform.Platform
import java.util.Date
import org.gradle.api.internal.artifacts.PublishArtifactInternal

public abstract class AbstractExeArtifact(private val name: String, public val platform: Platform) : PublishArtifactInternal {
    public val id: String get() = "$name.$classifier.$extension"

    final override fun getClassifier(): String = platform.toString()
    final override fun getName(): String = name
    final override fun getExtension(): String = "exe"
    final override fun getType(): String = extension
    final override fun getDate(): Date? = null
    final override fun shouldBePublished(): Boolean = true
}
