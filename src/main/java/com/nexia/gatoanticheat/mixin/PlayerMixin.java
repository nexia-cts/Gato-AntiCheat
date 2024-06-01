package com.nexia.gatoanticheat.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "attack", at = @At("TAIL"))
    public void detectCriticals(Entity entity, CallbackInfo ci) {
        double criticalHitDamage = this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 1.5F;

        if (this.onGround && this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() == criticalHitDamage) {
            System.out.println("This player has been caught using criticals report him now");
        }
    }
}
