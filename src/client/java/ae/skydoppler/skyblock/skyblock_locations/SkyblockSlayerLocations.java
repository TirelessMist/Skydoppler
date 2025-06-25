package ae.skydoppler.skyblock.skyblock_locations;

public enum SkyblockSlayerLocations {
    ZOMBIE(new Enum[]{SkyblockLocationEnum.HubZones.CATACOMBS_ENTRANCE, SkyblockLocationEnum.HubZones.COAL_MINE, SkyblockLocationEnum.HubZones.GRAVEYARD}),
    SPIDER(new Enum[]{SkyblockLocationEnum.SPIDER_DEN, SkyblockLocationEnum.CrimsonIsleZones.BURNING_DESERT}),
    WOLF(new Enum[] {SkyblockLocationEnum.HubZones.RUINS, SkyblockLocationEnum.TheParkZones.HOWLING_CAVE}),
    ENDERMAN(new Enum[] {SkyblockLocationEnum.THE_END}),
    BLAZE(new Enum[] {SkyblockLocationEnum.CrimsonIsleZones.STRONGHOLD, SkyblockLocationEnum.CrimsonIsleZones.SMOLDERING_TOMB}),
    VAMPIRE(new Enum[] {SkyblockLocationEnum.RiftRegions.VAMPIRE});

    private final Enum<?>[] locations;

    SkyblockSlayerLocations(Enum<?>[] locations) {
        this.locations = locations;
    }
}
