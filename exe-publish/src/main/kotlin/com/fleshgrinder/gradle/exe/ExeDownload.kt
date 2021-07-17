package com.fleshgrinder.gradle.exe

import com.fleshgrinder.gradle.exe.transformers.AutoExeTransformer
import com.fleshgrinder.platform.Platform
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption.ATOMIC_MOVE
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserCodeException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransport
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransportFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.*
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.*
import org.gradle.authentication.Authentication
import org.gradle.internal.resource.ExternalResource
import org.gradle.internal.resource.ExternalResourceName
import org.gradle.internal.resource.ResourceExceptions
import org.gradle.internal.verifier.HttpRedirectVerifierFactory
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

/**
 * Task optimized for downloading exe files with support for decompression and
 * unpacking as well as authentication.
 */
public open class ExeDownload @Inject constructor(
    layout: ProjectLayout,
    objects: ObjectFactory,
    providers: ProviderFactory,
    private val transportFactory: RepositoryTransportFactory,
) : DefaultTask() {
    /**
     * URI to download containing the exe file.
     *
     * The URI may point to the exe file itself or an, potentially compressed,
     * archive. The [AutoExeTransformer] is used to determine the appropriate
     * transformation to retrieve the exe file.
     */
    @get:Input
    // TODO accept only URL?
    public val uri: Property<URI> = objects.property()

    @get:Internal
    public val allowInsecureProtocols: Property<Boolean> = objects.property<Boolean>().convention(false)

    @get:Internal
    public val authentication: ListProperty<Authentication> = objects.listProperty()

    @get:Internal
    public val directory: DirectoryProperty = objects.directoryProperty()
        .convention(layout.buildDirectory.dir("exe"))

    @get:Internal
    public val name: Property<String> = objects.property<String>()
        .convention(uri.map { it.path.substringAfterLast('/').substringBefore('.') })

    @get:Internal
    public val platform: Property<Platform> = objects.property<Platform>()
        .convention(uri.map { Platform.parse(it.path.substringAfterLast('/')) })

    @get:Internal
    public var filter: Spec<String>? = null

    @get:Internal
    public var transformer: ExeTransformer? = null

    @get:Internal
    public val filename: Property<String> = objects.property<String>().convention(providers.provider { "${name.get()}-${platform.get()}.exe" })

    /**
     * Gets the downloaded exe file.
     */
    @get:OutputFile
    public val exe: Provider<RegularFile> = directory.zip(filename) { directory, filename ->
        directory.file(filename)
    }

    init {
        group = "exe"
    }

    @TaskAction
    public fun download() {
        val name = name.get()
        val uri = uri.get()

        uri.toRepositoryTransport(name)
            .repository
            .withProgressLogging()
            .resource(uri.toExternalResourceName(name))
            .download()
    }

    private fun URI.toRepositoryTransport(name: String): RepositoryTransport {
        val redirectVerifier = try {
            HttpRedirectVerifierFactory.create(
                URI(scheme, authority, null, null, null),
                allowInsecureProtocols.get(),
                { throw InvalidUserCodeException("Attempting to download $name exe from an insecure URI $this; 'allowInsecureProtocols' may be set to true to disable this check.") },
                { throw InvalidUserCodeException("Attempting to download $name exe from an insecure URI $it. This URI was reached as a redirect from $this; 'allowInsecureProtocols' may be set to true to disable this check.") },
            )
        } catch (e: URISyntaxException) {
            throw InvalidUserCodeException("Cannot extract host information from specified URI: $this")
        }

        return transportFactory.createTransport(
            scheme,
            "exe downloads",
            authentication.get(),
            redirectVerifier,
        )
    }

    private fun URI.toExternalResourceName(name: String): ExternalResourceName =
        object : ExternalResourceName(this) {
            override fun getShortDisplayName() = name
        }

    private fun ExternalResource.download() {
        val targetFile = exe.get().asFile
        val downloadFile = File(targetFile.absolutePath + ".part")

        try {
            try {
                (transformer ?: filter?.let { AutoExeTransformer(it) } ?: AutoExeTransformer(name.get())).transform(this, downloadFile)
            } catch (e: IOException) {
                throw ResourceExceptions.getFailed(uri, e)
            }

            try {
                try {
                    Files.move(downloadFile.toPath(), targetFile.toPath(), ATOMIC_MOVE)
                } catch (e: AtomicMoveNotSupportedException) {
                    Files.move(downloadFile.toPath(), targetFile.toPath(), REPLACE_EXISTING)
                }
            } catch (e: IOException) {
                throw GradleException("Unable to move downloaded exe '$downloadFile' to target destination '$targetFile'", e)
            }
        } finally {
            downloadFile.delete()
        }

        targetFile.setExecutable(true)
    }
}
