package ae.skydoppler;

import ae.skydoppler.structs.Size;
import net.minecraft.text.Text;

import java.awt.*;

public class HudModule {

    private Point position;
    private Size size;
    private boolean autoFit;

    private Color foregroundColor;
    private Color backgroundColor;

    private Text[] lines;

    public HudModule() {
        position = new Point();
        size = new Size();
        autoFit = true;

        foregroundColor = new Color(1, 1, 1);
        backgroundColor = new Color(1, 1, 1);

        lines = new Text[0];
    }

    public HudModule(Point position, Size size, boolean autoFit, Color foregroundColor, Color backgroundColor, Text[] lines) {
        this.position = position;
        this.size = size;
        this.autoFit = autoFit;

        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;

        this.lines = lines;
    }
}
