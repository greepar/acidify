plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.graalvm.native)
}

application {
    mainClass = "JvmMain"
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("yogurt")
            buildArgs.add("--no-fallback")
            buildArgs.add("--enable-url-protocols=http,https")
        }
    }
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

dependencies {
    implementation(project(":yogurt"))
}
