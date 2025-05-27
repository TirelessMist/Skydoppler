package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModBooleanToggleEntry extends ModConfigListEntry {
    public final Text name;
    public final boolean value;
    public final boolean defaultValue;
    public final Text tooltip;
    public final Consumer<Boolean> saveConsumer;

    public ModBooleanToggleEntry(Text name, boolean value, boolean defaultValue, Text tooltip, Consumer<Boolean> saveConsumer) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
    }
}

