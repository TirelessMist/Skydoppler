package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;

public class ModConfigEntryBuilder {
    public ModBooleanToggleEntryBuilder startBooleanToggle(Text name, boolean value) {
        return new ModBooleanToggleEntryBuilder(name, value);
    }
    public ModIntSliderEntryBuilder startIntSlider(Text name, int value, int min, int max) {
        return new ModIntSliderEntryBuilder(name, value, min, max);
    }
    public <T extends Enum<T>> ModEnumSelectorEntryBuilder<T> startEnumSelector(Text name, Class<T> enumClass, T value) {
        return new ModEnumSelectorEntryBuilder<>(name, enumClass, value);
    }
    public ModSubCategoryEntryBuilder startSubCategory(Text name, java.util.List<ModConfigListEntry> entries) {
        return new ModSubCategoryEntryBuilder(name, entries);
    }
    public ModTextFieldEntryBuilder startTextField(Text name, String value) {
        return new ModTextFieldEntryBuilder(name, value);
    }
    public <T> ModDropdownMenuEntryBuilder<T> startDropdownMenu(Text name, T value) {
        return new ModDropdownMenuEntryBuilder<>(name, value);
    }
    public ModColorPickerEntryBuilder startColorField(Text name, int color) {
        return new ModColorPickerEntryBuilder(name, color);
    }
}

