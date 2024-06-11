import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.konan.properties.Properties

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.plugin)
        classpath(libs.build.konfig)
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.realm.plugin)
    alias(libs.plugins.build.konfig)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.ktor.client.android)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.datetime)

            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.mongodb.realm)
            implementation(libs.kotlin.coroutines)
            implementation(libs.stately.common)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation.compose)
            implementation(libs.compottie)
        }
        iosMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.ktor.client.darwin)
        }
    }
}

buildkonfig {
    packageName = "org.example.currencyapptest"

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "CURRENCY_API_KEY",
            properties.getProperty("CURRENCY_API_KEY")
        )
    }

//    defaultConfigs("dev") {
//        buildConfigField(
//            FieldSpec.Type.STRING,
//            "CURRENCY_API_KEY",
//            "\"${properties.getProperty("CURRENCY_API_KEY")}\""
//        )
//    }
    targetConfigs {
        create("android") {
            buildConfigField(
                FieldSpec.Type.STRING,
                "CURRENCY_API_KEY",
                properties.getProperty("CURRENCY_API_KEY")
            )
        }
        create("ios") {
            buildConfigField(
                FieldSpec.Type.STRING,
                "CURRENCY_API_KEY",
                properties.getProperty("CURRENCY_API_KEY")
            )
        }
    }
}

android {
    namespace = "org.example.currencyapptest"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    tasks.register("testClasses") {}

    buildFeatures {
        compose = true
        buildConfig = true
    }

    val keystorePropFile = file("$rootDir/keystore.properties")
    val keystoreProps = Properties()
    keystoreProps.load(keystorePropFile.inputStream())

    signingConfigs {
        create("release") {
            keyAlias = keystoreProps.getProperty("keyAlias")
            keyPassword = keystoreProps.getProperty("keyPassword")
            storeFile = file(keystoreProps.getProperty("storeFile"))
            storePassword = keystoreProps.getProperty("storePassword")
        }
    }

    defaultConfig {
        applicationId = "org.example.currencyapptest"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

