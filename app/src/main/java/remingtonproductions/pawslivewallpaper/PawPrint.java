package remingtonproductions.pawslivewallpaper;

/**
 * Created by miroslav on 20/01/16.
 */
public class PawPrint {

    public int opacity = 255;
    public float x = 0;
    public float y = 0;
    public float angle = 0f;
    public int size = 0;

    PawPrint(float x, float y, int opacity, float angle, int size) {
        this.x = x;
        this.y = y;
        this.opacity = opacity;
        this.angle = angle;
        this.size = size;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
