package ae.skydoppler.config.main_config.categories;

import ae.skydoppler.config.main_config.MainConfigCategory;
import net.minecraft.text.Text;

public class Accessibility extends MainConfigCategory {

    public int textScrollingSpeed = 7; // characters per second

    public Accessibility() {
        super("accessibility", Text.translatable("config.ae.skydoppler.main_config.category.accessibility"), 5);
    }
}
