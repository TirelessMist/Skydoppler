package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModColorPickerEntry extends ModConfigListEntry {
    public final Text name;
    public final int color;
    public final int defaultValue;
    public final Text tooltip;
    public final Consumer<Integer> saveConsumer;

    public ModColorPickerEntry(Text name, int color, int defaultValue, Text tooltip, Consumer<Integer> saveConsumer) {
        this.name = name;
        this.color = color;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
    }
}

