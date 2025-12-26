buildscript {
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.diffplug.spotless) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint(ktlintVersion).editorConfigOverride(
                mapOf(
                    "android" to "true",
                ),
            ).customRuleSets(
                listOf(
                    "io.nlopez.compose.rules:ktlint:0.4.27",
                ),
            )
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
        groovy {
            target("**/*.gradle")
            targetExclude("**/build/**/*.gradle")
            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(
                rootProject.file("spotless/copyright.gradle"),
                  "(^(?![\\/ ]\\*).*$)",
            )
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("**/build/**/*.kts")
            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(
                rootProject.file(
                    "spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)"
            )
        }
        format("xml") {
            target("**/*.xml")
            targetExclude(
                "**/build/**/*.xml"
            )
            // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
            licenseHeaderFile(
                rootProject.file("spotless/copyright.xml"), "(<[^!?])"
            )
        }
    }

    afterEvaluate {
        tasks.named("preBuild") {
            dependsOn("spotlessApply")
        }
    }
}