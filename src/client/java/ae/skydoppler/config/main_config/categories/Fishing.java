package ae.skydoppler.config.main_config.categories;

public class Fishing {

    public boolean hideOthersFishingRods = false;

    public HidePlayersWhileFishing hidePlayersWhileFishing = new HidePlayersWhileFishing();
    public LegendarySeaCreatureAlerts legendarySeaCreatureAlerts = new LegendarySeaCreatureAlerts();

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
