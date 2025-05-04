package ae.skydoppler;

import ae.skydoppler.skyblock_locations.SkyblockLocation;

import java.util.List;

public class HideHubPlayersState {
    public static final List<SkyblockLocation> hidePlayersInListOfLocations = List.of(SkyblockLocation.THE_VILLAGE, SkyblockLocation.DUNGEON_HUB);
    public static double showRange = 12.0 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;
}
