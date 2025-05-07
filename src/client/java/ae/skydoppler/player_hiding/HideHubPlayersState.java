package ae.skydoppler.player_hiding;

public class HideHubPlayersState {
    private enum HideLocationMode {
        VILLAGE_ONLY, HUB_ENTIRE, DUNGEON_AND_NORMAL_HUB, DUNGEON_AND_VILLAGE
    }

    public static double showRange = 12.0 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;

}
