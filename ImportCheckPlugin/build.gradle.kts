plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.github.fadingafterglow"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

gradlePlugin {
    plugins {
        create("importCheck") {
            id = "com.github.fadingafterglow.import-check"
            implementationClass = "com.github.fadingafterglow.ImportCheckPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}