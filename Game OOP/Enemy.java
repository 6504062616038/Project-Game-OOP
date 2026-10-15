import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy extends GameObject {

    public Enemy(int x, int y, int speed) {
        super(x, y, 36, 36, speed);
    }

    @Override
    public void update() {
        y += speed;
        if (y > 640) {
            active = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Red Thruster (ไอพ่นสีแดง)
        g.setColor(new Color(255, 50, 50));
        g.fillRect(x + 15, y - 4, 6, 6);

        // Wings (ปีก)
        g.setColor(new Color(150, 30, 30));
        int[] xWings = {x + 18, x, x + 6, x + 18, x + 30, x + 36};
        int[] yWings = {y + 20, y + 2, y + 28, y + 24, y + 28, y + 2};
        g.fillPolygon(xWings, yWings, 6);

        // Main Body (ลำตัว)
        g.setColor(new Color(70, 70, 80));
        int[] xBody = {x + 18, x + 8, x + 12, x + 18, x + 24, x + 28};
        int[] yBody = {y + 34, y + 10, y + 2, y + 6, y + 2, y + 10};
        g.fillPolygon(xBody, yBody, 6);

        // Eye / Cockpit (ตาอินฟราเรดตรงกลาง)
        g.setColor(Color.RED);
        g.fillOval(x + 14, y + 14, 8, 8);
    }
}