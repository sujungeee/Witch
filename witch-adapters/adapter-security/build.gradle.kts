dependencies {
    implementation(project(":witch-commons"))
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
