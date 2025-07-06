package ae.skydoppler.config.main_config.categories;

import ae.skydoppler.config.main_config.MainConfigCategory;
import net.minecraft.text.Text;

public class Fishing extends MainConfigCategory {

    public boolean hideOthersFishingRods = false;
    public HidePlayersWhileFishing hidePlayersWhileFishing = new HidePlayersWhileFishing();
    public LegendarySeaCreatureAlerts legendarySeaCreatureAlerts = new LegendarySeaCreatureAlerts();

    public Fishing() {
        super("fishing", Text.translatable("config.ae.skydoppler.main_config.category.fishing"), 1);
    }

    public static class HidePlayersWhileFishing {
        public boolean enabled = false;
        public int hidePlayersWhileFishingRange = 12;
    }

    public static class LegendarySeaCreatureAlerts {
        public boolean enabled = false;
        public boolean shouldHideOriginalMessage = true;
        public boolean showCustomChatMessage = true;
        public boolean showTitle = true;
        public boolean shouldPlaySound = true;
    }

}
