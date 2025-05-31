package ae.skydoppler.config.held_item_config;

import net.minecraft.client.MinecraftClient;

public class HeldItemConfigScreenHandler {
    private final HeldItemConfig config = new HeldItemConfig();

    public HeldItemConfig getConfig() {
        return config;
    }

    public void openConfigScreen() {
        MinecraftClient.getInstance().setScreen(new HeldItemConfigScreen(config));
    }
}
