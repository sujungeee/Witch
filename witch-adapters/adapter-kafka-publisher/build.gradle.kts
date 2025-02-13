dependencies {

    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-commons"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.kafka:spring-kafka")
}
