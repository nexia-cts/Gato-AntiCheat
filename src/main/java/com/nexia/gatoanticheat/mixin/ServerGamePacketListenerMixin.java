package com.nexia.gatoanticheat.mixin;

import com.nexia.gatoanticheat.events.PlayerDetectionEvent;
import com.nexia.gatoanticheat.players.CombatUtil;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
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

    @Unique private static final String[] ABUSABLE_SEQUENCES = { "@", "[", "nbt", "=", "{", "}", "]" };

    @Inject(method = "handleCustomCommandSuggestions", at = @At("HEAD"), cancellable = true)
    private void fixSuggestionsCrash(ServerboundCommandSuggestionPacket serverboundCommandSuggestionPacket, CallbackInfo ci) {
        final String text = serverboundCommandSuggestionPacket.getCommand();
        int length = text.length();

        if(this.player.hasPermissions(2)) return;

        if (length > 256) {
            ci.cancel();
            return;
        }

        if (length > 64) {
            final int index = text.indexOf(' ');
            if (index == -1 || index >= 64) {
                ci.cancel();
                return;
            }
        }

        for (String sequence : ABUSABLE_SEQUENCES) {
            if (text.contains(sequence)) {
                ci.cancel();
                return;
            }
        }
    }

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
}
