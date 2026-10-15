import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends GameObject {
    private int hp = 100;
    private int maxHp = 100;

    public Player(int x, int y) {
        super(x, y, 44, 50, 6);
    }

    @Override
    public void update() {
        x = Math.max(0, Math.min(x, 480 - width));
        y = Math.max(0, Math.min(y, 640 - height));
    }

    @Override
    public void draw(Graphics2D g) {
        // Thruster Fire (แสงไอพ่น)
        g.setColor(new Color(255, 100, 0));
        g.fillRect(x + 14, y + 42, 6, 8);
        g.fillRect(x + 24, y + 42, 6, 8);
        g.setColor(new Color(255, 230, 0));
        g.fillRect(x + 16, y + 42, 2, 5);
        g.fillRect(x + 26, y + 42, 2, 5);

        // Main Wings (ปีกปีกยาน)
        g.setColor(new Color(0, 150, 200));
        int[] xWings = {x + 22, x, x + 8, x + 22, x + 36, x + 44};
        int[] yWings = {y + 10, y + 40, y + 45, y + 35, y + 45, y + 40};
        g.fillPolygon(xWings, yWings, 6);

        // Body (ตัวเครื่องหลัก)
        g.setColor(new Color(200, 220, 240));
        int[] xBody = {x + 22, x + 12, x + 12, x + 22, x + 32, x + 32};
        int[] yBody = {y, y + 15, y + 40, y + 38, y + 40, y + 15};
        g.fillPolygon(xBody, yBody, 6);

        // Cockpit (กระจกห้องนักบิน)
        g.setColor(new Color(0, 230, 255));
        g.fillOval(x + 17, y + 12, 10, 16);

        // Cannons (ลำกล้องปืนปีกสองข้าง)
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x + 2, y + 20, 3, 12);
        g.fillRect(x + 39, y + 20, 3, 12);
    }

    public void moveLeft() { x -= speed; }
    public void moveRight() { x += speed; }
    public void moveUp() { y -= speed; }
    public void moveDown() { y += speed; }

    public Bullet shoot() {
        return new Bullet(x + width / 2 - 3, y - 5);
    }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public void reset() {
        this.hp = maxHp;
        this.active = true;
        this.x = 218;
        this.y = 520;
    }

    public void takeDamage(int amount) {
        this.hp -= amount;
        if (this.hp <= 0) {
            this.hp = 0;
            this.active = false;
        }
    }
}