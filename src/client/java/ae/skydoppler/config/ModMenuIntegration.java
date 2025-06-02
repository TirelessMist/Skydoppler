package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent -> ConfigScreenHandler.buildConfigScreen(SkydopplerClient.CONFIG, parent));
    }
}