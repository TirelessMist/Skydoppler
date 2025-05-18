package ae.skydoppler.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.awt.*;

public class StatNumberModules {

    private static MinecraftClient client = MinecraftClient.getInstance();

    public static class HealthNumberModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;
        public static boolean textShadow = true;
        public static int textColor = new Color(240, 10, 40).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int health) {

            String healthText = Integer.toString(health);
            drawContext.drawText(client.textRenderer, healthText, x, y, textColor, textShadow);
        }
    }

    public static class ManaNumberModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;
        public static boolean textShadow = true;
        public static int textColor = new Color(40, 40, 240).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int mana) {

            String manaText = Integer.toString(mana);
            drawContext.drawText(client.textRenderer, manaText, x, y, textColor, textShadow);
        }
    }

    public static class SkillXpNumberModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;
        public static boolean textShadow = true;
        public static int textColor = new Color(138, 234, 244).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int skillXp) {

            String skillXpText = Integer.toString(skillXp);
            drawContext.drawText(client.textRenderer, skillXpText, x, y, textColor, textShadow);
        }
    }

    public static class DefenseNumberModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;
        public static boolean textShadow = true;
        public static int textColor = new Color(32, 211, 7).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int defense) {

            String defenseText = Integer.toString(defense);
            drawContext.drawText(client.textRenderer, defenseText, x, y, textColor, textShadow);
        }
    }

    public static class SpeedNumberModule {
        public static int x = 0;
        public static int y = 0;
        public static int width = 100;
        public static int height = 10;
        public static boolean textShadow = true;
        public static int textColor = new Color(255, 255, 255).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int speed) {

            String speedText = Integer.toString(speed);
            drawContext.drawText(client.textRenderer, speedText, x, y, textColor, textShadow);
        }
    }

}
