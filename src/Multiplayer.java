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
public class Multiplayer extends Canvas implements Runnable{
    private final int width = 1300; //Dimensions for playing area
    private final int height = 700;

    private BufferedImage boom;

    private final Rectangle player = new Rectangle();

    private int victory = 0;

    private int playerPos;
    private int playerTurn = 1;

    boolean boat2 = false;
    boolean boat3 = false;
    boolean boat4 = false;

    public ArrayList<gridSpace1> grids1 = new ArrayList<>();
    public ArrayList<gridSpace1> grids2 = new ArrayList<>();

    private boolean isRunning;

    private Thread thread;

    int fps = 60;

    public Multiplayer() {

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

        createGrid1();
        createGrid2();
        createBoats(grids1);
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

    private void createGrid1() { //Creates the grid as 50*50 squares in a 8*8 pattern
        int posX = 1;
        int posY = 1;
        for (int i = 0;i < 64; i++) {
            grids1.add(new gridSpace1(new Rectangle(49,49)));
            grids1.get(i).Hitbox.x = 100 + posX;
            grids1.get(i).Hitbox.y = 100 + posY;
            if (posX >= 350) {
                posX = 1;
                posY += 50;
            } else {
                posX += 50;
            }
        }
    }

    private void createGrid2() { //Creates the grid as 50*50 squares in a 8*8 pattern
        int posX = 1;
        int posY = 1;
        for (int i = 0;i < 64; i++) {
            grids2.add(new gridSpace1(new Rectangle(49,49)));
            grids2.get(i).Hitbox.x = 800 + posX;
            grids2.get(i).Hitbox.y = 100 + posY;
            if (posX >= 350) {
                posX = 1;
                posY += 50;
            } else {
                posX += 50;
            }
        }
    }

    public void update() { //Updates every frame
        if (playerTurn == 1) {
            player.x = grids1.get(playerPos).getHitbox().x;
            player.y = grids1.get(playerPos).getHitbox().y;
        } else {
            player.x = grids2.get(playerPos).getHitbox().x;
            player.y = grids2.get(playerPos).getHitbox().y;
        }

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

        g.setColor(new Color(156, 55, 8));

        g.drawRect(100,100,50*8 + 1,50*8 + 1);
        g.drawRect(800,100,50*8 + 1,50*8 + 1);
        //g.fillRect(70,100,30,402);
        drawGrids1(g);
        drawGrids2(g);
        drawPlayerRect(g);
        drawProgress(g);
        g.setFont(new Font("Serif", Font.BOLD, 24));

        g.setColor(Color.black);

        g.drawString("PlayerPos: " + playerPos, 30, 70);
        g.drawString("PlayerX: " + player.x, 160, 70);
        g.drawString("PlayerY: " + player.y, 300, 70);



        g.dispose();
        bs.show();
    }

    private void drawProgress(Graphics g) {
        g.setColor(new Color(255, 0, 0));
        g.fillRect(100, 510, 100, 50);
        if (boat2) {
            g.setColor(Color.black);
            g.drawLine(100,510,200,560);
            g.drawLine(100,560,200,510);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids1.toArray().length; i++) {
                if (grids1.get(i).boatNum == 2 && grids1.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 2) {
                System.out.println("YES");
                boat2 = true;
            }
        }
        g.setColor(new Color(255, 0, 0));
        g.fillRect(210, 510, 150, 50);
        if (boat3) {
            g.setColor(Color.black);
            g.drawLine(210,510,360,560);
            g.drawLine(210,560,360,510);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids1.toArray().length; i++) {
                if (grids1.get(i).boatNum == 3 && grids1.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 3) {
                System.out.println("YES");
                boat3 = true;
            }
        }
        g.setColor(new Color(255, 0, 0));
        g.fillRect(100, 570, 200, 50);
        if (boat4) {
            g.setColor(Color.black);
            g.drawLine(100,570,300,620);
            g.drawLine(100,620,300,570);
        } else {
            int destroyed;
            destroyed = 0;
            for (int i = 0;i < grids1.toArray().length; i++) {
                if (grids1.get(i).boatNum == 4 && grids1.get(i).hasBeenHit) {
                    destroyed++;
                }
            }
            if (destroyed == 4) {
                System.out.println("YES");
                boat4 = true;
            }
        }
    }

    private void drawPlayerRect(Graphics g) { //Handles the red player box
        g.setColor(new Color(255, 0, 0));
        g.drawRect(player.x - 3, player.y - 3, player.width +1, player.height +1);
    }

    private void drawGrids1(Graphics g) { //draws all previously made grids
        g.setColor(new Color(156,55,8));
        g.fillRect(100,100,-50,500);
        for (gridSpace1 grid : grids1) {
            g.setColor(new Color(156, 55, 8));
            g.drawRect(grid.Hitbox.x, grid.Hitbox.y, grid.Hitbox.width, grid.Hitbox.height);
            if (grid.hasBoat && grid.hasBeenHit) {
                g.setColor(Color.blue);
                g.drawImage(boom, grid.Hitbox.x, grid.Hitbox.y, 50, 50, null);
                //g.fillRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50);
            } else if (grid.hasBeenHit) {
                g.setColor(Color.black);
                g.drawLine(grid.Hitbox.x, grid.Hitbox.y, grid.Hitbox.x + 50, grid.Hitbox.y + 50);
                g.drawLine(grid.Hitbox.x, grid.Hitbox.y + 50, grid.Hitbox.x + 50, grid.Hitbox.y);
            }
        }
    }

    private void drawGrids2(Graphics g) { //draws all previously made grids
        g.setColor(new Color(156,55,8));
        g.fillRect(100,100,-50,500);
        for (gridSpace1 grid : grids2) {
            g.setColor(new Color(156, 55, 8));
            g.drawRect(grid.Hitbox.x, grid.Hitbox.y, grid.Hitbox.width, grid.Hitbox.height);
            if (grid.hasBoat && grid.hasBeenHit) {
                g.setColor(Color.blue);
                g.drawImage(boom, grid.Hitbox.x, grid.Hitbox.y, 50, 50, null);
                //g.fillRect(grids.get(i).Hitbox.x, grids.get(i).Hitbox.y, 50, 50);
            } else if (grid.hasBeenHit) {
                g.setColor(Color.black);
                g.drawLine(grid.Hitbox.x, grid.Hitbox.y, grid.Hitbox.x + 50, grid.Hitbox.y + 50);
                g.drawLine(grid.Hitbox.x, grid.Hitbox.y + 50, grid.Hitbox.x + 50, grid.Hitbox.y);
            }
        }
    }

    public static void main(String[] args) {
        // Här startas ditt program
        Multiplayer painting = new Multiplayer();
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
            if (playerTurn == 1) {
                if (keyEvent.getKeyChar() == 'd') {
                    if (player.x >= 450) {
                        playerPos -= 7;
                    } else {
                        playerPos++;
                    }
                }
                if (keyEvent.getKeyChar() == 'a') {
                    if (player.x <= 150) {
                        playerPos += 7;
                    } else {
                        playerPos--;
                    }
                }
                if (keyEvent.getKeyChar() == 'w') {
                    if (player.y <= 150) {
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
                    for (int i = 0; i < grids1.toArray().length; i++) {
                        grids1.get(i).hasBoat = false;
                    }
                    createBoats(grids1);
                }
            } else {
                if (keyEvent.getKeyChar() == 'd') {
                    if (player.x >= 1150) {
                        playerPos -= 7;
                    } else {
                        playerPos++;
                    }
                }
                if (keyEvent.getKeyChar() == 'a') {
                    if (player.x <= 850) {
                        playerPos += 7;
                    } else {
                        playerPos--;
                    }
                }
                if (keyEvent.getKeyChar() == 'w') {
                    if (player.y <= 150) {
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
                    for (int i = 0; i < grids1.toArray().length; i++) {
                        grids1.get(i).hasBoat = false;
                    }
                    createBoats(grids1);
                }
            }
            if (keyEvent.getKeyChar() == ' ') {
                if (playerTurn == 1) {
                    if (!grids1.get(playerPos).hasBeenHit) {
                        grids1.get(playerPos).hit();
                        playerTurn = 2;
                    }
                } else {
                    if (!grids2.get(playerPos).hasBeenHit) {
                        grids2.get(playerPos).hit();
                        playerTurn = 1;
                    }
                }

            }
            if (keyEvent.getKeyChar() == 'r') {
                System.out.println("Restarted");
                reset();
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

    private void reset() {
        //isRunning = false;

        player.x = 100;
        player.y = 100;
        player.width = 54;
        player.height = 54;
        victory = 0;

        boat2 = false;
        boat3 = false;
        boat4 = false;
        for (gridSpace1 grid : grids1) {
            grid.reset();
        }
        grids1.clear();
        createGrid1();
        createBoats(grids1);

    }
}
