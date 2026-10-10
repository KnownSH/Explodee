plugins { id("explodee-build") }

repositories {
	strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data") { name = "ParchmentMC" }
}

legacyForge {
	version = "${explodee.minecraft}-${prop("forge")}"
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

mixin {
	add(sourceSets.main.get(), "${explodee.modId}.refmap.json")
	config("${explodee.modId}.mixins.json")
}

dependencies {
	annotationProcessor(variantOf(libs.mixin.forge47) { classifier("processor") })
	compileOnly(libs.mixinextras.common)
	annotationProcessor(libs.mixinextras.common)
	implementation(libs.mixinextras.forge)
	jarJar(libs.mixinextras.forge)
}

tasks.named("createMinecraftArtifacts") { dependsOn("stonecutterGenerate") }
