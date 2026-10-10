plugins { id("explodee-build") }

loom {
	runConfigs.all {
		generateRunConfig = true
		runDirectory = rootProject.layout.projectDirectory.dir("run")
	}
}

repositories {
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "modrinth" }
	strictMaven("https://maven.bawnorton.com/releases", "com.github.bawnorton.mixinsquared") { name = "Bawnorton" }
	strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data") { name = "ParchmentMC" }
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "Terraformers" }
}

fun DependencyHandler.mod(dependency: Any) =
	add(if (explodee.obfuscated) "modImplementation" else "implementation", dependency)

dependencies {
	minecraft("com.mojang:minecraft:${explodee.minecraft}")
	if (explodee.obfuscated) "mappings"(loom.layered {
		officialMojangMappings()
		if (hasProperty("parchment")) parchment("org.parchmentmc.data:parchment-${explodee.minecraft}:${prop("parchment")}@zip")
	})
	mod(libs.fabric.loader)

	annotationProcessor(libs.mixinsquared.fabric)
	implementation(libs.mixinsquared.fabric)
	include(libs.mixinsquared.fabric)

	// explodee doesn't actually need fabric-api
	mod("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric_api")}+${explodee.minecraft}")
	mod("maven.modrinth:lithium:${prop("deps.lithium")}")

	if (hasProperty("deps.modmenu")) mod("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
}
