plugins {
    application
}

application {
    mainClass = "App"
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:all")
    }
}
