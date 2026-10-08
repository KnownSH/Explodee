plugins {
    id("dev.kikugie.stonecutter")
	alias(libs.plugins.fabric.loom) apply false
	alias(libs.plugins.neoforge.moddev) apply false
	alias(libs.plugins.neoforge.legacyforge) apply false
	alias(libs.plugins.forgegradle) apply false
	alias(libs.plugins.forge.jarjar) apply false
}

stonecutter active "1.21.1-fabric"

stonecutter parameters {
	val (_, loader) = current.project.split("-", limit = 2)
	constants.match(loader, "fabric", "forge", "neoforge")
}
