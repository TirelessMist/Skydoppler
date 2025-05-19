package ae.skydoppler.config;

import ae.skydoppler.player_hiding.HideHubPlayersState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class SkydopplerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public HideHubPlayersState.HideLocationMode hideFarPlayersMode = HideHubPlayersState.HideLocationMode.NONE;
    public int hideFarPlayersModeDistance = 12;
    public boolean hidePlayersNearNpc = false;

    public VanillaHudConfig vanillaHudConfig = new VanillaHudConfig();

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

    public static class VanillaHudConfig {
        public boolean shouldHideHungerArmorBubbles = true;
        public boolean shouldHideHealthBar = false;
        public boolean shouldHideStatusEffectOverlay = true;
        public boolean shouldHideHeldItemTooltip = false;
        public boolean shouldHideMountHealth = true;
        public boolean shouldHidePortalOverlay = true;
    }
}
