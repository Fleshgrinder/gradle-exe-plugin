package com.fleshgrinder.gradle.exe.transformers

import java.io.InputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.gradle.api.specs.Spec

public class ZipExeTransformer(predicate: Spec<String>) : ExeArchiveTransformer(predicate) {
    override fun wrap(source: InputStream): ArchiveInputStream =
        ZipArchiveInputStream(source)
}
