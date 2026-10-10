plugins { id("explodee-build") }

repositories {
	strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data") { name = "ParchmentMC" }
	strictMaven("https://maven.bawnorton.com/releases", "com.github.bawnorton.mixinsquared") { name = "Bawnorton" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

neoForge {
	version = prop("neoforge")
	validateAccessTransformers = true
	parchment {
		minecraftVersion = explodee.minecraft
		mappingsVersion = prop("parchment")
	}
	runs {
		register("client") { client(); gameDirectory = rootProject.file("run") }
		register("server") { server(); gameDirectory = rootProject.file("run"); programArgument("--nogui") }
	}
	mods.register(explodee.modId) { sourceSet(sourceSets.main.get()) }
}

dependencies {
	compileOnly(libs.mixinsquared.common)
	annotationProcessor(libs.mixinsquared.common)
	implementation(libs.mixinsquared.neoforge)
	jarJar(libs.mixinsquared.neoforge)

	runtimeOnly("maven.modrinth:lithium:${prop("deps.lithium")}")
}

tasks.named("createMinecraftArtifacts") { dependsOn("stonecutterGenerate") }
