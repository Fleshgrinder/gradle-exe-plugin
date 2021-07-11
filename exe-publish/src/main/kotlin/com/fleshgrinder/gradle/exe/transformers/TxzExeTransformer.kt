package com.fleshgrinder.gradle.exe.transformers

import java.io.InputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream
import org.gradle.api.specs.Spec

public class TxzExeTransformer(predicate: Spec<String>) : TarExeTransformer(predicate) {
    override fun wrap(source: InputStream): ArchiveInputStream =
        super.wrap(XZCompressorInputStream(source))
}
