package derg.explodee.mixin.compat;

//? fabric || neoforge {
/*import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import derg.explodee.api.ExplodeeMath;
import derg.explodee.mixin.ExplodeeMixinPlugin;
import net.minecraft.world.level.Explosion;
//? >=1.21.2
//import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//? >=1.21.2
//@Mixin(value = ServerExplosion.class, priority = 1500)
//? <1.21.2
@Mixin(value = Explosion.class, priority = 1500)
public class LithiumExplosionMixinMixin {
	@Shadow
	@Final
	private float radius;

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 0))
	private int explodee$rayCount(int c, @Share("rayCount") LocalIntRef rayCount) {
		int localRayCount = ExplodeeMath.rayCount(radius);
		rayCount.set(localRayCount);
		return localRayCount;
	}

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 1))
	private int explodee$singleY(int c) {
		return 1;
	}

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 2))
	private int explodee$singleZ(int c) {
		return 1;
	}

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyVariable(method = "@MixinSquared:Handler", at = @At("STORE"), ordinal = 0)
	private double explodee$dirX(double vecX, @Local(ordinal = 0) int rayX, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.x(rayX, rayCount.get());
	}

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyVariable(method = "@MixinSquared:Handler", at = @At("STORE"), ordinal = 1)
	private double explodee$dirY(double vecY, @Local(ordinal = 0) int rayX, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.y(rayX, rayCount.get());
	}

	@TargetHandler(mixin = ExplodeeMixinPlugin.LITHIUM_EXPLOSION_MIXIN, name = "collectBlocks")
	@ModifyVariable(method = "@MixinSquared:Handler", at = @At("STORE"), ordinal = 2)
	private double explodee$dirZ(double vecZ, @Local(ordinal = 0) int rayX, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.z(rayX, rayCount.get());
	}
}
*///?}
