package derg.explodee.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class ExplodeeMixinPlugin implements IMixinConfigPlugin {
	public static final String LITHIUM_EXPLOSION_MIXIN =
		//? >=1.21.2
		//"net.caffeinemc.mods.lithium.mixin.world.explosions.block_raycast.ServerExplosionMixin";
		//? <1.21.2
		"me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin";

	private static boolean hasLithiumExplosionMixin() {
		//? fabric {
		/*String path = LITHIUM_EXPLOSION_MIXIN.replace('.', '/') + ".class";
		return net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("lithium")
			.flatMap(lithium -> lithium.findPath(path))
			.isPresent();
		*///?}
		//? !fabric
		return false;
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String target, String mixin) {
		if (mixin.endsWith("LithiumExplosionMixinMixin")) {
			return hasLithiumExplosionMixin();
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
