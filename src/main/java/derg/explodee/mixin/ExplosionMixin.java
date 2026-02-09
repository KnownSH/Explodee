package derg.explodee.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.core.world.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = Explosion.class, remap = false)
public class ExplosionMixin {
	@Shadow
	public float explosionSize;

	@Unique
	private final int newExplosionSize = (int) Math.max(explosionSize * explosionSize, 100);

	@ModifyConstant(method = "calculateBlocksToDestroy", constant = @Constant(intValue = 0, ordinal = 0))
	private int explodee$modifyJvalue(int constant) {
		return 15;
	}

	@ModifyConstant(method = "calculateBlocksToDestroy", constant = @Constant(intValue = 0, ordinal = 1))
	private int explodee$modifyKvalue(int constant) {
		return 15;
	}

	@Expression("? < @(?)")
	@ModifyExpressionValue(method = "calculateBlocksToDestroy", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
	private int explodee$modifyJ1comparison(int original) {
		return newExplosionSize;
	}

	@ModifyVariable(method = "calculateBlocksToDestroy", at = @At("STORE"))
	private double explodee$modifyD(double d, @Local(name = "j1") int j1, @Share("theta") LocalDoubleRef thetaRef, @Share("phi") LocalDoubleRef phiRef) {
		double phi = Math.acos(1 - 2.0 * j1 / (int) Math.max(explosionSize * explosionSize, 100));
		double theta = Math.PI * 3.2360679775 * j1;

		thetaRef.set(theta);
		phiRef.set(phi);
		return Math.sin(phi) * Math.cos(theta);
}

	@ModifyVariable(method = "calculateBlocksToDestroy", ordinal = 1, at = @At(value = "STORE", ordinal = 0))
	private double explodee$modifyD1(double d1, @Share("theta") LocalDoubleRef thetaRef, @Share("phi") LocalDoubleRef phiRef) {
		return Math.sin(phiRef.get()) * Math.sin(thetaRef.get());
	}

	@ModifyVariable(method = "calculateBlocksToDestroy", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
	private double explodee$modifyD2(double d2, @Share("phi") LocalDoubleRef phiRef) {
		return Math.cos(phiRef.get());
	}

	@ModifyVariable(method = "calculateBlocksToDestroy", ordinal = 3, at = @At("STORE"))
	private double explodee$modifySqrt(double d3) {
		return 1;
	}
}
