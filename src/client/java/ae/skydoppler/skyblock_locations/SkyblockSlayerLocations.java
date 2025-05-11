package ae.skydoppler.skyblock_locations;

public enum SkyblockSlayerLocations {
    ZOMBIE(new Enum[]{SkyblockIslandEnum.HubZones.CATACOMBS_ENTRANCE, SkyblockIslandEnum.HubZones.COAL_MINE, SkyblockIslandEnum.HubZones.GRAVEYARD}),
    SPIDER(new Enum[]{SkyblockIslandEnum.SPIDER_DEN, SkyblockIslandEnum.CrimsonIsleZones.BURNING_DESERT}),
    WOLF(new Enum[] {SkyblockIslandEnum.HubZones.RUINS, SkyblockIslandEnum.TheParkZones.HOWLING_CAVE}),
    ENDERMAN(new Enum[] {SkyblockIslandEnum.THE_END}),
    BLAZE(new Enum[] {SkyblockIslandEnum.CrimsonIsleZones.STRONGHOLD, SkyblockIslandEnum.CrimsonIsleZones.SMOLDERING_TOMB}),
    VAMPIRE(new Enum[] {SkyblockIslandEnum.RiftRegions.VAMPIRE});

    private final Enum<?>[] locations;

    SkyblockSlayerLocations(Enum<?>[] locations) {
        this.locations = locations;
    }
}
