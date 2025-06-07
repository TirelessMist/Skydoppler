package ae.skydoppler.player_hiding;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock_locations.SkyblockIslandCategoryEnum;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public class PlayerHidingHelper {

    public static boolean doHideFishingEntities = false;

    public static Collection<Vec3d> npcPositions = new java.util.ArrayList<>();

    /**
     * Determines whether hub hiding should be enabled based on the Config file settings
     * and the player's current Skyblock Location.
     *
     * @return true if hub hiding should be enabled, false otherwise.
     */
    public static boolean shouldDoHubHiding() {

        // Check if the far player hiding feature is disabled in the configuration.
        if (!SkydopplerClient.CONFIG.doFarPlayerHiding) return false;

        // Evaluate the hiding mode and the player's current location.
        switch (SkydopplerClient.CONFIG.hideFarPlayersMode) {
            case HubHideLocationMode.HUB_ENTIRE -> {
                // Enable hiding if the player is on the HUB island.
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.HUB;
            }
            case HubHideLocationMode.DUNGEON_AND_NORMAL_HUB -> {
                // Enable hiding if the player is on the DUNGEON_HUB or HUB island.
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON_HUB || SkydopplerClient.currentIsland == SkyblockLocationEnum.HUB;
            }
            case HubHideLocationMode.VILLAGE_ONLY -> {
                // Enable hiding if the player is in the VILLAGE zone of the HUB.
                return SkydopplerClient.currentRegion == SkyblockLocationEnum.HubRegions.VILLAGE;
            }
            case HubHideLocationMode.DUNGEON_AND_VILLAGE -> {
                // Enable hiding if the player is on the DUNGEON_HUB island or in the VILLAGE region of the HUB.
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON_HUB || (SkydopplerClient.currentRegion == SkyblockLocationEnum.HubRegions.VILLAGE);
            }
        }
        // Default to false if no conditions are met.
        return false;
    }

    public static boolean shouldDoFishingPlayerHiding() {
        // Check if the player is fishing and if the fishing player hiding feature is enabled in the configuration.
        return SkydopplerClient.CONFIG.hidePlayersWhileFishing && SkydopplerClient.currentIsland.getIslandType() == SkyblockIslandCategoryEnum.FISHING_ISLAND;
    }

    public static boolean isPlayerAnNpc(Entity player) {
        return player.getUuid().version() != 4;
    }

    public enum HubHideLocationMode {
        VILLAGE_ONLY, HUB_ENTIRE, DUNGEON_AND_NORMAL_HUB, DUNGEON_AND_VILLAGE
    }
}

