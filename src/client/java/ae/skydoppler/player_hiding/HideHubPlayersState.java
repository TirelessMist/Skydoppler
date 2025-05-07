package ae.skydoppler.player_hiding;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock_locations.SkyblockIslandEnum;

public class HideHubPlayersState {
    public enum HideLocationMode {
        VILLAGE_ONLY, HUB_ENTIRE, DUNGEON_AND_NORMAL_HUB, DUNGEON_AND_VILLAGE
    }

    public static HideLocationMode hideLocationMode = HideLocationMode.DUNGEON_AND_NORMAL_HUB;
    public static double showRange = 12.0 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;

    public static boolean shouldHidePlayers() {
        switch (HideHubPlayersState.hideLocationMode) {
            case HideHubPlayersState.HideLocationMode.HUB_ENTIRE -> {
                return SkydopplerClient.currentIsland == SkyblockIslandEnum.HUB;
            }
            case HideHubPlayersState.HideLocationMode.DUNGEON_AND_NORMAL_HUB -> {
                return SkydopplerClient.currentIsland == SkyblockIslandEnum.DUNGEON_HUB || SkydopplerClient.currentIsland == SkyblockIslandEnum.HUB;
            }
            case HideHubPlayersState.HideLocationMode.VILLAGE_ONLY -> {
                return SkydopplerClient.currentIsland == SkyblockIslandEnum.HUB && SkydopplerClient.currentZone == SkyblockIslandEnum.HubZones.VILLAGE;
            }
            case HideHubPlayersState.HideLocationMode.DUNGEON_AND_VILLAGE -> {
                return SkydopplerClient.currentIsland == SkyblockIslandEnum.DUNGEON_HUB || (SkydopplerClient.currentIsland == SkyblockIslandEnum.HUB && SkydopplerClient.currentZone == SkyblockIslandEnum.HubZones.VILLAGE);
            }
        }
        return false;
    }

}
