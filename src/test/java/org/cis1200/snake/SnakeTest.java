package org.cis1200.snake;
import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class SnakeTest {
    Snake snake1 = new Snake(20, 20, 300, 300, Color.GREEN, Direction.LEFT);
    Snake snake2 = new Snake(30, 20, 300, 300, Color.GREEN, Direction.LEFT);
    Snake snake3 = new Snake(50, 70, 300, 300, Color.GREEN, Direction.RIGHT);
    ArrayList<Snake> s = new ArrayList<>();

    @Test
    public void eatFood() {
        s.add(snake1);
        s.add(snake2);
        Food food = new FoodEgg(300, 300, s);
        food.setPx(30);
        food.setPy(40);
        assertFalse(s.get(0).intersects(food));
        Food food2 = new FoodApple(300, 300, s);
        food2.setPx(20);
        food2.setPy(20);
        assertTrue(s.get(0).intersects(food2));
    }

    @Test
    public void changeDirection() {
        //Make sure that snake will not change direction if given opposite direction
        s.add(snake1);
        s.add(snake2);
        s.get(0).changeDirection(Direction.RIGHT);
        assertEquals(s.get(0).getDirection(), Direction.LEFT);
        s.get(0).changeDirection(Direction.UP);
        assertEquals(s.get(0).getDirection(), Direction.UP);
    }

    @Test
    public void snakeMove() {
        s.add(snake1);
        s.add(snake2);
        s.get(0).move(s); //move to the left
        assertEquals(10, s.get(0).getPx());
        assertEquals(20, s.get(0).getPy());
        assertEquals(20, s.get(1).getPx());
        assertEquals(20, s.get(1).getPy());
    }

    @Test
    public void moveKeepSize() {
        s.add(snake1);
        s.add(snake2);
        s.get(0).move(s);
        assertEquals(2, s.size());
    }

    @Test
    public void snakeMoveOutOfBounds() {
        s.add(snake1);
        s.get(0).move(s);
        s.get(0).move(s);
        assertEquals(0, s.get(0).getPx());
        assertEquals(20, s.get(0).getPy());
        s.get(0).move(s);
        assertEquals(-10, s.get(0).getPx());
        assertTrue(s.get(0).haveHitWall());
        assertEquals(20, s.get(0).getPy());
    }

    @Test
    public void setEdgeCases() {
        Food food = new FoodEgg(300, 300, s);
        food.setPx(350);
        food.setPy(-5);
        assertEquals(290, food.getPx());
        assertEquals(0, food.getPy());
    }

    @Test
    public void setEdgeCases2() {
        Food food = new FoodEgg(300, 300, s);
        food.setPx(-20);
        food.setPy(400);
        assertEquals(0, food.getPx());
        assertEquals(290, food.getPy());
    }

    @Test
    public void changeDirectionAndMove() {
        s.add(snake1);
        s.add(snake2);
        s.get(0).changeDirection(Direction.UP);
        s.get(0).move(s);
        assertEquals(10, s.get(0).getPy());
        assertEquals(20, s.get(0).getPx());
    }

    @Test
    public void hitBody() {
        Snake snake4 = new Snake(70, 60, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake5 = new Snake(70, 70, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake6 = new Snake(70, 80, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake7 = new Snake(60, 80, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake8 = new Snake(50, 80, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake9 = new Snake(40, 80, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake10 = new Snake(30, 80, 100, 100, Color.CYAN, Direction.LEFT);
        s.add(snake4);
        s.add(snake5);
        s.add(snake6);
        s.add(snake7);
        s.add(snake8);
        s.add(snake9);
        s.add(snake10);
        s.get(0).changeDirection(Direction.DOWN);
        s.get(0).move(s);
        s.get(0).move(s);
        assertTrue(s.get(0).hitBody(s));
    }

    @Test
    public void hitLeftWall() {
        s.add(snake1);
        s.add(snake2);
        s.get(0).move(s);
        s.get(0).move(s);
        s.get(0).move(s);
        assertEquals(Direction.LEFT, s.get(0).hitWall());
    }

    @Test
    public void hitUpperWall() {
        s.add(snake1);
        s.add(snake2);
        s.get(0).changeDirection(Direction.UP);
        s.get(0).move(s);
        s.get(0).move(s);
        s.get(0).move(s);
        assertEquals(Direction.UP, s.get(0).hitWall());
    }

    @Test
    public void hitLowerWall() {
        Snake snake4 = new Snake(80, 80, 100, 100, Color.CYAN, Direction.LEFT);
        Snake snake5 = new Snake(70, 80, 100, 100, Color.CYAN, Direction.LEFT);
        s.add(snake4);
        s.add(snake5);
        s.get(0).changeDirection(Direction.DOWN);
        s.get(0).move(s);
        s.get(0).move(s);
        s.get(0).move(s);
        assertEquals(Direction.DOWN, s.get(0).hitWall());
    }

    @Test
    public void hitRightWall() {
        Snake snake4 = new Snake(80, 80, 100, 100, Color.CYAN, Direction.UP);
        Snake snake5 = new Snake(70, 80, 100, 100, Color.CYAN, Direction.UP);
        s.add(snake4);
        s.add(snake5);
        s.get(0).changeDirection(Direction.RIGHT);
        s.get(0).move(s);
        s.get(0).move(s);
        s.get(0).move(s);
        assertEquals(Direction.RIGHT, s.get(0).hitWall());
    }

    @Test
    public void hitCorner() {
        Snake snake4 = new Snake(90, 90, 100, 100, Color.CYAN, Direction.RIGHT);
        Snake snake5 = new Snake(80, 90, 100, 100, Color.CYAN, Direction.RIGHT);
        s.add(snake4);
        s.add(snake5);
        s.get(0).move(s);
        s.get(0).move(s);
        assertTrue(s.get(0).haveHitWall());
    }

    @Test
    public void testHasNextAndNext() {
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testNullFileError() {
        assertThrows(IllegalArgumentException.class, () -> new FileLineIterator(null));
    }

    @Test
    public void testFileNotFoundError() {
        assertThrows(FileNotFoundException.class, () -> new FileLineIterator(
                new BufferedReader(new FileReader("./file/highscore.txt"))));
    }

    @Test
    public void testNoSuchElementException() {
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        li.next();
        li.next();
        assertThrows(NoSuchElementException.class, li::next);
    }

    @Test
    public void testHasNext() {
        String phrase = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(phrase);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
    }

    @Test
    public void testStringEmpty() {
        String empty = "";
        StringReader sr = new StringReader(empty);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertFalse(li.hasNext());
    }

    @Test
    public void encapsulationGameObj() {
        s.add(snake3);
        s.add(snake1);
        s.get(0).setPx(80);
        s.get(1).setPy(80);
        assertEquals(70, s.get(0).getPy());
        assertEquals(20, s.get(1).getPx());
    }

    @Test
    public void allocate() {
        JLabel j = new JLabel();
        JLabel j2 = new JLabel();
        Board b = new Board(j, j2);
        s.add(snake1);
        s.add(snake2);
        Food f = new FoodEgg(300, 300, s);
        b.allocate();
        Food a = new FoodApple(300, 300, s);
        a.allocateFood();
        assertFalse(f.haveHitWall());
        assertFalse(f.hitBody(s));
        assertFalse(a.haveHitWall());
        assertFalse(a.hitBody(s));
    }

    @Test
    public void changeColor() {
        snake1.changeColor(Color.PINK);
        assertEquals(Color.PINK, snake1.getColor());
    }

    @Test
    public void foodEgg() {
        s.add(snake1);
        s.add(snake2);
        Food egg = new FoodEgg(300, 300, s);
        assertEquals(-3, egg.getDelay());
    }

    @Test
    public void foodApple() {
        s.add(snake1);
        s.add(snake2);
        Food apple = new FoodApple(300, 300, s);
        assertEquals(5, apple.getDelay());
    }

    @Test
    public void foodAppleDoesNotChangeColor() {
        s.add(snake1);
        s.add(snake2);
        Food apple = new FoodApple(300, 300, s);
        apple.changeColor(); //will not work because does not intersect
        assertEquals(Color.GREEN, s.get(0).getColor());
    }

    @Test
    public void foodAppleChangeColor() {
        s.add(snake1);
        s.add(snake2);
        Food apple = new FoodApple(300, 300, s);
        apple.setPx(20);
        apple.setPy(20);
        apple.changeColor(); //intersects
        assertEquals(Color.PINK, s.get(0).getColor());
    }

    @Test
    public void foodEggChangeColor() {
        s.add(snake1);
        s.add(snake2);
        Food egg = new FoodEgg(300, 300, s);
        egg.setPx(50);
        assertEquals(Color.GREEN, s.get(0).getColor());
        egg.setPx(20);
        egg.setPy(20);
        egg.changeColor(); //intersects
        assertEquals(Color.YELLOW, s.get(0).getColor());
    }


}
