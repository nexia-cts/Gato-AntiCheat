package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.players.CombatUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Decreases invulnerable time for better hitreg
     */


    @ModifyArg(method = "hurt", index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    protected int modifyInvulnerableTicks(int original) {
        return CombatUtil.modifyInvulnerableTicks(original);
    }
}
