package ae.skydoppler.skyblock_locations;

public enum SkyblockIslandEnum {
    NONE(SkyblockIslandType.NONE, NoneZones.class),

    PRIVATE_ISLAND(SkyblockIslandType.NONE, PrivateIslandZones.class),

    HUB(SkyblockIslandType.HUB, HubZones.class),

    DUNGEON_HUB(SkyblockIslandType.HUB, DungeonHubZones.class),

    CRIMSON_ISLE(SkyblockIslandType.COMBAT_ISLAND, CrimsonIsleZones.class),
    SPIDER_DEN(SkyblockIslandType.COMBAT_ISLAND, SpiderDenZones.class),
    THE_END(SkyblockIslandType.COMBAT_ISLAND, TheEndZones.class),

    GOLD_MINE(SkyblockIslandType.MINING_ISLAND, GoldMineZones.class),
    DEEP_CAVERNS(SkyblockIslandType.MINING_ISLAND, DeepCavernsZones.class),
    DWARVEN_MINES(SkyblockIslandType.MINING_ISLAND, DwarvenMinesZones.class),
    CRYSTAL_HOLLOWS(SkyblockIslandType.MINING_ISLAND, CrystalHollowsZones.class),

    JERRY_ISLAND(SkyblockIslandType.NONE, JerryIslandZones.class),

    BACKWATER_BAYOU(SkyblockIslandType.FISHING_ISLAND, BackwaterBayouZones.class),

    THE_PARK(SkyblockIslandType.FORAGING_ISLAND, TheParkZones.class),

    THE_GARDEN(SkyblockIslandType.FARMING_ISLAND, null),
    THE_BARN(SkyblockIslandType.FARMING_ISLAND, TheBarnZones.class),
    MUSHROOM_DESERT(SkyblockIslandType.FARMING_ISLAND, MushroomDesertZones.class),

    THE_RIFT(SkyblockIslandType.RIFT_ISLAND, RiftZones.class);

    private final SkyblockIslandType islandType;
    private final Class<? extends Enum<?>> zones;

    SkyblockIslandEnum(SkyblockIslandType islandType, Class<? extends Enum<?>> zones) {
        this.islandType = islandType;
        this.zones = zones;
    }

    public SkyblockIslandType getIslandType() {
        return islandType;
    }

    public Class<? extends Enum<?>> getZones() {
        return zones;
    }

    public enum NoneZones {
        NONE("none");

        private final String name;

        NoneZones(String name) {
            this.name = name;
        }
    }

    public enum PrivateIslandZones {
        PRIVATE_ISLAND("private island");

        private final String name;

        PrivateIslandZones(String name) {
            this.name = name;
        }
    }

    public enum HubZones {
        HUB("hub"),
        VILLAGE("village"),
        BAZAAR("bazaar alley"),
        AUCTION_HOUSE("auction house"),
        BANK("bank"),
        COMMUNITY_CENTER("community center"),
        ELECTION_ROOM("election room"),
        MUSEUM("museum"),
        ARCHERY_RANGE("archery range"),
        ARTIST_ABODE("artist's abode"),
        BLACKSMITH("blacksmith"),
        BUILDER_HOUSE("builder's house"),
        CANVAS_ROOM("canvas room"),
        COAL_MINE("coal mine"),
        COLOSSEUM("colosseum"),
        FARM("farm"),
        FARMHOUSE("farmhouse"),
        FASHION_SHOP("fashion shop"),
        FISHERMAN_HUT("fisherman's hut"),
        FLOWER_HOUSE("flower house"),
        FOREST("forest"),
        GRAVEYARD("graveyard"),
        CATACOMBS_ENTRANCE("catacombs entrance"),
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

        HubZones(String name) {
            this.name = name;
        }
    }

    public enum TheGardenZones {
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
    }

    public enum TheBarnZones {
        BARN("barn"),
        WINDMILL("windmill");

        private final String name;

        TheBarnZones(String name) {
            this.name = name;
        }
    }

    public enum MushroomDesertZones {
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
    }

    public enum TheParkZones {
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
    }

    public enum SpiderDenZones {
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
    }

    public enum TheEndZones {
        THE_END("the end"),
        DRAGON_NEST("dragon's nest"),
        VOID_SEPULTURE("void sepulchre"),
        VOID_SLATE("void slate"),
        ZEALOT_BRUISER_HIDEOUT("zealot bruiser hideout");

        private final String name;

        TheEndZones(String name) {
            this.name = name;
        }
    }

    public enum CrimsonIsleZones {
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
        DRAGONTAIL("dragontail"),
        CHIEF_HUT("chief's hut"),
        DRAGONTAIL_BLACKSMITH("dragontail blacksmith"),
        DRAGONTAIL_TOWNSQUARE("dragontail townsquare"),
        DRAGONTAIL_AUCTION_HOUSE("dragontail auction house"),
        DRAGONTAIL_BAZAAR("dragontail bazaar"),
        DRAGONTAIL_BANK("dragontail bank"),
        MINION_SHOP("minion shop"),
        THE_DUKEDOM("the dukedom"),
        THE_BASTION("the bastion"),
        SCARLETON("scarleton"),
        COMMUNITY_CENTER("community center"),
        THRONE_ROOM("throne room"),
        MAGE_COUNCIL("mage council"),
        SCARLETON_PLAZA("scarleton plaza"),
        SCARLETON_MINION_SHOP("scarleton minion shop"),
        SCARLETON_AUCTION_HOUSE("scarleton auction house"),
        SCARLETON_BAZAAR("scarleton bazaar"),
        SCARLETON_BANK("scarleton bank"),
        SCARLETON_BLACKSMITH("scarleton blacksmith"),
        IGRUPAN_HOUSE("igrupan's house"),
        IGRUPAN_CHICKEN_COOP("igrupan's chicken coop"),
        CATHEDRAL("cathedral"),
        COURTYARD("courtyard"),
        THE_WASTELAND("the wasteland"),
        RUINS_OF_ASHFANG("ruins of ashfang"),
        FORGOTTEN_SKULL("forgotten skull"),
        SMOLDERING_TOMB("smoldering tomb");

        private final String name;

        CrimsonIsleZones(String name) {
            this.name = name;
        }
    }

    public enum GoldMineZones {
        GOLD_MINE("gold mine");

        private final String name;

        GoldMineZones(String name) {
            this.name = name;
        }
    }

    public enum DeepCavernsZones {
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
    }

    public enum DwarvenMinesZones {
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
    }

    public enum CrystalHollowsZones {
        CRYSTAL_HOLLOWS("crystal hollows"),
        CRYSTAL_NUCLEUS("crystal nucleus"),
        GOBLIN_HOLDOUT("goblin holdout"),
        GOBLIN_QUEEN_DEN("goblin queen's den"),
        JUNGLE("jungle"),
        JUNGLE_TEMPLE("jungle temple"),
        PRECURSOR_REMNANTS("precursor remnants"),
        LOST_PRECURSOR_CITY("lost precursor city"),
        MITHRIL_DEPOSITS("mithril deposits"),
        DRAGON_LAIR("dragon's lair"),
        MINES_OF_DIVAN("mines of divan"),
        MAGMA_FIELDS("magma fields"),
        KHAZAD_DUM("khazad-dum"),
        FAIRY_GROTTO("fairy grotto");

        private final String name;

        CrystalHollowsZones(String name) {
            this.name = name;
        }
    }

    public enum JerryIslandZones {
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
    }

    public enum DungeonHubZones {
        DUNGEON_HUB("dungeon hub");

        private final String name;

        DungeonHubZones(String name) {
            this.name = name;
        }
    }

    public enum RiftZones {
        THE_RIFT("the rift"),
        WIZARD_TOWER("wizard tower"),
        WYLD_WOODS("wyld woods"),
        ENIGMA_CRIB("enigma's crib"),
        BROKEN_CAGE("broken cage"),
        SHIFTED_TAVERN("shifted tavern"),
        PUMPGROTTO("pumpgrotto"),
        THE_BASTION("the bastion"),
        OTHERSIDE("otherside"),
        BLACK_LAGOON("black lagoon"),
        LAGOON_CAVE("lagoon cave"),
        LAGOON_HUT("lagoon hut"),
        LEECHES_LAIR("leeches lair"),
        AROUND_COLOSSEUM("around colosseum"),
        RIFT_GALLERY_ENTRANCE("rift gallery entrance"),
        RIFT_GALLERY("rift gallery"),
        WEST_VILLAGE("west village"),
        DOLPHIN_TRAINER("dolphin trainer"),
        CAKE_HOUSE("cake house"),
        INFESTED_HOUSE("infested house"),
        MIRRORVERSE("mirrorverse"),
        DREADFARM("dreadfarm"),
        GREAT_BEANSTALK("great beanstalk"),
        VILLAGE_PLAZA("village plaza"),
        TAYLORS("taylor's"),
        LONELY_TERRACE("lonely terrace"),
        MURDER_HOUSE("murder house"),
        BOOK_IN_A_BOOK("book in a book"),
        HALF_EATEN_CAVE("half-eaten cave"),
        YOUR_ISLAND("'your' island"),
        EMPTY_BANK("empty bank"),
        BARRY_CENTER("barry center"),
        BARRY_HQ("barry hq"),
        DEJA_VU_ALLEY("deja vu alley"),
        LIVING_CAVE("living cave"),
        LIVING_STILLNESS("living stillness"),
        COLOSSEUM("colosseum"),
        BARRIER_STREET("barrier street"),
        PHOTON_PATHWAY("photon pathway"),
        STILLGORE_CHATEAU("stillgore chateau"),
        OUBLIETTE("oubliette"),
        FAIRYLOSOPHER_TOWER("fairylosopher tower"),
        MOUNTAINTOP("mountaintop"),
        CONTINUUM("continuum"),
        TIME_CHAMBER("time chamber"),
        TIME_TORN_ISLES("time-torn isles"),
        ROSE_END("rose's end"),
        WALK_OF_FAME("walk of fame"),
        CEREBRAL_CITADEL("cerebral citadel"),
        WIZARDMAN_BUREAU("wizardman bureau"),
        WIZARD_BRAWL("wizard brawl"),
        TRIAL_GROUNDS("trial grounds");

        private final String name;

        RiftZones(String name) {
            this.name = name;
        }
    }

    public enum BackwaterBayouZones {
        BACKWATER_BAYOU("backwater bayou");

        private final String name;

        BackwaterBayouZones(String name) {
            this.name = name;
        }
    }

}