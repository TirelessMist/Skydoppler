package ae.skydoppler.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ConfigScreenHandler {

    public static Screen buildConfigScreen(SkydopplerConfig config) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(MinecraftClient.getInstance().currentScreen)
                .setTitle(Text.translatable("config.ae.skydoppler.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();


        //region GENERAL_CATEGORY
        var generalCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.general"));


        // TODO: add hide players in hub range (and fix region detection).


        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpc"), config.hidePlayersNearNpc)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpc.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersNearNpc = newValue)
                .build());

        // TODO: add conversion to divide long by 100 to get decimal number.
        generalCategory.addEntry(entryBuilder.startLongSlider(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpcRange"), config.hidePlayersNearNpcRange, 0L, 500L)
                .setDefaultValue(125L)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpcRange.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersNearNpcRange = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.showFog"), config.showFog)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.showFog.tooltip"))
                .setSaveConsumer(newValue -> config.showFog = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.glowingDroppedItems"), config.glowingDroppedItems)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.glowingDroppedItems.tooltip"))
                .setSaveConsumer(newValue -> config.glowingDroppedItems = newValue)
                .build());

        //noinspection rawtypes
        List<AbstractConfigListEntry> vanillaHudEntries = java.util.List.of(
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_hearts"), config.vanillaHudConfig.shouldHideHealthBar)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_hearts.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHealthBar = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_hunger_armor_bubbles"), config.vanillaHudConfig.shouldHideHungerArmorBubbles)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_hunger_armor_bubbles"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHungerArmorBubbles = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_effect_hud"), config.vanillaHudConfig.shouldHideStatusEffectOverlay)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_effect_hud.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideStatusEffectOverlay = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.held_item_tooltips"), config.vanillaHudConfig.shouldHideHeldItemTooltip)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.held_item_tooltips.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHeldItemTooltip = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_mount_health"), config.vanillaHudConfig.shouldHideMountHealth)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_mount_health.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideMountHealth = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_portal_overlay"), config.vanillaHudConfig.shouldHidePortalOverlay)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_portal_overlay.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHidePortalOverlay = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_fire_overlay_first_person"), config.vanillaHudConfig.shouldHideFireOverlayFirstPerson)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hide_fire_overlay_first_person.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideFireOverlayFirstPerson = newValue)
                        .build()
        );

        generalCategory.addEntry(entryBuilder.startSubCategory(
                                Text.translatable("config.ae.skydoppler.general.option.subcategory.vanilla_hud"),
                                vanillaHudEntries
                        )
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.subcategory.vanilla_hud.tooltip"))
                        .build()
        );

        //endregion

        //region Description

        var fishingCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.fishing"));

        fishingCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishing"), config.hidePlayersWhileFishing)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishing.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersWhileFishing = newValue)
                .build());
        fishingCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.hideOtherFishingRods"), config.hideOtherFishingRods)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.hideOtherFishingRods.tooltip"))
                .setSaveConsumer(newValue -> config.hideOtherFishingRods = newValue)
                .build());

        //endregion


        return builder.build();
    }


}