package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModTextFieldEntry extends ModConfigListEntry {
    public final Text name;
    public final String value;
    public final String defaultValue;
    public final Text tooltip;
    public final Consumer<String> saveConsumer;

    public ModTextFieldEntry(Text name, String value, String defaultValue, Text tooltip, Consumer<String> saveConsumer) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
    }
}

