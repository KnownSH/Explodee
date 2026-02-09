package derg.explodee;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.world.World;

import static com.mojang.brigadier.builder.ArgumentBuilderLiteral.literal;

public class SummonExplosionCommand implements CommandManager.CommandRegistry {
	@SuppressWarnings("unchecked")
	@Override
	public void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(
			(ArgumentBuilderLiteral<CommandSource>) (Object) literal("explodee:explode").requires(t -> ((CommandSource) t).hasAdmin())
				.then(ArgumentBuilderRequired.argument("size", ArgumentTypeInteger.integer())
					.executes(cmd -> {
						int size = cmd.getArgument("size", Integer.class);
						summonExplosion(cmd, size);
						return 1;
					})));
	}

	private static void summonExplosion(CommandContext<Object> cmd, int size) {
		CommandSource source = (CommandSource) cmd.getSource();
		Player sender = source.getSender();
		World world = source.getWorld();

		if (sender == null || world.isClientSide) return;
		world.createExplosion(null, sender.x, sender.y + 0.5, sender.z, size);
	}
}
