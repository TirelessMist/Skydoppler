package ae.skydoppler.skyblock;

public enum SkyblockRarity {
    COMMON("§f", "§fCommon"),
    UNCOMMON("§a", "§aUncommon"),
    RARE("§9", "§9Rare"),
    EPIC("§5", "§5Epic"),
    LEGENDARY("§6", "§6Legendary"),
    MYTHIC("§d", "§dMythic"),
    DIVINE("§b", "§bDivine"),
    SPECIAL("§c", "§cSpecial");

    private final String colorCode;
    private final String displayName;

    SkyblockRarity(String colorCode, String displayName) {
        this.colorCode = colorCode;
        this.displayName = displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }
}
