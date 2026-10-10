package derg.explodee.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import derg.explodee.api.ExplodeeMath;
import net.minecraft.world.level.Explosion;
//? >=1.21.2
//import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//? >=1.21.2
//@Mixin(ServerExplosion.class)
//? !forge && <1.21.2 || <1.21
//@Mixin(Explosion.class)
//? forge && >=1.21 && <1.21.2
@Mixin(value = Explosion.class, remap = false)
public class ExplosionMixin {
	@Unique
	private static final String explodee$METHOD =
		//? >=1.21.2
		//"calculateExplodedPositions";
	//? <1.21.2
	"explode";

	@Shadow
	@Final
	private float radius;

	// Skip the first and second for loops
	@ModifyConstant(method = explodee$METHOD, constant = @Constant(intValue = 0, ordinal = 0))
	private int explodee$singleJ(int _unused) { return 15; }
	@ModifyConstant(method = explodee$METHOD, constant = @Constant(intValue = 0, ordinal = 1))
	private int explodee$singleK(int _unused) { return 15; }

	// Make inner-most for loop iterate raycount amount of times
	@ModifyConstant(method = explodee$METHOD, constant = @Constant(intValue = 16, ordinal = 3))
	private int explodee$rayCount(int _unused, @Share("rayCount") LocalIntRef rayCount) {
		int localRayCount = ExplodeeMath.rayCount(radius);
		rayCount.set(localRayCount);
		return localRayCount;
	}

	@ModifyVariable(method = explodee$METHOD, at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private double explodee$directionX(double d, @Local(ordinal = 3) int l, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.x(l, rayCount.get());
	}
	@ModifyVariable(method = explodee$METHOD, at = @At(value = "STORE", ordinal = 0), ordinal = 1)
	private double explodee$directionY(double f, @Local(ordinal = 3) int l, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.y(l, rayCount.get());
	}
	@ModifyVariable(method = explodee$METHOD, at = @At(value = "STORE", ordinal = 0), ordinal = 2)
	private double explodee$directionZ(double f, @Local(ordinal = 3) int l, @Share("rayCount") LocalIntRef rayCount) {
		return ExplodeeMath.z(l, rayCount.get());
	}

	@ModifyVariable(method = explodee$METHOD, at = @At("STORE"), ordinal = 3)
	private double epxlodee$unitLength(double g) { return 1; }
}
