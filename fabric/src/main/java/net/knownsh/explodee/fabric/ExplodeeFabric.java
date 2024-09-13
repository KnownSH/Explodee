package net.knownsh.explodee.fabric;

import net.knownsh.explodee.Explodee;
import net.fabricmc.api.ModInitializer;

public final class ExplodeeFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Explodee.init();
    }
}
