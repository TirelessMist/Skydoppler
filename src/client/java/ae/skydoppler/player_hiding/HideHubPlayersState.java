package ae.skydoppler.player_hiding;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;

public class HideHubPlayersState {
    public static int showRange = 12 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;

    public static boolean shouldDoHubHiding() {

        if (!SkydopplerClient.CONFIG.doFarPlayerHiding) return false;

        switch (SkydopplerClient.CONFIG.hideFarPlayersMode) {
            case HideHubPlayersState.HideLocationMode.HUB_ENTIRE -> {
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.HUB;
            }
            case HideHubPlayersState.HideLocationMode.DUNGEON_AND_NORMAL_HUB -> {
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON_HUB || SkydopplerClient.currentIsland == SkyblockLocationEnum.HUB;
            }
            case HideHubPlayersState.HideLocationMode.VILLAGE_ONLY -> {
                return SkydopplerClient.currentZone == SkyblockLocationEnum.HubZones.VILLAGE;
            }
            case HideHubPlayersState.HideLocationMode.DUNGEON_AND_VILLAGE -> {
                return SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON_HUB || (SkydopplerClient.currentRegion == SkyblockLocationEnum.HubRegions.VILLAGE);
            }
        }
        return false;
    }

    public enum HideLocationMode {
        VILLAGE_ONLY, HUB_ENTIRE, DUNGEON_AND_NORMAL_HUB, DUNGEON_AND_VILLAGE;

        private final String translationKey;

        HideLocationMode() {
            this.translationKey = "config.ae.skydoppler.option." + this.name().toLowerCase();
        }

        public String getTranslationKey() {
            return translationKey;
        }
    }

}
