package ae.skydoppler.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreenHandler {

    public static Screen buildConfigScreen(SkydopplerConfig config) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(MinecraftClient.getInstance().currentScreen)
                .setTitle(Text.translatable("config.ae.skydoppler.title"));

        var generalCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.hidePlayersNearNpc"), config.hidePlayersNearNpc)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.hidePlayersNearNpc = newValue)
                .build());


        var vanillaHudEntries = java.util.List.of(
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_hearts"), config.vanillaHudConfig.shouldHideHealthBar)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_hearts.tooltip"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHealthBar = newValue)
                .build(),
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_hunger_armor_bubbles"), config.vanillaHudConfig.shouldHideHungerArmorBubbles)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_hunger_armor_bubbles"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHungerArmorBubbles = newValue)
                .build(),
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_effect_hud"), config.vanillaHudConfig.shouldHideStatusEffectOverlay)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_effect_hud.tooltip"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideStatusEffectOverlay = newValue)
                .build(),
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.held_item_tooltips"), config.vanillaHudConfig.shouldHideHeldItemTooltip)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.held_item_tooltips.tooltip"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHeldItemTooltip = newValue)
                .build(),
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_mount_health"), config.vanillaHudConfig.shouldHideMountHealth)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_mount_health.tooltip"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideMountHealth = newValue)
                .build(),
            entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_portal_overlay"), config.vanillaHudConfig.shouldHidePortalOverlay)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.hide_portal_overlay.tooltip"))
                .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHidePortalOverlay = newValue)
                .build()
        );

        generalCategory
            .addEntry(entryBuilder.startSubCategory(Text.literal("Vanilla HUD Options"))
                .setTooltip(Text.translatable("config.ae.skydoppler.option.subcategory.vanilla_hud.tooltip"))
                .setExpanded(true).add((AbstractConfigListEntry) vanillaHudEntries).);


        return builder.build();
    }


}