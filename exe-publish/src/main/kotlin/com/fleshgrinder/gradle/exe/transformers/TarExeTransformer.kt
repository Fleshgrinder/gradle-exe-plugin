package com.fleshgrinder.gradle.exe.transformers

import java.io.InputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.gradle.api.specs.Spec

public open class TarExeTransformer(predicate: Spec<String>) : ExeArchiveTransformer(predicate) {
    override fun wrap(source: InputStream): ArchiveInputStream =
        TarArchiveInputStream(source)
}
