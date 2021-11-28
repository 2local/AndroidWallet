// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven(url = uri(Repositories.MAVEN_GOOGLE))
        maven(url = uri(Repositories.MAVEN_JITPACK))
        maven(url = "https://plugins.gradle.org/m2/")
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(GradlePlugin.GRADLE_BUILD_TOOLS)
        classpath(GradlePlugin.KOTLIN_GRADLE_PLUGIN)
        classpath(GradlePlugin.GOOGLE_SERVICES_PLUGIN)
        classpath(GradlePlugin.CRASHLYTICS_PLUGIN)
        classpath(GradlePlugin.GREEN_DAO_PLUGIN)
    }
}

allprojects {
    repositories {
        maven(url = uri(Repositories.MAVEN_GOOGLE))
        maven(url = uri(Repositories.MAVEN_JITPACK))
        maven(url = "https://plugins.gradle.org/m2/")
        google()
        jcenter()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}