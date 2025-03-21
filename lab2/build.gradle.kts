plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.commons:commons-csv:1.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("net.bytebuddy:byte-buddy:1.15.11")
    implementation("org.knowm.xchart:xchart:3.8.8")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}