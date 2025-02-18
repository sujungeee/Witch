dependencies {

    compileOnly(project(":core:core-domain"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}
