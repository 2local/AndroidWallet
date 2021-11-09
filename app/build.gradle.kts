import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("org.greenrobot.greendao")
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.secrets_gradle_plugin") version "0.5"
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
}
apply {
    from("../app_properties.gradle")
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK
    buildToolsVersion = AndroidConfig.BUILD_TOOLS_VERSION

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK
        targetSdk = AndroidConfig.TARGET_SDK
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        applicationId = AndroidConfig.ID

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }



    buildTypes {
        getByName(BuildType.DEBUG) {
            isDebuggable = BuildTypeDebug.isDebuggable
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            buildConfigField("String", "BASE_URL", gradleLocalProperties(rootDir).getProperty("BASE_URL").toString())
            buildConfigField("String", "API_KEY", gradleLocalProperties(rootDir).getProperty("API_KEY").toString())
            buildConfigField("String", "BSC_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("BSC_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_MAINNET_INFURA_API_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_MAINNET_INFURA_API_KEY").toString())
        }

        maybeCreate(BuildType.STAGING)
        getByName(BuildType.STAGING) {
//            initWith(getByName(BuildType.DEBUG))
            isDebuggable = BuildTypeStage.isDebuggable
            isMinifyEnabled = BuildTypeStage.isMinifyEnabled
            buildConfigField("String", "BASE_URL", gradleLocalProperties(rootDir).getProperty("BASE_URL").toString())
            buildConfigField("String", "API_KEY", gradleLocalProperties(rootDir).getProperty("API_KEY").toString())
            buildConfigField("String", "BSC_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("BSC_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_MAINNET_INFURA_API_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_MAINNET_INFURA_API_KEY").toString())
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName(BuildType.RELEASE) {
            isDebuggable = BuildTypeRelease.isDebuggable
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            buildConfigField("String", "BASE_URL", gradleLocalProperties(rootDir).getProperty("BASE_URL").toString())
            buildConfigField("String", "API_KEY", gradleLocalProperties(rootDir).getProperty("API_KEY").toString())
            buildConfigField("String", "BSC_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("BSC_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_SCAN_PAI_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_SCAN_PAI_KEY").toString())
            buildConfigField("String", "ETHER_MAINNET_INFURA_API_KEY", gradleLocalProperties(rootDir).getProperty("ETHER_MAINNET_INFURA_API_KEY").toString())
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
    }

    buildFeatures {
//        dataBinding = true
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        isCheckGeneratedSources = true
        // Running lint over the debug variant is enough
        isCheckReleaseBuilds = false
        // See lint.xml for rules configuration
    }

    greendao {
        schemaVersion(9)
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
//            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STANDARD_LIBRARY)
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.MATERIAL)

    implementation(Dependencies.LIFECYCLE_COMMON)
    implementation(Dependencies.ARCH_CORE_ANDROIDX)

    implementation(Dependencies.SWIPE_REFRESH)
    implementation(Dependencies.CONSTRAINT_LAYOUT)

    //retrofit
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_JSON)


    //database
    implementation(Dependencies.GREEN_DAO)

    implementation(Dependencies.TIMBER)
    //NAVIGATION
    implementation(Dependencies.FRAGMENT_KTX)
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_UI)

    //MAP
    implementation(Dependencies.MAP)

    //GLIDE
    implementation(Dependencies.GLIDE)
    annotationProcessor(Dependencies.GLIDE)
    kapt(Dependencies.GLIDE_COMPILER)

    //dagger
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)

    //RX
    implementation(Dependencies.RX_JAVA)
    implementation(Dependencies.RX_ANDROID)
    // Import the BoM for the Firebase platform
    implementation(Dependencies.STURTUP)
    implementation(
        platform("com.google.firebase:firebase-bom:28.0.1")
    )
    // Declare the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("io.branch.sdk.android:library")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
// Branch.io
    implementation("io.branch.sdk.android:library:5.0.4")
    implementation("org.greenrobot:eventbus:3.2.0")
    implementation("com.github.kenglxn.QRGen:android:2.6.0")
    implementation("org.apmem.tools:layouts:1.10@aar")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("com.raycoarana.codeinputview:codeinputview:2.1.4")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.github.AppIntro:AppIntro:6.1.0")
    implementation("com.karumi:dexter:6.0.1")

    implementation("com.github.f0ris.sweetalert:library:1.6.2")
    // qr-gen
    implementation("com.github.kenglxn.QRGen:android:2.6.0")

    // qr-scanner
    implementation("com.budiyev.android:code-scanner:2.1.0")

    // ethereum
    implementation("org.web3j:core:4.6.0-android")
    implementation("org.bitcoinj:bitcoinj-core:0.15.10") {
        exclude(group = "org.bouncycastle")
    }

    implementation("androidx.browser:browser:1.3.0")


    // android test
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("com.google.truth:truth:1.0.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
//    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation("android.arch.core:core-testing:1.1.1")
//    androidTestImplementation("org.mockito:mockito-core:3.9.0")
    androidTestImplementation("org.mockito:mockito-android:3.9.0")
    androidTestImplementation("io.mockk:mockk:1.9.3")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    debugImplementation("androidx.fragment:fragment-testing:1.3.6")

    // test
    testImplementation("junit:junit:4.13.1")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.8")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")


    testImplementation("org.powermock:powermock-module-junit4:2.0.9")
    testImplementation("org.powermock:powermock-module-junit4-rule:2.0.9")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
    testImplementation("org.powermock:powermock-classloading-xstream:2.0.9")
    testImplementation("org.robolectric:robolectric:4.6.1")

}