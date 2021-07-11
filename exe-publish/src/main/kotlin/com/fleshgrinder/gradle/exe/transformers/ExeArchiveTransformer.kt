package com.fleshgrinder.gradle.exe.transformers

import com.fleshgrinder.gradle.exe.ExeTransformer
import java.io.File
import java.io.InputStream
import java.nio.channels.Channels
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.gradle.api.InvalidUserDataException
import org.gradle.api.specs.Spec
import org.gradle.internal.resource.ExternalResource

/**
 * Base class for [ArchiveInputStream] based [ExeTransformer]s that implements
 * the logic that is the same for all of them regardless of the used algorithm.
 */
public abstract class ExeArchiveTransformer(private val spec: Spec<String>) : ExeTransformer {
    override fun transform(source: ExternalResource, target: File) {
        source.withContent {
            val archive = wrap(this)
            val entries = mutableSetOf<String>()
            var found = false
            var multiple = false

            generateSequence { archive.nextEntry }.forEach { entry ->
                if (!entry.isDirectory) {
                    entries.add(entry.name)
                    if (!multiple) {
                        if (found) {
                            multiple = true
                        } else if (spec.isSatisfiedBy(entry.name)) {
                            found = true
                            target.outputStream().channel.use { target ->
                                target.lock().use { target.transferFrom(Channels.newChannel(archive), 0, entry.size) }
                            }
                        }
                    }
                }
            }

            if (!found || multiple) {
                throw InvalidUserDataException(
                    buildString {
                        append(if (multiple) "Found multiple archive entries" else "Could not find any entry")
                        append(" for the given spec, the archive contains the following entries:\n")
                        entries.forEach { append("- ").append(it).append('\n') }
                        append("\n(Archive: ").append(source.uri).append(")\n")
                    },
                )
            }
        }
    }

    protected abstract fun wrap(source: InputStream): ArchiveInputStream
}
