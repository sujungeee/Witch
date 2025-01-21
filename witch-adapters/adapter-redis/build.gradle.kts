dependencies {
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-commons"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    integrationImplementation("org.springframework.boot:spring-boot-starter-test")
}
