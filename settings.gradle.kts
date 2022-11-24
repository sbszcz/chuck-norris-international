rootProject.name = "chuck-norris-international"

pluginManagement {
    repositories {
        mavenLocal()
        // TODO this is just temporary as Spring Boot 3 is still a milestone. No release yet
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}