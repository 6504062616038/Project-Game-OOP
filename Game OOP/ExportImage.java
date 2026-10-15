import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportImage {
    public static void main(String[] args) {
        int width = 550;
        int height = 220;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // พื้นหลังสีขาว
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // กรอบสีดำรอบนอก
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, width - 40, height - 40);

        // ตั้งค่าฟอนต์ภาษาไทย
        Font font = new Font("Tahoma", Font.PLAIN, 18);
        g.setFont(font);

        // 1. วาด ยานผู้เล่น
        Player player = new Player(80, 60);
        player.draw(g);
        g.setColor(Color.BLACK);
        g.drawString("ยานผู้เล่น", 70, 160);

        // 2. วาด ยานศัตรู
        Enemy enemy = new Enemy(250, 65, 0);
        enemy.draw(g);
        g.setColor(Color.BLACK);
        g.drawString("ยานศัตรู", 240, 160);

        // 3. วาด กระสุนเลเซอร์
        Bullet bullet = new Bullet(415, 80);
        bullet.draw(g);
        g.setColor(Color.BLACK);
        g.drawString("กระสุนเลเซอร์", 380, 160);

        g.dispose();

        try {
            ImageIO.write(image, "png", new File("space_characters.png"));
            System.out.println("สร้างไฟล์รูปภาพ space_characters.png เรียบร้อยแล้ว!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}