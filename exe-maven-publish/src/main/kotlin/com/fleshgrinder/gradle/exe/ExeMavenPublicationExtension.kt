package com.fleshgrinder.gradle.exe

import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication

/**
 * Extension for further customization of the [MavenPublication] so that users
 * do not have to wire things together manually.
 */
@ExeDsl
public abstract class ExeMavenPublicationExtension @Inject constructor(
    @JvmField private val publication: NamedDomainObjectProvider<MavenPublication>,
) : ExtensionAware {
    /**
     * Configures the POM of the publication.
     *
     * @see MavenPublication.pom
     */
    public fun pom(action: Action<in MavenPom>) {
        publication.configure { action.execute(pom) }
    }
}
