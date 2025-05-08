package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        // The location icon and accents are already removed from the string

        System.out.println("============RAW LOCATION: " + '"' + location + "\"============");

        if (location.equalsIgnoreCase("none")) {
            System.out.println("Location is none, not setting anything.");
            return;
        }

        for (SkyblockIslandEnum island : SkyblockIslandEnum.values()) {

            Enum<?>[] zones = island.getZonesForIsland();

            for (Enum<?> zone : zones) {
                if (zone instanceof SkyblockIslandEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                    System.out.println("Found location: " + zone);

                    SkydopplerClient.currentIsland = island;
                    System.out.println("Current island set to: " + island);

                    SkydopplerClient.currentZone = zone;
                    System.out.println("Current zone set to: " + zone.name());
                    return;
                }
                /*public <T extends NamedEnum> void printEnumName(T enumValue) {
                      System.out.println(enumValue.getName());
                  }*/
            }
        }

        /*SkydopplerClient.currentIsland = SkyblockIslandEnum.NONE;
        SkydopplerClient.currentZone = SkyblockIslandEnum.NONE.getZonesForIsland()[0]; // Sets currentZone to the first enum for the island of type "NONE", which is also "NONE" (the only value for the island of type "NONE").*/
    }
}