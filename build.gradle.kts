@file:Suppress("UnstableApiUsage")

buildscript {
	repositories {
		mavenCentral()
		google()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:7.3.1")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
	}
}

plugins {
	id("com.android.application") version("7.3.1") apply(true)
	kotlin("android") version("1.6.21") apply(true)
	pmd
	checkstyle
}

dependencies {
	implementation("androidx.core:core-ktx:1.9.0")
	implementation("androidx.annotation:annotation:1.5.0")
	implementation("androidx.appcompat:appcompat:1.6.0")
	implementation("androidx.recyclerview:recyclerview:1.2.1")
	implementation("com.google.android.flexbox:flexbox:3.0.0")
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
	implementation("androidx.preference:preference:1.2.0")
	implementation("com.google.android.material:material:1.8.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.fragment:fragment:1.5.5")

	implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.apache.commons:commons-text:1.9")
	implementation("net.danlew:android.joda:2.12.1.1")
	implementation("com.squareup.okhttp3:okhttp:3.12.13")
	implementation("info.guardianproject.netcipher:netcipher:1.2.1")
	implementation("com.google.android.exoplayer:exoplayer-core:2.18.2")
	implementation("com.google.android.exoplayer:exoplayer-ui:2.18.2")
	implementation("com.github.luben:zstd-jni:1.5.1-1@aar")

	testImplementation("junit:junit:4.13.2")

	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation("androidx.test:rules:1.5.0")
	androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
}

android {
	compileSdk = 33
	buildToolsVersion = "33.0.1"
	ndkVersion = "23.1.7779620"
	namespace = "org.quantumbadger.redreader"

	defaultConfig {
		applicationId = "org.quantumbadger.redreader"
		minSdk = 16
		targetSdk = 31
		versionCode = 104
		versionName = "1.20"

		vectorDrawables.generatedDensities("mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi")
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	// Flag to tell aapt to keep the attribute ids around
	androidResources {
		additionalParameters("--no-version-vectors")
	}

	buildTypes.forEach {
		it.isMinifyEnabled = true
		it.isShrinkResources = false

		it.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
	}

	compileOptions {
		encoding = "UTF-8"
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	lint {
		checkReleaseBuilds = false
		abortOnError = true
		warningsAsErrors = true

		error.add("DefaultLocale")

		baseline = file("config/lint/lint-baseline.xml")
		lintConfig = file("config/lint/lint.xml")
	}

	packagingOptions {
		excludes.add("META-INF/*")
	}

	testOptions {
		animationsDisabled = true
	}
}

pmd {
	toolVersion = "6.36.0"
}

tasks.register("pmd", Pmd::class) {
	dependsOn.add("assembleDebug")
	ruleSetFiles = files("${project.rootDir}/config/pmd/rules.xml")
	ruleSets = emptyList() // otherwise defaults clash with the list in rules.xml
	source("src/main/java/org/quantumbadger")
	include("**/*.java")
	isConsoleOutput = true
}

checkstyle {
	toolVersion = "8.35"
}

tasks.register("Checkstyle", Checkstyle::class) {
	source("src/main/java/org/quantumbadger")
	ignoreFailures = false
	isShowViolations = true
	include("**/*.java")
	classpath = files()
	maxWarnings = 0
	configFile = rootProject.file("${project.rootDir}/config/checkstyle/checkstyle.xml")
}
