plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    `alia-publish`
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(29)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api("ua.railian.alia:alia-core:0.0.11-experimental")

    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    testImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

aliaPublishing {
    groupId = "ua.railian.alia"
    artifactId = "alia-list"
    version = "0.0.8-experimental"
    bintray {
        repositoryName = "Alia"
        packageName = "alia-list"
        override = true
        publish = true
    }
}