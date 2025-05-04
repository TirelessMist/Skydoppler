package ae.skydoppler.skyblock_locations;

public enum SkyblockLocation {
    NONE("None"),
    PRIVATE_ISLAND("Private Island"),
    HUB("Hub"),
    DUNGEON("Dungeon"),
    DUNGEON_HUB("Dungeon Hub"),
    CRIMSON_ISLE("Crimson Isle"),
    SPIDER_CAVE("Spider Cave"),
    THE_END("The End"),
    THE_NETHER("The Nether"),
    THE_WILD("The Wild"),
    THE_VILLAGE("The Village"),
    THE_GARDEN("The Garden"),
    THE_CAVES("The Caves");

    private final String name;

    SkyblockLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
