import java.awt.*;

public class boat {
    public Rectangle Hitbox;
    public int pos;

    public boat(Rectangle hitbox) {
        Hitbox = hitbox;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
