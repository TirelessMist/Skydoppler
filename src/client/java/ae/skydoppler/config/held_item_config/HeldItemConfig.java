package ae.skydoppler.config.held_item_config;

public class HeldItemConfig {
    private float sliderValue = 0.5f;
    private boolean booleanValue = false;

    public float getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(float value) {
        this.sliderValue = value;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean value) {
        this.booleanValue = value;
    }
}

