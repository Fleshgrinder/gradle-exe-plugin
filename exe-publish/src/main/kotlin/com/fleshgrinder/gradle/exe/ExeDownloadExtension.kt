package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Platform
import java.util.function.Function
import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

/**
 * The [ExeDownloadExtension] configures common settings for the [ExeDownload]
 * task that is created by [ExeVariants.download] functions to keep things DRY.
 */
@ExeDsl
public abstract class ExeDownloadExtension @Inject constructor(
    objects: ObjectFactory,
) : ExtensionAware {
    // TODO authentication

    /**
     * @see ExeDownload.allowInsecureProtocols
     */
    public val allowInsecureProtocols: Property<Boolean> = objects.property<Boolean>().convention(false)

    private var urlBuilder: (UrlSpec.() -> String)? = null

    /**
     * @see ExeDownload.uri
     */
    public fun url(builder: Function<UrlSpec, String>) {
        urlBuilder = builder::apply
    }

    /**
     * @see ExeDownload.uri
     */
    public fun url(builder: UrlSpec.() -> String) {
        urlBuilder = builder
    }

    @ExeDsl
    public class UrlSpec internal constructor(
        public val name: String,
        public val version: String,
        public val platform: Platform,
        public val arg: String?,
    )
}
