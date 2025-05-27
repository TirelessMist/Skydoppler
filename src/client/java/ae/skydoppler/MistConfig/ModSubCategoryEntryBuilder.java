package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ModSubCategoryEntryBuilder {
    private Text name;
    private final List<ModConfigListEntry> entries = new ArrayList<>();

    public ModSubCategoryEntryBuilder(Text name, List<ModConfigListEntry> entries) {
        this.name = name;
        if (entries != null) {
            this.entries.addAll(entries);
        }
    }

    public ModSubCategoryEntryBuilder setName(Text name) {
        this.name = name;
        return this;
    }

    public ModSubCategoryEntryBuilder addEntry(ModConfigListEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public ModSubCategoryEntryBuilder addEntries(List<ModConfigListEntry> entries) {
        this.entries.addAll(entries);
        return this;
    }

    public ModSubCategoryEntry build() {
        return new ModSubCategoryEntry(name, new ArrayList<>(entries));
    }
}

