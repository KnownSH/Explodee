package derg.explodee

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turniplabs.halplibe.util.GameStartEntrypoint

object Explodee : ModInitializer, GameStartEntrypoint {
    const val MOD_ID: String = "explodee"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {}

    override fun beforeGameStart() {}

    override fun afterGameStart() {}
}
