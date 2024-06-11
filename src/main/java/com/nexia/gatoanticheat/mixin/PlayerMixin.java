package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.events.PlayerDetectionEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    // i have no idea
    @Unique
    private static ArrayList<Float> commonCheatFallDistances = new ArrayList<>();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;crit(Lnet/minecraft/world/entity/Entity;)V"))
    public void detectCriticals(Entity entity, CallbackInfo ci) {
        if(commonCheatFallDistances.contains(this.fallDistance) || this.fallDistance >= 1024) {
            PlayerDetectionEvent.CRITICALS.invoker().onCriticalsDetection((Player) (Object) this, entity);
        }
    }

    static {
        // LiquidBounce
        commonCheatFallDistances.add(0.19f); // Vanilla
        commonCheatFallDistances.add(0.11f); // No Cheat Plus
        commonCheatFallDistances.add(0.0625f); // Falling
    }
}
