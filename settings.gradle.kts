pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.25"
        id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
        id("org.jetbrains.kotlin.plugin.jpa") version "1.9.25"
        id("org.jetbrains.kotlin.kapt") version "1.9.25"
        id("org.springframework.boot") version "3.4.1"
        id("io.spring.dependency-management") version "1.1.7"
        id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "dobby-backend"

include("domain", "application", "presentation", "infrastructure", "common")
