package com.fleshgrinder.gradle.exe

import org.gradle.api.plugins.ExtensionAware

/**
 * The [ExeExtension] interfaces is used by the exe plugins to add functionality
 * through extensions while the interface itself does not define any
 * functionality, however, it is annotated with the [ExeDsl] marker for better
 * control over the DSL that is provided through its extensions.
 */
@ExeDsl
public interface ExeExtension : ExtensionAware
