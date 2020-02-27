buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.61"))
        classpath("com.android.tools.build:gradle:4.0.0-beta01")
    }
}

allprojects {
    group = "ua.railian.alia"
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/railian/Alia")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}