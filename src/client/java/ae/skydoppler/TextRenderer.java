package ae.skydoppler;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.awt.*;

public class TextRenderer {

    // The text to use for the current title
    static Text currentTitle = Text.empty();
    static Text currentSubtitle = Text.empty();

    // The tick time (using the in-world time) when the title display should end.
    public static long titleDisplayEndTick = 0;


    static MinecraftClient client;

    public TextRenderer() {
        client = MinecraftClient.getInstance();
    }

    public TextRenderer(MinecraftClient client) {
        TextRenderer.client = client;
    }

    public void initialize() {
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    public static void DrawTextLine(Text text, Point position) {
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

        float tickDelta = renderTickCounter.getDynamicDeltaTicks();

        if (client.world == null || client.player == null) {
            return;
        }
        long currentTick = client.world.getTime();
        if (!currentTitle.getString().isEmpty() && currentTick < titleDisplayEndTick) {
            float scale = 2.0f;


            int scaledWidth = client.getWindow().getScaledWidth();
            int scaledHeight = client.getWindow().getScaledHeight();
            int textHeight = client.textRenderer.fontHeight;

            int titleTextWidth = client.textRenderer.getWidth(currentTitle);
            int titleX = (int) ((scaledWidth - titleTextWidth * scale) / 2f);
            int titleY = (int) ((scaledHeight - textHeight * scale) / 2f); // arbitrary vertical position for the title

            int subtitleTextWidth = client.textRenderer.getWidth(currentSubtitle);
            int subtitleX = (int) ((scaledWidth - subtitleTextWidth * scale) / 2f);
            int subtitleY = (int) ((scaledHeight - textHeight * scale) / 2f); // arbitrary vertical position for the subtitle


            drawContext.getMatrices().push();

            drawContext.getMatrices().scale(scale, scale, 1.0f);

            drawContext.drawText(client.textRenderer, currentTitle, (int) (titleX / scale), (int) ((titleY / scale) * .75f), 0xFFFFFF, true);
            drawContext.drawText(client.textRenderer, currentSubtitle, (int) (subtitleX / scale), (int) ((subtitleY / scale) * .5f), 0xEEEEEE, true);

            drawContext.getMatrices().pop();
        } else {
            // Clear the title if the display time has expired.
            currentTitle = Text.empty();
        }
    }
}
