package ae.skydoppler.config.held_item_config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeldItemConfigScreen extends Screen {
    private final SkydopplerConfig config;
    private final List<SliderWidget> sliders = new ArrayList<>();
    private final List<CheckboxWidget> checkboxes = new ArrayList<>();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final boolean isLeftHanded;
    private final Screen parent;
    private int panelWidth;

    public HeldItemConfigScreen(SkydopplerConfig config, Screen parent) {
        super(Text.translatable("config.ae.skydoppler.helditem.title"));
        this.config = config;
        this.parent = parent;
        this.isLeftHanded = client.options.getMainArm().getValue() == Arm.LEFT;
    }

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        return new HeldItemConfigScreen(config, parent);
    }

    @Override
    protected void init() {
        this.panelWidth = (int) (this.width * 0.25f);
        int x = this.isLeftHanded ? this.width - panelWidth : 0;
        int y = 50;
        int sliderWidth = panelWidth - 20;
        int sliderHeight = 20;
        int spacing = 28;

        // Float sliders
        sliders.clear();
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.pos_x", config.heldItemRendererConfig.posX, -2.0f, 2.0f, 0.0f, v -> config.heldItemRendererConfig.posX = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.pos_y", config.heldItemRendererConfig.posY, -2.0f, 2.0f, 0.0f, v -> config.heldItemRendererConfig.posY = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.pos_z", config.heldItemRendererConfig.posZ, -2.0f, 2.0f, 0.0f, v -> config.heldItemRendererConfig.posZ = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.rot_x", config.heldItemRendererConfig.rotX, -180.0f, 180.0f, 0.0f, v -> config.heldItemRendererConfig.rotX = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.rot_y", config.heldItemRendererConfig.rotY, -180.0f, 180.0f, 0.0f, v -> config.heldItemRendererConfig.rotY = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.rot_z", config.heldItemRendererConfig.rotZ, -180.0f, 180.0f, 0.0f, v -> config.heldItemRendererConfig.rotZ = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.scale", config.heldItemRendererConfig.scale, 0.1f, 3.0f, 1.0f, v -> config.heldItemRendererConfig.scale = v);
        y += spacing;
        addSlider(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.swing_speed_multiplier", config.heldItemRendererConfig.swingSpeedMultiplier, 0.1f, 3.0f, 1.0f, v -> config.heldItemRendererConfig.swingSpeedMultiplier = v);
        y += spacing;

        // Boolean toggles
        addToggle(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.disable_swap_animation", config.heldItemRendererConfig.disableSwapAnimation, v -> config.heldItemRendererConfig.disableSwapAnimation = v);
        y += spacing;
        addToggle(x, y, sliderWidth, sliderHeight, "config.ae.skydoppler.helditem.disable_modern_swing", config.heldItemRendererConfig.disableModernSwing, v -> config.heldItemRendererConfig.disableModernSwing = v);
        y += spacing;

        // Main arm toggle
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("config.ae.skydoppler.helditem.main_arm", client.options.getMainArm().getValue() == Arm.LEFT ? Text.translatable("left").getString() : Text.translatable("right").getString()),
                        btn -> {
                            boolean setLeft = client.options.getMainArm().getValue() == Arm.RIGHT;
                            client.player.setMainArm(setLeft ? Arm.LEFT : Arm.RIGHT);
                            client.options.getMainArm().setValue(setLeft ? Arm.LEFT : Arm.RIGHT);
                            btn.setMessage(Text.translatable("config.ae.skydoppler.helditem.main_arm", client.player.getMainArm() == Arm.LEFT ? Text.translatable("left").getString() : Text.translatable("right").getString()));
                            client.setScreen(HeldItemConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, this.parent));
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build());
        y += spacing;

        // Test punch button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("config.ae.skydoppler.helditem.test_punch"),
                        btn -> {
                            if (client.player != null) {
                                client.player.swingHand(Hand.MAIN_HAND);
                            }
                        })
                .position(x + 10, y)
                .size(sliderWidth, sliderHeight)
                .build());
        y += spacing;

        // Exit button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("config.ae.skydoppler.helditem.exit"),
                        btn -> this.close())
                .position(x + 10, this.height - 30)
                .size(sliderWidth, 20)
                .tooltip(Tooltip.of(Text.translatable("config.ae.skydoppler.helditem.exit.tooltip")))
                .build());

        // Add sliders
        for (SliderWidget slider : sliders) {
            this.addDrawableChild(slider);
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {

        int panelX = isLeftHanded ? this.width - panelWidth : 0;
        int panelY = 0;
        int panelRight = isLeftHanded ? this.width : panelWidth;
        int panelBottom = this.height;
        int panelColor = new Color(255, 0, 0, 128).getRGB();

        // Draw the panel background
        drawContext.fill(panelX, panelY, panelRight, panelBottom, panelColor);

        // Draw the content area
        super.render(drawContext, mouseX, mouseY, delta);

        // Draw the title text
        drawContext.drawText(this.textRenderer, this.title, panelX + 12, 20, 0xFFFFFF, true);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void addSlider(int x, int y, int width, int height, String key, float value, float min, float max, float defaultValue, java.util.function.Consumer<Float> onChange) {
        FloatSlider slider = new FloatSlider(x + 10, y, width - 25, height, Text.translatable(key).getString(), value, min, max, defaultValue, onChange);
        sliders.add(slider);

        // Add reset button beside the slider
        ButtonWidget resetButton = ButtonWidget.builder(
                        Text.literal("↺"), // Reset icon
                        btn -> {
                            slider.setValue((defaultValue - min) / (max - min));
                            onChange.accept(defaultValue);
                            config.save(SkydopplerClient.CONFIG_PATH);
                        })
                .position(x + width - 10, y)
                .size(20, height)
                .tooltip(Tooltip.of(Text.literal("Reset to default")))
                .build();
        this.addDrawableChild(resetButton);
    }

    private void addToggle(int x, int y, int width, int height, String key, boolean initialValue, java.util.function.Consumer<Boolean> onChange) {
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable(key, initialValue ? Text.translatable("on").getString() : Text.translatable("off").getString()),
                        btn -> {
                            // Dynamically fetch the current value from the config
                            boolean currentValue = key.equals("config.ae.skydoppler.helditem.disable_swap_animation")
                                    ? config.heldItemRendererConfig.disableSwapAnimation
                                    : config.heldItemRendererConfig.disableModernSwing;

                            boolean newValue = !currentValue;
                            onChange.accept(newValue);

                            // Update the button text
                            btn.setMessage(Text.translatable(key, newValue ? Text.translatable("on").getString() : Text.translatable("off").getString()));

                            // Save the updated config
                            config.save(SkydopplerClient.CONFIG_PATH);
                        })
                .position(x + 10, y)
                .size(width, height)
                .build());
    }

    private static class FloatSlider extends SliderWidget {
        private final String label;
        private final float min;
        private final float max;
        private final float defaultValue;
        private final java.util.function.Consumer<Float> onChange;

        public FloatSlider(int x, int y, int width, int height, String label, float value, float min, float max, float defaultValue, java.util.function.Consumer<Float> onChange) {
            super(x, y, width, height, Text.literal(label), (value - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.defaultValue = defaultValue;
            this.onChange = onChange;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.literal(label + ": " + String.format("%.2f", getValue())));
        }

        @Override
        protected void applyValue() {
            onChange.accept(getValue());
        }

        private float getValue() {
            return (float) (min + (max - min) * this.value);
        }

        public void setValue(double value) {
            this.value = value;
            this.updateMessage();
        }
    }
}
