dependencies {
    compileOnly(project(":core:core-domain"))

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")

    testImplementation(project(":tests:api-docs"))
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
