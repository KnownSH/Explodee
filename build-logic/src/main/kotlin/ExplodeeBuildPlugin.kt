import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.prop(key: String) = findProperty(key) as String

fun RepositoryHandler.strictMaven(
    url: String,
    vararg groups: String,
    configure: MavenArtifactRepository.() -> Unit = {}
) = exclusiveContent {
    forRepository { maven(url) { configure() } }
    filter { groups.forEach(::includeGroup) }
}

enum class Loader { FABRIC, FORGE, NEOFORGE }

abstract class ExplodeeBuildExtension {
    abstract val minecraftVersion: Property<String>
    abstract val modIdProp: Property<String>

    val minecraft: String get() = minecraftVersion.get()
    val modId: String get() = modIdProp.get()
}

abstract class ExplodeeBuildPlugin : Plugin<Project> {
    @OptIn(StonecutterExperimentalAPI::class)
    override fun apply(target: Project) = with(target) {
        val sc = extensions.getByType<StonecutterBuildExtension>()
        val (mc, rawLoader) = sc.current.project.split('-', limit = 2)
        val loader = Loader.valueOf(rawLoader.uppercase())
        val fg7 = loader == Loader.FORGE && sc.eval(sc.current.version, ">=1.20.6")

        sc.properties.tags(mc, rawLoader)

        val modId = prop("mod.id")
        val javaVer = if (sc.eval(sc.current.version, ">=1.20.5")) 21 else 17

        extensions.create<ExplodeeBuildExtension>("explodee").apply {
            minecraftVersion.convention(mc)
            modIdProp.convention(modId)
        }

        // this would be like "1.1.0+1.20.1-fabric"
        version = "${prop("mod.version")}+$mc-$rawLoader"
        group = prop("mod.group")
        extensions.configure<BasePluginExtension> { archivesName.set(modId) }

        when {
            loader == Loader.FABRIC -> apply(plugin = "net.fabricmc.fabric-loom-remap")
            loader == Loader.NEOFORGE -> apply(plugin = "net.neoforged.moddev")
            fg7 -> {
                apply(plugin = "net.minecraftforge.gradle")
                apply(plugin = "net.minecraftforge.jarjar")
            }

            else -> apply(plugin = "net.neoforged.moddev.legacyforge")
        }

        // everything below would usually be put in the mods build script, although here we can unify stuff
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(javaVer))
            withSourcesJar()
        }
        tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
        tasks.withType<Jar>().configureEach {
            if (loader == Loader.FORGE) manifest.attributes("MixinConfigs" to "$modId.mixins.json")
        }

        registerCollect(if (loader == Loader.FABRIC) "remapJar" else if (fg7) "jarJar" else "jar")
        afterEvaluate { configureResources(loader, mc, modId, javaVer) }
    }

    private fun Project.configureResources(loader: Loader, mc: String, modId: String, javaVer: Int) {
        val mixinextras = extensions.getByType<VersionCatalogsExtension>()
            .named("libs").findVersion("mixinextras").get().requiredVersion

        val props = mapOf(
            "id" to modId,
            "name" to prop("mod.name"),
            "version" to version.toString(),
            "description" to prop("mod.description"),
            "authors" to prop("mod.authors"),
            "license" to prop("mod.license"),
            "minecraft" to mc,
            "java" to "JAVA_$javaVer",
            "pack_format" to if (mc == "1.20.1") 15 else 34,
            "dependencies" to modsTomlDependencies(loader, modId, mc),
        )

        tasks.named<ProcessResources>("processResources") {
            dependsOn("stonecutterGenerate")
            inputs.properties(props)
            filesMatching("*.mixins.json") { expand(props) }
            when (loader) {
                Loader.FABRIC -> {
                    filesMatching("fabric.mod.json") { expand(props) }
                    exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "pack.mcmeta")
                }

                Loader.FORGE -> {
                    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) { expand(props) }
                    exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
                }

                Loader.NEOFORGE -> {
                    filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta")) { expand(props) }
                    exclude("fabric.mod.json", "META-INF/mods.toml")
                }
            }
        }
    }

    private fun Project.modsTomlDependencies(loader: Loader, modId: String, mc: String): String {
        if (loader == Loader.FABRIC) return ""
        val forge = loader == Loader.FORGE

        fun dep(id: String, range: String) = """
            |[[dependencies.$modId]]
            |modId = "$id"
            |${if (forge) "mandatory = true" else "type = \"required\""}
            |versionRange = "$range"
            |ordering = "NONE"
            |side = "BOTH"
            """.trimMargin()

        val loaderDep = if (forge) dep("forge", "[${prop("forge").substringBefore('.')},)")
            else dep("neoforge", "[${prop("neoforge").split('.').take(2).joinToString(".")},)")

        return loaderDep + "\n\n" + dep("minecraft", "[$mc]")
    }

    private fun Project.registerCollect(jarTask: String) {
        tasks.register<Copy>("buildAndCollect") {
            group = "build"
            from(tasks.named(jarTask))
            into(rootProject.layout.buildDirectory.dir("libs/${prop("mod.version")}"))
            dependsOn("build")
        }
    }
}
