package ae.skydoppler.chat;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.TextRenderer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatMatchHandler {
    private static JsonObject jsonObject;
    private static JsonArray messageEntries;

    public static void checkForMatches(Text chatMessage) {
        for (JsonElement element : messageEntries) { // for each message "block"

            JsonObject obj = element.getAsJsonObject(); // gets the current message "block" as a JsonObject

            String textOfChatMessage = chatMessage.getString();
            String currentChatMatchType = obj.get("ChatMatchType").getAsString();
            String currentChatMatchCaseSensitivityType = obj.get("ChatMatchCaseSensitivityType").getAsString();
            JsonArray matchesJsonArray = obj.getAsJsonArray("matches");
            List<String> matchStrings = new ArrayList<>();
            for (JsonElement jsonElement : matchesJsonArray) {
                matchStrings.add(jsonElement.getAsString());
            }

            if (CheckMatch(textOfChatMessage, matchStrings, ChatMatchType.valueOf(currentChatMatchType), ChatMatchCaseSensitivityType.valueOf(currentChatMatchCaseSensitivityType))) {
                if (obj.get("playSound").getAsBoolean()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {

                        client.player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);

                    }
                }
                TextRenderer.DisplayTitle(Text.literal(obj.get("displayText").getAsString()), Text.empty(), 0, 90, 0);
                System.out.println("Chat Match Display Text: " + obj.get("displayText"));
                break;
            }

        }
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
            jsonObject = gson.fromJson(reader, JsonObject.class);
            reader.close();
            messageEntries = jsonObject.getAsJsonArray("messages");
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
