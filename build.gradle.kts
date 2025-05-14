plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.yourname.auth"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    // In-memory H2 database (for dev/testing)
    runtimeOnly("com.h2database:h2")

    //Postgres
    implementation("org.postgresql:postgresql:42.6.0")
    runtimeOnly("org.postgresql:postgresql")

    //JWT Stuff
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.28.1")

    // WebDriverManager for managing browser drivers (e.g., ChromeDriver, GeckoDriver)
    implementation("io.github.bonigarcia:webdrivermanager:5.6.0")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

application {
    mainClass.set("org.com.Main")
}
