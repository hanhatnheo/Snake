package org.cis1200.snake;

import java.awt.*;
import javax.swing.*;

public class RunSnake implements Runnable {

    public void run() {
        final JFrame frame = new JFrame("SNAKE GAME");
        Image icon = Toolkit.getDefaultToolkit().getImage("files/snake.png");
        final Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(icon);
        frame.setLocation(300, 300);
        String startInstruction =  "Welcome to Snake! \n"
                + "Use the arrow keys to move up, down, left or right.\n" +
                "Every time you eat an egg, you will speed up.\nYou can press " +
                "P to pause the game.\nIf you run into your own body or touch " +
                "the boundaries of the screen, you will die.\nMake sure to stay " +
                "in bounds!\nYou will start with an apple, " +
                "which will give you a power up, and you will not meet an apple until you " +
                "have eaten 4 eggs in a row, which will slow you down.\nEvery " +
                "time you eat a food, your body will briefly change color.\nIf you " +
                "exit in the middle of the game, when you re-open, you can resume " +
                "your previous game.\nPress OK to begin the game when you are ready!";
        JOptionPane.showMessageDialog(frame, startInstruction,
                "Instructions", JOptionPane.OK_OPTION);
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel points = new JLabel("Current Score: 0");
        status_panel.add(points);
        final JLabel highest = new JLabel("   Highest Score: 0");
        status_panel.add(highest);

        final Board snakeGame = new Board(points, highest);
        frame.add(snakeGame, BorderLayout.CENTER);

        // Reset and Instruction button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> snakeGame.reset());
        final JButton instruction = new JButton("Instruction");
        String gameInstruction =  "Use the arrow keys to move up, down, left or right. " +
                "You can press P to pause the game.\nIf you run into your own body or " +
                "touch the boundaries of the screen, you will die.\nMake sure to stay " +
                "in bounds!\nYou will start with an apple, which will give you a power " +
                "up, and you will not meet an apple until you have eaten 4 eggs in a row, " +
                "which will slow you down.\nEvery time you eat a food, your body will " +
                "briefly change color.\nIf you exit in the middle of the game, when you " +
                "re-open, you can resume your previous game.\nPress OK to start a new game" +
                " when you are ready!";

        instruction.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, gameInstruction,
                    "Instructions", JOptionPane.OK_OPTION);
            snakeGame.reset();
        });
        control_panel.add(reset);
        control_panel.add(instruction);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        snakeGame.restart();
    }
}
