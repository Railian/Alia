plugins {
    kotlin("jvm")
    `alia-publish`
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

aliaPublishing {
    groupId = "ua.railian.alia"
    artifactId = "alia-core"
    version = "0.0.12-experimental"
    bintray {
        repositoryName = "Alia"
        packageName = "alia-core"
        override = true
        publish = true
    }
}