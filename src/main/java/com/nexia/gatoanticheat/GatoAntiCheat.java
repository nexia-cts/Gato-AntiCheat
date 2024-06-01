package com.nexia.gatoanticheat;

import com.nexia.gatoanticheat.players.PlayerData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

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

    }

}
