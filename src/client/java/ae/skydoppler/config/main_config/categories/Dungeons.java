package ae.skydoppler.config.main_config.categories;

import ae.skydoppler.config.main_config.MainConfigCategory;
import net.minecraft.text.Text;

public class Dungeons extends MainConfigCategory {

    public boolean hideMageBeams = true;

    public Dungeons() {
        super("dungeons", Text.translatable("config.ae.skydoppler.main_config.category.dungeons"), 2);
    }
}
