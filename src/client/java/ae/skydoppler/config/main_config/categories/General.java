package ae.skydoppler.config.main_config.categories;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import ae.skydoppler.config.main_config.MainConfigCategory;
import ae.skydoppler.player_hiding.PlayerHidingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class General extends MainConfigCategory {

    public boolean doTransferCooldownFinishedAlert = true;
    public HideFarPlayers hideFarPlayers = new HideFarPlayers();
    public HidePlayersNearNpc hidePlayersNearNpc = new HidePlayersNearNpc();
    public AlwaysSprint alwaysSprint = new AlwaysSprint();
    public VisualSettings visualSettings = new VisualSettings();
    public GlowingSettings glowingSettings = new GlowingSettings();
    public OldVersionParityConfig oldVersionParityConfig = new OldVersionParityConfig();
    public General() {
        super("general", Text.translatable("config.ae.skydoppler.main_config.category.general"), 0);
    }

    public static class HideFarPlayers {
        public boolean enabled = false;
        public PlayerHidingHelper.HubHideLocationMode hideFarPlayersMode = PlayerHidingHelper.HubHideLocationMode.HUB_ENTIRE;
        public int hideFarPlayersRange = 12;
    }

    public static class HidePlayersNearNpc {
        public boolean enabled = false;
        public float hidePlayersNearNpcRange = 1.25f;
    }

    public static class AlwaysSprint {
        public boolean enabled = true;
        public boolean alwaysSprintOnlyInSkyblock = false;
    }

    public static class VisualSettings {

        public boolean hideExplosionParticle = true;
        public boolean doFullbright = false;
        public boolean hideNightVisionEffect = false;
        public boolean hideThirdPersonFireOverlay = false;
        public VanillaHud vanillaHudConfig = new VanillaHud();

        public Screen getHeldItemConfigScreen(Screen parent) {
            return new HeldItemConfigScreen(SkydopplerClient.CONFIG, parent);
        }

        public static class VanillaHud {
            public boolean hideChatIndicators = true;
            public boolean shouldHideHealthBar = false;
            public boolean shouldHideHungerArmorBubbles = true;
            public boolean shouldHideStatusEffectOverlay = true;
            public boolean shouldHideHeldItemTooltip = false;
            public boolean shouldHideMountHealth = true;
            public boolean shouldHidePortalOverlay = true;
            public boolean shouldHideFireOverlayFirstPerson = false;
            public boolean shouldHideWitherBossbarsInSkyblock = false;
        }
    }

    public static class GlowingSettings {
        public boolean glowingDroppedItems = false;
        public boolean glowingPlayers = false;
    }

    public static class OldVersionParityConfig {
        public boolean doSwordBlocking = false;
        public boolean doOldCrouchHeight = false;
    }


}
