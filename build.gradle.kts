plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("jacoco")
	id("org.sonarqube") version "6.0.1.5171"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.dobby"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

tasks.jar {
	enabled = false
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("com.github.f4b6a3:ulid-creator:5.2.3")
	implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("io.github.cdimascio:java-dotenv:5.2.2")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	val jjwtVersion = "0.12.5"
	implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

	val koTestVersion = "5.8.1"
	testImplementation("io.kotest:kotest-runner-junit5-jvm:$koTestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$koTestVersion")
	testImplementation("io.kotest:kotest-property:$koTestVersion")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

jacoco {
	toolVersion = "0.8.8"
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

sonar {
	properties {
		property("sonar.projectKey", "YAPP-Github_25th-Web-Team-2-BE")
		property("sonar.organization", "yapp-github")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/index.xml")
		property("sonar.sources", "src/main/kotlin")
		property("sonar.sourceEncoding", "UTF-8")
		property("sonar.exclusions", "**/test/**, **/resources/**, **/*Application*.kt, **/*Controller*.kt, " +
				"**/*Config.kt, **/*Repository*.kt, **/*Dto*.kt, **/*Response*.kt, **/*Request*.kt, **/*Exception*.kt," +
				"**/*Filter.kt, **/*Handler.kt, **/*Properties.kt, **/*Utils.kt")
		property("sonar.test.inclusions", "**/*Test.kt")
		property("sonar.kotlin.coveragePlugin", "jacoco")
	}
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports{
		html.required.set(true)
		xml.required.set(true)
		html.outputLocation.set(file(layout.buildDirectory.dir("reports/jacoco/index.html").get().asFile))
		xml.outputLocation.set(file(layout.buildDirectory.dir("reports/jacoco/index.xml").get().asFile))
	}

	classDirectories.setFrom(
		files(
			classDirectories.files.flatMap { dir ->
				fileTree(dir) {
					exclude(
						"**/*Application*",
						"**/config/*",
						"**/domain/exception/*",
						"**/domain/gateway/*",
						"**/domain/model/*",
						"**/infrastructure/*",
						"**/presentation/*",
						"**/util/*"
					)
				}.files
			}
		)
	)
	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			isFailOnViolation = false
			isEnabled = true
			element = "CLASS"

			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = 0.70.toBigDecimal()
			}

			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = 0.70.toBigDecimal()
			}

			excludes = listOf(
				"**.*Application*",
				"**.config.*",
				"**.domain.exception.*",
				"**.domain.gateway.*",
				"**.domain.model.*",
				"**.infrastructure.*",
				"**.presentation.*",
				"**.util.*"
			)
		}
	}
}
