dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-tx")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.springframework.security:spring-security-crypto")
    testImplementation("org.springframework.security:spring-security-crypto")

    implementation("com.fasterxml.jackson.core:jackson-databind")
}