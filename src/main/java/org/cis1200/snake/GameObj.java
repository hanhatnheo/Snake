package org.cis1200.snake;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * An object in the game.
 *
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private int px;
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;

    /**
     * Constructor
     */
    public GameObj(
            int px, int py, int width, int height, int courtWidth,
            int courtHeight
    ) {
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    /***
     * GETTERS
     **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**************************************************************************
     * SETTERS
     **************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }


    /**************************************************************************
     * UPDATES AND OTHER METHODS
     **************************************************************************/

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its length/size. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void move(ArrayList<Snake> s) {
        Snake head = s.get(0);
        if (head.getDirection() == Direction.UP) {
            s.add(0, new Snake(head.getPx(), head.getPy() - 10, maxX + 10, maxY + 10,
                    Color.CYAN, Direction.UP));
            s.remove(s.size() - 1);
        } else if (head.getDirection() == Direction.DOWN) {
            s.add(0, new Snake(head.getPx(), head.getPy() + 10, maxX + 10, maxY + 10,
                    Color.CYAN, Direction.DOWN));
            s.remove(s.size() - 1);
        } else if (head.getDirection() == Direction.LEFT) {
            s.add(0, new Snake(head.getPx() - 10, head.getPy(), maxX + 10, maxY + 10,
                    Color.CYAN, Direction.LEFT));
            s.remove(s.size() - 1);
        } else if (head.getDirection() == Direction.RIGHT) {
            s.add(0, new Snake(head.getPx() + 10, head.getPy(), maxX + 10, maxY + 10,
                    Color.CYAN, Direction.RIGHT));
            s.remove(s.size() - 1);
        }
    }

    /**
     * Determine whether this game object is currently intersecting another
     * object.
     *
     * Intersection is determined by comparing bounding boxes. If the bounding
     * boxes overlap, then an intersection is considered to occur.
     *
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(GameObj that) {
        return (this.px + this.width > that.px
                && this.py + this.height > that.py
                && that.px + that.width > this.px
                && that.py + that.height > this.py);
    }

    public boolean hitBody(ArrayList<Snake> s) {
        boolean flag = false;
        for (int i = 1; i < s.size(); i++) {
            if (s.get(0).intersects(s.get(i))) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    /**
     * Determine whether the game object will hit a wall in the next time step.
     * If so, return the direction of the wall in relation to this game object.
     *
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.px < 0) {
            return Direction.LEFT;
        } else if (this.px > this.maxX) {
            return Direction.RIGHT;
        }

        if (this.py < 0) {
            return Direction.UP;
        } else if (this.py > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    public boolean haveHitWall() {
        return hitWall() == Direction.UP || hitWall() == Direction.LEFT
                || hitWall() == Direction.DOWN || hitWall() == Direction.RIGHT;
    }

    /**
     * Default draw method that provides how the object should be drawn in the
     * GUI. This method does not draw anything. Subclass should override this
     * method based on how their object should appear.
     *
     * @param g The <code>Graphics</code> context used for drawing the object.
     *          Remember graphics contexts that we used in OCaml, it gives the
     *          context in which the object should be drawn (a canvas, a frame,
     *          etc.)
     */
    public abstract void draw(Graphics g);
}
