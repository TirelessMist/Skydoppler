package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModEnumSelectorEntry<T extends Enum<T>> extends ModConfigListEntry {
    public final Text name;
    public final Class<T> enumClass;
    public final T value;
    public final T defaultValue;
    public final Text tooltip;
    public final Consumer<T> saveConsumer;
    public final Function<T, Text> enumNameProvider;
    public final Function<T, Optional<Text[]>> tooltipSupplier;

    public ModEnumSelectorEntry(Text name, Class<T> enumClass, T value, T defaultValue, Text tooltip, Consumer<T> saveConsumer, Function<T, Text> enumNameProvider, Function<T, Optional<Text[]>> tooltipSupplier) {
        this.name = name;
        this.enumClass = enumClass;
        this.value = value;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
        this.enumNameProvider = enumNameProvider;
        this.tooltipSupplier = tooltipSupplier;
    }
}

