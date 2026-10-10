plugins { id("explodee-build") }

repositories {
	minecraft.mavenizer(this)
	maven(fg.forgeMaven)
	maven(fg.minecraftLibsMaven)
	mavenCentral()
}

minecraft {
	mappings("parchment", "${prop("parchment")}-${explodee.minecraft}")
	runs {
		configureEach {
			workingDir.set(rootProject.layout.projectDirectory.dir("run"))
			args("--mixin.config=${explodee.modId}.mixins.json")
		}
		register("client")
		register("server")
	}
}

sourceSets.configureEach {
	val dir = layout.buildDirectory.dir("sourcesSets/$name")
	output.setResourcesDir(dir)
	java.destinationDirectory = dir
}

jarJar.register { archiveClassifier = null }

dependencies {
	implementation(minecraft.dependency("net.minecraftforge:forge:${explodee.minecraft}-${prop("forge")}"))
	annotationProcessor(variantOf(libs.mixin.forge52) { classifier("processor") })
	compileOnly(libs.mixinextras.common)
	annotationProcessor(libs.mixinextras.common)
	implementation(libs.mixinextras.forge)
	"jarJar"(libs.mixinextras.forge)
}

tasks.named<Jar>("jar") { archiveClassifier = "slim" }
