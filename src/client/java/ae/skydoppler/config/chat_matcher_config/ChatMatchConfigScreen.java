package ae.skydoppler.config.chat_matcher_config;

import ae.skydoppler.config.SkydopplerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Configuration screen for chat matching functions.
 */
public class ChatMatchConfigScreen extends Screen {

    private final SkydopplerConfig config;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Screen parent;

    public ChatMatchConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        super(Text.translatable("config.ae.skydoppler.chatnotification.title"));
        this.config = config;
        this.parent = parent;
    }

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        return new ChatMatchConfigScreen(config, parent);
    }
}
/*Thank you for the code. Now, please make a config screen for adding and managing chat match "functions". It should be in ChatMatchConfigScreen.java. buildConfigScreen method will be the method used to create instances of this config screen. The screen should have a main scrollable list of all the "functions" from SkydopplerClient.CONFIG (hereon referred to as "the config"), and an exit button. The scrolling should be smooth and there should be a scroll bar. Each "function" in the main list can be clicked on to open a child menu that allows the user to manage/see all the details of the specific chat matching "function". Inside, it should allow the user to configure all the settings for a ChatMatchConfigEntryData instance. There should be a text box for the name (it should default to the first 24 (at maximum, 24. can be less) characters of the first ChatMatchEntryData's matchString. there should be a toggle for enabled, playSound, text boxes for displayTitle and displayCustomChatMessage. there should be a toggle for hideOriginalChatMessage. there should be two collapsable sections. One for the List of executeCommands text boxes, and the other for the List of matches, where each match should be its own child collapsable section. This child menu should also have the same scrolling features as the main menu. Whenever a value is modified/verified, the config should be updated and saved. Thank you.*/
