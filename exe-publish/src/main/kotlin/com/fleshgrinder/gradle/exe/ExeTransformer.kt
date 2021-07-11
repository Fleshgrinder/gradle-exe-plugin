package com.fleshgrinder.gradle.exe

import java.io.File
import org.gradle.internal.resource.ExternalResource

/**
 * The [ExeTransformer] is used by the [ExeDownload] task for transforming a
 * download stream into a local exe file.
 */
public fun interface ExeTransformer {
    /**
     * Transforms the [source] to the [target] applying any arbitrary logic that
     * is required to turn the [source] into an exe file.
     */
    public fun transform(source: ExternalResource, target: File)
}
