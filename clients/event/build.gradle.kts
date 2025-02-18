dependencies {
    compileOnly(project(":core:core-domain"))

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework:spring-context")


    implementation("org.springframework:spring-context")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-json")
}
