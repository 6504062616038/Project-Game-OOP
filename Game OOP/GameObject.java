import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class GameObject {
    protected int x, y;
    protected int width, height;
    protected int speed;
    protected boolean active = true;

    public GameObject(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}