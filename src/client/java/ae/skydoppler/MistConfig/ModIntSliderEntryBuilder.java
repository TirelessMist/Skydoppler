package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModIntSliderEntryBuilder {
    private final Text name;
    private final int value;
    private int min;
    private int max;
    private int defaultValue;
    private Text tooltip;
    private Consumer<Integer> saveConsumer;
    private Function<Integer, Text> textGetter;

    public ModIntSliderEntryBuilder(Text name, int value, int min, int max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ModIntSliderEntryBuilder setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModIntSliderEntryBuilder setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModIntSliderEntryBuilder setSaveConsumer(Consumer<Integer> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModIntSliderEntryBuilder setTextGetter(Function<Integer, Text> textGetter) {
        this.textGetter = textGetter;
        return this;
    }

    public ModIntSliderEntry build() {
        return new ModIntSliderEntry(name, value, min, max, defaultValue, tooltip, saveConsumer, textGetter);
    }
}

