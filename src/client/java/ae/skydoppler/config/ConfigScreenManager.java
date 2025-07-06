package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.chat_matcher_config.ChatMatchConfigScreen;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import ae.skydoppler.config.main_config.MainConfigScreen;
import net.minecraft.client.MinecraftClient;

public class ConfigScreenManager {

    public static void openMainConfigScreen() {
        MinecraftClient.getInstance().setScreen(MainConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null));
    }

    public static void openHeldItemConfigScreen() {
        MinecraftClient.getInstance().setScreen(HeldItemConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null));
    }

    public static void openChatMatchConfigScreen() {
        MinecraftClient.getInstance().setScreen(ChatMatchConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null));
    }

}
