package ae.skydoppler.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.awt.*;

public class StatBarModules {

    public static class HealthBarModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, float healthPercentage) {

            int finalWidth = (int) (x + (width * healthPercentage));

            drawContext.drawBorder(x, y, finalWidth, y + height, new Color(30, 0, 0).getRGB());
            drawContext.fill(x, y, finalWidth, y + height, new Color(227, 16, 16).getRGB());

        }
    }

    public static class ManaBarModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, float manaPercentage) {

            int finalWidth = (int) (x + (width * manaPercentage));

            drawContext.drawBorder(x, y, finalWidth, y + height, new Color(0, 0, 30).getRGB());
            drawContext.fill(x, y, finalWidth, y + height, new Color(16, 16, 227).getRGB());

        }
    }

    public static class SkillXpBarModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, float skillXpPercentage) {

            int finalWidth = (int) (x + (width * skillXpPercentage));

            drawContext.drawBorder(x, y, finalWidth, y + height, new Color(30, 18, 0).getRGB());
            drawContext.fill(x, y, finalWidth, y + height, new Color(106, 147, 221).getRGB());

        }
    }

}
