package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.main_config.MainConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent -> MainConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, parent));
    }
}