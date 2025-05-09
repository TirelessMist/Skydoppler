package ae.skydoppler.chat;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.TextRenderer;
import ae.skydoppler.fishing.SeacreatureMessageState;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatMatchHandler {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static JsonObject chat_matches_json;
    private static JsonArray chat_matches_entries;
    private static JsonObject sea_creature_matches_json;
    private static JsonArray sea_creature_matches_entries;

    public static String checkForMatches(String chatMessage) {
        // can return a "replace" string, where it replaces the original chat message with the String returned from this function

        String returnString = "";

        for (JsonElement element : chat_matches_entries) { // for each message "block"

            JsonObject obj = element.getAsJsonObject(); // gets the current message "block" as a JsonObject

            String currentChatMatchType = obj.get("ChatMatchType").getAsString();
            String currentChatMatchCaseSensitivityType = obj.get("ChatMatchCaseSensitivityType").getAsString();
            JsonArray matchesJsonArray = obj.getAsJsonArray("matches");
            List<String> matchStrings = new ArrayList<>();
            for (JsonElement jsonElement : matchesJsonArray)
                matchStrings.add(jsonElement.getAsString());

            if (CheckMatch(chatMessage, matchStrings, ChatMatchType.valueOf(currentChatMatchType), ChatMatchCaseSensitivityType.valueOf(currentChatMatchCaseSensitivityType))) {
                if (obj.get("playSound").getAsBoolean()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {

                        client.player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);

                    }
                }
                TextRenderer.DisplayTitle(Text.literal(obj.get("displayText").getAsString()), Text.empty(), 0, 90, 0);
                //System.out.println("Chat Match Display Text: " + obj.get("displayText"));
                return returnString;
            }

        }

        for (JsonElement element : sea_creature_matches_entries) {

            JsonObject obj = element.getAsJsonObject();

            JsonArray matchesJsonArray = obj.getAsJsonArray("matches");
            List<String> matchStrings = new ArrayList<>();
            for (JsonElement jsonElement : matchesJsonArray) {
                matchStrings.add(jsonElement.getAsString());
            }

            if (CheckMatch(chatMessage, matchStrings, ChatMatchType.match_exactly, ChatMatchCaseSensitivityType.case_sensitive)) {
                String displayText = obj.get("displayText").getAsString();

                if (SeacreatureMessageState.showTitle) {
                    TextRenderer.DisplayTitle(Text.literal(displayText), Text.empty(), 0, 90, 0);
                }

                if (client.player != null && SeacreatureMessageState.shouldPlaySound) {
                    client.player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }

                if (SeacreatureMessageState.showCustomChatMessage) {
                    returnString = "§eYou fished up a " + displayText + "§e.";
                }

                if (SeacreatureMessageState.shouldHideOriginalMessage) {
                    returnString = "\\hide" + returnString;
                }

                return returnString;

                //System.out.println("Legendary Sea Creature: " + obj.get("displayText"));

            }
        }

        return returnString;
    }

    private static boolean CheckMatch(String chatMessage, List<String> matchStrings, ChatMatchType matchType, ChatMatchCaseSensitivityType caseSensitivityType) {

        if (caseSensitivityType == ChatMatchCaseSensitivityType.not_case_sensitive) {
            chatMessage = chatMessage.toLowerCase(); // sets all the strings to lower case for a faster way to do no case sensitivity
            matchStrings.replaceAll(String::toLowerCase);
        }

        if (matchType == ChatMatchType.match_exactly) {
            boolean isMatch = false;
            for (String s : matchStrings) {
                if (chatMessage.equals(s)) {
                    isMatch = true; // checking quick case before all computation work
                }
            }
            return isMatch;
        }


        switch (matchType) {
            case ChatMatchType.contains -> {
                for (String s : matchStrings) {
                    if (chatMessage.contains(s)) {
                        return true;
                    }
                }
            }
            case ChatMatchType.starts_with -> {
                for (String s : matchStrings) {
                    if (chatMessage.startsWith(s)) {
                        return true;
                    }
                }
            }
        }

        return false; // if it cannot find a match, returns false
    }

    public static void loadJsonData() {
        try (InputStream stream = SkydopplerClient.class.getResourceAsStream("/chat_matches.json")) {
            if (stream == null) {
                System.err.println("Could not find chat_matches.json in resources!");
                return;
            }
            // Use InputStreamReader with an appropriate charset.
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            // Parse the JSON file using Gson
            Gson gson = new Gson();
            chat_matches_json = gson.fromJson(reader, JsonObject.class);
            reader.close();
            chat_matches_entries = chat_matches_json.getAsJsonArray("messages");
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }

        try (InputStream stream = SkydopplerClient.class.getResourceAsStream("/sea_creature_messages.json")) {
            if (stream == null) {
                System.err.println("Could not find sea_creature_messages.json in resources!");
                return;
            }
            // Use InputStreamReader with an appropriate charset.
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            // Parse the JSON file using Gson
            Gson gson = new Gson();
            sea_creature_matches_json = gson.fromJson(reader, JsonObject.class);
            reader.close();
            sea_creature_matches_entries = sea_creature_matches_json.getAsJsonArray("messages");
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
