package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class SkydopplerClient implements ClientModInitializer {

	public static MinecraftClient client = MinecraftClient.getInstance();

	public static KeyBinding printScoreboardLines;

	private TextRenderer textRenderer;

	public static SkyblockPlayerDataStruct playerDataStruct;

	@Override
	public void onInitializeClient() {
		textRenderer = new TextRenderer(client);
		textRenderer.initialize();
		ChatMatchHandler.loadJsonData();

		printScoreboardLines = KeyBindingHelper.registerKeyBinding(new KeyBinding("Print Scoreboard Lines to Log", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (printScoreboardLines.wasPressed()) {
				if (client.player != null && client.world != null) {


					Scoreboard scoreboard = client.world.getScoreboard();
					for (Team team : scoreboard.getTeams()) {
						System.out.println("===SCOREBOARD TEAM \"" + team.getDisplayName().getSiblings() + "\"===");

						System.out.println("Scoreboard Line Team Prefix: " + team.getPrefix().getSiblings());
						System.out.println("Scoreboard Line Team Name: " + team.getName());
						System.out.println("Scoreboard Line Team Suffix: " + team.getSuffix().getSiblings());

						System.out.println("--------------------------");
					}


				}
			}
		});

	}
}