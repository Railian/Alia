
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0-alpha09")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}