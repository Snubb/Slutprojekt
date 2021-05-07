import java.awt.*;

public class gridSpace {
    public Rectangle Hitbox;
    public boolean hasBoat = false;
    public boolean hasBeenHit = false;

    public gridSpace(Rectangle Hitbox) {
        this.Hitbox = Hitbox;
    }

    public void setHasBoat(boolean hasBoat) {
        this.hasBoat = hasBoat;
    }

    public void hasBoat() {
        this.hasBoat = true;
    }

    public void hit() {
        this.hasBeenHit = true;
    }

    public Rectangle getHitbox() {
        return Hitbox;
    }


}
