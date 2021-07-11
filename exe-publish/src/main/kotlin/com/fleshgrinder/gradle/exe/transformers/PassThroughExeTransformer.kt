package com.fleshgrinder.gradle.exe.transformers

import com.fleshgrinder.gradle.exe.ExeTransformer
import java.io.File
import org.gradle.internal.resource.ExternalResource

public class PassThroughExeTransformer : ExeTransformer {
    override fun transform(source: ExternalResource, target: File) {
        source.writeTo(target)
    }
}
