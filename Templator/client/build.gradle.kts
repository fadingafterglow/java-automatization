plugins {
    application
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