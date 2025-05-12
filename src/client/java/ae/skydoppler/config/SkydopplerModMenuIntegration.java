package ae.skydoppler.config;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import ae.skydoppler.config.SkydopplerConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class SkydopplerModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Return a lambda that creates your config screen.
        return parent -> new SkydopplerConfigScreen(parent);
    }
}
