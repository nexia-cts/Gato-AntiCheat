package net.blumbo.ctsanticheat;

import net.blumbo.ctsanticheat.players.CombatUtil;
import net.fabricmc.api.ModInitializer;

public class CtsAntiCheat implements ModInitializer {

    @Override
    public void onInitialize() {
        if(CombatUtil.savedLocationTicks <= 0) CombatUtil.savedLocationTicks = 1;
    }

}
