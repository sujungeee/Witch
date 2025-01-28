dependencies {
    implementation(project(":witch-commons"))

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
