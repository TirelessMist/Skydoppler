package ae.skydoppler.MistConfig;

import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ModConfigCategory {
    private final Text name;
    private final List<ModConfigListEntry> entries = new ArrayList<>();

    public ModConfigCategory(Text name) {
        this.name = name;
    }

    public Text getName() {
        return name;
    }

    public void addEntry(ModConfigListEntry entry) {
        entries.add(entry);
    }

    public List<ModConfigListEntry> getEntries() {
        return entries;
    }
}

