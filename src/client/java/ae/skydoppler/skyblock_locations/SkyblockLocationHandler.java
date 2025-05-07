package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

import java.util.Arrays;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        // The location icon is already removed from the string

        for (SkyblockIslandEnum island : SkyblockIslandEnum.values()) {

            Enum<?>[] zones = island.getZonesForIsland();

            for (Enum<?> zone : zones) {
                if (zone.toString().equalsIgnoreCase(location)) {
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
