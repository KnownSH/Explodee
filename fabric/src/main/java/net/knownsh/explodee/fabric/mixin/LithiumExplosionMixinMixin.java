package net.knownsh.explodee.fabric.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Lithium skips the main explosion loop, which is _needed_ for Explodee's spherecasting.
// This Mixin injects Explodee's spherecasting back into Lithium while keeping the advantage of lithium's optimizations
@Debug(export = true)
@Mixin(value = Explosion.class, priority = 1500)
public class LithiumExplosionMixinMixin {
    @Shadow @Final private float radius;

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 0))
    private int explodee$modifyRayX(int original) {
        return (int) Math.max(radius * radius, 100);
    }

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 1))
    private int explodee$modifyRayY(int original) {
        return 1;
    }

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(intValue = 16, ordinal = 2))
    private int explodee$modifyRayZ(int original) {
        return 1;
    }

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @Definition(id = "rayX", local = @Local(type = int.class, ordinal = 0))
    @Expression("(double)((float)rayX / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcVecX(double original, @Local(ordinal = 0) int i, @Share("theta") LocalDoubleRef thetaRef, @Share("phi") LocalDoubleRef phiRef) {
        double phi = Math.acos(1 - 2.0 * i / (int) Math.max(radius * radius, 100));
        double theta = Math.PI * (1 + Math.sqrt(5)) * i;

        thetaRef.set(theta);
        phiRef.set(phi);
        return Math.sin(phi) * Math.cos(theta);
    }

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @Definition(id = "rayY", local = @Local(type = int.class, ordinal = 1))
    @Expression("(double)((float)rayY / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcVecY(double original, @Share("theta") LocalDoubleRef theta, @Share("phi") LocalDoubleRef phi) {
        return Math.sin(phi.get()) * Math.sin(theta.get());
    }

    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.world.explosions.ExplosionMixin",
            name = "collectBlocks"
    )
    @Definition(id = "rayZ", local = @Local(type = int.class, ordinal = 2))
    @Expression("(double)((float)rayZ / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcVecZ(double original, @Share("phi") LocalDoubleRef phi) {
        return Math.cos(phi.get());
    }
}