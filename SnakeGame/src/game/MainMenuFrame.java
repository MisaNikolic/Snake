package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;

public class MainMenuFrame extends JFrame implements ActionListener {   // JFrame will be sufficient, no need for JPanel

    // buttons - we are defining it here as class attributes so that our ActionListener can "see" them
    private final JButton newGame;
    private final JButton highScore;
    private final JButton exitGame;
    private final JButton snakeLogo;
    // JLabel for the game title, we need it defined in this score, so we can reference it later and change it's color
    private final JLabel snakeTitle;
    // resource
    ImageIcon snake = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/snake.png")));

    MainMenuFrame()  {      // creating our frame
        this.setSize(new Dimension(600, 600));
        this.setTitle("Main screen");
        this.setVisible(true);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(101, 183, 57));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // JLabel
        snakeTitle = new JLabel("Snake", JLabel.CENTER);
        snakeTitle.setFont(new Font("Ink Free", Font.BOLD, 120));
        snakeTitle.setBounds(120, 25, 340, 160);
        snakeTitle.setVisible(true);
        this.add(snakeTitle);
        // newGame button
        newGame = new JButton("New game");
        newGame.setFont(new Font("Ink Free", Font.BOLD, 15));
        newGame.setBounds(100, 450, 100, 45);
        newGame.setBackground(new Color(184, 33, 61));
        newGame.setForeground(Color.WHITE);
        newGame.setFocusable(false);
        newGame.setBorder(null);
        newGame.addActionListener(this);
        this.add(newGame);
        // highScore button
        highScore = new JButton("High score");
        highScore.setFont(new Font("Ink Free", Font.BOLD, 15));
        highScore.setBounds(250, 450, 100, 45);
        highScore.setBackground(new Color(98, 52, 181));
        highScore.setForeground(Color.WHITE);
        highScore.setFocusable(false);
        highScore.setBorder(null);
        highScore.addActionListener(this);
        this.add(highScore);
        // exitGame button
        exitGame = new JButton("Exit");
        exitGame.setBounds(400, 450, 100, 45);
        exitGame.setBackground(new Color(25, 28, 79));
        exitGame.setForeground(Color.WHITE);
        exitGame.setFocusable(false);
        exitGame.setBorder(null);
        exitGame.addActionListener(this);
        this.add(exitGame);
        // snakeLogo button
        snakeLogo = new JButton();
        snakeLogo.setBounds(235, 200, 130, 130);
        snakeLogo.setIcon(snake);
        snakeLogo.setBackground(new Color(101, 183, 57));
        snakeLogo.setBorder(null);
        snakeLogo.setFocusable(false);
        snakeLogo.addActionListener(this);
        this.add(snakeLogo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGame) {
            new GameFrame();
            this.dispose();
        }
        if(e.getSource() == highScore) {
            this.dispose();
            new HighScoreFrame();
        }
        if(e.getSource() == exitGame) {
            if(JOptionPane.showConfirmDialog(this,"Are you sure you want to quit playing?") == 0) {
                JOptionPane.showMessageDialog(this,"Thank you for playing!");
                this.dispose();
            }
        }
        if(e.getSource() == snakeLogo) {
            Random rnd = new Random();
            snakeTitle.setForeground(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            snakeLogo.setBackground(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            newGame.setBackground(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            highScore.setBackground(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            exitGame.setBackground(new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
        }
    }

}

