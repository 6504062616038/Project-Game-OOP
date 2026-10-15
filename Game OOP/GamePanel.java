import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    // Game States
    public enum State { HOME, PLAYING, GAMEOVER }
    private static final Preferences HIGH_SCORE_PREFERENCES = Preferences.userRoot().node("space-shooter");
    private static final int TRIPLE_SHOT_DURATION = 60 * 10;
    private State currentState = State.HOME;

    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private int level = 1;
    private final int[] levelScoreGoals = {300, 750};
    private final String[] levelNames = {"ASTEROID BELT", "NEBULA", "ALIEN RUINS"};

    // Starfield Background
    private int[] starX = new int[60];
    private int[] starY = new int[60];

    // Controls
    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private int score = 0;
    private int highScore = HIGH_SCORE_PREFERENCES.getInt("highScore", 0);
    private int tripleShotTicks = 0;
    private Random random = new Random();
    private Timer timer;

    // Buttons Bounds
    private Rectangle startBtn = new Rectangle(140, 300, 200, 50);
    private Rectangle restartBtn = new Rectangle(140, 380, 200, 50);
    private boolean startHover = false, restartHover = false;

    public GamePanel() {
        setPreferredSize(new Dimension(480, 640));
        setBackground(new Color(10, 10, 20));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(218, 520);

        // Generate Random Stars
        for (int i = 0; i < starX.length; i++) {
            starX[i] = random.nextInt(480);
            starY[i] = random.nextInt(640);
        }

        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateStars();

        if (currentState == State.PLAYING) {
            if (player.isActive()) {
                if (tripleShotTicks > 0) tripleShotTicks--;
                updatePlayerMovement();
                spawnEnemies();
                spawnEnemyBullets();
                spawnObstacles();
                spawnPowerUps();
                updateObjects();
                checkCollisions();
                updateStageProgress();
            } else {
                currentState = State.GAMEOVER;
            }
        }
        repaint();
    }

    private void updateStars() {
        for (int i = 0; i < starY.length; i++) {
            starY[i] += (i % 3) + 1;
            if (starY[i] > 640) {
                starY[i] = 0;
                starX[i] = random.nextInt(480);
            }
        }
    }

    private void updatePlayerMovement() {
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();
        if (upPressed) player.moveUp();
        if (downPressed) player.moveDown();
        player.update();
    }

    private void spawnEnemies() {
        if (random.nextInt(100) < level) {
            int x = random.nextInt(430);
            int speed = random.nextInt(3) + 2;
            enemies.add(new Enemy(x, -40, speed));
        }
    }

    private void spawnEnemyBullets() {
        Rectangle playerBounds = player.getBounds();
        int playerCenterX = playerBounds.x + playerBounds.width / 2;
        int playerCenterY = playerBounds.y + playerBounds.height / 2;
        for (Enemy enemy : enemies) {
            if (enemy.isActive() && random.nextInt(300) == 0) {
                Rectangle enemyBounds = enemy.getBounds();
                int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
                int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;
                enemyBullets.add(new EnemyBullet(enemyCenterX, enemyCenterY, playerCenterX, playerCenterY));
            }
        }
    }

    private void spawnObstacles() {
        if (random.nextInt(100) < level * 2) {
            int x = random.nextInt(446);
            int speed = random.nextInt(2) + level + 1;
            Obstacle.Type type = Obstacle.Type.values()[level - 1];
            obstacles.add(new Obstacle(x, -40, speed, type));
        }
    }

    private void spawnPowerUps() {
        if (random.nextInt(480) == 0) {
            int x = random.nextInt(452);
            powerUps.add(new PowerUp(x, -36, 2));
        }
    }

    private void shoot() {
        Bullet centerBullet = player.shoot();
        bullets.add(centerBullet);
        if (tripleShotTicks > 0) {
            Rectangle bounds = centerBullet.getBounds();
            bullets.add(new Bullet(bounds.x - 12, bounds.y + 4, -3));
            bullets.add(new Bullet(bounds.x + 12, bounds.y + 4, 3));
        }
    }

    private void updateObjects() {
        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
            Bullet bullet = it.next();
            bullet.update();
            if (!bullet.isActive()) it.remove();
        }

        for (Iterator<EnemyBullet> it = enemyBullets.iterator(); it.hasNext();) {
            EnemyBullet bullet = it.next();
            bullet.update();
            if (!bullet.isActive()) it.remove();
        }

        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
            Enemy enemy = it.next();
            enemy.update();
            if (!enemy.isActive()) it.remove();
        }

        for (Iterator<Obstacle> it = obstacles.iterator(); it.hasNext();) {
            Obstacle obstacle = it.next();
            obstacle.update();
            if (!obstacle.isActive()) it.remove();
        }

        for (Iterator<PowerUp> it = powerUps.iterator(); it.hasNext();) {
            PowerUp powerUp = it.next();
            powerUp.update();
            if (!powerUp.isActive()) it.remove();
        }
    }

    private void checkCollisions() {
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.isActive() && enemy.isActive() && bullet.getBounds().intersects(enemy.getBounds())) {
                    bullet.setActive(false);
                    enemy.setActive(false);
                    addScore(10);
                }
            }
            for (Obstacle obstacle : obstacles) {
                if (bullet.isActive() && obstacle.isActive() && bullet.getBounds().intersects(obstacle.getBounds())) {
                    bullet.setActive(false);
                    obstacle.setActive(false);
                    addScore(5);
                }
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.isActive() && player.getBounds().intersects(enemy.getBounds())) {
                enemy.setActive(false);
                player.takeDamage(25);
            }
        }

        for (Iterator<EnemyBullet> it = enemyBullets.iterator(); it.hasNext();) {
            EnemyBullet bullet = it.next();
            if (player.getBounds().intersects(bullet.getBounds())) {
                bullet.setActive(false);
                player.takeDamage(10);
                it.remove();
            }
        }

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isActive() && player.getBounds().intersects(obstacle.getBounds())) {
                obstacle.setActive(false);
                player.takeDamage(20);
            }
        }

        for (Iterator<PowerUp> it = powerUps.iterator(); it.hasNext();) {
            PowerUp powerUp = it.next();
            if (player.getBounds().intersects(powerUp.getBounds())) {
                tripleShotTicks = TRIPLE_SHOT_DURATION;
                it.remove();
            }
        }
    }

    private void addScore(int points) {
        score += points;
        if (score > highScore) {
            highScore = score;
            HIGH_SCORE_PREFERENCES.putInt("highScore", highScore);
            try {
                HIGH_SCORE_PREFERENCES.flush();
            } catch (BackingStoreException e) {
                System.err.println("Unable to save high score: " + e.getMessage());
            }
        }
    }

    private void updateStageProgress() {
        if (level >= levelNames.length) return;
        if (score < levelScoreGoals[level - 1]) return;

        level++;
        enemies.clear();
        obstacles.clear();
        bullets.clear();
        enemyBullets.clear();
        powerUps.clear();
        if (level == 2) setBackground(new Color(16, 8, 28));
        else setBackground(new Color(7, 16, 24));
    }

    private void resetGame() {
        score = 0;
        level = 1;
        enemies.clear();
        bullets.clear();
        enemyBullets.clear();
        obstacles.clear();
        powerUps.clear();
        tripleShotTicks = 0;
        player.reset();
        setBackground(new Color(10, 10, 20));
        currentState = State.PLAYING;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawLevelMap(g2d);

        // Draw Stars
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < starX.length; i++) {
            int size = (i % 3) + 1;
            g2d.fillRect(starX[i], starY[i], size, size);
        }

        if (currentState == State.HOME) {
            drawHomeScreen(g2d);
        } else if (currentState == State.PLAYING) {
            drawGamePlay(g2d);
        } else if (currentState == State.GAMEOVER) {
            drawGameOverScreen(g2d);
        }
    }

    private void drawLevelMap(Graphics2D g) {
        if (level == 1) {
            g.setColor(new Color(30, 46, 58));
            g.fillOval(330, 85, 105, 105);
            g.setColor(new Color(52, 70, 80));
            g.fillOval(350, 108, 18, 12);
            g.fillOval(390, 145, 24, 16);
            g.setColor(new Color(90, 125, 140, 100));
            g.drawOval(312, 112, 145, 54);
        } else if (level == 2) {
            g.setColor(new Color(88, 24, 86, 45));
            g.fillOval(-80, 130, 300, 190);
            g.fillOval(260, 345, 300, 210);
            g.setColor(new Color(180, 70, 135, 75));
            g.drawOval(270, 75, 155, 80);
            g.drawOval(285, 85, 125, 60);
        } else {
            g.setColor(new Color(20, 55, 63));
            g.fillRect(0, 0, 42, 640);
            g.fillRect(438, 0, 42, 640);
            g.setColor(new Color(45, 112, 120, 120));
            for (int y = 30; y < 640; y += 70) {
                g.drawRect(7, y, 27, 42);
                g.drawRect(446, y, 27, 42);
            }
            g.drawLine(42, 0, 42, 640);
            g.drawLine(438, 0, 438, 640);
        }
    }

    private void drawHomeScreen(Graphics2D g) {
        // Title
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 38));
        g.drawString("SPACE SHOOTER", 75, 250);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("HIGH SCORE: " + highScore, 170, 280);

        // Start Button
        g.setColor(startHover ? new Color(0, 220, 255) : new Color(0, 150, 200));
        g.fillRoundRect(startBtn.x, startBtn.y, startBtn.width, startBtn.height, 20, 20);
        g.setColor(Color.WHITE);
        g.drawRoundRect(startBtn.x, startBtn.y, startBtn.width, startBtn.height, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("START GAME", startBtn.x + 32, startBtn.y + 32);
    }

    private void drawGamePlay(Graphics2D g) {
        player.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        for (EnemyBullet bullet : enemyBullets) bullet.draw(g);
        for (Enemy enemy : enemies) enemy.draw(g);
        for (Obstacle obstacle : obstacles) obstacle.draw(g);
        for (PowerUp powerUp : powerUps) powerUp.draw(g);

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("SCORE: " + score, 15, 30);
        g.drawString("LEVEL " + level + "/3: " + levelNames[level - 1], 190, 30);
        g.drawString(level < levelNames.length ? "TARGET: " + levelScoreGoals[level - 1] : "ENDLESS", 190, 53);
        g.drawString("BEST: " + highScore, 15, 80);
        if (tripleShotTicks > 0) {
            g.setColor(new Color(120, 240, 255));
            g.drawString("TRIPLE SHOT: " + ((tripleShotTicks + 59) / 60) + "s", 190, 78);
        }

        // HP Bar
        g.drawString("HP:", 15, 55);
        g.setColor(Color.GRAY);
        g.fillRect(50, 42, 120, 14);
        g.setColor(player.getHp() > 30 ? Color.GREEN : Color.RED);
        g.fillRect(50, 42, (int) (120 * (player.getHp() / (double) player.getMaxHp())), 14);
        g.setColor(Color.WHITE);
        g.drawRect(50, 42, 120, 14);
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        String title = "GAME OVER";
        FontMetrics titleMetrics = g.getFontMetrics();
        g.drawString(title, (getWidth() - titleMetrics.stringWidth(title)) / 2, 220);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("FINAL SCORE: " + score, 150, 280);
        g.drawString("HIGH SCORE: " + highScore, 150, 320);

        // Restart Button
        g.setColor(restartHover ? new Color(255, 80, 80) : new Color(200, 40, 40));
        g.fillRoundRect(restartBtn.x, restartBtn.y, restartBtn.width, restartBtn.height, 20, 20);
        g.setColor(Color.WHITE);
        g.drawRoundRect(restartBtn.x, restartBtn.y, restartBtn.width, restartBtn.height, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("PLAY AGAIN", restartBtn.x + 40, restartBtn.y + 32);
    }

    // Input Listeners
    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (currentState == State.HOME && startBtn.contains(p)) {
            resetGame();
        } else if (currentState == State.GAMEOVER && restartBtn.contains(p)) {
            resetGame();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        startHover = startBtn.contains(p);
        restartHover = restartBtn.contains(p);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_DOWN) downPressed = true;
        if (code == KeyEvent.VK_SPACE && currentState == State.PLAYING && player.isActive()) {
            shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_RIGHT) rightPressed = false;
        if (code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_DOWN) downPressed = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}