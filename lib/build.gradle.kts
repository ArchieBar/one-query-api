@file:Suppress("SpellCheckingInspection", "PropertyName")

import com.diffplug.spotless.LineEnding
import org.jooq.meta.jaxb.GeneratedSerialVersionUID

plugins {
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "6.25.0"
    id("org.jooq.jooq-codegen-gradle") version "3.19.6"
}

repositories {
    mavenCentral()
}

val assertj_version: String by project
val testcontainers_version: String by project
val jooq_version: String by project
val jakarta_version: String by project
val apache_commons_version: String by project
val postgres_version: String by project
val junit_version: String by project
val hikaricp_version: String by project
val logback_version: String by project
val jackson_version: String by project

dependencies {
    testImplementation("org.assertj:assertj-core:$assertj_version")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainers_version")
    testImplementation("org.testcontainers:postgresql:$testcontainers_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testImplementation(libs.junit.jupiter)
    testImplementation("com.zaxxer:HikariCP:$hikaricp_version")

    testRuntimeOnly("org.postgresql:postgresql:$postgres_version")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    api(libs.commons.math3)

    implementation("jakarta.validation:jakarta.validation-api:$jakarta_version")
    implementation("org.apache.commons:commons-lang3:$apache_commons_version")
    implementation("org.jooq:jooq:$jooq_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")

    jooqCodegen("org.jooq:jooq-meta-extensions:$jooq_version")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
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
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "one.query.api"
            artifactId = "one-query-api"
            version = "0.0.9"

            from(components["java"])
        }
    }
}

jooq {
    configuration {
        generator {
            generate {
                generatedSerialVersionUID = GeneratedSerialVersionUID.HASH
                isDaos = true
                isImmutablePojos = true
            }

            target {
                packageName = "one.query.api.jooq.generated"
                directory = "src/test/java"
            }

            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"

                properties {
                    property {
                        key = "scripts"
                        value = "src/test/resources/db/sql/table_for_jooq_codegen.sql"
                    }

                    property {
                        key = "sort"
                        value = "semantic"
                    }

                    property {
                        key = "unqualifiedSchema"
                        value = "none"
                    }

                    property {
                        key = "defaultNameCase"
                        value = "as_is"
                    }
                }
            }
        }
    }
}
