package ae.skydoppler.skyblock_locations;

import ae.skydoppler.SkydopplerClient;

public class SkyblockLocationHandler {
    public static void setLocationFromString(String location) {
        // The location icon is already removed from the string

        // Check if the location is a valid Skyblock location
        if (SkyblockIslandEnum.isValidLocation(location)) {
            // Set the location
            SkydopplerClient.playerDataStruct.setSkyblockLocation(SkyblockIslandEnum.valueOf(location));
            SkydopplerClient.playerDataStruct.setIslandType();
        } else {
            // If the location is not valid, set it to null
            SkydopplerClient.playerDataStruct.setSkyblockLocation(SkyblockIslandEnum.NONE);
        }
    }
}
