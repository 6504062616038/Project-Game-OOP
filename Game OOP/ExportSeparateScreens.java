import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class ExportSeparateScreens {
    private static Random random = new Random(42); // กำหนด Seed เพื่อให้ตำแหน่งดาวนิ่งสวยงาม

    public static void main(String[] args) {
        int width = 360;
        int height = 480;

        // 1. สร้างภาพฉาก Home (ตรงตามแบบภาพตัวอย่าง)
        generateHomeScreen(width, height);

        // 2. สร้างภาพฉาก Gameplay
        generateGameplayScreen(width, height);

        // 3. สร้างภาพฉาก Game Over
        generateGameOverScreen(width, height);

        System.out.println("สร้างไฟล์รูปภาพฉากอวกาศทั้ง 3 ฉากเรียบร้อยแล้ว!");
    }

    private static void generateHomeScreen(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        drawSpaceBackground(g, w, h);

        // Title: SPACE SHOOTER (ตรงตามแบบตัวอย่าง)
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        FontMetrics fm = g.getFontMetrics();
        String title = "SPACE SHOOTER";
        int titleX = (w - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, 210);

        // Start Button (ปุ่มขอบมนสีฟ้า ขอบสีขาว ตรงตามตัวอย่าง)
        int btnW = 160;
        int btnH = 42;
        int btnX = (w - btnW) / 2;
        int btnY = 260;

        g.setColor(new Color(0, 150, 200));
        g.fillRoundRect(btnX, btnY, btnW, btnH, 15, 15);
        g.setColor(Color.WHITE);
        g.drawRoundRect(btnX, btnY, btnW, btnH, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 15));
        fm = g.getFontMetrics();
        String btnText = "START GAME";
        int textX = btnX + (btnW - fm.stringWidth(btnText)) / 2;
        g.drawString(btnText, textX, btnY + 26);

        saveImage(img, "screen_home.png");
    }

    private static void generateGameplayScreen(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        drawSpaceBackground(g, w, h);

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("SCORE: 150", 15, 30);
        g.drawString("HP:", 15, 55);

        g.setColor(Color.GRAY);
        g.fillRect(50, 43, 100, 12);
        g.setColor(Color.GREEN);
        g.fillRect(50, 43, 80, 12);
        g.setColor(Color.WHITE);
        g.drawRect(50, 43, 100, 12);

        // Player & Enemies
        Player p = new Player(w / 2 - 22, 380);
        p.draw(g);

        Enemy e1 = new Enemy(70, 100, 0);
        Enemy e2 = new Enemy(230, 130, 0);
        e1.draw(g);
        e2.draw(g);

        Bullet b = new Bullet(w / 2 - 3, 290);
        b.draw(g);

        saveImage(img, "screen_gameplay.png");
    }

    private static void generateGameOverScreen(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        drawSpaceBackground(g, w, h);

        // Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g.getFontMetrics();
        String title = "GAME OVER";
        g.drawString(title, (w - fm.stringWidth(title)) / 2, 190);

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        fm = g.getFontMetrics();
        String scoreText = "FINAL SCORE: 150";
        g.drawString(scoreText, (w - fm.stringWidth(scoreText)) / 2, 240);

        // Restart Button
        int btnW = 160;
        int btnH = 42;
        int btnX = (w - btnW) / 2;
        int btnY = 280;

        g.setColor(new Color(200, 40, 40));
        g.fillRoundRect(btnX, btnY, btnW, btnH, 15, 15);
        g.setColor(Color.WHITE);
        g.drawRoundRect(btnX, btnY, btnW, btnH, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 15));
        fm = g.getFontMetrics();
        String btnText = "PLAY AGAIN";
        g.drawString(btnText, btnX + (btnW - fm.stringWidth(btnText)) / 2, btnY + 26);

        saveImage(img, "screen_gameover.png");
    }

    private static void drawSpaceBackground(Graphics2D g, int w, int h) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // พื้นหลังห้วงอวกาศสีมืด
        g.setColor(new Color(10, 10, 20));
        g.fillRect(0, 0, w, h);

        // วาดดวงดาวแบบสุ่มกระจายทั่วฉาก
        g.setColor(Color.WHITE);
        for (int i = 0; i < 70; i++) {
            int sx = random.nextInt(w);
            int sy = random.nextInt(h);
            int size = (i % 3) + 1;
            g.fillRect(sx, sy, size, size);
        }
    }

    private static void saveImage(BufferedImage img, String filename) {
        try {
            ImageIO.write(img, "png", new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}