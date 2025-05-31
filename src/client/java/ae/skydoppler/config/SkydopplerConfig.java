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

    public boolean doFarPlayerHiding = false;
    public HideHubPlayersState.HideLocationMode hideFarPlayersMode = HideHubPlayersState.HideLocationMode.HUB_ENTIRE;
    public int hideFarPlayersRange = 12;

    public boolean hideExplosionParticle = true;

    public float hidePlayersNearNpcRange = 1.25f;
    public boolean hidePlayersNearNpc = false;

    public boolean alwaysSprint = true;
    public boolean alwaysSprintOnlyInSkyblock = false;

    public boolean showFog = true;
    public boolean doFullbright = false;
    public boolean hideClouds = false;
    public boolean hideNightVisionEffect = false;

    public boolean glowingDroppedItems = false;
    public boolean glowingPlayers = false;

    public boolean hideThirdPersonFireOverlay = false;
    public VanillaHudConfig vanillaHudConfig = new VanillaHudConfig();

    public boolean hidePlayersWhileFishing = false;
    public int hidePlayersWhileFishingRange = 12;

    public boolean hideOtherFishingRods = false;
    public boolean doLegendarySeacreatureAlerts = true;
    public SeacreatureMessageConfig seacreatureMessageConfig = new SeacreatureMessageConfig();


    public boolean hideMageBeams = true;


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
        public boolean shouldHideFireOverlayFirstPerson = false;
        public boolean shouldHideWitherBossbarsInSkyblock = false;
    }

    public static class SeacreatureMessageConfig {
        public boolean shouldHideOriginalMessage = true;
        public boolean showCustomChatMessage = true;
        public boolean showTitle = true;
        public boolean shouldPlaySound = true;
    }

    public static class HeldItemRendererConfig {
        public float posX = 0.0f;
        public float posY = 0.0f;
        public float posZ = 0.0f;
        public float rotX = 0.0f;
        public float rotY = 0.0f;
        public float rotZ = 0.0f;
        public float scale = 1.0f;

        public boolean disableSwapAnimation = false;
        public boolean disableModernSwing = false;
    }
}
