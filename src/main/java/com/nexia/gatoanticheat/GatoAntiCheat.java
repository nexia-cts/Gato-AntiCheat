package com.nexia.gatoanticheat;

import com.nexia.gatoanticheat.players.PlayerData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class GatoAntiCheat implements ModInitializer {

    @Override
    public void onInitialize() {

        // JOIN
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerData.addPlayerData(handler.player);
        });

        // QUIT
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            PlayerData.removePlayerData(handler.player);
        }));

    }

}
