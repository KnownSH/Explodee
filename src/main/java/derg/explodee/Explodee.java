package derg.explodee;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;

public class Explodee implements ModInitializer, GameStartEntrypoint {
	public static final String MOD_ID = "explodee";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {}

	@Override
	public void beforeGameStart() {
		CommandManager.registerCommand(new SummonExplosionCommand());
	}

	@Override
	public void afterGameStart() {}
}
