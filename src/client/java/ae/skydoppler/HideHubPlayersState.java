package ae.skydoppler;

import ae.skydoppler.skyblock_locations.SkyblockIslandEnum;

import java.util.List;

public class HideHubPlayersState {
    public static final List<SkyblockIslandEnum> hidePlayersInListOfLocations = List.of(SkyblockIslandEnum.HUB, SkyblockIslandEnum.DUNGEON_HUB);
    public static double showRange = 12.0 /*0.1 * MinecraftClient.getInstance().worldRenderer.getViewDistance()*/;
}
