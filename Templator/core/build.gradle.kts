plugins {
    `java-library`
}

dependencies {
    implementation("com.google.guava:guava:33.4.8-jre")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}