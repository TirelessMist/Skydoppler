package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;
import java.util.List;

public class ModDropdownMenuEntryBuilder<T> {
    private final Text name;
    private final T value;
    private List<T> options;
    private T defaultValue;
    private Text tooltip;
    private Consumer<T> saveConsumer;

    public ModDropdownMenuEntryBuilder(Text name, T value) {
        this.name = name;
        this.value = value;
    }

    public ModDropdownMenuEntryBuilder<T> setOptions(List<T> options) {
        this.options = options;
        return this;
    }

    public ModDropdownMenuEntryBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModDropdownMenuEntryBuilder<T> setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModDropdownMenuEntryBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModDropdownMenuEntry<T> build() {
        return new ModDropdownMenuEntry<>(name, value, options, defaultValue, tooltip, saveConsumer);
    }
}

