/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 */

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.4.21"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("com.softeq.gradle.itest") version "1.0.4"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // library dependencies
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.10.+")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.+")
    implementation("com.fasterxml.woodstox:woodstox-core:5.1.+")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // Test Dependencies
    testImplementation("io.mockk:mockk:1.10.0")

    // Integ Tests
    itestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
    }
    test {
        java {
            srcDirs("tst")
        }
        resources {
            srcDirs("tst-resources")
        }
    }
    itest {
        java {
            srcDirs("integ-tst")
        }
    }
}
