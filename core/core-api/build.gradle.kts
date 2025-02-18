dependencies {
    implementation(project(":core:core-domain"))
    runtimeOnly(project(":storage:db-core"))
    runtimeOnly(project(":storage:cache-core"))
    runtimeOnly(project(":storage:file-core"))
    runtimeOnly(project(":clients:event"))
    runtimeOnly(project(":clients:mail"))

    runtimeOnly(project(":notification"))
    runtimeOnly(project(":security"))


    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.data:spring-data-commons")

    implementation("org.flywaydb:flyway-core")

    testImplementation(project(":tests:api-docs"))
    testImplementation(project(":security")) // >
}

val appMainClassName = "com.ssafy.witch.WitchApiApplication"
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set(appMainClassName)
    archiveClassifier.set("boot")
}