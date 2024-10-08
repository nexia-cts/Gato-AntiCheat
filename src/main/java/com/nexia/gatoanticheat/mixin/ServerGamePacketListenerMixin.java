package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.events.PlayerDetectionEvent;
import com.nexia.gatoanticheat.players.CombatUtil;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
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
            PlayerDetectionEvent.REACH.invoker().onReachDetection(player, targetEntity);
            return Integer.MAX_VALUE;
        }
    }

    // Thank you, our lord and saviour
    //   _____  _          _____            _
    // |  __ \(_)        / ____|          | |
    // | |__) |_ _______| |     ___   ___ | | _____ _   _
    // |  _  /| |_  / _ \ |    / _ \ / _ \| |/ / _ \ | | |
    // | | \ \| |/ /  __/ |___| (_) | (_) |   <  __/ |_| |
    // |_|  \_\_/___\___|\_____\___/ \___/|_|\_\___|\__, |
    //                                               __/ |
    //                                              |___/
    @Redirect(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getCurrentAttackReach(F)F"))
    public float redirectReachLonger(ServerPlayer playerEntity, float f) {
        return playerEntity.getCurrentAttackReach(f) + CombatUtil.padding;
    }

    /*
    @Inject(method = "handleContainerClick", cancellable = true, at = @At("HEAD"))
    private void fixContainerCrash(ServerboundContainerClickPacket clickPacket, CallbackInfo ci) {
        int containerId = clickPacket.getContainerId();
        int slot = clickPacket.getSlotNum();

        if (containerId < 0 || containerId > 10 && containerId != 40 && containerId != 99) {
            ci.cancel();
            return;
        }

        if (slot != -999 && slot != -1) {
            if (slot < 0) {
                ci.cancel();
            }
            return;
        }

        if (containerId == 40) {
            ci.cancel();
            return;
        }

        switch (clickPacket.getClickType()) {
            case SWAP, PICKUP_ALL -> ci.cancel();
            case THROW -> {
                if (slot == -1) {
                    ci.cancel();
                }
            }
            case QUICK_MOVE -> {
                if (slot == -999) {
                    ci.cancel();
                }
            }
        }
    }

     */
}
