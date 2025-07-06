package ae.skydoppler.config;

import ae.skydoppler.config.chat_matcher_config.ChatMatchConfigEntryData;
import ae.skydoppler.config.main_config.categories.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    // Save config to file
    public void save(Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class MainConfig {
        public General general = new General();
        public Fishing fishing = new Fishing();
        public Dungeons dungeons = new Dungeons();
        public Inventory inventory = new Inventory();
        public Miscellaneous miscellaneous = new Miscellaneous();
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
