package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        if (SkydopplerClient.debugModeEnabled)
            System.out.println("============RAW LOCATION: " + '"' + location + "\"============");

        if (location.equalsIgnoreCase("none")) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("Location is none, not setting anything.");
            return;
        }

        if (SkydopplerClient.currentIsland == null) {
            // Determine the current island and zone from the string
            for (SkyblockLocationEnum island : SkyblockLocationEnum.values()) {
                Enum<?>[] zones = island.getZonesForIsland();

                for (Enum<?> zone : zones) {
                    if (zone instanceof SkyblockLocationEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                        if (SkydopplerClient.debugModeEnabled)
                            System.out.println("Found location: " + zone);

                        SkydopplerClient.currentIsland = island;
                        SkydopplerClient.currentZone = zone;

                        if (SkydopplerClient.debugModeEnabled) {
                            System.out.println("Current island set to: " + island);
                            System.out.println("Current zone set to: " + zone.name());
                        }

                        setRegionForZone(zone);
                        if (island == SkyblockLocationEnum.DUNGEON) {
                            handleDungeonLocation(location);
                        }
                        return;
                    }
                }
            }
        } else {
            // Try to set the current zone from the current island
            Enum<?>[] zones = SkydopplerClient.currentIsland.getZonesForIsland();
            boolean zoneFound = false;

            for (Enum<?> zone : zones) {
                if (zone instanceof SkyblockLocationEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                    if (SkydopplerClient.debugModeEnabled)
                        System.out.println("Found location in current island: " + zone);

                    SkydopplerClient.currentZone = zone;

                    if (SkydopplerClient.debugModeEnabled)
                        System.out.println("Current zone set to: " + zone.name());

                    setRegionForZone(zone);
                    if (SkydopplerClient.currentIsland == SkyblockLocationEnum.DUNGEON) {
                        handleDungeonLocation(location);
                    }
                    zoneFound = true;
                    break;
                }
            }

            // If no zone is found in the current island, search all islands
            if (!zoneFound) {
                for (SkyblockLocationEnum island : SkyblockLocationEnum.values()) {
                    zones = island.getZonesForIsland();

                    for (Enum<?> zone : zones) {
                        if (zone instanceof SkyblockLocationEnum.EnumName enumName && enumName.getName().equalsIgnoreCase(location)) {
                            if (SkydopplerClient.debugModeEnabled)
                                System.out.println("Found location in another island: " + zone);

                            SkydopplerClient.currentIsland = island;
                            SkydopplerClient.currentZone = zone;

                            if (SkydopplerClient.debugModeEnabled) {
                                System.out.println("Current island set to: " + island);
                                System.out.println("Current zone set to: " + zone.name());
                            }

                            setRegionForZone(zone);
                            if (island == SkyblockLocationEnum.DUNGEON) {
                                handleDungeonLocation(location);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void setRegionForZone(Enum<?> zone) {
        if (zone instanceof SkyblockLocationEnum.EnumRegion) {
            SkydopplerClient.currentRegion = ((SkyblockLocationEnum.EnumRegion) zone).getRegion();
        } else {
            SkydopplerClient.currentRegion = null;
        }

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("Current region set to: " + SkydopplerClient.currentRegion);
    }

    private static void handleDungeonLocation(String location) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(location);

        if (matcher.find()) {
            String floor = matcher.group(1);

            try {
                if (floor.equalsIgnoreCase("e")) {
                    SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(0);
                } else {
                    floor = floor.replaceFirst("([fm])", "");
                    SkydopplerClient.dungeonClientHandler.setCurrentDungeonFloor(Integer.parseInt(floor));
                }

                if (SkydopplerClient.debugModeEnabled)
                    System.out.println("Current dungeon floor set to: " + SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor());
            } catch (NumberFormatException e) {
                System.err.println("Invalid dungeon floor format: " + floor);
            }
        }
    }
}
