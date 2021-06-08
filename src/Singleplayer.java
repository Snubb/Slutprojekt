import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created 2021-04-27
 *
 * @author me :)
 */
public class Singleplayer extends Canvas implements Runnable{
    private final int width = 750; //Dimensions for playing area
    private final int height = 700;

    private BufferedImage boom;
    private BufferedImage aim;
    private BufferedImage boat;

    private final Rectangle player = new Rectangle();
    private final Rectangle title = new Rectangle(100, 50);

    private int victory = 0;

    private int playerPos;
    private int numOfShots;

    boolean boat2 = false;
    boolean boat3 = false;
    boolean boat4 = false;

    boolean boats = true;

    public ArrayList<gridSpace1> grids = new ArrayList<>();

    public ArrayList<Rectangle> shots = new ArrayList(); //Handles the displayed number of shots

    private boolean isRunning;

    private Thread thread;

    int fps = 20;

    private final Rectangle mouse = new Rectangle();

    JFrame frame = new JFrame("Battleship clone");

    public Singleplayer() {

        try {
            boom = ImageIO.read(getClass().getResourceAsStream("images/boom.png"));
            aim = ImageIO.read(getClass().getResourceAsStream("images/Crosshair.png"));
            boat = ImageIO.read(getClass().getResourceAsStream("images/boat1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        this.addMouseMotionListener(new Singleplayer.MML());
        this.addMouseListener(new Singleplayer.ML());

        isRunning = false;

        player.x = 100;
        player.y = 100;
        player.width = 54;
        player.height = 54;
        mouse.width = 5;
        mouse.height = 5;

        numOfShots = 30;

        int shotPosX = 500;
        int shotPosY = 100;
        for (int i = 0; i <= numOfShots; i++) {
            shots.add(new Rectangle(shotPosX + 5, shotPosY + 5, 40, 40));
            shotPosY += 50;
            if (shotPosY > 450) {
                shotPosX += 50;
                shotPosY = 100;
            }
        }

        createGrid();
        createBoats(grids);
    }

    private void createBoats(ArrayList<gridSpace1> grids) { //This shit is way too long but it works and I have other stuff to focus on
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
                    //No "clipping" where boat starts at one side and continues on the other
                    allowed = randomNum / 8 == (randomNum + randomDirection) / 8;
                }
                else {
                    allowed = true;
                }
        }
        grids.get(randomNum).hasBoat(2);
        grids.get(randomNum + randomDirection).hasBoat(2);

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
                allowed = randomNum / 8 == (randomNum + 2 * randomDirection) / 8;
            }
            else {
                allowed = true;
            }
        }
        grids.get(randomNum).hasBoat(3);
        grids.get(randomNum + randomDirection).hasBoat(3);
        grids.get(randomNum + 2 * randomDirection).hasBoat(3);


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
            if ((randomNum + 3*randomDirection) < 0 || (randomNum + 3*randomDirection) > grids.size()) {
                allowed = false;
            } else if (randomNum + 2*randomDirection <= 0 || randomNum + 3*randomDirection >= 63) {
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
                allowed = randomNum / 8 == (randomNum + 4 * randomDirection) / 8;
            }
            else {
                allowed = true;
            }
        }
        grids.get(randomNum).hasBoat(4);
        grids.get(randomNum + randomDirection).hasBoat(4);
        grids.get(randomNum + 2 * randomDirection).hasBoat(4);
        grids.get(randomNum + 3 * randomDirection).hasBoat(4);
    }

    private void createGrid() { //Creates the grid as 50*50 squares in a 8*8 pattern
        int posX = 1;
        int posY = 1;
        for (int i = 0;i < 64; i++) {
            grids.add(new gridSpace1(new Rectangle(49,49)));
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
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,width,height);
        g.drawImage(boat, 100, 510, 100, 50, null);
        g.drawImage(boat,210, 510, 150, 50,null);
        g.drawImage(boat, 100, 570, 200, 50, null);

        g.setColor(new Color(255,255,255));
        g.fillRect(100,0,50*8 + 1,50*8 + 101);

        g.setColor(new Color(255,50,50));
        for (int i = 0; i < numOfShots; i++) {
            g.fillOval(shots.get(i).x, shots.get(i).y, shots.get(i).height, shots.get(i).width);
        }

        g.setColor(new Color(210, 101, 13));
        g.fillRect(0, 0, title.width, title.height);
        g.setColor(new Color(0,0,0));
        g.setFont(new Font("Serif", Font.BOLD, 42));
        g.drawString("Back", 2, 35);

        g.setColor(new Color(156, 55, 8));

        g.drawRect(100,100,50*8 + 1,50*8 + 1);
        drawGrids(g);
        drawPlayerRect(g);
        drawProgress(g);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        g.setColor(Color.black);
        if (victory >= 9) {
            g.drawString("Congratulations!!", 300, 30);
            g.drawString("Press r to restart.", 300, 50);
        } else if (numOfShots == 0) {
            g.drawString("You suck!!", 300, 30);
            g.drawString("Press r to restart.", 300, 50);
        }
        g.dispose();
        bs.show();
    }

    private void drawProgress(Graphics g) { //Handles the boats below the playing field to show if a boat is destroyed
        if (boat2) { //true if boat is destroyed
            g.setColor(Color.black);
            g.drawLine(100,510,200,560);
            g.drawLine(100,560,200,510);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids.toArray().length; i++) {
                if (grids.get(i).boatNum == 2 && grids.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 2) {
                boat2 = true;
            }
        }
        if (boat3) {
            g.setColor(Color.black);
            g.drawLine(210,510,360,560);
            g.drawLine(210,560,360,510);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids.toArray().length; i++) {
                if (grids.get(i).boatNum == 3 && grids.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 3) {
                boat3 = true;
            }
        }
        if (boat4) {
            g.setColor(Color.black);
            g.drawLine(100,570,300,620);
            g.drawLine(100,620,300,570);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids.toArray().length; i++) {
                if (grids.get(i).boatNum == 4 && grids.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 4) {
                boat4 = true;
            }
        }
    }

    private void drawPlayerRect(Graphics g) { //Handles the red player box
        g.setColor(new Color(255, 0, 0));
        g.drawImage(aim, player.x, player.y, 50, 50, null);
    }

    private void drawGrids(Graphics g) { //draws all previously made grids
        for (int i = 0; i < grids.toArray().length; i++) {
            g.setColor(new Color(156, 55, 8));
            g.drawRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, grids.get(i).Hitbox.width, grids.get(i).Hitbox.height);
            if (grids.get(i).hasBoat && grids.get(i).hasBeenHit || numOfShots == 0 && grids.get(i).hasBoat) { //handles explosion if there is a boat there
                g.setColor(Color.blue);
                g.drawImage(boom, grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50, null);
            } else if (grids.get(i).hasBeenHit) { //Draws an x if there is no boat
                g.setColor(Color.black);
                g.drawLine(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, grids.get(i).Hitbox.x + 50, grids.get(i).Hitbox.y + 50);
                g.drawLine(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y + 50, grids.get(i).Hitbox.x + 50, grids.get(i).Hitbox.y);
            }
        }
    }

    public static void main(String[] args) {
        // Här startas ditt program
        Singleplayer painting = new Singleplayer();
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
            if (keyEvent.getKeyChar() == 'd' || keyEvent.getKeyChar() == 'D') {
                if (player.x >= 450) {
                    playerPos -= 7;
                } else {
                    playerPos++;
                }
            }
            if (keyEvent.getKeyChar() == 'a' || keyEvent.getKeyChar() == 'A') {
                if (player.x <= 150) {
                    playerPos += 7;
                } else {
                    playerPos--;
                }
            }
            if (keyEvent.getKeyChar() == 'w' || keyEvent.getKeyChar() == 'W') {
                if (player.y <= 150) {
                    playerPos += 56;
                } else {
                    playerPos -= 8;
                }
            }
            if (keyEvent.getKeyChar() == 's' || keyEvent.getKeyChar() == 'S') {
                if (player.y >= 450) {
                    playerPos -= 56;
                } else {
                    playerPos += 8;
                }
            }
            if (keyEvent.getKeyChar() == ' ') {
                shoot();
            }
            if (keyEvent.getKeyChar() == 'r' || keyEvent.getKeyChar() == 'R') {
                reset();
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) { }
        @Override
        public void keyReleased(KeyEvent keyEvent) { }
    }

    private void shoot() { //Handles shooting
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

    private class ML implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) { }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if (mouse.intersects(title)) {
                Titlescreen c = new Titlescreen();
                c.start();
                c.frame.setVisible(true);
                frame.setVisible(false);
            }
            shoot();
        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) { }
        @Override
        public void mouseEntered(MouseEvent mouseEvent) { }
        @Override
        public void mouseExited(MouseEvent mouseEvent) { }
    }
    private class MML implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent mouseEvent) { }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            mouse.x = mouseEvent.getX();
            mouse.y = mouseEvent.getY();
            for (int i = 0; i < grids.toArray().length; i++) {
                if (mouse.intersects(grids.get(i).Hitbox)) {
                    player.x = grids.get(i).Hitbox.x;
                    player.y = grids.get(i).Hitbox.y;
                    playerPos = i;
                }
            }
        }
    }

    private void reset() { //Resets a bunch of variables to "reset" the game
        boats = true;
        victory = 0;
        numOfShots = 30;
        boat2 = false;
        boat3 = false;
        boat4 = false;
        for (gridSpace1 grid : grids) {
            grid.reset();
        }
        createBoats(grids);
    }
}
