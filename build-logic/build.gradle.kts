plugins { `kotlin-dsl` }

gradlePlugin {
	plugins {
		register("explodeeBuild") {
			id = "explodee-build"
			implementationClass = "ExplodeeBuildPlugin"
		}
	}
}

repositories {
	mavenCentral()
	gradlePluginPortal()
	maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
}

dependencies {
	implementation(libs.kikugie.stonecutter)
}
