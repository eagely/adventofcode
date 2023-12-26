plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.eagely"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.p-org.solvers:z3:4.8.14-v5")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")
    implementation("org.slf4j:slf4j-log4j12:2.0.9")
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}


application {
    mainClass.set("MainKt")
}

tasks.withType<JavaExec> {
    if (name == "run") {
        systemProperty("java.library.path", "/usr/lib/")
    }
}