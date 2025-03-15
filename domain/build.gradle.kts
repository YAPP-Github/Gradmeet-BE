plugins {
    kotlin("jvm") version "1.9.25"
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
