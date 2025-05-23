package ae.skydoppler.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class NewConfigScreenHandler {

    public static Screen createConfigScreen(SkydopplerConfig config) {
        ModConfigAPI.ConfigScreen builder = new ModConfigAPI.ConfigScreen(Text.translatable("config.ae.skydoppler.title"),
                List.of(
                        new ModConfigAPI.ConfigCategory(Text.translatable("config.ae.skydoppler.category.general")),
                        new ModConfigAPI.ConfigCategory(Text.translatable("config.ae.skydoppler.category.dungeon")),
                        new ModConfigAPI.ConfigCategory(Text.translatable("config.ae.skydoppler.category.fishing"))),
                true);

        return builder;
    }

}
