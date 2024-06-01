package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.players.CombatUtil;
import com.nexia.gatoanticheat.players.PlayerData;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;
    @Unique Entity targetEntity;

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  Stores the target for use later.
     */


    @Inject(method = "handleInteract", at = @At("HEAD"))
    private void handleInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        targetEntity = packet.getTarget(player.getLevel());
    }

    /**
     *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
     *  <h4>Licensed under MIT</h4> <br>
     *  If return value is smaller than player reach the interaction will be a success, otherwise pass
     */


    // If return value is smaller than player reach the interaction will be a success, otherwise pass
    @Redirect(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    private double getAttackDistance(Vec3 instance, Vec3 vec3) {

        /*
        // If target is not a player do vanilla code
        if (!(targetEntity instanceof ServerPlayer target)) {
            Vec3 eyePosition = player.getEyePosition(0);
            return (eyePosition).distanceToSqr(targetEntity.getBoundingBox().getNearestPointTo(eyePosition));
        }
         */

        if (CombatUtil.allowReach(player, targetEntity)) {
            return 0;
        } else {
            PlayerData.get(player).hitsBlocked++;
            return Integer.MAX_VALUE;
        }

    }

}
