import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends GameObject {
    private final int horizontalSpeed;

    public Bullet(int x, int y) {
        this(x, y, 0);
    }

    public Bullet(int x, int y, int horizontalSpeed) {
        super(x, y, 6, 16, 12);
        this.horizontalSpeed = horizontalSpeed;
    }

    @Override
    public void update() {
        x += horizontalSpeed;
        y -= speed;
        if (y < -20 || x < -width || x > 480) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Laser Core (แกนแสง)
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(x + 2, y, 2, height);
    }
}