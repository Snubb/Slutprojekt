import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Titlescreen extends Canvas implements Runnable{
    private final int width = 1300; //Dimensions for playing area
    private final int height = 700;
    private boolean isRunning;

    Singleplayer a = new Singleplayer();
    Multiplayer b = new Multiplayer();

    private final Rectangle single = new Rectangle(200,400,300,80);
    private final Rectangle multi = new Rectangle(700,400,300,80);
    private final Rectangle mouse = new Rectangle();

    private Thread thread;

    int fps = 60;

    JFrame frame = new JFrame("Battleship clone");

    public Titlescreen() {

        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new Titlescreen.KL());
        frame.setVisible(true);

        this.addMouseMotionListener(new MML());
        this.addMouseListener(new ML());

        a.frame.setVisible(false);
        b.frame.setVisible(false);

        mouse.width = 5;
        mouse.height = 5;

        isRunning = false;
    }
    public void update() { //Updates every frame

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

        g.setColor(new Color(246, 44, 68));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        g.drawString("TOTALLY ACCURATE BATTLESHIP CLONE", 25, 100);

        g.setColor(new Color(21, 74, 168));
        g.fillRect(single.x, single.y, single.width, single.height);
        g.fillRect(multi.x, multi.y, multi.width, multi.height);
        g.setColor(new Color(0,0,0));
        g.drawString("SOLO", single.x + 50, single.y + 60);
        g.drawString("1v1", multi.x + 90, multi.y + 60);

        g.dispose();
        bs.show();
    }
    public static void main(String[] args) {
        // Här startas ditt program
        Titlescreen painting = new Titlescreen();
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
            if (keyEvent.getKeyChar() == 's') {
                a.start();
                a.frame.setVisible(true);
                frame.setVisible(false);
            }
            if (keyEvent.getKeyChar() == 'm') {
                b.start();
                b.frame.setVisible(true);
                frame.setVisible(false);
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }
    private class ML implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouse.intersects(single)) {
                a.start();
                a.frame.setVisible(true);
                frame.setVisible(false);
            } else if (mouse.intersects(multi)) {
                b.start();
                b.frame.setVisible(true);
                frame.setVisible(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }
    private class MML implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            // Uppdatera rektangeln för muspekaren
            mouse.x = mouseEvent.getX();
            mouse.y = mouseEvent.getY();
        }
    }
}

