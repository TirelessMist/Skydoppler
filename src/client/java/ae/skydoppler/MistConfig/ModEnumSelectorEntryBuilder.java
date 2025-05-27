package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModEnumSelectorEntryBuilder<T extends Enum<T>> {
    private final Text name;
    private final Class<T> enumClass;
    private final T value;
    private T defaultValue;
    private Text tooltip;
    private Consumer<T> saveConsumer;
    private Function<T, Text> enumNameProvider;
    private Function<T, Optional<Text[]>> tooltipSupplier;

    public ModEnumSelectorEntryBuilder(Text name, Class<T> enumClass, T value) {
        this.name = name;
        this.enumClass = enumClass;
        this.value = value;
    }

    public ModEnumSelectorEntryBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModEnumSelectorEntryBuilder<T> setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModEnumSelectorEntryBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModEnumSelectorEntryBuilder<T> setEnumNameProvider(Function<T, Text> provider) {
        this.enumNameProvider = provider;
        return this;
    }

    public ModEnumSelectorEntryBuilder<T> setTooltipSupplier(Function<T, Optional<Text[]>> supplier) {
        this.tooltipSupplier = supplier;
        return this;
    }

    public ModEnumSelectorEntry<T> build() {
        return new ModEnumSelectorEntry<>(name, enumClass, value, defaultValue, tooltip, saveConsumer, enumNameProvider, tooltipSupplier);
    }
}

