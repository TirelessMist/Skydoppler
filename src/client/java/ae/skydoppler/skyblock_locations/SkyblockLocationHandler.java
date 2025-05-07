package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        // The location icon and accents are already removed from the string

        System.out.println("============RAW LOCATION: " + '"' + location + "\"============");

        for (SkyblockIslandEnum island : SkyblockIslandEnum.values()) {

            Enum<?>[] zones = island.getZonesForIsland();

            for (Enum<?> zone : zones) {
                if (zone instanceof SkyblockIslandEnum.EnumName enumName && enumName.getName().equals(location)) {
                    System.out.println("Found location: " + zone);

                    SkydopplerClient.currentIsland = island;
                    System.out.println("Current island set to: " + island);

                    SkydopplerClient.currentZone = zone;
                    System.out.println("Current zone set to: " + zone.name());
                    return;
                }
            }
        }
    }
}