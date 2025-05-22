package ae.skydoppler.player_hiding;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock_locations.SkyblockIslandEnum;

public class HideHubPlayersState {
    public static int showRange = 12 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;

    public static boolean shouldDoHubHiding() {

        if (!SkydopplerClient.CONFIG.doFarPlayerHiding) return false;

        switch (SkydopplerClient.CONFIG.hideFarPlayersMode) {
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
