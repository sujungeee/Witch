pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://maven.springframework.org/release")
        }
        maven {
            url = uri("https://maven.restlet.com")
        }
    }
}

rootProject.name = "witch"

include(
    "core:core-api",
    "core:core-domain",
    "storage:db-core",
    "storage:cache-core",
    "storage:file-core",
    "clients:event",
    "clients:mail",
    "security",
    "notification",
    "tests:api-docs"
)
