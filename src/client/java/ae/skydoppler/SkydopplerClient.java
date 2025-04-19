package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.w3c.dom.Text;

public class SkydopplerClient implements ClientModInitializer {

	public static MinecraftClient client = MinecraftClient.getInstance();

	private TextRenderer textRenderer;

	@Override
	public void onInitializeClient() {
		textRenderer = new TextRenderer(client);
		textRenderer.initialize();
		ChatMatchHandler.loadJsonData();
	}
}