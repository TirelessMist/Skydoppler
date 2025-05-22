package ae.skydoppler.behavior;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class AlwaysSprintState {

    public static boolean canSprint(ClientPlayerEntity player) {

        // Checks for vanilla sprinting conditions.
        // If none of these conditions are met, the player can sprint (return true).
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

    public static boolean shouldNotDoAlwaysSprint() {

        if (SkydopplerClient.CONFIG.alwaysSprint) {
            if (SkydopplerClient.CONFIG.alwaysSprintOnlyInSkyblock) {
                return !SkydopplerClient.isPlayingSkyblock;
            }
            return false;
        }
        return true;
    }

}
