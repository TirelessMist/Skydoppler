package ae.skydoppler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TextRenderer {

    // The text to use for the current title
    static Text currentTitle = Text.empty();
    static Text currentSubtitle = Text.empty();

    // The tick time (using the in-world time) when the title display should end.
    public static long titleDisplayEndTick = 0;




    private static JsonObject hudLinesJsonObject;


    static MinecraftClient client;

    public TextRenderer() {
        client = MinecraftClient.getInstance();
    }

    public TextRenderer(MinecraftClient client) {
        TextRenderer.client = client;
    }

    public void initialize() {
        HudRenderCallback.EVENT.register(this::onHudRender);
        loadJsonData();
    }

    public void DrawTextLine(Text text, Point position) {
        if (SkydopplerClient.client.player != null) {
        }
    }

    public static void DisplayTitle(Text titleText, Text subtitleText, int fadeIn, int stay, int fadeOut) {

        // Cancels the function if the game is not in a valid world or there is no valid player.
        if (SkydopplerClient.client.world == null) return;

        long currentTick = client.world.getTime();

        long totalDuration = fadeIn + stay + fadeOut;
        titleDisplayEndTick = currentTick + totalDuration;
        currentTitle = titleText;
        currentSubtitle = subtitleText;

    }

    private void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        drawTitle(drawContext, renderTickCounter);
        drawAllLines(drawContext, renderTickCounter);
    }

    private void drawTitle(DrawContext drawContext, RenderTickCounter renderTickCounter) {

        float tickDelta = renderTickCounter.getDynamicDeltaTicks();

        if (client.world == null || client.player == null) {
            return;
        }
        long currentTick = client.world.getTime();
        if (!currentTitle.getString().isEmpty() && currentTick < titleDisplayEndTick) {
            float scaleTitle = 2.5f;
            float scaleSubtitle = 2.0f;


            int scaledWidth = client.getWindow().getScaledWidth();
            int scaledHeight = client.getWindow().getScaledHeight();
            int textHeight = client.textRenderer.fontHeight;

            int titleTextWidth = client.textRenderer.getWidth(currentTitle);
            int titleX = (int) ((scaledWidth - titleTextWidth * scaleTitle) / 2f);
            int titleY = (int) ((scaledHeight - textHeight * scaleTitle) / 2f); // arbitrary vertical position for the title

            int subtitleTextWidth = client.textRenderer.getWidth(currentSubtitle);
            int subtitleX = (int) ((scaledWidth - subtitleTextWidth * scaleSubtitle) / 2f);
            int subtitleY = (int) ((scaledHeight - textHeight * scaleSubtitle) / 2f); // arbitrary vertical position for the subtitle


            drawContext.getMatrices().push();

            drawContext.getMatrices().scale(scaleTitle, scaleTitle, 1.0f);

            drawContext.drawText(client.textRenderer, currentTitle, (int) (titleX / scaleTitle), (int) ((titleY / scaleTitle) * .8f), 0xFFFFFF, true);
            drawContext.getMatrices().pop();

            drawContext.getMatrices().push();

            drawContext.getMatrices().scale(scaleSubtitle, scaleSubtitle, 1.0f);

            drawContext.drawText(client.textRenderer, currentSubtitle, (int) (subtitleX / scaleSubtitle), (int) ((subtitleY / scaleSubtitle) * 1.15f), 0xEEEEEE, true);

            drawContext.getMatrices().pop();

        } else {
            // Clear the title if the display time has expired.
            currentTitle = Text.empty();
            currentSubtitle = Text.empty();
        }
    }

    private void drawAllLines(DrawContext drawContext, RenderTickCounter renderTickCounter) {

    }

    private void loadJsonData() {
        try (InputStream stream = SkydopplerClient.class.getResourceAsStream("/chat_matches.json")) {
            if (stream == null) {
                System.err.println("Could not find chat_matches.json in resources!");
                return;
            }
            // Use InputStreamReader with an appropriate charset.
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            // Parse the JSON file using Gson
            Gson gson = new Gson();
            hudLinesJsonObject = gson.fromJson(reader, JsonObject.class);
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
