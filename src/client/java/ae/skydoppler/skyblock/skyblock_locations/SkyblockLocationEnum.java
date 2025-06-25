package ae.skydoppler.skyblock.skyblock_locations;

public enum SkyblockLocationEnum {

    NONE(SkyblockIslandCategoryEnum.NONE, NoneZones.class),

    PRIVATE_ISLAND(SkyblockIslandCategoryEnum.PRIVATE_ISLAND, PrivateIslandZones.class),

    HUB(SkyblockIslandCategoryEnum.HUB_ISLAND, HubZones.class),

    DUNGEON_HUB(SkyblockIslandCategoryEnum.HUB_ISLAND, DungeonHubZones.class),
    DUNGEON(SkyblockIslandCategoryEnum.DUNGEON_ISLAND, DungeonZones.class),

    CRIMSON_ISLE(SkyblockIslandCategoryEnum.COMBAT_ISLAND, CrimsonIsleZones.class),
    KUUDRA(SkyblockIslandCategoryEnum.KUUDRA_ISLAND, KuudraZones.class),
    SPIDER_DEN(SkyblockIslandCategoryEnum.COMBAT_ISLAND, SpiderDenZones.class),
    THE_END(SkyblockIslandCategoryEnum.COMBAT_ISLAND, TheEndZones.class),

    GOLD_MINE(SkyblockIslandCategoryEnum.MINING_ISLAND, GoldMineZones.class),
    DEEP_CAVERNS(SkyblockIslandCategoryEnum.MINING_ISLAND, DeepCavernsZones.class),
    DWARVEN_MINES(SkyblockIslandCategoryEnum.MINING_ISLAND, DwarvenMinesZones.class),
    CRYSTAL_HOLLOWS(SkyblockIslandCategoryEnum.MINING_ISLAND, CrystalHollowsZones.class),

    JERRY_ISLAND(SkyblockIslandCategoryEnum.SPECIAL_ISLAND, JerryIslandZones.class),

    BACKWATER_BAYOU(SkyblockIslandCategoryEnum.FISHING_ISLAND, BackwaterBayouZones.class),

    THE_PARK(SkyblockIslandCategoryEnum.FORAGING_ISLAND, TheParkZones.class),

    THE_GARDEN(SkyblockIslandCategoryEnum.FARMING_ISLAND, TheGardenZones.class),
    THE_BARN(SkyblockIslandCategoryEnum.FARMING_ISLAND, TheBarnZones.class),
    MUSHROOM_DESERT(SkyblockIslandCategoryEnum.FARMING_ISLAND, MushroomDesertZones.class),

    THE_RIFT(SkyblockIslandCategoryEnum.RIFT_ISLAND, RiftZones.class);

    private final SkyblockIslandCategoryEnum islandType;
    private final Class<? extends Enum<?>> zoneClass;

    SkyblockLocationEnum(SkyblockIslandCategoryEnum islandType, Class<? extends Enum<?>> zoneClass) {
        this.islandType = islandType;
        this.zoneClass = zoneClass;
    }

    public SkyblockIslandCategoryEnum getIslandType() {
        return islandType;
    }

    public Class<? extends Enum<?>> getZoneClass() {
        return zoneClass;
    }

    public Enum<?>[] getZonesForIsland() {
        if (zoneClass == null) {
            return new Enum<?>[0]; // Return an empty array if no zones are defined
        }
        return zoneClass.getEnumConstants(); // Retrieve all enum constants for the zone class
    }

    public interface EnumKey {
        String getKey();
    }

    public interface EnumRegion {
        Enum<?> getRegion();
    }

    public enum HubRegions {
        VILLAGE,
        CRYPTS;
    }

    public enum CrystalHollowsRegions {
        NUCLEUS,
        JUNGLE,
        GOBLIN_HOLDOUT,
        MITHRIL_DEPOSITS,
        PRECURSOR_REMNANTS,
        MAGMA_FIELDS;
    }

    public enum CrimsonIsleRegions {
        SCARLETON,
        DRAGONTAIL;
    }

    public enum RiftRegions {
        WYLD_WOODS,
        LAGOON,
        FARM,
        WEST_VILLAGE,
        LIVING_STILLNESS,
        PLAZA,
        VAMPIRE,
        MOUNTAINTOP,
        WIZARD_TOWER,
        LEECH_SUPREME,
        BACTE,
        MIRRORVERSE,
        SUN_GOD;
    }


    public enum NoneZones implements EnumKey {
        NONE("hypixel.skyblock.location.none.locations.none");

        private final String key;

        NoneZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum PrivateIslandZones implements EnumKey {
        PRIVATE_ISLAND("hypixel.skyblock.location.private_island.locations.private_island");

        private final String key;

        PrivateIslandZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum HubZones implements EnumKey, EnumRegion {
        HUB("hypixel.skyblock.location.hub.locations.hub"),
        VILLAGE("hypixel.skyblock.location.hub.locations.village", HubRegions.VILLAGE),
        BAZAAR("hypixel.skyblock.location.hub.locations.bazaar", HubRegions.VILLAGE),
        AUCTION_HOUSE("hypixel.skyblock.location.hub.locations.auction_house", HubRegions.VILLAGE),
        BANK("hypixel.skyblock.location.hub.locations.bank", HubRegions.VILLAGE),
        COMMUNITY_CENTER("hypixel.skyblock.location.hub.locations.community_center", HubRegions.VILLAGE),
        ELECTION_ROOM("hypixel.skyblock.location.hub.locations.election_room"),
        MUSEUM("hypixel.skyblock.location.hub.locations.museum"),
        ARCHERY_RANGE("hypixel.skyblock.location.hub.locations.archery_range"),
        ARTIST_ABODE("hypixel.skyblock.location.hub.locations.artist_abode"),
        BLACKSMITH("hypixel.skyblock.location.hub.locations.blacksmith"),
        BUILDER_HOUSE("hypixel.skyblock.location.hub.locations.builder_house"),
        CANVAS_ROOM("hypixel.skyblock.location.hub.locations.canvas_room"),
        COAL_MINE("hypixel.skyblock.location.hub.locations.coal_mine", HubRegions.CRYPTS),
        COLOSSEUM("hypixel.skyblock.location.hub.locations.colosseum"),
        FARM("hypixel.skyblock.location.hub.locations.farm"),
        FARMHOUSE("hypixel.skyblock.location.hub.locations.farmhouse"),
        FASHION_SHOP("hypixel.skyblock.location.hub.locations.fashion_shop"),
        FISHERMAN_HUT("hypixel.skyblock.location.hub.locations.fisherman_hut"),
        FLOWER_HOUSE("hypixel.skyblock.location.hub.locations.flower_house"),
        FOREST("hypixel.skyblock.location.hub.locations.forest"),
        GRAVEYARD("hypixel.skyblock.location.hub.locations.graveyard", HubRegions.CRYPTS),
        CATACOMBS_ENTRANCE("hypixel.skyblock.location.hub.locations.catacombs_entrance", HubRegions.CRYPTS),
        HEXATORUM("hypixel.skyblock.location.hub.locations.hexatorum"),
        UNINCORPORATED("hypixel.skyblock.location.hub.locations.unincorporated"),
        LIBRARY("hypixel.skyblock.location.hub.locations.library"),
        MOUNTAIN("hypixel.skyblock.location.hub.locations.mountain"),
        PET_CARE("hypixel.skyblock.location.hub.locations.pet_care"),
        RABBIT_HOUSE("hypixel.skyblock.location.hub.locations.rabbit_house"),
        RUINS("hypixel.skyblock.location.hub.locations.ruins"),
        SHEN_AUCTION("hypixel.skyblock.location.hub.locations.shen_auction"),
        TAVERN("hypixel.skyblock.location.hub.locations.tavern"),
        THAUMATURGIST("hypixel.skyblock.location.hub.locations.thaumaturgist"),
        WEAPONSMITH("hypixel.skyblock.location.hub.locations.weaponsmith"),
        WILDERNESS("hypixel.skyblock.location.hub.locations.wilderness"),
        WIZARD_TOWER("hypixel.skyblock.location.hub.locations.wizard_tower"),
        FISHING_OUTPOST("hypixel.skyblock.location.hub.locations.fishing_outpost");

        private final String key;
        private final Enum<?> region;

        HubZones(String key, Enum<?> region) {
            this.key = key;
            this.region = region;
        }

        HubZones(String key) {
            this(key, null); // Default the region to null if not provided
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum TheGardenZones implements EnumKey {
        THE_GARDEN("hypixel.skyblock.location.the_garden.locations.the_garden"),
        PLOT_1("hypixel.skyblock.location.the_garden.locations.plot_1"),
        PLOT_2("hypixel.skyblock.location.the_garden.locations.plot_2"),
        PLOT_3("hypixel.skyblock.location.the_garden.locations.plot_3"),
        PLOT_4("hypixel.skyblock.location.the_garden.locations.plot_4"),
        PLOT_5("hypixel.skyblock.location.the_garden.locations.plot_5"),
        PLOT_6("hypixel.skyblock.location.the_garden.locations.plot_6"),
        PLOT_7("hypixel.skyblock.location.the_garden.locations.plot_7"),
        PLOT_8("hypixel.skyblock.location.the_garden.locations.plot_8"),
        PLOT_9("hypixel.skyblock.location.the_garden.locations.plot_9"),
        PLOT_10("hypixel.skyblock.location.the_garden.locations.plot_10"),
        PLOT_11("hypixel.skyblock.location.the_garden.locations.plot_11"),
        PLOT_12("hypixel.skyblock.location.the_garden.locations.plot_12"),
        PLOT_13("hypixel.skyblock.location.the_garden.locations.plot_13"),
        PLOT_14("hypixel.skyblock.location.the_garden.locations.plot_14"),
        PLOT_15("hypixel.skyblock.location.the_garden.locations.plot_15"),
        PLOT_16("hypixel.skyblock.location.the_garden.locations.plot_16"),
        PLOT_17("hypixel.skyblock.location.the_garden.locations.plot_17"),
        PLOT_18("hypixel.skyblock.location.the_garden.locations.plot_18"),
        PLOT_19("hypixel.skyblock.location.the_garden.locations.plot_19"),
        PLOT_20("hypixel.skyblock.location.the_garden.locations.plot_20"),
        PLOT_21("hypixel.skyblock.location.the_garden.locations.plot_21"),
        PLOT_22("hypixel.skyblock.location.the_garden.locations.plot_22"),
        PLOT_23("hypixel.skyblock.location.the_garden.locations.plot_23"),
        PLOT_24("hypixel.skyblock.location.the_garden.locations.plot_24");

        private final String key;

        TheGardenZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum TheBarnZones implements EnumKey {
        BARN("hypixel.skyblock.location.the_barn.locations.barn"),
        WINDMILL("hypixel.skyblock.location.the_barn.locations.windmill");

        private final String key;

        TheBarnZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum MushroomDesertZones implements EnumKey {
        MUSHROOM_DESERT("hypixel.skyblock.location.mushroom_desert.locations.mushroom_desert"),
        DESERT_SETTLEMENT("hypixel.skyblock.location.mushroom_desert.locations.desert_settlement"),
        GLOWING_MUSHROOM_CAVE("hypixel.skyblock.location.mushroom_desert.locations.glowing_mushroom_cave"),
        JAKE_HOUSE("hypixel.skyblock.location.mushroom_desert.locations.jake_house"),
        MUSHROOM_GORGE("hypixel.skyblock.location.mushroom_desert.locations.mushroom_gorge"),
        OASIS("hypixel.skyblock.location.mushroom_desert.locations.oasis"),
        OVERGROWN_MUSHROOM_CAVE("hypixel.skyblock.location.mushroom_desert.locations.overgrown_mushroom_cave"),
        SHEPHERD_KEEP("hypixel.skyblock.location.mushroom_desert.locations.shepherd_keep"),
        TRAPPER_DEN("hypixel.skyblock.location.mushroom_desert.locations.trapper_den"),
        TREASURE_HUNTER_CAMP("hypixel.skyblock.location.mushroom_desert.locations.treasure_hunter_camp");

        private final String key;

        MushroomDesertZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum TheParkZones implements EnumKey {
        THE_PARK("hypixel.skyblock.location.the_park.locations.the_park"),
        BIRCH_PARK("hypixel.skyblock.location.the_park.locations.birch_park"),
        HOWLING_CAVE("hypixel.skyblock.location.the_park.locations.howling_cave"),
        SPRUCE_WOODS("hypixel.skyblock.location.the_park.locations.spruce_woods"),
        LONELY_ISLAND("hypixel.skyblock.location.the_park.locations.lonely_island"),
        VIKING_LONGHOUSE("hypixel.skyblock.location.the_park.locations.viking_longhouse"),
        DARK_THICKET("hypixel.skyblock.location.the_park.locations.dark_thicket"),
        SAVANNA_WOODLAND("hypixel.skyblock.location.the_park.locations.savanna_woodland"),
        MELODY_PLATEAU("hypixel.skyblock.location.the_park.locations.melody_plateau"),
        JUNGLE_ISLAND("hypixel.skyblock.location.the_park.locations.jungle_island");

        private final String key;

        TheParkZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum SpiderDenZones implements EnumKey {
        SPIDER_DEN("hypixel.skyblock.location.spider_den.locations.spider_den"),
        SPIDER_MOUND("hypixel.skyblock.location.spider_den.locations.spider_mound"),
        GRAVEL_MINES("hypixel.skyblock.location.spider_den.locations.gravel_mines"),
        GRANDMA_HOUSE("hypixel.skyblock.location.spider_den.locations.grandma_house"),
        ARACHNE_BURROW("hypixel.skyblock.location.spider_den.locations.arachne_burrow"),
        ARACHNE_SANCTUARY("hypixel.skyblock.location.spider_den.locations.arachne_sanctuary"),
        ARCHAEOLOGIST_CAMP("hypixel.skyblock.location.spider_den.locations.archaeologist_camp");

        private final String key;

        SpiderDenZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum TheEndZones implements EnumKey {
        THE_END("hypixel.skyblock.location.the_end.locations.the_end"),
        DRAGON_NEST("hypixel.skyblock.location.the_end.locations.dragon_nest"),
        VOID_SEPULTURE("hypixel.skyblock.location.the_end.locations.void_sepulture"),
        VOID_SLATE("hypixel.skyblock.location.the_end.locations.void_slate"),
        ZEALOT_BRUISER_HIDEOUT("hypixel.skyblock.location.the_end.locations.zealot_bruiser_hideout");

        private final String key;

        TheEndZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum CrimsonIsleZones implements EnumKey, EnumRegion {
        CRIMSON_ISLE("hypixel.skyblock.location.crimson_isle.locations.crimson_isle"),
        STRONGHOLD("hypixel.skyblock.location.crimson_isle.locations.stronghold"),
        CRIMSON_FIELDS("hypixel.skyblock.location.crimson_isle.locations.crimson_fields"),
        BLAZING_VOLCANO("hypixel.skyblock.location.crimson_isle.locations.blazing_volcano"),
        ODGER_HUT("hypixel.skyblock.location.crimson_isle.locations.odger_hut"),
        PLHLEGBLAST_POOL("hypixel.skyblock.location.crimson_isle.locations.plhlegblast_pool"),
        MAGMA_CHAMBER("hypixel.skyblock.location.crimson_isle.locations.magma_chamber"),
        AURA_LAB("hypixel.skyblock.location.crimson_isle.locations.aura_lab"),
        MATRIARCH_LAIR("hypixel.skyblock.location.crimson_isle.locations.matriarch_lair"),
        BELLY_OF_THE_BEAST("hypixel.skyblock.location.crimson_isle.locations.belly_of_the_beast"),
        DOJO("hypixel.skyblock.location.crimson_isle.locations.dojo"),
        DOJO_ARENA("hypixel.skyblock.location.crimson_isle.locations.dojo_arena"),
        BURNING_DESERT("hypixel.skyblock.location.crimson_isle.locations.burning_desert"),
        MYSTIC_MARSH("hypixel.skyblock.location.crimson_isle.locations.mystic_marsh"),
        BARBARIAN_OUTPOST("hypixel.skyblock.location.crimson_isle.locations.barbarian_outpost"),
        MAGE_OUTPOST("hypixel.skyblock.location.crimson_isle.locations.mage_outpost"),
        DRAGONTAIL("hypixel.skyblock.location.crimson_isle.locations.dragontail", CrimsonIsleRegions.DRAGONTAIL),
        CHIEF_HUT("hypixel.skyblock.location.crimson_isle.locations.chief_hut", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BLACKSMITH("hypixel.skyblock.location.crimson_isle.locations.dragontail_blacksmith", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_TOWNSQUARE("hypixel.skyblock.location.crimson_isle.locations.dragontail_townsquare", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_AUCTION_HOUSE("hypixel.skyblock.location.crimson_isle.locations.dragontail_auction_house", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BAZAAR("hypixel.skyblock.location.crimson_isle.locations.dragontail_bazaar", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BANK("hypixel.skyblock.location.crimson_isle.locations.dragontail_bank", CrimsonIsleRegions.DRAGONTAIL),
        MINION_SHOP("hypixel.skyblock.location.crimson_isle.locations.minion_shop"),
        THE_DUKEDOM("hypixel.skyblock.location.crimson_isle.locations.the_dukedom"),
        THE_BASTION("hypixel.skyblock.location.crimson_isle.locations.the_bastion"),
        SCARLETON("hypixel.skyblock.location.crimson_isle.locations.scarleton", CrimsonIsleRegions.SCARLETON),
        COMMUNITY_CENTER("hypixel.skyblock.location.crimson_isle.locations.community_center", CrimsonIsleRegions.SCARLETON),
        THRONE_ROOM("hypixel.skyblock.location.crimson_isle.locations.throne_room", CrimsonIsleRegions.SCARLETON),
        MAGE_COUNCIL("hypixel.skyblock.location.crimson_isle.locations.mage_council", CrimsonIsleRegions.SCARLETON),
        SCARLETON_PLAZA("hypixel.skyblock.location.crimson_isle.locations.scarleton_plaza", CrimsonIsleRegions.SCARLETON),
        SCARLETON_MINION_SHOP("hypixel.skyblock.location.crimson_isle.locations.scarleton_minion_shop", CrimsonIsleRegions.SCARLETON),
        SCARLETON_AUCTION_HOUSE("hypixel.skyblock.location.crimson_isle.locations.scarleton_auction_house", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BAZAAR("hypixel.skyblock.location.crimson_isle.locations.scarleton_bazaar", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BANK("hypixel.skyblock.location.crimson_isle.locations.scarleton_bank", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BLACKSMITH("hypixel.skyblock.location.crimson_isle.locations.scarleton_blacksmith", CrimsonIsleRegions.SCARLETON),
        IGRUPAN_HOUSE("hypixel.skyblock.location.crimson_isle.locations.igrupan_house", CrimsonIsleRegions.SCARLETON),
        IGRUPAN_CHICKEN_COOP("hypixel.skyblock.location.crimson_isle.locations.igrupan_chicken_coop", CrimsonIsleRegions.SCARLETON),
        CATHEDRAL("hypixel.skyblock.location.crimson_isle.locations.cathedral", CrimsonIsleRegions.SCARLETON),
        COURTYARD("hypixel.skyblock.location.crimson_isle.locations.courtyard", CrimsonIsleRegions.SCARLETON),
        THE_WASTELAND("hypixel.skyblock.location.crimson_isle.locations.the_wasteland"),
        RUINS_OF_ASHFANG("hypixel.skyblock.location.crimson_isle.locations.ruins_of_ashfang"),
        FORGOTTEN_SKULL("hypixel.skyblock.location.crimson_isle.locations.forgotten_skull"),
        SMOLDERING_TOMB("hypixel.skyblock.location.crimson_isle.locations.smoldering_tomb");

        private final String key;
        private final Enum<?> region;

        CrimsonIsleZones(String key, Enum<?> region) {
            this.key = key;
            this.region = region;
        }

        CrimsonIsleZones(String key) {
            this(key, null); // Default the region to null if not provided
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum KuudraZones implements EnumKey {
        KUUDRA_HOLLOW("hypixel.skyblock.location.kuudra.locations.kuudra_hollow");

        private final String key;

        KuudraZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum GoldMineZones implements EnumKey {
        GOLD_MINE("hypixel.skyblock.location.gold_mine.locations.gold_mine");

        private final String key;

        GoldMineZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum DeepCavernsZones implements EnumKey {
        DEEP_CAVERNS("hypixel.skyblock.location.deep_caverns.locations.deep_caverns"),
        GUNPOWDER_MINES("hypixel.skyblock.location.deep_caverns.locations.gunpowder_mines"),
        LAPIS_QUARRY("hypixel.skyblock.location.deep_caverns.locations.lapis_quarry"),
        PIGMEN_DEN("hypixel.skyblock.location.deep_caverns.locations.pigmen_den"),
        SLIMEHILL("hypixel.skyblock.location.deep_caverns.locations.slimehill"),
        DIAMOND_RESERVE("hypixel.skyblock.location.deep_caverns.locations.diamond_reserve"),
        OBSIDIAN_SANCTUARY("hypixel.skyblock.location.deep_caverns.locations.obsidian_sanctuary");

        private final String key;

        DeepCavernsZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum DwarvenMinesZones implements EnumKey {
        DWARVEN_MINES("hypixel.skyblock.location.dwarven_mines.locations.dwarven_mines"),
        ARISTOCRAT_PASSAGE("hypixel.skyblock.location.dwarven_mines.locations.aristocrat_passage"),
        BARRACKS_OF_HEROES("hypixel.skyblock.location.dwarven_mines.locations.barracks_of_heroes"),
        CC_MINECARTS_CO("hypixel.skyblock.location.dwarven_mines.locations.cc_minecarts_co"),
        CLIFFSIDE_VEINS("hypixel.skyblock.location.dwarven_mines.locations.cliffside_veins"),
        DIVAN_GATEWAY("hypixel.skyblock.location.dwarven_mines.locations.divan_gateway"),
        DWARVEN_TAVERN("hypixel.skyblock.location.dwarven_mines.locations.dwarven_tavern"),
        DWARVEN_VILLAGE("hypixel.skyblock.location.dwarven_mines.locations.dwarven_village"),
        FAR_RESERVE("hypixel.skyblock.location.dwarven_mines.locations.far_reserve"),
        FORGE_BASIN("hypixel.skyblock.location.dwarven_mines.locations.forge_basin"),
        GATES_TO_THE_MINES("hypixel.skyblock.location.dwarven_mines.locations.gates_to_the_mines"),
        GOBLIN_BURROWS("hypixel.skyblock.location.dwarven_mines.locations.goblin_burrows"),
        GRAND_LIBRARY("hypixel.skyblock.location.dwarven_mines.locations.grand_library"),
        GREAT_ICE_WALL("hypixel.skyblock.location.dwarven_mines.locations.great_ice_wall"),
        HANGING_COURT("hypixel.skyblock.location.dwarven_mines.locations.hanging_court"),
        LAVA_SPRINGS("hypixel.skyblock.location.dwarven_mines.locations.lava_springs"),
        MINER_GUILD("hypixel.skyblock.location.dwarven_mines.locations.miner_guild"),
        PALACE_BRIDGE("hypixel.skyblock.location.dwarven_mines.locations.palace_bridge"),
        RAMPART_QUARRY("hypixel.skyblock.location.dwarven_mines.locations.rampart_quarry"),
        ROYAL_MINES("hypixel.skyblock.location.dwarven_mines.locations.royal_mines"),
        ROYAL_PALACE("hypixel.skyblock.location.dwarven_mines.locations.royal_palace"),
        ROYAL_QUARTERS("hypixel.skyblock.location.dwarven_mines.locations.royal_quarters"),
        THE_FORGE("hypixel.skyblock.location.dwarven_mines.locations.the_forge"),
        THE_LIFT("hypixel.skyblock.location.dwarven_mines.locations.the_lift"),
        THE_MIST("hypixel.skyblock.location.dwarven_mines.locations.the_mist"),
        UPPER_MINES("hypixel.skyblock.location.dwarven_mines.locations.upper_mines"),
        DWARVEN_BASE_CAMP("hypixel.skyblock.location.dwarven_mines.locations.dwarven_base_camp"),
        FOSSIL_RESEARCH_CENTER("hypixel.skyblock.location.dwarven_mines.locations.fossil_research_center"),
        GLACITE_TUNNELS("hypixel.skyblock.location.dwarven_mines.locations.glacite_tunnels"),
        GREAT_GLACITE_LAKE("hypixel.skyblock.location.dwarven_mines.locations.great_glacite_lake"),
        GLACITE_MINESHAFTS("hypixel.skyblock.location.dwarven_mines.locations.glacite_mineshafts");

        private final String key;

        DwarvenMinesZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum CrystalHollowsZones implements EnumKey, EnumRegion {
        CRYSTAL_HOLLOWS("hypixel.skyblock.location.crystal_hollows.locations.crystal_hollows"),
        CRYSTAL_NUCLEUS("hypixel.skyblock.location.crystal_hollows.locations.crystal_nucleus", CrystalHollowsRegions.NUCLEUS),
        GOBLIN_HOLDOUT("hypixel.skyblock.location.crystal_hollows.locations.goblin_holdout", CrystalHollowsRegions.GOBLIN_HOLDOUT),
        GOBLIN_QUEEN_DEN("hypixel.skyblock.location.crystal_hollows.locations.goblin_queen_den", CrystalHollowsRegions.GOBLIN_HOLDOUT),
        JUNGLE("hypixel.skyblock.location.crystal_hollows.locations.jungle", CrystalHollowsRegions.JUNGLE),
        JUNGLE_TEMPLE("hypixel.skyblock.location.crystal_hollows.locations.jungle_temple", CrystalHollowsRegions.JUNGLE),
        PRECURSOR_REMNANTS("hypixel.skyblock.location.crystal_hollows.locations.precursor_remnants", CrystalHollowsRegions.PRECURSOR_REMNANTS),
        LOST_PRECURSOR_CITY("hypixel.skyblock.location.crystal_hollows.locations.lost_precursor_city", CrystalHollowsRegions.PRECURSOR_REMNANTS),
        MITHRIL_DEPOSITS("hypixel.skyblock.location.crystal_hollows.locations.mithril_deposits", CrystalHollowsRegions.MITHRIL_DEPOSITS),
        DRAGON_LAIR("hypixel.skyblock.location.crystal_hollows.locations.dragon_lair"),
        MINES_OF_DIVAN("hypixel.skyblock.location.crystal_hollows.locations.mines_of_divan", CrystalHollowsRegions.MITHRIL_DEPOSITS),
        MAGMA_FIELDS("hypixel.skyblock.location.crystal_hollows.locations.magma_fields", CrystalHollowsRegions.MAGMA_FIELDS),
        KHAZAD_DUM("hypixel.skyblock.location.crystal_hollows.locations.khazad_dum", CrystalHollowsRegions.MAGMA_FIELDS),
        FAIRY_GROTTO("hypixel.skyblock.location.crystal_hollows.locations.fairy_grotto");

        private final String key;
        private final Enum<?> region;

        CrystalHollowsZones(String key, Enum<?> region) {
            this.key = key;
            this.region = region;
        }
        CrystalHollowsZones(String key) {
            this(key, null); // Default the region to null if not provided
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum JerryIslandZones implements EnumKey {
        JERRY_WORKSHOP("hypixel.skyblock.location.jerry_island.locations.jerry_workshop"),
        JERRY_POND("hypixel.skyblock.location.jerry_island.locations.jerry_pond"),
        REFLECTIVE_POND("hypixel.skyblock.location.jerry_island.locations.reflective_pond"),
        SUNKEN_JERRY_POND("hypixel.skyblock.location.jerry_island.locations.sunken_jerry_pond"),
        MOUNT_JERRY("hypixel.skyblock.location.jerry_island.locations.mount_jerry"),
        GARY_SHACK("hypixel.skyblock.location.jerry_island.locations.gary_shack"),
        GLACIAL_CAVE("hypixel.skyblock.location.jerry_island.locations.glacial_cave"),
        TERRY_SHACK("hypixel.skyblock.location.jerry_island.locations.terry_shack"),
        HOT_SPRINGS("hypixel.skyblock.location.jerry_island.locations.hot_springs"),
        SHERRY_SHOWROOM("hypixel.skyblock.location.jerry_island.locations.sherry_showroom"),
        EINARY_EMPORIUM("hypixel.skyblock.location.jerry_island.locations.einary_emporium");

        private final String key;

        JerryIslandZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum DungeonHubZones implements EnumKey {
        DUNGEON_HUB("hypixel.skyblock.location.dungeon_hub.locations.dungeon_hub");

        private final String key;

        DungeonHubZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum DungeonZones implements EnumKey {
        CATACOMBS_ENTRANCE("hypixel.skyblock.location.dungeon.locations.catacombs_entrance"),
        CATACOMBS_FLOOR_ONE("hypixel.skyblock.location.dungeon.locations.catacombs_floor_one"),
        CATACOMBS_FLOOR_TWO("hypixel.skyblock.location.dungeon.locations.catacombs_floor_two"),
        CATACOMBS_FLOOR_THREE("hypixel.skyblock.location.dungeon.locations.catacombs_floor_three"),
        CATACOMBS_FLOOR_FOUR("hypixel.skyblock.location.dungeon.locations.catacombs_floor_four"),
        CATACOMBS_FLOOR_FIVE("hypixel.skyblock.location.dungeon.locations.catacombs_floor_five"),
        CATACOMBS_FLOOR_SIX("hypixel.skyblock.location.dungeon.locations.catacombs_floor_six"),
        CATACOMBS_FLOOR_SEVEN("hypixel.skyblock.location.dungeon.locations.catacombs_floor_seven"),
        MASTER_CATACOMBS_FLOOR_ONE("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_one"),
        MASTER_CATACOMBS_FLOOR_TWO("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_two"),
        MASTER_CATACOMBS_FLOOR_THREE("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_three"),
        MASTER_CATACOMBS_FLOOR_FOUR("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_four"),
        MASTER_CATACOMBS_FLOOR_FIVE("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_five"),
        MASTER_CATACOMBS_FLOOR_SIX("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_six"),
        MASTER_CATACOMBS_FLOOR_SEVEN("hypixel.skyblock.location.dungeon.locations.master_catacombs_floor_seven");

        private final String key;

        DungeonZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    public enum RiftZones implements EnumKey, EnumRegion {
        THE_RIFT("hypixel.skyblock.location.THE_RIFT.locations.the_rift"),
        WIZARD_TOWER("hypixel.skyblock.location.THE_RIFT.locations.wizard_tower", RiftRegions.WIZARD_TOWER),
        WYLD_WOODS("hypixel.skyblock.location.THE_RIFT.locations.wyld_woods", RiftRegions.WYLD_WOODS),
        ENIGMA_CRIB("hypixel.skyblock.location.THE_RIFT.locations.enigmas_crib", RiftRegions.WYLD_WOODS),
        BROKEN_CAGE("hypixel.skyblock.location.THE_RIFT.locations.broken_cage", RiftRegions.WYLD_WOODS),
        SHIFTED_TAVERN("hypixel.skyblock.location.THE_RIFT.locations.shifted_tavern", RiftRegions.WYLD_WOODS),
        PUMPGROTTO("hypixel.skyblock.location.THE_RIFT.locations.pumpgrotto"),
        THE_BASTION("hypixel.skyblock.location.THE_RIFT.locations.the_bastion"),
        OTHERSIDE("hypixel.skyblock.location.THE_RIFT.locations.otherside"),
        BLACK_LAGOON("hypixel.skyblock.location.THE_RIFT.locations.black_lagoon", RiftRegions.LAGOON),
        LAGOON_CAVE("hypixel.skyblock.location.THE_RIFT.locations.lagoon_cave", RiftRegions.LAGOON),
        LAGOON_HUT("hypixel.skyblock.location.THE_RIFT.locations.lagoon_hut", RiftRegions.LAGOON),
        LEECHES_LAIR("hypixel.skyblock.location.THE_RIFT.locations.leeches_lair", RiftRegions.LEECH_SUPREME),
        AROUND_COLOSSEUM("hypixel.skyblock.location.THE_RIFT.locations.around_colosseum"),
        RIFT_GALLERY_ENTRANCE("hypixel.skyblock.location.THE_RIFT.locations.rift_gallery_entrance"),
        RIFT_GALLERY("hypixel.skyblock.location.THE_RIFT.locations.rift_gallery"),
        WEST_VILLAGE("hypixel.skyblock.location.THE_RIFT.locations.west_village", RiftRegions.WEST_VILLAGE),
        DOLPHIN_TRAINER("hypixel.skyblock.location.THE_RIFT.locations.dolphin_trainer", RiftRegions.WEST_VILLAGE),
        CAKE_HOUSE("hypixel.skyblock.location.THE_RIFT.locations.cake_house", RiftRegions.WEST_VILLAGE),
        INFESTED_HOUSE("hypixel.skyblock.location.THE_RIFT.locations.infested_house", RiftRegions.WEST_VILLAGE),
        MIRRORVERSE("hypixel.skyblock.location.THE_RIFT.locations.mirrorverse", RiftRegions.MIRRORVERSE),
        DREADFARM("hypixel.skyblock.location.THE_RIFT.locations.dreadfarm", RiftRegions.FARM),
        GREAT_BEANSTALK("hypixel.skyblock.location.THE_RIFT.locations.great_beanstalk", RiftRegions.FARM),
        VILLAGE_PLAZA("hypixel.skyblock.location.THE_RIFT.locations.village_plaza", RiftRegions.PLAZA),
        TAYLORS("hypixel.skyblock.location.THE_RIFT.locations.taylors", RiftRegions.PLAZA),
        LONELY_TERRACE("hypixel.skyblock.location.THE_RIFT.locations.lonely_terrace", RiftRegions.PLAZA),
        MURDER_HOUSE("hypixel.skyblock.location.THE_RIFT.locations.murder_house", RiftRegions.PLAZA),
        BOOK_IN_A_BOOK("hypixel.skyblock.location.THE_RIFT.locations.book_in_a_book", RiftRegions.PLAZA),
        HALF_EATEN_CAVE("hypixel.skyblock.location.THE_RIFT.locations.half_eaten_cave", RiftRegions.PLAZA),
        YOUR_ISLAND("hypixel.skyblock.location.THE_RIFT.locations.your_island", RiftRegions.PLAZA),
        EMPTY_BANK("hypixel.skyblock.location.THE_RIFT.locations.empty_bank", RiftRegions.PLAZA),
        BARRY_CENTER("hypixel.skyblock.location.THE_RIFT.locations.barry_center", RiftRegions.PLAZA),
        BARRY_HQ("hypixel.skyblock.location.THE_RIFT.locations.barry_hq", RiftRegions.PLAZA),
        DEJA_VU_ALLEY("hypixel.skyblock.location.THE_RIFT.locations.deja_vu_alley", RiftRegions.PLAZA),
        LIVING_CAVE("hypixel.skyblock.location.THE_RIFT.locations.living_cave", RiftRegions.LIVING_STILLNESS),
        LIVING_STILLNESS("hypixel.skyblock.location.THE_RIFT.locations.living_stillness", RiftRegions.LIVING_STILLNESS),
        COLOSSEUM("hypixel.skyblock.location.THE_RIFT.locations.colosseum", RiftRegions.BACTE),
        BARRIER_STREET("hypixel.skyblock.location.THE_RIFT.locations.barrier_street", RiftRegions.PLAZA),
        PHOTON_PATHWAY("hypixel.skyblock.location.THE_RIFT.locations.photon_pathway", RiftRegions.PLAZA),
        STILLGORE_CHATEAU("hypixel.skyblock.location.THE_RIFT.locations.stillgore_chateau", RiftRegions.VAMPIRE),
        OUBLIETTE("hypixel.skyblock.location.THE_RIFT.locations.oubliette", RiftRegions.VAMPIRE),
        FAIRYLOSOPHER_TOWER("hypixel.skyblock.location.THE_RIFT.locations.fairylosopher_tower", RiftRegions.VAMPIRE),
        MOUNTAINTOP("hypixel.skyblock.location.THE_RIFT.locations.mountaintop", RiftRegions.MOUNTAINTOP),
        CONTINUUM("hypixel.skyblock.location.THE_RIFT.locations.continuum", RiftRegions.MOUNTAINTOP),
        TIME_CHAMBER("hypixel.skyblock.location.THE_RIFT.locations.time_chamber", RiftRegions.SUN_GOD),
        TIME_TORN_ISLES("hypixel.skyblock.location.THE_RIFT.locations.time_torn_isles", RiftRegions.MOUNTAINTOP),
        ROSE_END("hypixel.skyblock.location.THE_RIFT.locations.rose_end", RiftRegions.MOUNTAINTOP),
        WALK_OF_FAME("hypixel.skyblock.location.THE_RIFT.locations.walk_of_fame", RiftRegions.MOUNTAINTOP),
        CEREBRAL_CITADEL("hypixel.skyblock.location.THE_RIFT.locations.cerebral_citadel", RiftRegions.MOUNTAINTOP),
        WIZARDMAN_BUREAU("hypixel.skyblock.location.THE_RIFT.locations.wizardman_bureau", RiftRegions.MOUNTAINTOP),
        WIZARD_BRAWL("hypixel.skyblock.location.THE_RIFT.locations.wizard_brawl", RiftRegions.MOUNTAINTOP),
        TRIAL_GROUNDS("hypixel.skyblock.location.THE_RIFT.locations.trial_grounds", RiftRegions.MOUNTAINTOP);

        private final String key;
        private final Enum<?> region;

        RiftZones(String key, Enum<?> region) {
            this.key = key;
            this.region = region;
        }

        RiftZones(String key) {
            this(key, null);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum BackwaterBayouZones implements EnumKey {
        BACKWATER_BAYOU("hypixel.skyblock.location.backwater_bayou.locations.backwater_bayou");

        private final String key;

        BackwaterBayouZones(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

}
