package com.nexia.gatoanticheat.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class PlayerDetectionEvent {

    private PlayerDetectionEvent() {
    }

    public static final Event<Reach> REACH = EventFactory.createArrayBacked(Reach.class,
            (listeners) -> (player, target) -> {
                for (Reach event : listeners) {
                    event.onReachDetection(player, target);
                }
            }
    );

    @FunctionalInterface
    public interface Reach {
        void onReachDetection(Player player, Entity target);
    }
}
