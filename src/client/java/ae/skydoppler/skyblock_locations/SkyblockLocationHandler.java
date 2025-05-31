package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        // The location icon and accents are already removed from the string

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("============RAW LOCATION: " + '"' + location + "\"============");

        if (location.equalsIgnoreCase("none")) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("Location is none, not setting anything.");
            return;
        }

        // If the current stored island is null, determine the island, zone, and region.
        // Otherwise, determine only the zone and region, based on the current island.
        if (SkydopplerClient.currentIsland == null) {

            // Go through each island and check if the location is in the zones for that island.
            for (SkyblockLocationEnum island : SkyblockLocationEnum.values()) {

                Enum<?>[] zones = island.getZonesForIsland();

                for (Enum<?> zone : zones) {

                    // Cast the zone to the correct type, and check if the name of the zone matches the location.
                    if (zone instanceof SkyblockLocationEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Found location: " + zone);

                        SkydopplerClient.currentIsland = island;
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Current island set to: " + island);

                        SkydopplerClient.currentZone = zone;
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Current zone set to: " + zone.name());

                        // If the zone contains any region, set it to the zone's (unknown) region by using a generic getRegion interface method.
                        if (zone instanceof SkyblockLocationEnum.EnumRegion) {

                            SkydopplerClient.currentRegion = ((SkyblockLocationEnum.EnumRegion) zone).getRegion();
                            if (SkydopplerClient.debugModeEnabled)
                                System.out.println("Current region set to: " + SkydopplerClient.currentRegion);

                        } else {

                            SkydopplerClient.currentRegion = null;
                            if (SkydopplerClient.debugModeEnabled)
                                System.out.println("Current region set to: null");

                        }

                        if (island == SkyblockLocationEnum.DUNGEON) {

                            Pattern pattern = Pattern.compile("\\((.*?)\\)");
                            Matcher matcher = pattern.matcher(location);

                            if (matcher.find()) {
                                String floor = matcher.group(1);

                                if (floor.equalsIgnoreCase("e")) {
                                    SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(0);
                                    if (SkydopplerClient.debugModeEnabled)
                                        System.out.println("Current dungeon floor set to: " + SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor());
                                    return;
                                }
                                floor = floor.replaceFirst("([fm])", "");
                                SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(Integer.parseInt(floor));
                                if (SkydopplerClient.debugModeEnabled)
                                    System.out.println("Current dungeon floor set to: " + SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor());

                            }


                        }
                        break;
                    }
                    /*public <T extends NamedEnum> void printEnumName(T enumValue) {
                        System.out.println(enumValue.getName());
                    }*/
                }
            }

        } else { // If the current stored island is not null...

            // Get the zones for the current stored island, and go through the zones list until there is a match.
            // If there is a match, set the current zone and region (if applicable).
            Enum<?>[] zones = SkydopplerClient.currentIsland.getZonesForIsland();

            for (Enum<?> zone : zones) {

                // Cast the zone to the correct type, and check if the name of the zone matches the location.
                if (zone instanceof SkyblockLocationEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                    if (SkydopplerClient.debugModeEnabled)
                        System.out.println("Found location: " + zone);

                    SkydopplerClient.currentZone = zone;
                    if (SkydopplerClient.debugModeEnabled)
                        System.out.println("Current zone set to: " + zone.name());

                    // If the zone contains any region, set it to the zone's (unknown) region by using a generic getRegion interface method.
                    if (zone instanceof SkyblockLocationEnum.EnumRegion) {

                        SkydopplerClient.currentRegion = ((SkyblockLocationEnum.EnumRegion) zone).getRegion();
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Current region set to: " + SkydopplerClient.currentRegion);

                    } else {

                        SkydopplerClient.currentRegion = null;
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Current region set to: null");

                    }

                    if (SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON) {

                        Pattern pattern = Pattern.compile("\\((.*?)\\)");
                        Matcher matcher = pattern.matcher(location);

                        if (matcher.find()) {
                            String floor = matcher.group(1);

                            if (floor.equalsIgnoreCase("e")) {
                                SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(0);
                                if (SkydopplerClient.debugModeEnabled)
                                    System.out.println("Current dungeon floor set to: " + SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor());
                                return;
                            }
                            floor = floor.replaceFirst("([fm])", "");
                            SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(Integer.parseInt(floor));
                            if (SkydopplerClient.debugModeEnabled)
                                System.out.println("Current dungeon floor set to: " + SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor());

                        }


                    }
                    break;
                }
                    /*public <T extends NamedEnum> void printEnumName(T enumValue) {
                        System.out.println(enumValue.getName());
                    }*/
            }
        }

    }

        

        /*SkydopplerClient.currentIsland = SkyblockIslandEnum.NONE;
        SkydopplerClient.currentZone = SkyblockIslandEnum.NONE.getZonesForIsland()[0]; // Sets currentZone to the first enum for the island of type "NONE", which is also "NONE" (the only value for the island of type "NONE").*/
}
