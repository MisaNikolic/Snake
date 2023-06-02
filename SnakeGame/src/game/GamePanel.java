package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    // resources: apple, banana, snake head and body
    ImageIcon food1 = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/apple.png")));
    ImageIcon food2 = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/bananas.png")));
    ImageIcon snakeLR = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/snake-head.png")));
    ImageIcon snakeUD = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/snake-headUD.png")));
    ImageIcon snake_bodyLR = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/snake-bodyLR.png")));
    ImageIcon snake_bodyUD = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/snake-bodyUD.png")));

    // constants - screen grid for painting the snake and delay for our times-game speed
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    static final int DELAY = 55; // game speed, we can later change this to increase depending on the current score

    // class fields
    int bodyParts = 6;  // game will have a length of 6(head + 5 b.parts)
    int foodEaten, foodX, foodY, appleCounter;
    boolean isBanana;
    char direction = 'R';   // starting direction
    boolean running = false;
    Timer timer;
    Random random;


    public GamePanel() {
        // creating our panel
        random = new Random();  // we will this method to generate food on our game screen
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(217, 199, 179));
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new myKeyAdapter());    // controlling our snake with a keyboard
        startGame();
    }

    public void startGame() {
        // generating starting apple, enable running so that the snake can start moving, creating and running timer to make snake move
        // with a certain speed
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {    // overriding method from java.awt.Graphics, note that can only be used/called in JPanel, not JFrame!
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {  // method we use to draw food and snake
        if(running) {

            if(appleCounter < 4) {  //  draw apple
                food1.paintIcon(this, g, foodX, foodY);
                isBanana = false;
            }
            else {
                food2.paintIcon(this, g, foodX, foodY);  // draw banana every 5th time
                isBanana = true;
            }

            for (int i = 0; i < bodyParts; i++) {   // going thru our snake and drawing it accordingly
                if (i == 0) {   // starting position, drawing head
                    switch (direction) {
                        case 'R' -> snakeLR.paintIcon(this, g, x[0], y[0]);
                        case 'L' -> snakeLR.paintIcon(this, g, x[0], y[0]);
                        case 'U' -> snakeUD.paintIcon(this, g, x[0], y[0]);
                        case 'D' -> snakeUD.paintIcon(this, g, x[0], y[0]);
                    }

                } else {    // other positions, drawing snake body; we have rect-like body parts
                    // we draw snake differently horizontally and vertically
                    if(direction == 'L' || direction == 'R')
                        snake_bodyLR.paintIcon(this, g, x[i], y[i]);
                    else
                        snake_bodyUD.paintIcon(this, g, x[i], y[i]);

                }
            }
            // displaying the current score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + foodEaten, 10, g.getFont().getSize());
        }
        else {  // when we collide(running = false) we call our gameOver()
            gameOver(g);
        }
    }

    public void move() {    // moving(visually) our snake
        for(int i = bodyParts; i > 0; i--) {   // moving body harmoniously with the head
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {    // moving our head in different directions
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void newFood() {    // drawing food on our screen
        foodX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) *UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) *UNIT_SIZE;
    }

    public void checkFood() {  // if food is eaten, grow snake and make new apple
        if(x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            if(isBanana) {
                foodEaten += 3;
                appleCounter = 0;
            }
            else {
                foodEaten++;
                appleCounter ++;
            }
            newFood();
        }
    }

    public void checkCollisions() {     // if snake collides, we set running to false, stop the timer and call gameOver()
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        // check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head touches top border
        if(y[0] < 0) {
            running  = false;
        }
        // check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) {
            timer.stop();
        }
    }


    public void gameOver(Graphics g) {

//         Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
//         final score text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Final Score: " + foodEaten, (SCREEN_WIDTH - metrics2.stringWidth("Final Score: " + foodEaten))/2, SCREEN_HEIGHT/2 + metrics1.getHeight());


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();

    }

    public class myKeyAdapter extends KeyAdapter {  // key adapter class, enabling snake to change direction using arrows or W,A,S,D combo
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }
    }


}
