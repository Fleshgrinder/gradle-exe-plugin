package com.fleshgrinder.gradle.exe.transformers

import java.io.InputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.gradle.api.specs.Spec

public class TbzExeTransformer(predicate: Spec<String>) : TarExeTransformer(predicate) {
    override fun wrap(source: InputStream): ArchiveInputStream =
        super.wrap(BZip2CompressorInputStream(source))
}
