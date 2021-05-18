import java.awt.*;

public class gridSpace {
    public Rectangle Hitbox;
    public boolean hasBoat = false;
    public boolean hasBeenHit = false;
    public boolean score = true;
    public int boatNum = 0; //Keeps track of length of boat

    public gridSpace(Rectangle Hitbox) {
        this.Hitbox = Hitbox;
    }

    public void setHasBoat(boolean hasBoat) {
        this.hasBoat = hasBoat;
    }

    public void hasBoat(int i) {
        this.hasBoat = true;
        this.boatNum = i;
    }

    public void hit() {
        this.hasBeenHit = true;
    }

    public Rectangle getHitbox() {
        return Hitbox;
    }
    public void score() {
        this.score = false;
    }
    public void reset() {
        this.score = true;
        this.hasBeenHit = false;
        this.hasBoat = false;
        this.boatNum = 0;
    }

}
