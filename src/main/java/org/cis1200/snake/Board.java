package org.cis1200.snake;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.util.List;

public class Board extends JPanel {
    private Food food;
    private ArrayList<Snake> s;
    private Timer timer;
    private JLabel currentScore;
    private JLabel highest;
    private int delay = 100;
    private int score = 0;
    private int highestScore;
    private boolean paused = false;
    private boolean playing = true;
    private boolean hasDied = false;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private int[][] gameBoard;
    private static final int SIZE = 10;

    public Board(JLabel currentScore, JLabel highest) {
        gameBoard = new int[WIDTH][HEIGHT];
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.DARK_GRAY);
        timer = new Timer(delay, e -> {
            try {
                tick();
            } catch (IOException ignored) {
            }
        });
        timer.start();
        setFocusable(true);
        s = new ArrayList<>();
        this.currentScore = currentScore;
        this.highest = highest;
        restart();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    for (Snake snake : s) {
                        snake.changeDirection(Direction.UP);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    for (Snake snake : s) {
                        snake.changeDirection(Direction.DOWN);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    for (Snake snake : s) {
                        snake.changeDirection(Direction.LEFT);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    for (Snake snake : s) {
                        snake.changeDirection(Direction.RIGHT);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    paused = true;
                    pause();
                }
            }
        });
    }
    public void leadership(int score) {
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("files/highscores.txt", true));
            w.write("\n" + score);
            w.close();
        } catch (FileNotFoundException f) {
            JOptionPane.showMessageDialog(null,
                    "No valid file found.", "File not Found",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ignored) {
        }
    }
    void tick() throws IOException {
        if (playing && !paused) {
            Snake head = s.get(0);
            Snake body = s.get(s.size() - 1);
            head.move(s);
            if (head.intersects(food)) {
                score++;
                currentScore.setText("Current Score: " + score);
                s.add(new Snake(body.getPx(), body.getPy(),
                        WIDTH, HEIGHT, Color.CYAN, body.getDirection()));
                delay = delay - food.getDelay();
                food.changeColor();
                allocate();
                timer.setDelay(delay);
            }
            saveGame();
            if (head.haveHitWall() || head.hitBody(s)) {
                hasDied = true;
                playing = false;
                JOptionPane.showMessageDialog(null, "Game Over! Your score is " + score + ".",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);

                clearFile();
                leadership(score);
                int selection = JOptionPane.showConfirmDialog(null, "Do you want to start " +
                        "a new game?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (selection == JOptionPane.YES_OPTION) {
                    restart();
                } else {
                    System.exit(0);
                }
            }
        }
        repaint();
    }
    public static void clearFile() {
        try {
            FileWriter fw = new FileWriter("files/gamestate.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        } catch (Exception exception) {
            System.out.println("Exception have been caught");
        }
    }

    public void reset() {
        String line;
        List<Integer> scores = new ArrayList<>();
        try {
            BufferedReader r = new BufferedReader(new FileReader("files/highscores.txt"));
            while ((line = r.readLine()) != null) {
                if (!line.equals("")) {
                    int pastScore = Integer.parseInt(line);
                    scores.add(pastScore);
                }
            }
            if (!scores.isEmpty()) {
                Collections.sort(scores);
                highestScore = scores.get(scores.size() - 1);
            }
            r.close();
            highest.setText("   Highest Score: \n" + highestScore);
        } catch (NumberFormatException n) {
            BufferedWriter w;
            try {
                w = new BufferedWriter(new FileWriter("files/highscores.txt", true));
                w.write("\n0\n0\n0");
                w.close();
            } catch (IOException er) {
                //ignore
            }
        } catch (FileNotFoundException f) {
            JOptionPane.showMessageDialog(null,
                    "No valid file found.", "File not Found",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ignored) {
        }
        gameBoard = new int[WIDTH][HEIGHT];
        s.clear();
        Snake snake = new Snake(20, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
        s.add(snake);
        Snake firstBody = new Snake(10, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
        s.add(firstBody);
        Snake secondBody = new Snake(0, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
        s.add(secondBody);
        food = new FoodEgg(WIDTH, HEIGHT, s);
        playing = true;
        hasDied = false;
        currentScore.setText("Current Score: 0");
        highest.setText("   Highest Score: " + highestScore);
        score = 0;
        delay = 100;
        timer.setDelay(delay);
        allocate();
        requestFocusInWindow();
    }
    private boolean noFoodLeft() {
        boolean flag = false;
        for (int[] ints : gameBoard) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (ints[j] == 2) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    private void pause() {
        timer.stop();
        int res = JOptionPane.showConfirmDialog(null, "The game has been paused. Do you " +
                "want to continue?", "Game Paused", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            continueGame();
        } else if (res == JOptionPane.NO_OPTION) {
            playing = true;
            paused = false;
            clearFile();
            timer.setDelay(100);
            timer.start();
            restart();
        }
    }
    public void continueGame() {
        playing = true;
        paused = false;
        timer.setDelay(delay);
        timer.start();
    }
    public void restart() {
        String line;
        List<Integer> scores = new ArrayList<>();
        try {
            BufferedReader r = new BufferedReader(new FileReader("files/highscores.txt"));
            while ((line = r.readLine()) != null) {
                if (!line.equals("")) {
                    int pastScore = Integer.parseInt(line);
                    scores.add(pastScore);
                }
            }
            if (!scores.isEmpty()) {
                Collections.sort(scores);
                highestScore = scores.get(scores.size() - 1);
            }
            r.close();
            highest.setText("   Highest Score: \n" + highestScore);
        } catch (NumberFormatException n) {
            BufferedWriter w;
            try {
                w = new BufferedWriter(new FileWriter("files/highscores.txt", true));
                w.write("\n0\n0\n0");
                w.close();
            } catch (IOException er) {
                //ignore
            }
        } catch (FileNotFoundException f) {
            JOptionPane.showMessageDialog(null,
                    "No valid file found.", "File not Found",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ignored) {

        }
        boolean flag = false;
        FileLineIterator f = null;
        try {
            BufferedReader r = new BufferedReader(new FileReader("files/gamestate.txt"));
            f = new FileLineIterator(r);
            if (f.hasNext()) {
                flag = true;
            }
        } catch (FileNotFoundException err) {
            JOptionPane.showMessageDialog(null,
                    "No valid file found.", "File not Found",
                    JOptionPane.PLAIN_MESSAGE);
        }
        gameBoard = new int[WIDTH][HEIGHT];
        highest.setText("   Highest Score: " + highestScore);
        playing = true;
        hasDied = false;
        if (flag) {
            this.score = Integer.parseInt(f.next());
            int foodPosX = Integer.parseInt(f.next());
            int foodPosY = Integer.parseInt(f.next());
            String foodType = f.next();
            currentScore.setText("Current Score: " + score);
            delay = Integer.parseInt(f.next());
            timer.setDelay(delay);
            s.clear();
            String[] snakeX = f.next().trim().split(" ");
            String[] snakeY = f.next().trim().split(" ");
            String[] snakeDir = f.next().trim().split(" ");
            for (int i = 0; i < snakeX.length; i++) {
                Direction d = Direction.valueOf(snakeDir[i]);
                Snake snake = new Snake(Integer.parseInt(snakeX[i]),
                        Integer.parseInt(snakeY[i]), WIDTH, HEIGHT,
                        Color.CYAN, d);
                s.add(snake);
            }
            if (Integer.parseInt(foodType) == 8) {
                food = new FoodApple(WIDTH, HEIGHT, s);
            } else {
                food = new FoodEgg(WIDTH, HEIGHT, s);
            }
            food.setPx(foodPosX);
            food.setPy(foodPosY);
            gameBoard[food.getPx()][food.getPy()] = 2;
        } else {
            currentScore.setText("Current Score: 0");
            score = 0;
            delay = 100;
            timer.setDelay(delay);
            s.clear();
            Snake snake = new Snake(20, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
            s.add(snake);
            Snake firstBody = new Snake(10, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
            s.add(firstBody);
            Snake secondBody = new Snake(0, 10, WIDTH, HEIGHT, Color.CYAN, Direction.RIGHT);
            s.add(secondBody);
            allocate();
        }
        requestFocusInWindow();
    }
    public void saveGame() {
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("files/gamestate.txt", false));
            if (!hasDied) {
                w.write(score + "\n");
                w.write(food.getPx() + "\n");
                w.write(food.getPy() + "\n");
                w.write(food.getDelay() + "\n");
                w.write(delay + "\n");
                StringBuilder snakeDataPx = new StringBuilder();
                StringBuilder snakeDataPy = new StringBuilder();
                StringBuilder snakeDir = new StringBuilder();
                for (Snake snake : s) {
                    snakeDataPx.append(snake.getPx()).append(" ");
                    snakeDataPy.append(snake.getPy()).append(" ");
                    snakeDir.append(snake.getDirection().name()).append(" ");
                }
                w.write(snakeDataPx + "\n");
                w.write(snakeDataPy + "\n");
                w.write(snakeDir + "\n");
            }
            w.close();
        } catch (FileNotFoundException f) {
            JOptionPane.showMessageDialog(null,
                    "No valid file found.", "File not Found",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ignored) {
        }
    }
    public void allocate() {
        if (score % 5 == 0) {
            food = new FoodApple(WIDTH, HEIGHT, s);
        } else {
            food = new FoodEgg(WIDTH, HEIGHT, s);
        }
        food.allocateFood();
    }

    public void paintComponent(Graphics g) {
        // Paint the background
        super.paintComponent(g);

        // Paint the board
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (gameBoard[i][j] == 0) {
                    // Set the color for empty cells
                    g.setColor(Color.BLACK);
                }
                // Draw the cell
                g.fillRect(i * SIZE, j * SIZE, SIZE, SIZE);
            }
            for (Snake snake : s) {
                snake.draw(g);
            }
            food.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
