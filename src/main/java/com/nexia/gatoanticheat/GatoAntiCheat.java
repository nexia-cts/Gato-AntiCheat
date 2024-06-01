package com.nexia.gatoanticheat;

import com.nexia.gatoanticheat.events.PlayerDetectionEvent;
import com.nexia.gatoanticheat.players.CombatUtil;
import com.nexia.gatoanticheat.players.PlayerData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class GatoAntiCheat implements ModInitializer {

    @Override
    public void onInitialize() {

        // JOIN
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

            /**
             *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat">Blumbo's CTS Anti-Cheat</a>
             *  <h4>Licensed under MIT</h4> <br>
             *  Creates player data when they join the server
             */

            PlayerData.addPlayerData(handler.player);
        });

        // QUIT
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {

            /**
             *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
             *  <h4>Licensed under MIT</h4> <br>
             *  Clears player data when they exit the server
             */

            PlayerData.removePlayerData(handler.player);
        }));


        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                if (!CombatUtil.allowReach(serverPlayer, entity)) {
                    PlayerDetectionEvent.REACH.invoker().onReachDetection(player, entity);
                    return InteractionResult.FAIL;
                }
            }

            return InteractionResult.PASS;
        });
    }

}
