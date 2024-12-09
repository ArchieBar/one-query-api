@file:Suppress("SpellCheckingInspection", "PropertyName")

import com.diffplug.spotless.LineEnding

plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "6.25.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val jooq_version: String by project
val jakarta_version: String by project
val apache_commons_version: String by project

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)

    implementation("jakarta.validation:jakarta.validation-api:$jakarta_version")
    implementation("org.apache.commons:commons-lang3:$apache_commons_version")
    implementation("org.jooq:jooq:$jooq_version")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

spotless {
    java {
        importOrder()
        googleJavaFormat()
        removeUnusedImports()
        toggleOffOn()
        lineEndings = LineEnding.GIT_ATTRIBUTES
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.2.1")
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.codefine"
            artifactId = "one-query-api"
            version = "0.0.1"

            from(components["java"])
        }
    }
}
