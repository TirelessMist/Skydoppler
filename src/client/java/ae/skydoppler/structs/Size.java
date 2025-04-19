package ae.skydoppler.structs;

public class Size {

    private int w;
    private int h;

    public Size() {
        w = 1;
        h = 1;
    }

    public Size(int width, int height) {
        w = width;
        h = height;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }
}
