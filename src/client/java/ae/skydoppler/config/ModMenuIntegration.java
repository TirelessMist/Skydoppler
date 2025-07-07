package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.main_config.MainConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class ModMenuIntegration implements ModMenuApi {

    private static Screen buildConfigScreen(Screen parent) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f)); // Play the click sound because ModMenu doesn't
        return MainConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, parent);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::buildConfigScreen;
    }
}