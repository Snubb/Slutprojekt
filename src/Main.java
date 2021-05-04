import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Created 2021-04-27
 *
 * @author me :)
 */
public class Main extends Canvas implements Runnable{
    private final int width = 600; //Dimensions for playing area
    private final int height = 600;

    private Rectangle player = new Rectangle();

    public ArrayList<gridSpace> grids = new ArrayList<>();

    private boolean isRunning;

    private Thread thread;

    int fps = 60;

    private BufferStrategy bs;

    public Main() {
        JFrame frame = new JFrame("Battleship clone");
        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        isRunning = false;

        player.x = 100;
        player.y = 100;
        player.width = 54;
        player.height = 54;

        createGrid();
    }

    private void createGrid() {
        int posX = 0;
        int posY = 0;
        for (int i = 0;i < 64; i++) {
            grids.add(new gridSpace(new Rectangle(50,50)));
            grids.get(i).Hitbox.x = 100 + posX;
            grids.get(i).Hitbox.y = 100 + posY;
            if (posX >= 350) {
                posX = 0;
                posY += 50;
            } else {
                posX += 50;
            }
        }
    }

    public void update() {

    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,width,height);

        g.setColor(new Color(156, 55, 8));

        g.drawRect(100,100,50*8,50*8);
        drawGrids(g);
        drawPlayerRect(g);


        g.dispose();
        bs.show();
    }

    private void drawPlayerRect(Graphics g) {
        g.setColor(new Color(255, 0, 0));
        g.drawRect(player.x - 2, player.y - 2, player.width, player.height);
    }

    private void drawGrids(Graphics g) {
        for (int i = 0;i < grids.size(); i++) {
            g.drawRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, grids.get(i).Hitbox.width, grids.get(i).Hitbox.height);
        }
    }

    public static void main(String[] args) {
        // Här startas ditt program
        Main painting = new Main();
        painting.start();
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double deltaT = 1000.0/fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if (now-lastTime > deltaT) {
                update();
                draw();
                lastTime = now;
            }
        }
        stop();
    }

    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'd') {
                if (player.x >= 450) {
                    player.x = 100;
                } else {
                    player.x += 50;
                }
            }
            if (keyEvent.getKeyChar() == 'a') {
                if (player.x <= 100) {
                    player.x = 450;
                } else {
                    player.x -= 50;
                }
            }
            if (keyEvent.getKeyChar() == 'w') {
                if (player.y <= 100) {
                    player.y = 450;
                } else {
                    player.y -= 50;
                }
            }
            if (keyEvent.getKeyChar() == 's') {
                if (player.y >= 450) {
                    player.y = 100;
                } else {
                    player.y += 50;
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }
}
