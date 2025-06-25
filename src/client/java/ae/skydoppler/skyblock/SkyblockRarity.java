package ae.skydoppler.skyblock;

import net.minecraft.text.Text;

public enum SkyblockRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    DIVINE,
    SPECIAL;

    public String getColorCode() {
        return "§" + Text.translatable(getDisplayNameTranslationKey()).getString().charAt(1);
    }

    public String getDisplayNameTranslationKey() {
        return "hypixel.skyblock.rarity." + name().toLowerCase();
    }
}
