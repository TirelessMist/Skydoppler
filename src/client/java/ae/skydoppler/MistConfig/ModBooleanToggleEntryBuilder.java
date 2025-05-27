package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModBooleanToggleEntryBuilder {
    private final Text name;
    private final boolean value;
    private boolean defaultValue = false;
    private Text tooltip;
    private Consumer<Boolean> saveConsumer;

    public ModBooleanToggleEntryBuilder(Text name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public ModBooleanToggleEntryBuilder setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModBooleanToggleEntryBuilder setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModBooleanToggleEntryBuilder setSaveConsumer(Consumer<Boolean> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModBooleanToggleEntry build() {
        return new ModBooleanToggleEntry(name, value, defaultValue, tooltip, saveConsumer);
    }
}

