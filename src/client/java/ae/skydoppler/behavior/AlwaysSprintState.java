/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ae.skydoppler.behavior;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class AlwaysSprintState {

    public static boolean canSprint(ClientPlayerEntity player) {

        // Checks for vanilla sprinting conditions
        return !player.horizontalCollision &&
                !player.isSwimming() &&
                !player.isSneaking() &&
                !player.isBlocking() &&
                !player.isDescending() &&
                !player.isClimbing() &&
                !player.isUsingItem() &&
                !player.isUsingRiptide() &&
                !player.isSpectator() &&
                !player.isCrawling();
    }

    public static boolean shouldDoAlwaysSprint() {

        if (SkydopplerClient.CONFIG.alwaysSprint) {
            if (SkydopplerClient.CONFIG.alwaysSprintOnlyInSkyblock) {
                return SkydopplerClient.isPlayingSkyblock;
            }
            return true;
        }
        return false;
    }

}
