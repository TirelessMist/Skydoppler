package ae.skydoppler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.w3c.dom.Text;

public class SkydopplerClient implements ClientModInitializer {
	public static MinecraftClient client = MinecraftClient.getInstance();

	TextRenderer textRenderer;
	@Override
	public void onInitializeClient() {
		ChatNotificationHandler.loadJsonData();
		textRenderer = new TextRenderer(client);
		textRenderer.initialize();
	}

	private void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {

	}
}