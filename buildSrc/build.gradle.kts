plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.0.0-beta01")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
}

allprojects {
    gradlePlugin {
        plugins.register("alia-publish-plugin") {
            id = "alia-publish"
            implementationClass = "AliaPublishPlugin"
        }
    }
}