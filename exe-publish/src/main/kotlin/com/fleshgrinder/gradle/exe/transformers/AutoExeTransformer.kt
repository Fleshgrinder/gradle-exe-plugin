package com.fleshgrinder.gradle.exe.transformers

import com.fleshgrinder.gradle.exe.ExeTransformer
import java.io.File
import org.gradle.api.specs.Spec
import org.gradle.internal.resource.ExternalResource

public class AutoExeTransformer(private val spec: Spec<String>) : ExeTransformer {
    public constructor(name: String) : this(ExeArchiveEntrySpec(name))

    override fun transform(source: ExternalResource, target: File) {
        val it = source.uri.path.substringAfterLast('/')
        when {
            it ext ".tar" -> TarExeTransformer(spec)
            it ext ".tbz" || it ext ".tbz2" || it ext ".tar.bz2" -> TbzExeTransformer(spec)
            it ext ".tgz" || it ext ".tar.gz" || it ext ".tar.gzip" -> TgzExeTransformer(spec)
            it ext ".txz" || it ext ".tar.xz" -> TxzExeTransformer(spec)
            it ext ".zip" -> ZipExeTransformer(spec)
            else -> PassThroughExeTransformer()
        }.transform(source, target)
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private infix fun String.ext(suffix: String): Boolean =
        (this as java.lang.String).endsWith(suffix)

    private class ExeArchiveEntrySpec(private val name: String) : Spec<String> {
        private val exe = "$name.exe"

        override fun isSatisfiedBy(it: String): Boolean =
            if (it == name || it == exe) true else {
                val stem = it.substringAfterLast('/')
                stem == name || stem == exe
            }.also { result ->
                println("matching '$it' ('${it.substringAfterLast('/')}') against '$name' and '$exe', result: $result")
            }
    }
}
