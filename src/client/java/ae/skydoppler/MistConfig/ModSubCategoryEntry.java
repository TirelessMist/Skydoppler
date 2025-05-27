package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.List;

public class ModSubCategoryEntry extends ModConfigListEntry {
    private final Text name;
    private final List<ModConfigListEntry> entries;

    public ModSubCategoryEntry(Text name, List<ModConfigListEntry> entries) {
        this.name = name;
        this.entries = entries;
    }

    public Text getName() {
        return name;
    }

    public List<ModConfigListEntry> getEntries() {
        return entries;
    }
}

