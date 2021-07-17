package com.fleshgrinder.gradle.exe

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

private class ExeAttributesTest {
    @Test fun `PLATFORM_NAME is never changed because otherwise everything is broken`() {
        expectThat(PLATFORM_NAME).isEqualTo("com.fleshgrinder.gradle.exe.platform")
    }

    @Test fun `PLATFORM_ATTRIBUTE uses PLATFORM_NAME for its name`() {
        expectThat(PLATFORM_ATTRIBUTE).get { name }.isEqualTo(PLATFORM_NAME)
    }

    @Test fun `PLATFORM_ATTRIBUTE has a string type`() {
        expectThat(PLATFORM_ATTRIBUTE).get { type }.isEqualTo(String::class.java)
    }
}
