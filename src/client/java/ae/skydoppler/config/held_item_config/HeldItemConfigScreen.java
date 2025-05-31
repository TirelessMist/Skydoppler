package ae.skydoppler.config.held_item_config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class HeldItemConfigScreen extends Screen {
    private final SkydopplerConfig config;
    private final List<SliderWidget> sliders = new ArrayList<>();
    private final List<CheckboxWidget> checkboxes = new ArrayList<>();
    private final int panelWidth;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final boolean isLeftHanded;
    private final Screen parent;

    public HeldItemConfigScreen(Screen parent) {
        super(Text.literal("Hand Renderer Config"));
        this.config = SkydopplerClient.CONFIG;
        this.parent = parent;
        this.panelWidth = (int) (this.width * 0.25f);
        this.isLeftHanded = client.player.getMainArm() == Arm.LEFT;
    }

    @Override
    protected void init() {
        int w = (int) (this.width * 0.25f);
        int x = this.isLeftHanded ? this.width - w : 0;
        int y = 20;
        int sliderWidth = w - 20;
        int sliderHeight = 20;
        int spacing = 28;

        // Float sliders
        sliders.clear();
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Pos X", config.heldItemRendererConfig.posX, -2.0f, 2.0f, v -> { config.heldItemRendererConfig.posX = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Pos Y", config.heldItemRendererConfig.posY, -2.0f, 2.0f, v -> { config.heldItemRendererConfig.posY = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Pos Z", config.heldItemRendererConfig.posZ, -2.0f, 2.0f, v -> { config.heldItemRendererConfig.posZ = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Rot X", config.heldItemRendererConfig.rotX, -180.0f, 180.0f, v -> { config.heldItemRendererConfig.rotX = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Rot Y", config.heldItemRendererConfig.rotY, -180.0f, 180.0f, v -> { config.heldItemRendererConfig.rotY = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Rot Z", config.heldItemRendererConfig.rotZ, -180.0f, 180.0f, v -> { config.heldItemRendererConfig.rotZ = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Scale", config.heldItemRendererConfig.scale, 0.1f, 3.0f, v -> { config.heldItemRendererConfig.scale = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;
        sliders.add(new FloatSlider(x + 10, y, sliderWidth, sliderHeight, "Swing Speed Multiplier", config.heldItemRendererConfig.swingSpeedMultiplier, 0.1f, 3.0f, v -> { config.heldItemRendererConfig.swingSpeedMultiplier = v; config.save(SkydopplerClient.CONFIG_PATH); }));
        y += spacing;

        // Boolean toggles
        checkboxes.clear();
        // Use ButtonWidget as a toggle for disableSwapAnimation
        ButtonWidget swapAnimToggle = ButtonWidget.builder(
                        Text.literal("Disable Swap Animation: " + (config.heldItemRendererConfig.disableSwapAnimation ? "ON" : "OFF")),
                        btn -> {
                            config.heldItemRendererConfig.disableSwapAnimation = !config.heldItemRendererConfig.disableSwapAnimation;
                            btn.setMessage(Text.literal("Disable Swap Animation: " + (config.heldItemRendererConfig.disableSwapAnimation ? "ON" : "OFF")));
                            config.save(SkydopplerClient.CONFIG_PATH);
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build();
        this.addDrawableChild(swapAnimToggle);
        y += spacing;
        // Use ButtonWidget as a toggle for disableModernSwing
        ButtonWidget modernSwingToggle = ButtonWidget.builder(
                        Text.literal("Disable Modern Swing: " + (config.heldItemRendererConfig.disableModernSwing ? "ON" : "OFF")),
                        btn -> {
                            config.heldItemRendererConfig.disableModernSwing = !config.heldItemRendererConfig.disableModernSwing;
                            btn.setMessage(Text.literal("Disable Modern Swing: " + (config.heldItemRendererConfig.disableModernSwing ? "ON" : "OFF")));
                            config.save(SkydopplerClient.CONFIG_PATH);
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build();
        this.addDrawableChild(modernSwingToggle);
        y += spacing;
        // Use ButtonWidget as a toggle for the player's main arm
        ButtonWidget mainArm = ButtonWidget.builder(
                        Text.literal("Main Arm: " + (client.options.getMainArm().getValue() == Arm.LEFT ? "Left" : "Right")),
                        btn -> {
                            // Update both the player's main arm and the game options setting
                            boolean setLeft = client.options.getMainArm().getValue() == Arm.RIGHT;
                            client.player.setMainArm(setLeft ? Arm.LEFT : Arm.RIGHT);
                            client.options.getMainArm().setValue(setLeft ? Arm.LEFT : Arm.RIGHT);
                            btn.setMessage(Text.literal("Main Arm: " + (client.player.getMainArm() == Arm.LEFT ? "Left" : "Right")));
                            client.setScreen(new ae.skydoppler.config.held_item_config.HeldItemConfigScreen(parent)); // Reinitialize to update the screen
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build();
        this.addDrawableChild(mainArm);
        y += spacing;
        // Use ButtonWidget as a button to trigger a hand swing/punch action
        ButtonWidget testPunch = ButtonWidget.builder(
                        Text.literal("Test Punch"),
                        btn -> {
                            if (client.player != null) {
                                client.player.swingHand(Hand.MAIN_HAND);
                            }
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build();
        this.addDrawableChild(testPunch);
        y += spacing;

        // Add widgets
        for (SliderWidget slider : sliders) {
            this.addDrawableChild(slider);
        }
        for (CheckboxWidget checkbox : checkboxes) {
            this.addDrawableChild(checkbox);
        }

        // Exit button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Exit"), btn -> this.close()).position(x + 10, this.height - 30).size(sliderWidth, 20).build());

    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        drawContext.fill(0, 0, panelWidth, this.height, 0xAA000000);
        super.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawText(this.textRenderer, this.title, panelWidth / 2, 8, 0xFFFFFF, true);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class FloatSlider extends SliderWidget {
        private final String label;
        private final float min;
        private final float max;
        private final java.util.function.Consumer<Float> onChange;

        public FloatSlider(int x, int y, int width, int height, String label, float value, float min, float max, java.util.function.Consumer<Float> onChange) {
            super(x, y, width, height, Text.literal(label + ": " + value), (value - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.onChange = onChange;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.literal(label + ": " + String.format("%.2f", getValue())));
        }

        @Override
        protected void applyValue() {
            float v = getValue();
            onChange.accept(v);
        }

        private float getValue() {
            return (float) (min + (max - min) * this.value);
        }
    }
}
