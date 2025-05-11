package ae.skydoppler.skyblock_locations;

public enum SkyblockIslandEnum {

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

    SkyblockIslandEnum(SkyblockIslandCategoryEnum islandType, Class<? extends Enum<?>> zoneClass) {
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

    public interface EnumName {
        String getName();
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


    public enum NoneZones implements EnumName {
        NONE("none");

        private final String name;

        NoneZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum PrivateIslandZones implements EnumName {
        PRIVATE_ISLAND("your island");

        private final String name;

        PrivateIslandZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum HubZones implements EnumName, EnumRegion {
        HUB("hub"),
        VILLAGE("village", HubRegions.VILLAGE),
        BAZAAR("bazaar alley", HubRegions.VILLAGE),
        AUCTION_HOUSE("auction house", HubRegions.VILLAGE),
        BANK("bank", HubRegions.VILLAGE),
        COMMUNITY_CENTER("community center", HubRegions.VILLAGE),
        ELECTION_ROOM("election room"),
        MUSEUM("museum"),
        ARCHERY_RANGE("archery range"),
        ARTIST_ABODE("artist's abode"),
        BLACKSMITH("blacksmith"),
        BUILDER_HOUSE("builder's house"),
        CANVAS_ROOM("canvas room"),
        COAL_MINE("coal mine", HubRegions.CRYPTS),
        COLOSSEUM("colosseum"),
        FARM("farm"),
        FARMHOUSE("farmhouse"),
        FASHION_SHOP("fashion shop"),
        FISHERMAN_HUT("fisherman's hut"),
        FLOWER_HOUSE("flower house"),
        FOREST("forest"),
        GRAVEYARD("graveyard", HubRegions.CRYPTS),
        CATACOMBS_ENTRANCE("catacombs entrance", HubRegions.CRYPTS),
        HEXATORUM("hexatorum"),
        UNINCORPORATED("unincorporated"),
        LIBRARY("library"),
        MOUNTAIN("mountain"),
        PET_CARE("pet care"),
        RABBIT_HOUSE("rabbit house"),
        RUINS("ruins"),
        SHEN_AUCTION("shen's auction"),
        TAVERN("tavern"),
        THAUMATURGIST("thaumaturgist"),
        WEAPONSMITH("weaponsmith"),
        WILDERNESS("wilderness"),
        WIZARD_TOWER("wizard tower"),
        FISHING_OUTPOST("fishing outpost");

        private final String name;
        private final Enum<?> region;

        HubZones(String name, Enum<?> region) {
            this.name = name;
            this.region = region;
        }

        HubZones(String name) {
            this(name, null); // Default the region to null if not provided
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum TheGardenZones implements EnumName {
        THE_GARDEN("the garden"),
        PLOT_1("plot: 1"),
        PLOT_2("plot: 2"),
        PLOT_3("plot: 3"),
        PLOT_4("plot: 4"),
        PLOT_5("plot: 5"),
        PLOT_6("plot: 6"),
        PLOT_7("plot: 7"),
        PLOT_8("plot: 8"),
        PLOT_9("plot: 9"),
        PLOT_10("plot: 10"),
        PLOT_11("plot: 11"),
        PLOT_12("plot: 12"),
        PLOT_13("plot: 13"),
        PLOT_14("plot: 14"),
        PLOT_15("plot: 15"),
        PLOT_16("plot: 16"),
        PLOT_17("plot: 17"),
        PLOT_18("plot: 18"),
        PLOT_19("plot: 19"),
        PLOT_20("plot: 20"),
        PLOT_21("plot: 21"),
        PLOT_22("plot: 22"),
        PLOT_23("plot: 23"),
        PLOT_24("plot: 24");
        private final String name;

        TheGardenZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum TheBarnZones implements EnumName {
        BARN("barn"),
        WINDMILL("windmill");

        private final String name;

        TheBarnZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum MushroomDesertZones implements EnumName {
        MUSHROOM_DESERT("mushroom desert"),
        DESERT_SETTLEMENT("desert settlement"),
        GLOWING_MUSHROOM_CAVE("glowing mushroom cave"),
        JAKE_HOUSE("jake's house"),
        MUSHROOM_GORGE("mushroom gorge"),
        OASIS("oasis"),
        OVERGROWN_MUSHROOM_CAVE("overgrown mushroom cave"),
        SHEPHERD_KEEP("shepherd's keep"),
        TRAPPER_DEN("trapper's den"),
        TREASURE_HUNTER_CAMP("treasure hunter camp");

        private final String name;

        MushroomDesertZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum TheParkZones implements EnumName {
        THE_PARK("the park"),
        BIRCH_PARK("birch park"),
        HOWLING_CAVE("howling cave"),
        SPRUCE_WOODS("spruce woods"),
        LONELY_ISLAND("lonely island"),
        VIKING_LONGHOUSE("viking longhouse"),
        DARK_THICKET("dark thicket"),
        SAVANNA_WOODLAND("savanna woodland"),
        MELODY_PLATEAU("melody's plateau"),
        JUNGLE_ISLAND("jungle island");

        private final String name;

        TheParkZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum SpiderDenZones implements EnumName {
        SPIDER_DEN("spider's den"),
        SPIDER_MOUND("spider mound"),
        GRAVEL_MINES("gravel mines"),
        GRANDMA_HOUSE("grandma's house"),
        ARACHNE_BURROW("arachne's burrow"),
        ARACHNE_SANCTUARY("arachne's sanctuary"),
        ARCHAEOLOGIST_CAMP("archaeologist's camp");

        private final String name;

        SpiderDenZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum TheEndZones implements EnumName {
        THE_END("the end"),
        DRAGON_NEST("dragon's nest"),
        VOID_SEPULTURE("void sepulchre"),
        VOID_SLATE("void slate"),
        ZEALOT_BRUISER_HIDEOUT("zealot bruiser hideout");

        private final String name;

        TheEndZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum CrimsonIsleZones implements EnumName, EnumRegion {
        CRIMSON_ISLE("crimson isle"),
        STRONGHOLD("stronghold"),
        CRIMSON_FIELDS("crimson fields"),
        BLAZING_VOLCANO("blazing volcano"),
        ODGER_HUT("odger's hut"),
        PLHLEGBLAST_POOL("plhlegblast pool"),
        MAGMA_CHAMBER("magma chamber"),
        AURA_LAB("aura's lab"),
        MATRIARCH_LAIR("matriarch's lair"),
        BELLY_OF_THE_BEAST("belly of the beast"),
        DOJO("dojo"),
        DOJO_ARENA("dojo arena"),
        BURNING_DESERT("burning desert"),
        MYSTIC_MARSH("mystic marsh"),
        BARBARIAN_OUTPOST("barbarian outpost"),
        MAGE_OUTPOST("mage outpost"),
        DRAGONTAIL("dragontail", CrimsonIsleRegions.DRAGONTAIL),
        CHIEF_HUT("chief's hut", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BLACKSMITH("dragontail blacksmith", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_TOWNSQUARE("dragontail townsquare", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_AUCTION_HOUSE("dragontail auction house", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BAZAAR("dragontail bazaar", CrimsonIsleRegions.DRAGONTAIL),
        DRAGONTAIL_BANK("dragontail bank", CrimsonIsleRegions.DRAGONTAIL),
        MINION_SHOP("minion shop"),
        THE_DUKEDOM("the dukedom"),
        THE_BASTION("the bastion"),
        SCARLETON("scarleton", CrimsonIsleRegions.SCARLETON),
        COMMUNITY_CENTER("community center", CrimsonIsleRegions.SCARLETON),
        THRONE_ROOM("throne room", CrimsonIsleRegions.SCARLETON),
        MAGE_COUNCIL("mage council", CrimsonIsleRegions.SCARLETON),
        SCARLETON_PLAZA("scarleton plaza", CrimsonIsleRegions.SCARLETON),
        SCARLETON_MINION_SHOP("scarleton minion shop", CrimsonIsleRegions.SCARLETON),
        SCARLETON_AUCTION_HOUSE("scarleton auction house", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BAZAAR("scarleton bazaar", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BANK("scarleton bank", CrimsonIsleRegions.SCARLETON),
        SCARLETON_BLACKSMITH("scarleton blacksmith", CrimsonIsleRegions.SCARLETON),
        IGRUPAN_HOUSE("igrupan's house", CrimsonIsleRegions.SCARLETON),
        IGRUPAN_CHICKEN_COOP("igrupan's chicken coop", CrimsonIsleRegions.SCARLETON),
        CATHEDRAL("cathedral", CrimsonIsleRegions.SCARLETON),
        COURTYARD("courtyard", CrimsonIsleRegions.SCARLETON),
        THE_WASTELAND("the wasteland"),
        RUINS_OF_ASHFANG("ruins of ashfang"),
        FORGOTTEN_SKULL("forgotten skull"),
        SMOLDERING_TOMB("smoldering tomb");

        private final String name;
        private final Enum<?> region;

        CrimsonIsleZones(String name, Enum<?> region) {
            this.name = name;
            this.region = region;
        }

        CrimsonIsleZones(String name) {
            this(name, null); // Default the region to null if not provided
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum KuudraZones implements EnumName {
        KUUDRA_HOLLOW("kuudra's hollow");

        private final String name;

        KuudraZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum GoldMineZones implements EnumName {
        GOLD_MINE("gold mine");

        private final String name;

        GoldMineZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum DeepCavernsZones implements EnumName {
        DEEP_CAVERNS("deep caverns"),
        GUNPOWDER_MINES("gunpowder mines"),
        LAPIS_QUARRY("lapis quarry"),
        PIGMEN_DEN("pigmen's den"),
        SLIMEHILL("slimehill"),
        DIAMOND_RESERVE("diamond reserve"),
        OBSIDIAN_SANCTUARY("obsidian sanctuary");

        private final String name;

        DeepCavernsZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum DwarvenMinesZones implements EnumName {
        DWARVEN_MINES("dwarven mines"),
        ARISTOCRAT_PASSAGE("aristocrat passage"),
        BARRACKS_OF_HEROES("barracks of heroes"),
        CC_MINECARTS_CO("c&c minecarts co."),
        CLIFFSIDE_VEINS("cliffside veins"),
        DIVAN_GATEWAY("divan's gateway"),
        DWARVEN_TAVERN("dwarven tavern"),
        DWARVEN_VILLAGE("dwarven village"),
        FAR_RESERVE("far reserve"),
        FORGE_BASIN("forge basin"),
        GATES_TO_THE_MINES("gates to the mines"),
        GOBLIN_BURROWS("goblin burrows"),
        GRAND_LIBRARY("grand library"),
        GREAT_ICE_WALL("great ice wall"),
        HANGING_COURT("hanging court"),
        LAVA_SPRINGS("lava springs"),
        MINER_GUILD("miner's guild"),
        PALACE_BRIDGE("palace bridge"),
        RAMPART_QUARRY("rampart's quarry"),
        ROYAL_MINES("royal mines"),
        ROYAL_PALACE("royal palace"),
        ROYAL_QUARTERS("royal quarters"),
        THE_FORGE("the forge"),
        THE_LIFT("the lift"),
        THE_MIST("the mist"),
        UPPER_MINES("upper mines"),
        DWARVEN_BASE_CAMP("dwarven base camp"),
        FOSSIL_RESEARCH_CENTER("fossil research center"),
        GLACITE_TUNNELS("glacite tunnels"),
        GREAT_GLACITE_LAKE("great glacite lake"),
        GLACITE_MINESHAFTS("glacite mineshafts");

        private final String name;

        DwarvenMinesZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum CrystalHollowsZones implements EnumName, EnumRegion {
        CRYSTAL_HOLLOWS("crystal hollows"),
        CRYSTAL_NUCLEUS("crystal nucleus", CrystalHollowsRegions.NUCLEUS),
        GOBLIN_HOLDOUT("goblin holdout", CrystalHollowsRegions.GOBLIN_HOLDOUT),
        GOBLIN_QUEEN_DEN("goblin queen's den", CrystalHollowsRegions.GOBLIN_HOLDOUT),
        JUNGLE("jungle", CrystalHollowsRegions.JUNGLE),
        JUNGLE_TEMPLE("jungle temple", CrystalHollowsRegions.JUNGLE),
        PRECURSOR_REMNANTS("precursor remnants", CrystalHollowsRegions.PRECURSOR_REMNANTS),
        LOST_PRECURSOR_CITY("lost precursor city", CrystalHollowsRegions.PRECURSOR_REMNANTS),
        MITHRIL_DEPOSITS("mithril deposits", CrystalHollowsRegions.MITHRIL_DEPOSITS),
        DRAGON_LAIR("dragon's lair"),
        MINES_OF_DIVAN("mines of divan", CrystalHollowsRegions.MITHRIL_DEPOSITS),
        MAGMA_FIELDS("magma fields", CrystalHollowsRegions.MAGMA_FIELDS),
        KHAZAD_DUM("khazad-dum", CrystalHollowsRegions.MAGMA_FIELDS),
        FAIRY_GROTTO("fairy grotto");

        private final String name;
        private final Enum<?> region;

        CrystalHollowsZones(String name, Enum<?> region) {
            this.name = name;
            this.region = region;
        }
        CrystalHollowsZones(String name) {
            this(name, null); // Default the region to null if not provided
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum JerryIslandZones implements EnumName {
        JERRY_WORKSHOP("jerry's workshop"),
        JERRY_POND("jerry pond"),
        REFLECTIVE_POND("reflective pond"),
        SUNKEN_JERRY_POND("sunken jerry pond"),
        MOUNT_JERRY("mount jerry"),
        GARY_SHACK("gary's shack"),
        GLACIAL_CAVE("glacial cave"),
        TERRY_SHACK("terry's shack"),
        HOT_SPRINGS("hot springs"),
        SHERRY_SHOWROOM("sherry's showroom"),
        EINARY_EMPORIUM("einary's emporium");

        private final String name;

        JerryIslandZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum DungeonHubZones implements EnumName {
        DUNGEON_HUB("dungeon hub");

        private final String name;

        DungeonHubZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum DungeonZones implements EnumName {
        CATACOMBS_ENTRANCE("the catacombs (e)"),
        CATACOMBS_FLOOR_ONE("the catacombs (f1)"),
        CATACOMBS_FLOOR_TWO("the catacombs (f2)"),
        CATACOMBS_FLOOR_THREE("the catacombs (f3)"),
        CATACOMBS_FLOOR_FOUR("the catacombs (f4)"),
        CATACOMBS_FLOOR_FIVE("the catacombs (f5)"),
        CATACOMBS_FLOOR_SIX("the catacombs (f6)"),
        CATACOMBS_FLOOR_SEVEN("the catacombs (f7)"),
        MASTER_CATACOMBS_FLOOR_ONE("the catacombs (m1)"),
        MASTER_CATACOMBS_FLOOR_TWO("the catacombs (m2)"),
        MASTER_CATACOMBS_FLOOR_THREE("the catacombs (m3)"),
        MASTER_CATACOMBS_FLOOR_FOUR("the catacombs (m4)"),
        MASTER_CATACOMBS_FLOOR_FIVE("the catacombs (m5)"),
        MASTER_CATACOMBS_FLOOR_SIX("the catacombs (m6)"),
        MASTER_CATACOMBS_FLOOR_SEVEN("the catacombs (m7)");

        private final String name;

        DungeonZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum RiftZones implements EnumName, EnumRegion {
        THE_RIFT("the rift"),
        WIZARD_TOWER("wizard tower", RiftRegions.WIZARD_TOWER),
        WYLD_WOODS("wyld woods", RiftRegions.WYLD_WOODS),
        ENIGMA_CRIB("enigma's crib", RiftRegions.WYLD_WOODS),
        BROKEN_CAGE("broken cage", RiftRegions.WYLD_WOODS),
        SHIFTED_TAVERN("shifted tavern", RiftRegions.WYLD_WOODS),
        PUMPGROTTO("pumpgrotto"),
        THE_BASTION("the bastion"),
        OTHERSIDE("otherside"),
        BLACK_LAGOON("black lagoon", RiftRegions.LAGOON),
        LAGOON_CAVE("lagoon cave", RiftRegions.LAGOON),
        LAGOON_HUT("lagoon hut", RiftRegions.LAGOON),
        LEECHES_LAIR("leeches lair", RiftRegions.LEECH_SUPREME),
        AROUND_COLOSSEUM("around colosseum"),
        RIFT_GALLERY_ENTRANCE("rift gallery entrance"),
        RIFT_GALLERY("rift gallery"),
        WEST_VILLAGE("west village", RiftRegions.WEST_VILLAGE),
        DOLPHIN_TRAINER("dolphin trainer", RiftRegions.WEST_VILLAGE),
        CAKE_HOUSE("cake house", RiftRegions.WEST_VILLAGE),
        INFESTED_HOUSE("infested house", RiftRegions.WEST_VILLAGE),
        MIRRORVERSE("mirrorverse", RiftRegions.MIRRORVERSE),
        DREADFARM("dreadfarm", RiftRegions.FARM),
        GREAT_BEANSTALK("great beanstalk", RiftRegions.FARM),
        VILLAGE_PLAZA("village plaza", RiftRegions.PLAZA),
        TAYLORS("taylor's", RiftRegions.PLAZA),
        LONELY_TERRACE("lonely terrace", RiftRegions.PLAZA),
        MURDER_HOUSE("murder house", RiftRegions.PLAZA),
        BOOK_IN_A_BOOK("book in a book", RiftRegions.PLAZA),
        HALF_EATEN_CAVE("half-eaten cave", RiftRegions.PLAZA),
        YOUR_ISLAND("'your' island", RiftRegions.PLAZA),
        EMPTY_BANK("empty bank", RiftRegions.PLAZA),
        BARRY_CENTER("barry center", RiftRegions.PLAZA),
        BARRY_HQ("barry hq", RiftRegions.PLAZA),
        DEJA_VU_ALLEY("deja vu alley", RiftRegions.PLAZA),
        LIVING_CAVE("living cave", RiftRegions.LIVING_STILLNESS),
        LIVING_STILLNESS("living stillness", RiftRegions.LIVING_STILLNESS),
        COLOSSEUM("colosseum", RiftRegions.BACTE),
        BARRIER_STREET("barrier street", RiftRegions.PLAZA),
        PHOTON_PATHWAY("photon pathway", RiftRegions.PLAZA),
        STILLGORE_CHATEAU("stillgore chateau", RiftRegions.VAMPIRE),
        OUBLIETTE("oubliette", RiftRegions.VAMPIRE),
        FAIRYLOSOPHER_TOWER("fairylosopher tower", RiftRegions.VAMPIRE),
        MOUNTAINTOP("mountaintop", RiftRegions.MOUNTAINTOP),
        CONTINUUM("continuum", RiftRegions.MOUNTAINTOP),
        TIME_CHAMBER("time chamber", RiftRegions.SUN_GOD),
        TIME_TORN_ISLES("time-torn isles", RiftRegions.MOUNTAINTOP),
        ROSE_END("rose's end", RiftRegions.MOUNTAINTOP),
        WALK_OF_FAME("walk of fame", RiftRegions.MOUNTAINTOP),
        CEREBRAL_CITADEL("cerebral citadel", RiftRegions.MOUNTAINTOP),
        WIZARDMAN_BUREAU("wizardman bureau", RiftRegions.MOUNTAINTOP),
        WIZARD_BRAWL("wizard brawl", RiftRegions.MOUNTAINTOP),
        TRIAL_GROUNDS("trial grounds", RiftRegions.MOUNTAINTOP);

        private final String name;
        private final Enum<?> region;

        RiftZones(String name, Enum<?> region) {
            this.name = name;
            this.region = region;
        }

        RiftZones(String name) {
            this(name, null);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getRegion() {
            return region;
        }
    }

    public enum BackwaterBayouZones implements EnumName {
        BACKWATER_BAYOU("backwater bayou");

        private final String name;

        BackwaterBayouZones(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}