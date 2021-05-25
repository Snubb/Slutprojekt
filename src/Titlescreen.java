import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Titlescreen extends Canvas implements Runnable{
    private final int width = 1300; //Dimensions for playing area
    private final int height = 700;
    private boolean isRunning;

    Singleplayer a = new Singleplayer();
    Multiplayer b = new Multiplayer();

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

        a.frame.setVisible(false);
        b.frame.setVisible(false);

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
        g.drawString("TITLESCREEN.TXT", 400, 200);

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
}

