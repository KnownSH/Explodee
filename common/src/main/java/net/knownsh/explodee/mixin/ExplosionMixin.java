package net.knownsh.explodee.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Debug(export = true)
@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow
    @Final
    private float radius;

    @Shadow
    @Final
    private Level level;

    // Make both outer for loops only run once
    @ModifyConstant(method = "explode", constant = @Constant(intValue = 0, ordinal = 0))
    private int explodee$modifyJvalue(int constant) {
        return 15;
    }
    @ModifyConstant(method = "explode", constant = @Constant(intValue = 0, ordinal = 1))
    private int explodee$modifyKvalue(int constant) {
        return 15;
    }

    // spherecast sample size
    @ModifyConstant(method = "explode", constant = @Constant(intValue = 16, ordinal = 3))
    private int explodee$modifyLvalue(int constant) {
        return (int) Math.max(radius * radius, 100);
    }

    @Definition(id = "j", local = @Local(type = int.class, ordinal = 1))
    @Expression("(double) ((float)j / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "explode", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcDirectionX(double original, @Local(ordinal = 3) int l, @Share("theta") LocalDoubleRef thetaRef, @Share("phi") LocalDoubleRef phiRef) {
        double phi = Math.acos(1 - 2.0 * l / (int) Math.max(radius * radius, 100));
        double theta = Math.PI * (1 + Math.sqrt(5)) * l;

        thetaRef.set(theta);
        phiRef.set(phi);
        return Math.sin(phi) * Math.cos(theta);
    }

    @Definition(id = "k", local = @Local(type = int.class, ordinal = 2))
    @Expression("(double) ((float)k / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "explode", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcDirectionY(double original, @Share("theta") LocalDoubleRef theta, @Share("phi") LocalDoubleRef phi) {
        return Math.sin(phi.get()) * Math.sin(theta.get());
    }

    @Definition(id = "l", local = @Local(type = int.class, ordinal = 3))
    @Expression("(double) ((float)l / 15.0 * 2.0 - 1.0)")
    @ModifyExpressionValue(method = "explode", at = @At("MIXINEXTRAS:EXPRESSION"))
    private double explodee$calcDirectionZ(double original, @Share("phi") LocalDoubleRef phi) {
        return Math.cos(phi.get());
    }

    @ModifyExpressionValue(method = "explode", at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D", ordinal = 0))
    private double explodee$ignoreSqrt(double original) {
        return 1;
    }
}