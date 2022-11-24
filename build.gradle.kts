//todo remove this suppression when https://github.com/gradle/gradle/issues/22797 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.testsets)
}

group = "dev.sbszcz"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()

    // TODO this is just temporary as Spring Boot 3 is still a milestone. No release yet
    maven { url = uri("https://repo.spring.io/milestone")}
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

testSets {
    "componentTest" {
        dirName = "componenttest"
    }
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    developmentOnly(libs.spring.boot.devtools)

    implementation(libs.apache.httpclient)
    implementation(libs.jetbrains.annotations)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.wiremock)
    testImplementation(libs.assertj)
    testImplementation(libs.jsonassert)
    testImplementation(libs.javax.annotation.api)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
//            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
//            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
