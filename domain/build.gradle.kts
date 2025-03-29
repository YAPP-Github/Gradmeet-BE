plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
