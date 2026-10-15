import java.awt.Color;
import java.awt.Graphics2D;

public class Obstacle extends GameObject {
    public enum Type { ASTEROID, MINE, DEBRIS }

    private final Type type;

    public Obstacle(int x, int y, int speed, Type type) {
        super(x, y, 34, 34, speed);
        this.type = type;
    }

    @Override
    public void update() {
        y += speed;
        if (y > 640) active = false;
    }

    @Override
    public void draw(Graphics2D g) {
        if (type == Type.ASTEROID) {
            g.setColor(new Color(130, 105, 78));
            int[] xPoints = {x + 8, x + 19, x + 32, x + 29, x + 20, x + 5, x + 2};
            int[] yPoints = {y + 4, y, y + 10, y + 25, y + 33, y + 28, y + 15};
            g.fillPolygon(xPoints, yPoints, xPoints.length);
            g.setColor(new Color(185, 155, 115));
            g.fillOval(x + 9, y + 10, 7, 6);
            g.fillOval(x + 21, y + 20, 5, 4);
        } else if (type == Type.MINE) {
            g.setColor(new Color(230, 76, 64));
            for (int angle = 0; angle < 360; angle += 45) {
                double radians = Math.toRadians(angle);
                int centerX = x + width / 2;
                int centerY = y + height / 2;
                g.drawLine(centerX, centerY,
                        centerX + (int) (Math.cos(radians) * 16),
                        centerY + (int) (Math.sin(radians) * 16));
            }
            g.fillOval(x + 7, y + 7, 20, 20);
            g.setColor(new Color(255, 190, 100));
            g.fillOval(x + 13, y + 13, 8, 8);
        } else {
            g.setColor(new Color(100, 155, 165));
            g.fillRect(x + 4, y + 8, 26, 18);
            g.fillRect(x + 10, y + 2, 12, 30);
            g.setColor(new Color(40, 75, 88));
            g.fillRect(x + 11, y + 12, 12, 10);
            g.setColor(new Color(150, 210, 205));
            g.drawRect(x + 4, y + 8, 26, 18);
        }
    }
}