package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModColorPickerEntryBuilder {
    private final Text name;
    private final int color;
    private int defaultValue;
    private Text tooltip;
    private Consumer<Integer> saveConsumer;

    public ModColorPickerEntryBuilder(Text name, int color) {
        this.name = name;
        this.color = color;
    }

    public ModColorPickerEntryBuilder setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModColorPickerEntryBuilder setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModColorPickerEntryBuilder setSaveConsumer(Consumer<Integer> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModColorPickerEntry build() {
        return new ModColorPickerEntry(name, color, defaultValue, tooltip, saveConsumer);
    }
}

