package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.players.CombatUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    // Decrease invulnerability time slightly for less strict hitreg
    @ModifyArg(method = "hurt", index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    protected int modifyInvulnerableTicks(int original) {
        return CombatUtil.modifyInvulnerableTicks(original);
    }
}
