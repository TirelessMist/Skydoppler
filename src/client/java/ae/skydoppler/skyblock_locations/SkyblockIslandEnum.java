package ae.skydoppler.skyblock_locations;

import java.util.Collection;
import java.util.List;

public enum SkyblockIslandEnum {
    // These are the different islands in Hypixel Skyblock

    NONE("None", SkyblockIslandType.NONE, List.of("None")),

    PRIVATE_ISLAND("Private Island", SkyblockIslandType.NONE, List.of("Your Island")),

    HUB("Hub", SkyblockIslandType.HUB, List.of("Archery Range", "Artist's Abode", "Auction House", "Bank",
            "Bazaar Alley", "Blacksmith", "Builder's House", "Canvas Room",
            "Coal Mine", "Colosseum", "Community Center", "Election Room",
            "Farm", "Farmhouse", "Fashion Shop", "Fisherman's Hut",
            "Flower House", "Thaumaturgist", "Village", "Weaponsmith", "Wilderness", "Wizard Tower", "Fishing Outpost")),

    DUNGEON_HUB("Dungeon Hub", SkyblockIslandType.HUB, List.of("Dungeon Hub")),
    DUNGEON("Dungeon", SkyblockIslandType.DUNGEON_ISLAND, List.of("Dungeon", "The Catacombs")),

    CRIMSON_ISLE("Crimson Isle", SkyblockIslandType.COMBAT_ISLAND, List.of("Crimson Isle", "Stronghold", "Blazing Volcano", "Scarleton", "Mystic Marsh", "Dragontail", "Burning Desert", "Phlegblast Pool")),
    SPIDER_DEN("Spider's Den", SkyblockIslandType.COMBAT_ISLAND, List.of("Spider Den")),
    THE_END("The End", SkyblockIslandType.COMBAT_ISLAND, List.of("The End", "Dragon's Nest", "Zealot Bruiser Hideout", "End Slate")),

    GOLD_MINE("Gold Mine", SkyblockIslandType.MINING_ISLAND, List.of("Gold Mine")),
    DEEP_CAVERNS("Deep Caverns", SkyblockIslandType.MINING_ISLAND, List.of("Deep Caverns", "Lapis Quarry", "Gold Mine", "Diamond Mine", "Emerald Mine", "Obsidian Mine")),
    DWARVEN_MINES("Dwarven Mines", SkyblockIslandType.MINING_ISLAND, List.of("Dwarven Mines")),
    CRYSTAL_HOLLOWS("Crystal Hollows", SkyblockIslandType.MINING_ISLAND, List.of("Crystal Hollows", "Crystal Nucleus", "Goblin Holdout", "Magma Fields", "Jungle Temple", "Mines of Divan", "Jungle", "Precursor Remnants", "Mithril Deposits", "Fairy Grotto")),
    GLACITE_TUNNELS("Glacite Tunnels", SkyblockIslandType.MINING_ISLAND, List.of("Glacite Mineshaft", "Glacite Tunnels")),

    JERRY_ISLAND("Jerry Island", SkyblockIslandType.NONE, List.of("Jerry's Workshop", "Glacial Cave", "Hot Springs")),

    BACKWATER_BAYOU("Backwater Bayou", SkyblockIslandType.FISHING_ISLAND, List.of("Backwater Bayou")),

    THE_PARK("The Park", SkyblockIslandType.FORAGING_ISLAND, List.of("The Park")),

    THE_GARDEN("The Garden", SkyblockIslandType.FARMING_ISLAND, List.of("The Garden")),
    THE_BARN("The Barn", SkyblockIslandType.FARMING_ISLAND, List.of("The Barn")),
    MUSHROOM_DESERT("Mushroom Desert", SkyblockIslandType.FARMING_ISLAND, List.of("Mushroom Desert")),

    THE_RIFT("The Rift", SkyblockIslandType.RIFT_ISLAND, List.of("The Rift", "Mountaintop"));


    private final String name;
    private final SkyblockIslandType islandType;
    private final List<String> literalNames; // the name that is used on the scoreboard, case-sensitive (e.g. "The Park")

    SkyblockIslandEnum(String name, SkyblockIslandType islandType, List<String> literalNames) {
        this.name = name;
        this.islandType = islandType;
        this.literalNames = literalNames;
    }

    public static boolean isValidLocation(String location) {
        for (SkyblockIslandEnum loc : SkyblockIslandEnum.values()) {
            if (loc.name.equalsIgnoreCase(location)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public SkyblockIslandType getIslandType() {
        return islandType;
    }

    public List<String> getLiteralNames() {
        return literalNames;
    }
}