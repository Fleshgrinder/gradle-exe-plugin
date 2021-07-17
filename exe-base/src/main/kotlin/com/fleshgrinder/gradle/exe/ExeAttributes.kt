@file:JvmName("ExeAttributes")

package com.fleshgrinder.gradle.exe

import com.fleshgrinder.platform.Arch
import com.fleshgrinder.platform.Os
import com.fleshgrinder.platform.Platform
import org.gradle.api.attributes.Attribute

/**
 * Gets the name of the exe plugin's [PLATFORM_ATTRIBUTE].
 */
public const val PLATFORM_NAME: String = "com.fleshgrinder.gradle.exe.platform"

/**
 * Gets the exe plugin's platform attribute.
 *
 * The platform attribute contains the [Os] and [Arch] a variant of the native
 * executable targets. This information is used by Gradle to automatically
 * select the correct executable for the current platform.
 *
 * @see Platform
 */
@JvmField public val PLATFORM_ATTRIBUTE: Attribute<String> = Attribute.of(PLATFORM_NAME, String::class.java)
