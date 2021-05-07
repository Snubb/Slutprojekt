import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    private BufferedImage boom;

    private final Rectangle player = new Rectangle();

    private int victory = 0;

    private int playerPos;
    private int numOfShots;

    public ArrayList<gridSpace> grids = new ArrayList<>();

    private boolean isRunning;

    private Thread thread;

    int fps = 60;

    private BufferStrategy bs;

    public Main() {

        try {
            boom = ImageIO.read(new File("boom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        numOfShots = 24;

        createGrid();
        createBoats(grids);
    }

    private void createBoats(ArrayList<gridSpace> grids) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 63 + 1); //random number that determines boat positions
        int randomDirection = 0; //randomizes direction that boat goes
        int randomVar; // little helper for determining direction
        boolean allowed = false; //used to determine weather the boat is "valid"(no overlap, no out of bounds etc.)

        while (!allowed) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 63 + 1);
                randomVar = ThreadLocalRandom.current().nextInt(0, 3 + 1);
                switch (randomVar) { //Determines direction
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
                if (randomNum + randomDirection <= 0 || randomNum + randomDirection >= 63) {//No out of bounds
                    allowed = false;
                } else if (grids.get(randomNum).hasBoat) { //No overlap with old boat
                    allowed = false;
                } else if (grids.get(randomNum + randomDirection).hasBoat) {
                    allowed = false;
                } else if (randomDirection == -1 || randomDirection == 1) {
                    if(randomNum/8 != (randomNum + randomDirection)/8) {//No "cliping" where boat starts at one side and continues on the other
                        allowed = false;
                    } else {
                        allowed = true;
                    }
                }
                else {
                    allowed = true;
                }
        }
        grids.get(randomNum).hasBoat();
        grids.get(randomNum + randomDirection).hasBoat();

        allowed = false;
        while (!allowed) { //Refer to comments above for specifics
            randomNum = ThreadLocalRandom.current().nextInt(0, 63 + 1);
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
            if (randomNum + 2*randomDirection <= 0 || randomNum + 2*randomDirection >= 63) {
                allowed = false;
            } else if (grids.get(randomNum).hasBoat) {
                allowed = false;
            } else if (grids.get(randomNum + randomDirection).hasBoat) {
                allowed = false;
            } else if (grids.get(randomNum + 2*randomDirection).hasBoat) {
                allowed = false;
            } else if (randomDirection == -1 || randomDirection == 1) {
                if(randomNum/8 != (randomNum + 2*randomDirection)/8) {
                    allowed = false;
                } else {
                    allowed = true;
                }
            }
            else {
                allowed = true;
            }
        }
        grids.get(randomNum).hasBoat();
        grids.get(randomNum + randomDirection).hasBoat();
        grids.get(randomNum + 2 * randomDirection).hasBoat();


        allowed = false;
        while (!allowed) { //Refer to comments above for specifics
            randomNum = ThreadLocalRandom.current().nextInt(0, 63 + 1);
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
            if (randomNum + 2*randomDirection <= 0 || randomNum + 3*randomDirection >= 63) {
                allowed = false;
            } else if (grids.get(randomNum).hasBoat) {
                allowed = false;
            } else if (grids.get(randomNum + randomDirection).hasBoat) {
                allowed = false;
            } else if (grids.get(randomNum + 2*randomDirection).hasBoat) {
                allowed = false;
            } else if (grids.get(randomNum + 3*randomDirection).hasBoat) {
                allowed = false;
            } else if (randomDirection == -1 || randomDirection == 1) {
                if(randomNum/8 != (randomNum + 4*randomDirection)/8) {
                    allowed = false;
                } else {
                    allowed = true;
                }
            }
            else {
                allowed = true;
            }
        }
        grids.get(randomNum).hasBoat();
        grids.get(randomNum + randomDirection).hasBoat();
        grids.get(randomNum + 2 * randomDirection).hasBoat();
        grids.get(randomNum + 3 * randomDirection).hasBoat();
    }

    private void createGrid() { //Creates the grid as 50*50 squares in a 8*8 pattern
        int posX = 1;
        int posY = 1;
        for (int i = 0;i < 64; i++) {
            grids.add(new gridSpace(new Rectangle(49,49)));
            grids.get(i).Hitbox.x = 100 + posX;
            grids.get(i).Hitbox.y = 100 + posY;
            if (posX >= 350) {
                posX = 1;
                posY += 50;
            } else {
                posX += 50;
            }
        }
    }

    public void update() { //Updates every frame
        player.x = grids.get(playerPos).getHitbox().x;
        player.y = grids.get(playerPos).getHitbox().y;

    }

    public void draw() { //draws every frame
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,width,height);

        g.setColor(new Color(156, 55, 8));

        g.drawRect(100,100,50*8 + 1,50*8 + 1);
        drawGrids(g);
        drawPlayerRect(g);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        g.drawString("Number of shots: " + numOfShots, 30, 30);

        if (victory >= 9) {
            g.drawString("Congratulations!!", 300, 30);
        } else if (victory < 9 && numOfShots == 0) {
            g.drawString("You suck!!", 300, 30);
        }

        g.dispose();
        bs.show();
    }

    private void drawPlayerRect(Graphics g) { //Handles the red player box
        g.setColor(new Color(255, 0, 0));
        g.drawRect(player.x - 3, player.y - 2, player.width +1, player.height);
    }

    private void drawGrids(Graphics g) { //draws all previously made grids
        for (int i = 0;i < grids.size(); i++) {
            g.setColor(new Color(156,55,8));
            g.drawRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, grids.get(i).Hitbox.width, grids.get(i).Hitbox.height);
            if (grids.get(i).hasBoat && grids.get(i).hasBeenHit || numOfShots == 0 && grids.get(i).hasBoat) {
                g.setColor(Color.blue);
                g.drawImage(boom, grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50, null);
                //g.fillRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50);
            } else if (grids.get(i).hasBeenHit) {
                g.setColor(Color.black);
                g.drawLine(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, grids.get(i).Hitbox.x + 50, grids.get(i).Hitbox.y + 50);
                g.drawLine(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y + 50, grids.get(i).Hitbox.x + 50, grids.get(i).Hitbox.y);
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
            if (keyEvent.getKeyChar() == 'p') {
                for (int i = 0; i < grids.toArray().length; i++) {
                    grids.get(i).hasBoat = false;
                }
                createBoats(grids);
            }
            if (keyEvent.getKeyChar() == ' ') {
                if (!grids.get(playerPos).hasBeenHit && numOfShots > 0) {
                    grids.get(playerPos).hit();
                    numOfShots--;
                }
                for (int i = 0;i < grids.toArray().length; i++) {
                    if (grids.get(i).hasBoat && grids.get(i).hasBeenHit && grids.get(i).score) {
                        grids.get(i).score();
                        victory++;
                    }
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
