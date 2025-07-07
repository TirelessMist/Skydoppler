package ae.skydoppler.config;

import ae.skydoppler.config.chat_matcher_config.ChatMatchConfigEntryData;
import ae.skydoppler.config.main_config.MainConfigCategory;
import ae.skydoppler.config.main_config.categories.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SkydopplerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MainConfig mainConfig = new MainConfig();

    public HeldItemRendererConfig heldItemRendererConfig = new HeldItemRendererConfig();

    public UserChatMatchConfig userChatMatchConfig = new UserChatMatchConfig();

    // Load config from file
    public static SkydopplerConfig load(Path path) {
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                return GSON.fromJson(reader, SkydopplerConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new SkydopplerConfig();
    }

    /**
     * Save the entire configuration to a file. This is costly, so it should be used sparingly.
     *
     * @param path The path to save the configuration file to.
     */
    public void save(Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save a specific field in the configuration to a file.
     * This is more efficient than saving the entire config, but it requires the field to be specified by path.
     * The path should be in dot notation (e.g., "mainConfig.general.someField").
     *
     * @param path      The path to save the configuration file to.
     * @param fieldPath The path to the field to save, in dot notation.
     * @param value     The value to set for the specified field.
     */
    public void saveField(Path path, String fieldPath, Object value) {
        try {
            JsonObject configJson;
            if (Files.exists(path)) {
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    configJson = JsonParser.parseReader(reader).getAsJsonObject();
                }
            } else {
                configJson = new JsonObject();
            }

            // Navigate to the field and update it
            String[] pathParts = fieldPath.split("\\.");
            JsonObject current = configJson;

            for (int i = 0; i < pathParts.length - 1; i++) {
                if (!current.has(pathParts[i])) {
                    current.add(pathParts[i], new JsonObject());
                }
                current = current.getAsJsonObject(pathParts[i]);
            }

            // Set the final value
            current.add(pathParts[pathParts.length - 1], GSON.toJsonTree(value));

            // Save the updated config
            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                GSON.toJson(configJson, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fall back to full save if field save fails
            save(path);
        }
    }

    public static class MainConfig {
        // UI state
        public boolean categoryPanelCollapsed = false;

        // Categories - now they extend MainConfigCategory and handle their own ordering
        public General general = new General();
        public Accessibility accessibility = new Accessibility();
        public Fishing fishing = new Fishing();
        public Dungeons dungeons = new Dungeons();
        public Inventory inventory = new Inventory();
        public Miscellaneous miscellaneous = new Miscellaneous();

        // Get all categories in priority order
        public MainConfigCategory[] getCategories() {
            MainConfigCategory[] categories = {
                    general, accessibility, fishing, dungeons, inventory, miscellaneous
            };
            // Sort by priority (lower number = higher priority)
            java.util.Arrays.sort(categories, java.util.Comparator.comparingInt(MainConfigCategory::getPriority));
            return categories;
        }
    }

    public static class HeldItemRendererConfig {
        public float posX = 0.0f;
        public float posY = 0.0f;
        public float posZ = 0.0f;
        public float rotX = 0.0f;
        public float rotY = 0.0f;
        public float rotZ = 0.0f;
        public float scale = 1.0f;

        public float swingSpeedMultiplier = 1.0f;

        public boolean disableSwapAnimation = false;
        public boolean disableModernSwing = false;
    }

    public static class UserChatMatchConfig {
        public List<ChatMatchConfigEntryData> functions = new ArrayList<>();
    }
}
