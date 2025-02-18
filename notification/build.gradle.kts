dependencies {

    implementation(project(":core:core-domain"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-json")

    implementation("com.google.firebase:firebase-admin:9.4.3")
}
