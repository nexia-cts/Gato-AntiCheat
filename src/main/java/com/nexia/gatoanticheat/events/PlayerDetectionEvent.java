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

    public static final Event<NoSwing> NO_SWING = EventFactory.createArrayBacked(NoSwing.class,
            (listeners) -> (player, target) -> {
                for (NoSwing event : listeners) {
                    event.onNoSwingDetection(player, target);
                }
            }
    );

    public static final Event<BadAttackAngle> BAD_ATTACK_ANGLE = EventFactory.createArrayBacked(BadAttackAngle.class,
            (listeners) -> (player, target) -> {
                for (BadAttackAngle event : listeners) {
                    event.onBadAttackAngleDetection(player, target);
                }
            }
    );

    public static final Event<Criticals> CRITICALS = EventFactory.createArrayBacked(Criticals.class,
            (listeners) -> (player, target) -> {
                for (Criticals event : listeners) {
                    event.onCriticalsDetection(player, target);
                }
            }
    );

    @FunctionalInterface
    public interface Reach {
        void onReachDetection(Player player, Entity target);
    }

    @FunctionalInterface
    public interface NoSwing {
        void onNoSwingDetection(Player player, Entity target);
    }

    @FunctionalInterface
    public interface Criticals {
        void onCriticalsDetection(Player player, Entity target);
    }

    @FunctionalInterface
    public interface BadAttackAngle {
        void onBadAttackAngleDetection(Player player, Entity target);
    }
}
