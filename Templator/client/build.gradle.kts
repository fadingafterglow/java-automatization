plugins {
    application
    id("com.github.fadingafterglow.import-check") version "0.1.0"
}

application {
    mainClass.set("com.github.fadingafterglow.templator.client.Main")
}

dependencies {
    implementation(project(":core"))
    runtimeOnly(project(":processors"))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}