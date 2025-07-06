package ae.skydoppler.config.main_config;

import net.minecraft.text.Text;

public abstract class MainConfigCategory {
    private final String fieldName;
    private final Text label;
    private final int priority;

    protected MainConfigCategory(String fieldName, Text label, int priority) {
        this.fieldName = fieldName;
        this.label = label;
        this.priority = priority;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Text getLabel() {
        return label;
    }

    public int getPriority() {
        return priority;
    }

    public Object getCategoryInstance() {
        return this;
    }
}
