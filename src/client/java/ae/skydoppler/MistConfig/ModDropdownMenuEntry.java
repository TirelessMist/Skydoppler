package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.List;
import java.util.function.Consumer;

public class ModDropdownMenuEntry<T> extends ModConfigListEntry {
    public final Text name;
    public final T value;
    public final List<T> options;
    public final T defaultValue;
    public final Text tooltip;
    public final Consumer<T> saveConsumer;

    public ModDropdownMenuEntry(Text name, T value, List<T> options, T defaultValue, Text tooltip, Consumer<T> saveConsumer) {
        this.name = name;
        this.value = value;
        this.options = options;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
    }
}

