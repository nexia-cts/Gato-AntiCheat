package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.players.CombatUtil;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Stores Player's position every tick
     */

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        CombatUtil.setPosition((ServerPlayer)(Object)this);
    }
}
