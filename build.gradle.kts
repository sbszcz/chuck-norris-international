plugins {
    java
    `jvm-test-suite`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    idea
}

group = "dev.sbszcz"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadJavadoc = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    developmentOnly(libs.spring.boot.devtools)

    implementation(libs.apache.httpclient)
    implementation(libs.jetbrains.annotations)
}

testing {
    suites {

        register<JvmTestSuite>("componentTest") {
            dependencies {
                implementation(libs.spring.boot.starter.test)
                implementation(libs.wiremock)
                implementation(libs.assertj)
                implementation(libs.jsonassert)
                implementation(libs.javax.annotation.api)

                // why does this NOT work???
                // implementation(project())

                // why does this work????
                implementation(sourceSets.main.get().runtimeClasspath)
            }
        }

        configureEach {
            if (this is JvmTestSuite) {

                targets {
                    all {
                        testTask {

                            testLogging {
                                events = setOf(
                                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                                )
//                                showStandardStreams = true
                                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                            }

                            outputs.upToDateWhen { false }
                        }
                    }
                }
            }
        }
    }
}