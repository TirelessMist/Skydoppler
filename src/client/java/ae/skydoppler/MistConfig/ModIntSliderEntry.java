package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModIntSliderEntry extends ModConfigListEntry {
    public final Text name;
    public final int value;
    public final int min;
    public final int max;
    public final int defaultValue;
    public final Text tooltip;
    public final Consumer<Integer> saveConsumer;
    public final Function<Integer, Text> textGetter;

    public ModIntSliderEntry(Text name, int value, int min, int max, int defaultValue, Text tooltip, Consumer<Integer> saveConsumer, Function<Integer, Text> textGetter) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.tooltip = tooltip;
        this.saveConsumer = saveConsumer;
        this.textGetter = textGetter;
    }
}

