package ae.skydoppler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChatNotificationHandler {
    private static JsonObject jsonObject;
    private static JsonArray messageEntries;

    public static void checkForMatches(Text chatMessage) {
        String matchString = null;
        for (JsonElement element : messageEntries) {
            JsonObject obj = element.getAsJsonObject();
            switch (obj.get("ChatMatchCaseSensitivityType").toString()) {
                case "match_exactly": {

                }
                case "case_sensitive": {

                }
                case "not_case_sensitive": {

                }
            }
        }
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
