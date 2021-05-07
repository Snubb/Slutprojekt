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
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created 2021-04-27
 *
 * @author me :)
 */
public class Main extends Canvas implements Runnable{
    private final int width = 600; //Dimensions for playing area
    private final int height = 600;

    private final Rectangle player = new Rectangle();

    private int playerPos;

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
        createBoats(grids);
    }

    private void createBoats(ArrayList<gridSpace> grids) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 64 + 1);
        int randomDirection = 0;
        int randomVar;
        grids.get(randomNum).hasBoat();

        while (true) { //This makes sure that no boat or boatpart overlaps with another boat
            randomVar = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            switch (randomVar) {
                case 0:
                    randomDirection = -1;
                    break;
                case 1:
                    randomDirection = 1;
                    break;
                case 2:
                    randomDirection = -8;
                    break;
                case 3:
                    randomDirection = 8;
                    break;
            }
            randomNum = ThreadLocalRandom.current().nextInt(0, 64 + 1);
            if (grids.get(randomNum).hasBoat) {
                continue;
            } else if (grids.get(randomNum + randomDirection).hasBoat) {
                break;
            }
            break;
        }
        grids.get(randomNum).hasBoat();
        grids.get(randomNum + randomDirection).hasBoat();

        while (true) { //This makes sure that no boat or boatpart overlaps with another boat
            randomVar = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            switch (randomVar) {
                case 0:
                    randomDirection = -1;
                    break;
                case 1:
                    randomDirection = 1;
                    break;
                case 2:
                    randomDirection = -8;
                    break;
                case 3:
                    randomDirection = 8;
                    break;
            }
            randomNum = ThreadLocalRandom.current().nextInt(0, 64 + 1);
            if (grids.get(randomNum).hasBoat) {
                continue;
            } else if (grids.get(randomNum + randomDirection).hasBoat) {
                continue;
            } else if (grids.get(randomNum + 2 * randomDirection).hasBoat) {
                break;
            }
            break;
        }
        grids.get(randomNum).hasBoat();
        grids.get(randomNum + randomDirection).hasBoat();
        grids.get(randomNum + 2 * randomDirection).hasBoat();
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
        player.x = grids.get(playerPos).getHitbox().x;
        player.y = grids.get(playerPos).getHitbox().y;

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
            if (grids.get(i).hasBoat) {
                g.fillRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50);
            }
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
                    playerPos -= 7;
                } else {
                    playerPos++;
                }
            }
            if (keyEvent.getKeyChar() == 'a') {
                if (player.x <= 100) {
                    playerPos += 7;
                } else {
                    playerPos--;
                }
            }
            if (keyEvent.getKeyChar() == 'w') {
                if (player.y <= 100) {
                    playerPos += 56;
                } else {
                    playerPos -= 8;
                }
            }
            if (keyEvent.getKeyChar() == 's') {
                if (player.y >= 450) {
                    playerPos -= 56;
                } else {
                    playerPos += 8;
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
