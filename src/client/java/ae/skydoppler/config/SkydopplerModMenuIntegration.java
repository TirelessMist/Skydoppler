package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;

public class SkydopplerModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent -> {;
            return NewConfigScreenHandler.createConfigScreen(SkydopplerClient.CONFIG);
        });
    }
}
