# BggPuller
 A bgg pulling library for my website.
 
**NOTE:** This library is mostly for personal use, and as such support will be limited.

# Status
[![Build Status](https://travis-ci.com/thewells1024/BggPuller.svg?token=5U4s3xswx3Jon584EckJ&branch=master)](https://travis-ci.com/thewells1024/BggPuller)

# Usage

Add the following to your `settings.gradle`

```kotlin
sourceControl {
    gitRepository("https://github.com/thewells1024/BggPuller.git") {
        producesModule("me.kentkawa:bggpuller")
    }
}
```

and add the following to your `build.gradle`

```kotlin
dependencies {
    implementation("me.kentkawa:bggpuller:v0.1.0")
}
```