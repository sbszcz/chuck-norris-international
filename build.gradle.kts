import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("java")

    id("org.unbroken-dome.test-sets") version "4.0.0"
}

group = "dev.sbszcz"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

testSets {
    "componentTest" {
        dirName = "componenttest"
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.jetbrains:annotations:20.1.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.33.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.skyscreamer:jsonassert:1.5.0")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()

    testLogging {
        events = setOf(
            PASSED,
            SKIPPED,
            FAILED,
            STANDARD_ERROR,
            STANDARD_OUT
        )
    }

    // always run this task
    outputs.upToDateWhen { false }
}

val componentTest by tasks.named<Test>("componentTest") {
    useJUnitPlatform()

    systemProperties(System.getProperties().mapKeys { it.key as String })

    // always run this task
    outputs.upToDateWhen { false }
}
