package com.fleshgrinder.gradle.exe

import org.gradle.kotlin.dsl.apply
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isNotNull

private class ExeBasePluginTest {
    @Test fun `ExeBasePlugin adds exe extension to project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(ExeBasePlugin::class)
        expectThat(project.extensions.findByName("exe")).isNotNull().isA<ExeExtension>()
    }
}
