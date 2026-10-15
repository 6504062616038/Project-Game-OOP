import java.awt.Color;
import java.awt.Graphics2D;

public class PowerUp extends GameObject {
    public PowerUp(int x, int y, int speed) {
        super(x, y, 28, 34, speed);
    }

    @Override
    public void update() {
        y += speed;
        if (y > 640) active = false;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(230, 200, 80));
        g.fillRect(x + 9, y + 2, 10, 7);
        g.setColor(new Color(100, 235, 220));
        g.fillRoundRect(x + 4, y + 8, 20, 24, 9, 9);
        g.setColor(new Color(220, 255, 245));
        g.fillOval(x + 8, y + 13, 7, 8);
        g.setColor(new Color(245, 245, 255));
        g.drawRoundRect(x + 4, y + 8, 20, 24, 9, 9);
    }
}