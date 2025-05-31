package ae.skydoppler.config.held_item_config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class HeldItemConfigScreen extends Screen {
    private final HeldItemConfig config;
    private final List<HeldItemConfigOption<?>> options = new ArrayList<>();

    public HeldItemConfigScreen(HeldItemConfig config) {
        super(Text.literal("Held Item Config"));
        this.config = config;
        options.add(new HeldItemConfigOption<>("Slider Value", 0.5f) {
            @Override
            public Float getValue(HeldItemConfig config) {
                return config.getSliderValue();
            }
            @Override
            public void setValue(HeldItemConfig config, Float value) {
                config.setSliderValue(value);
            }
        });
        options.add(new HeldItemConfigOption<>("Boolean Value", false) {
            @Override
            public Boolean getValue(HeldItemConfig config) {
                return config.isBooleanValue();
            }
            @Override
            public void setValue(HeldItemConfig config, Boolean value) {
                config.setBooleanValue(value);
            }
        });
    }

    @Override
    protected void init() {
        int screenHeight = this.height / 2;
        int y = (this.height - screenHeight) / 2 + 20;
        int x = this.width / 4;
        int width = this.width / 2;
        int optionHeight = 24;
        int i = 0;
        for (HeldItemConfigOption<?> option : options) {
            if (option.getDefaultValue() instanceof Float) {
                this.addDrawableChild(new SliderWidget(x, y + i * optionHeight, width, 20, Text.literal(option.getLabel()), ((Float) option.getValue(config))) {
                    @Override
                    protected void updateMessage() {
                        setMessage(Text.literal(option.getLabel() + ": " + String.format("%.2f", value)));
                    }
                    @Override
                    @SuppressWarnings("unchecked")
                    protected void applyValue() {
                        ((HeldItemConfigOption<Object>) option).setValue(config, value);
                    }
                });
            } else if (option.getDefaultValue() instanceof Boolean) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal(option.getLabel() + ": " + ((Boolean) option.getValue(config) ? "ON" : "OFF")), btn -> {
                    boolean newValue = !(Boolean) option.getValue(config);
                    @SuppressWarnings("unchecked")
                    HeldItemConfigOption<Object> opt = (HeldItemConfigOption<Object>) option;
                    opt.setValue(config, newValue);
                    btn.setMessage(Text.literal(option.getLabel() + ": " + (newValue ? "ON" : "OFF")));
                }).dimensions(x, y + i * optionHeight, width, 20).build());
            }
            i++;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        int screenHeight = this.height / 2;
        int y = (this.height - screenHeight) / 2;
        drawContext.fill(0, y, this.width, y + screenHeight, 0xAA000000);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}

