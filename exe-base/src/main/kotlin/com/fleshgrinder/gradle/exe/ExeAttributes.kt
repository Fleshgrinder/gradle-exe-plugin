@file:JvmName("ExeAttributes")

package com.fleshgrinder.gradle.exe

import org.gradle.api.attributes.Attribute

public const val PLATFORM_NAME: String = "com.fleshgrinder.gradle.exe.platform"
public const val ENV_NAME: String = "com.fleshgrinder.gradle.exe.env"

@JvmField public val PLATFORM_ATTRIBUTE: Attribute<String> = Attribute.of(PLATFORM_NAME, String::class.java)
@JvmField public val ENV_ATTRIBUTE: Attribute<String> = Attribute.of(ENV_NAME, String::class.java)
