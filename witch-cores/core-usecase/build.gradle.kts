dependencies {
    implementation(project(":witch-cores:core-domain"))
    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
