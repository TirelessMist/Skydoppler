package ae.skydoppler.config.held_item_config;

public abstract class HeldItemConfigOption<T> {
    private final String label;
    private final T defaultValue;

    public HeldItemConfigOption(String label, T defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract T getValue(HeldItemConfig config);
    public abstract void setValue(HeldItemConfig config, T value);
}

