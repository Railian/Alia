plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(kotlin("stdlib-jdk8"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}