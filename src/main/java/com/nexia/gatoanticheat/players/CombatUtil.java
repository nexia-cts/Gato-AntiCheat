package com.nexia.gatoanticheat.players;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 *  Credits to <a href="https://github.com/Blumbo/CTS-AntiCheat/tree/master">Blumbo's CTS Anti-Cheat</a> <br>
 *  <h4>Licensed under MIT</h4> <br>
 *  General Combat improvement utility used across several classes
 */


public class CombatUtil {

    // The amount of ticks players' previous locations are saved for
    // A somewhat good rule for the optimal amount of ticks is ceiling(x/50ms) + 2
    // where x is the highest ping (in milliseconds) the anticheat takes into account.
    // E.g. if the highest "accepted" ping is 151ms-200ms this number would be 6, for 251-300ms it would be 8 etc.
    public static int savedLocationTicks = 8;

    // The amount of extra reach the player gets
    // e.g. player is 3 blocks away client side but is 4 blocks away server side
    // (assuming padding is 1 block) the hit goes through

    // 0.25 is vanilla padding
    // e.g. 0.75 is actually 1 block of padding
    public static final float padding = 0.75f;

    // If target is not in reach (possibly due to ping) check if target's previous locations are in reach
    public static boolean allowReach(ServerPlayer attacker, Entity entityTarget) {
        Vec3 eyePosition = attacker.getEyePosition(0);
        double reach = attacker.getCurrentAttackReach(1f) + padding;
        if (!attacker.canSee(entityTarget)) reach = 2.5;
        reach *= reach;

        if (canReach(eyePosition, entityTarget.getBoundingBox(), reach)) return true;

        if(entityTarget instanceof ServerPlayer target) {
            PlayerData victimData = PlayerData.get(target);
            for (AABB boundingBox : victimData.previousPositions) {
                if (boundingBox == null) continue;
                if (canReach(eyePosition, boundingBox, reach)) return true;
            }
        }


        return false;
    }

    public static boolean canReach(Vec3 eyePosition, AABB boundingBox, double reach) {
        return eyePosition.distanceToSqr(boundingBox.getNearestPointTo(eyePosition)) < reach;
    }

    // Update players' previous positions every tick
    public static void setPosition(ServerPlayer player) {
        PlayerData playerData = PlayerData.get(player);

        playerData.positionIndex++;
        if (playerData.positionIndex >= playerData.previousPositions.length) playerData.positionIndex = 0;

        playerData.previousPositions[playerData.positionIndex] = player.getBoundingBox();
    }

    // Decrease invulnerability ticks for non-fast hits to prevent no-regs for fast attacks
    public static int modifyInvulnerableTicks(int original) {
        if (original >= 4) return original - 1;
        return original;
    }


    public static boolean validAngle(ServerPlayer player, Entity target) {
        return player.canSee(target);
    }
}
