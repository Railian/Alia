# Module alia-core
Now core module is in active development that is why it has experimental suffix and you can left your feedback and or make the proposal. 

## Using in your projects
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0) 
[![Download](https://api.bintray.com/packages/railian/Alia/alia-core/images/download.svg)](https://bintray.com/railian/Alia/alia-core/_latestVersion)

The library is published to Alia bintray repository and linked to JCenter.

### Gradle
Add dependencies (you can also add other modules that you need):
```groovy
dependencies {
    implementation 'ua.railian.alia:alia-core:0.0.12-experimental'
}
```
Make sure that you have `jcenter()` in the list of repositories:
```groovy
repository {
    jcenter()
}
```

### Gradle Kotlin DSL
Add dependencies (you can also add other modules that you need):
```kotlin
dependencies {
    implementation("ua.railian.alia:alia-core:0.0.12-experimental")
}
```
Make sure that you have `jcenter()` in the list of repositories.
```kotlin
repository {
    jcenter()
}
```
