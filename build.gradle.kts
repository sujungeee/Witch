import com.linecorp.support.project.multi.recipe.configureByLabels

plugins {
    id("java")
    id("io.spring.dependency-management") version Versions.springDependencyManagementPlugin apply false
    id("org.springframework.boot") version Versions.springBoot apply false
    id("io.freefair.lombok") version Versions.lombokPlugin apply false
    id("com.coditory.integration-test") version Versions.integrationTestPlugin apply false
    id("com.epages.restdocs-api-spec") version Versions.restdocsApiSpec apply false
    id("org.asciidoctor.jvm.convert") version Versions.asciidoctorPlugin apply false
    id("com.linecorp.build-recipe-plugin") version Versions.lineRecipePlugin
}

allprojects {
    group = "com.ssafy"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.restlet.com") }
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    apply(plugin = "idea")
}

configureByLabels("java") {
    apply(plugin = "org.gradle.java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "com.coditory.integration-test")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${Versions.springBoot}")
        }

        dependencies {
            dependency("com.navercorp.fixturemonkey:fixture-monkey-starter:${Versions.fixtureMonkey}")
            dependency("org.mapstruct:mapstruct:${Versions.mapstruct}")
            dependency("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")

            dependencySet("io.jsonwebtoken:${Versions.jwt}") {
                entry("jjwt-api")
                entry("jjwt-impl")
                entry("jjwt-jackson")
            }
        }
    }

    dependencies {
        val implementation by configurations
        val annotationProcessor by configurations

        val testImplementation by configurations
        val testRuntimeOnly by configurations

        val integrationImplementation by configurations
        val integrationRuntimeOnly by configurations

        implementation("org.mapstruct:mapstruct")

        annotationProcessor("org.mapstruct:mapstruct-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        integrationImplementation("org.springframework.boot:spring-boot-starter-test")
        integrationImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter")
        integrationRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

configureByLabels("boot") {
    apply(plugin = "org.springframework.boot")

    tasks.getByName<Jar>("jar") {
        enabled = false
    }

    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
        archiveClassifier.set("boot")
    }
}

configureByLabels("library") {
    apply(plugin = "java-library")

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

configureByLabels("asciidoctor") {
    apply(plugin = "org.asciidoctor.jvm.convert")

    tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
        sourceDir(file("src/docs"))
        outputs.dir(file("build/docs"))
        attributes(
            mapOf(
                "snippets" to file("build/generated-snippets")
            )
        )
    }
}

configureByLabels("restdocs") {
    apply(plugin = "com.epages.restdocs-api-spec")
}

configureByLabels("querydsl") {
    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("com.querydsl:querydsl-bom:${Versions.querydsl}")
        }

        dependencies {
            dependency("com.querydsl:querydsl-core:${Versions.querydsl}")
            dependency("com.querydsl:querydsl-jpa:${Versions.querydsl}")
            dependency("com.querydsl:querydsl-apt:${Versions.querydsl}")
        }
    }

    dependencies {
        val implementation by configurations
        val annotationProcessor by configurations

        implementation("com.querydsl:querydsl-jpa:${Versions.querydsl}:jakarta")
        implementation("com.querydsl:querydsl-core:${Versions.querydsl}")

        annotationProcessor("com.querydsl:querydsl-apt:${Versions.querydsl}:jakarta")
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    }
}
