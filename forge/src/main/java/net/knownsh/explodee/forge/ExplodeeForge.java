package net.knownsh.explodee.forge;

import net.knownsh.explodee.Explodee;
import net.minecraftforge.fml.common.Mod;

@Mod(Explodee.MOD_ID)
public final class ExplodeeForge {
    public ExplodeeForge() {
        // Run our common setup.
        Explodee.init();
    }
}
