package ae.skydoppler.config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.player_hiding.PlayerHidingHelper;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConfigScreenHandler {

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.ae.skydoppler.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        builder.setGlobalized(true);

        //region GENERAL_CATEGORY
        var generalCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.general"));


        //region hideFarPlayers

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.doFarPlayerHiding"), config.doFarPlayerHiding)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.doFarPlayerHiding.tooltip"))
                .setSaveConsumer(v -> config.doFarPlayerHiding = v)
                .build()
        );

        generalCategory.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode"),
                        PlayerHidingHelper.HubHideLocationMode.class,
                        config.hideFarPlayersMode
                )
                .setDefaultValue(PlayerHidingHelper.HubHideLocationMode.HUB_ENTIRE)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode.tooltip"))
                .setEnumNameProvider(mode -> Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode." + mode.name().toLowerCase()))
                .setTooltipSupplier(mode -> Optional.of(new Text[]{
                        Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode." + mode.name().toLowerCase() + ".tooltip")
                }))
                .setSaveConsumer(newValue -> config.hideFarPlayersMode = newValue)

                .build());

        /*generalCategory.addEntry(entryBuilder.startDropdownMenu(
                                Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode"),
                                DropdownMenuBuilder.TopCellElementBuilder.of(
                                        config.hideFarPlayersMode,
                                        mode -> Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode." + HideHubPlayersState.HubHideLocationMode.valueOf(mode).toString().toLowerCase()).getString()
                                )
                        )
                        .setDefaultValue(HideHubPlayersState.HubHideLocationMode.HUB_ENTIRE)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode.tooltip"))
                        .build()
        );*/

        generalCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode.hideStartDistance"), config.hideFarPlayersRange, 2, 64)
                .setDefaultValue(12)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideFarPlayersMode.hideStartDistance.tooltip"))
                .setSaveConsumer(v -> config.hideFarPlayersRange = v)
                .build()
        );

        //endregion


        //region hidePlayersNearNpc
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpc"), config.hidePlayersNearNpc)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpc.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersNearNpc = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startIntSlider(
                        Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpcRange"),
                        (int) (config.hidePlayersNearNpcRange * 100), // stored as float, converted to int (b/c of no float support), visually converted to float, returned as float
                        50, // 0.50f * 100
                        500 // 5.00f * 100
                )
                .setDefaultValue(125)
                .setTextGetter(val -> Text.literal(String.format("%.2f", val / 100.0f)))
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hidePlayersNearNpcRange.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersNearNpcRange = newValue / 100.0f)
                .build());
        //endregion

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.doTransferCooldownFinishedAlert"), config.doTransferCooldownFinishedAlert)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.doTransferCooldownFinishedAlert.tooltip"))
                .setSaveConsumer(v -> config.doTransferCooldownFinishedAlert = v)
                .build());


        //region alwaysSprint
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.alwaysSprint"), config.alwaysSprint)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.alwaysSprint.tooltip"))
                .setSaveConsumer(newValue -> config.alwaysSprint = newValue)
                .build());
        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.alwaysSprintOnlyInSkyblock"), config.alwaysSprintOnlyInSkyblock)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.alwaysSprintOnlyInSkyblock.tooltip"))
                .setSaveConsumer(newValue -> config.alwaysSprintOnlyInSkyblock = newValue)
                .build());
        //endregion

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.showFog"), config.showFog)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.showFog.tooltip"))
                .setSaveConsumer(newValue -> config.showFog = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.doFullbright"), config.doFullbright)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.doFullbright.tooltip"))
                .setSaveConsumer(newValue -> config.doFullbright = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hideClouds"), config.hideClouds)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideClouds.tooltip"))
                .setSaveConsumer(newValue -> config.hideClouds = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hideNightVisionEffect"), config.hideNightVisionEffect)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideNightVisionEffect.tooltip"))
                .setSaveConsumer(newValue -> config.hideNightVisionEffect = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.glowingDroppedItems"), config.glowingDroppedItems)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.glowingDroppedItems.tooltip"))
                .setSaveConsumer(newValue -> config.glowingDroppedItems = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.glowingPlayers"), config.glowingPlayers)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.glowingPlayers.tooltip"))
                .setSaveConsumer(newValue -> config.glowingPlayers = newValue)
                .build());

        //region oldVersionParityConfig

        //noinspection rawtypes
        List<AbstractConfigListEntry> oldVersionParityEntries = List.of(
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doSwordBlocking"), config.oldVersionParityConfig.doSwordBlocking)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doSwordBlocking.tooltip"))
                        .setSaveConsumer(v -> config.oldVersionParityConfig.doSwordBlocking = v)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doOldCrouchHeight"), config.oldVersionParityConfig.doOldCrouchHeight)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doOldCrouchHeight.tooltip"))
                        .setSaveConsumer(v -> config.oldVersionParityConfig.doOldCrouchHeight = v)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doOldGlassPaneHitbox"), config.oldVersionParityConfig.doOldGlassPaneHitbox)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.oldVersionParityConfig.doOldGlassPaneHitbox.tooltip"))
                        .setSaveConsumer(v -> config.oldVersionParityConfig.doOldGlassPaneHitbox = v)
                        .build()
        );

        generalCategory.addEntry(entryBuilder.startSubCategory(
                                Text.translatable("config.ae.skydoppler.general.option.subcategory.oldVersionParityConfig"),
                                oldVersionParityEntries
                        )
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.subcategory.oldVersionParityConfig.tooltip"))
                        .build()
        );

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hideThirdPersonFireOverlay"), config.hideThirdPersonFireOverlay)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideThirdPersonFireOverlay.tooltip"))
                .setSaveConsumer(newValue -> config.hideThirdPersonFireOverlay = newValue)
                .build());

        generalCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.hideExplosionParticles"), config.hideExplosionParticle)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.general.option.hideExplosionParticles.tooltip"))
                .setSaveConsumer(newValue -> config.hideExplosionParticle = newValue)
                .build());

        //region vanillaHudConfig
        //noinspection rawtypes
        List<AbstractConfigListEntry> vanillaHudEntries = java.util.List.of(
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hideChatIndicators"), config.vanillaHudConfig.hideChatIndicators)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.hideChatIndicators.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.hideChatIndicators = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHealthBar"), config.vanillaHudConfig.shouldHideHealthBar)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHealthBar.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHealthBar = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHungerArmorBubbles"), config.vanillaHudConfig.shouldHideHungerArmorBubbles)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHungerArmorBubbles.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHungerArmorBubbles = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideStatusEffectOverlay"), config.vanillaHudConfig.shouldHideStatusEffectOverlay)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideStatusEffectOverlay.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideStatusEffectOverlay = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHeldItemTooltip"), config.vanillaHudConfig.shouldHideHeldItemTooltip)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideHeldItemTooltip.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideHeldItemTooltip = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideMountHealth"), config.vanillaHudConfig.shouldHideMountHealth)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideMountHealth.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideMountHealth = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHidePortalOverlay"), config.vanillaHudConfig.shouldHidePortalOverlay)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHidePortalOverlay.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHidePortalOverlay = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideFireOverlayFirstPerson"), config.vanillaHudConfig.shouldHideFireOverlayFirstPerson)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideFireOverlayFirstPerson.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideFireOverlayFirstPerson = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideWitherBossbarsInSkyblock"), config.vanillaHudConfig.shouldHideWitherBossbarsInSkyblock)
                        .setDefaultValue(false)
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.vanillaHudConfig.shouldHideWitherBossbarsInSkyblock.tooltip"))
                        .setSaveConsumer(newValue -> config.vanillaHudConfig.shouldHideWitherBossbarsInSkyblock = newValue)
                        .build()
        );

        generalCategory.addEntry(entryBuilder.startSubCategory(
                                Text.translatable("config.ae.skydoppler.general.option.subcategory.vanillaHudConfig"),
                                vanillaHudEntries
                        )
                        .setTooltip(Text.translatable("config.ae.skydoppler.general.option.subcategory.vanillaHudConfig.tooltip"))
                        .build()
        );

        //endregion
        //endregion

        //region DUNGEON_CATEGORY

        var dungeonCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.dungeon"));


        dungeonCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.dungeon.option.hideMageBeams"), config.hideMageBeams)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.dungeon.option.hideMageBeams.tooltip"))
                .setSaveConsumer(v -> config.hideMageBeams = v)
                .build()
        );

        //endregion

        //region FISHING_CATEGORY

        var fishingCategory = builder.getOrCreateCategory(Text.translatable("config.ae.skydoppler.category.fishing"));

        fishingCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishing"), config.hidePlayersWhileFishing)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishing.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersWhileFishing = newValue)
                .build());

        fishingCategory.addEntry(entryBuilder.startIntSlider(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishingRange"), config.hidePlayersWhileFishingRange, 2, 64)
                .setDefaultValue(12)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.hidePlayersWhileFishingRange.tooltip"))
                .setSaveConsumer(newValue -> config.hidePlayersWhileFishingRange = newValue)
                .build());

        fishingCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.hideOtherFishingRods"), config.hideOtherFishingRods)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.hideOtherFishingRods.tooltip"))
                .setSaveConsumer(newValue -> config.hideOtherFishingRods = newValue)
                .build());
        fishingCategory.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.doLegendarySeacreatureAlerts"), config.doLegendarySeacreatureAlerts)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.doLegendarySeacreatureAlerts.tooltip"))
                .setSaveConsumer(newValue -> config.doLegendarySeacreatureAlerts = newValue)
                .build());

        //noinspection rawtypes
        List<AbstractConfigListEntry> seacreatureMessageConfigEntries = List.of(
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.shouldHideOriginalMessage"), config.seacreatureMessageConfig.shouldHideOriginalMessage)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.shouldHideOriginalMessage.tooltip"))
                        .setSaveConsumer(newValue -> config.seacreatureMessageConfig.shouldHideOriginalMessage = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.showCustomChatMessage"), config.seacreatureMessageConfig.showCustomChatMessage)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.showCustomChatMessage.tooltip"))
                        .setSaveConsumer(newValue -> config.seacreatureMessageConfig.showCustomChatMessage = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.showTitle"), config.seacreatureMessageConfig.showTitle)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.showTitle.tooltip"))
                        .setSaveConsumer(newValue -> config.seacreatureMessageConfig.showTitle = newValue)
                        .build(),
                entryBuilder.startBooleanToggle(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.shouldPlaySound"), config.seacreatureMessageConfig.shouldPlaySound)
                        .setDefaultValue(true)
                        .setTooltip(Text.translatable("config.ae.skydoppler.fishing.option.seacreatureMessageConfig.shouldPlaySound.tooltip"))
                        .setSaveConsumer(newValue -> config.seacreatureMessageConfig.shouldPlaySound = newValue)
                        .build()
        );

        fishingCategory.addEntry(entryBuilder.startSubCategory(
                                Text.translatable("config.ae.skydoppler.general.option.subcategory.seacreatureMessageConfig"),
                                seacreatureMessageConfigEntries
                        )
                        .build()
        );

        //endregion


        builder.setSavingRunnable(() -> config.save(SkydopplerClient.CONFIG_PATH));


        return builder.build();
    }


}
