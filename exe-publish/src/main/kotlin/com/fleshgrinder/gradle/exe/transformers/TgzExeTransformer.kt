package com.fleshgrinder.gradle.exe.transformers

import java.io.InputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.gradle.api.specs.Spec

public class TgzExeTransformer(predicate: Spec<String>) : TarExeTransformer(predicate) {
    override fun wrap(source: InputStream): ArchiveInputStream =
        super.wrap(GzipCompressorInputStream(source))
}
