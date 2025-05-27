package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ModTextFieldEntryBuilder {
    private final Text name;
    private final String value;
    private String defaultValue = "";
    private Text tooltip;
    private Consumer<String> saveConsumer;

    public ModTextFieldEntryBuilder(Text name, String value) {
        this.name = name;
        this.value = value;
    }

    public ModTextFieldEntryBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ModTextFieldEntryBuilder setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ModTextFieldEntryBuilder setSaveConsumer(Consumer<String> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public ModTextFieldEntry build() {
        return new ModTextFieldEntry(name, value, defaultValue, tooltip, saveConsumer);
    }
}

