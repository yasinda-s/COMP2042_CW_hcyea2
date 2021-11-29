package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * The Brick Class refers to a singular brick we see above the game.
 *
 * Refactoring -
 *
 * The Class Crack that was nested into the Brick Class has been refactored to a singular Class outside of Brick so that its purpose is more clear.
 * Added a getter method for the rnd variable (in order to build on encapsulation) so that it can be accessed from the Crack class.
 * The Brick Class consisted of an unused variable called MIN_CRACK, this has been removed to increase understanding and readability
 * of the code.
 *
 */
abstract public class Brick  { //this represents one of the bricks we see on top of the wall in the game

    public static final int DEF_CRACK_DEPTH = 1; //the thickness of the crack on the cement
    public static final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    private static Random rnd;

    private String name;
    Shape brickFace;

    private Color border;
    protected Color inner;

    private int fullStrength;
    protected int strength;

    protected boolean broken;

    /**
     * This is the constructor for Brick Object.
     * @param name Refers to the name of the brick.
     * @param pos The top-left position of the brick.
     * @param size The Dimension of the brick (width and height)
     * @param border The outer color of the brick.
     * @param inner The inner color of the brick.
     * @param strength The strength of the brick (How many impacts it can handle).
     */
    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength){
        rnd = new Random();
        broken = false;
        this.name = name; //XXXX
        brickFace = makeBrickFace(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;
    }

    /**
     * This method is used to make the face of the brick (Rectangle).
     * @param pos The top left position (coordinates) of the brick.
     * @param size The width and height of the brick (Dimension).
     * @return Returns Rectangular Shape of the brick face.
     */
    protected abstract Shape makeBrickFace(Point pos,Dimension size);

    /**
     * This method handles strength reduction of bricks on impact. It also checks if the brick is broken.
     * @param point Refers to the edge of the ball making contact with the brick.
     * @param dir Refers to the direction (edge) of the brick that is making contact with the ball.
     * @return Returns boolean value based on whether the brick makes or not.
     */
    public boolean setImpact(Point2D point , int dir){
        if(broken)
            return false;
        impact(); //if impact, then reduce strength
        return  broken;
    }

    /**
     * Getter for a singular brick.
     * @return Returns the Shape of the brick.
     */
    public abstract Shape getBrick();

    /**
     * Getter for the outlining color of brick.
     * @return Returns the Color of the border of the brick.
     */
    public Color getBorderColor(){
        return  border;
    }

    /**
     * Getter for the inner color of brick.
     * @return Returns the Color of the inside of the brick.
     */
    public Color getInnerColor(){
        return inner;
    }


    /**
     * This method is used to find in which direction the ball hit the brick.
     * @param b Refers to a ball object.
     * @return Returns Int Type, which corresponds to the direction of impact.
     */
    public int findImpact(Ball b){ //see how the ball hit the brick (direction)
        if(broken)
            return 0;
        int out  = 0;
        if(brickFace.contains(b.right)) //ball hit from left
            out = LEFT_IMPACT;
        else if(brickFace.contains(b.left)) //if ball hit from right
            out = RIGHT_IMPACT;
        else if(brickFace.contains(b.up)) //if ball hit from down
            out = DOWN_IMPACT;
        else if(brickFace.contains(b.down)) //if ball hit from up
            out = UP_IMPACT;
        return out;
    }

    /**
     * Method to find if a brick is broken already.
     * @return Returns a boolean value based on the brick's condition.
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * Resets the strength of a brick to its full state.
     */
    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    /**
     * Method used to decrease the strength of a brick on impact.
     */
    public void impact(){
        strength--;
        broken = (strength == 0);
    }

    /**
     * Method used to get the value of rnd for other classes like Crack
     * @return Returns the Random object rnd.
     */
    public static Random getRnd() {
        return rnd;
    }
}