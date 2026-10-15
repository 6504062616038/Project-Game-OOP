import java.awt.Color;
import java.awt.Graphics2D;

public class EnemyBullet extends GameObject {
    private final double velocityX;
    private final double velocityY;
    private double positionX;
    private double positionY;

    public EnemyBullet(int startX, int startY, int targetX, int targetY) {
        super(startX - 4, startY - 6, 8, 12, 5);
        positionX = x;
        positionY = y;

        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double distance = Math.max(1.0, Math.sqrt(deltaX * deltaX + deltaY * deltaY));
        velocityX = deltaX / distance * speed;
        velocityY = deltaY / distance * speed;
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;
        x = (int) Math.round(positionX);
        y = (int) Math.round(positionY);
        if (y > 640 || x < -width || x > 480) active = false;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 90, 65));
        g.fillOval(x, y, width, height);
        g.setColor(new Color(255, 220, 140));
        g.fillOval(x + 2, y + 2, width - 4, height - 4);
    }
}