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

include("witch-apps:app-api")

include("witch-adapters:adapter-persistence")
include("witch-adapters:adapter-redis")

include("witch-cores:core-domain")
include("witch-cores:core-port")
include("witch-cores:core-service")
include("witch-cores:core-usecase")

include("witch-commons")