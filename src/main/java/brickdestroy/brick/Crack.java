package brickdestroy.brick;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 *  The Crack Class focuses on the cracks that are placed on the bricks (Cement)
 *
 *  Refactoring -
 *
 *  Removed "inMiddle" and "jumps" method. Both these methods do not contribute significantly towards the path or design of the crack and it makes the understanding of the crack pattern much more confusing.
 *  By removing these two methods and the variables CRACK_SECTIONS and JUMP_PROBABILITY (which were used by the deleted methods) the class is now more concise and holds the similar functionality that is easier to understand.
 */

public class Crack {

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;
    public static final int VERTICAL = 100;
    public static final int HORIZONTAL = 200;

    private final Brick brick;
    private GeneralPath crack; //generalPath type called crack
    private int crackDepth;
    private int steps;
    private static Random rnd;

    /**
     * This is a constructor that represents a singular crack we see in a brick after the ball hits it.
     * @param crackDepth This refers to the thickness of a crack on cement.
     * @param steps This refers to the number of steps that are formed in a singular crack on a brick.
     */
    public Crack(Brick brick, int crackDepth, int steps){
        rnd = new Random();
        this.brick = brick; //constructor of Crack class
        crack = new GeneralPath(); //crack assigned generalpath object
        this.crackDepth = crackDepth;
        this.steps = steps;
    }

    /**
     * This method draws the cracks in each brick.
     * @return GeneralPath crack to be drawn.
     */
    public GeneralPath draw(){ //returns object generalpath (crack initialized before)
        return crack;
    }

    /**
     * This method undoes any cracks formed.
     */
    public void reset(){
        crack.reset();
    } //used to remove any geometric shape drawn using general path

    /**
     * This method is used to create the locations of the cracks in the brick and get the design by calling makeCrack().
     * @param point Point2D type, refers to the actual point of impact.
     * @param direction Int type, refers to the direction in which the ball hits the brick.
     */
    protected void makeCrack(Point2D point, int direction){ //makes random point to form the crack
        Rectangle bounds = brick.getBrickFace().getBounds(); //top left coordinate of brick it knocked with w and h
        Point impact = new Point((int)point.getX(),(int)point.getY()); //x, y coordinates on where the ball hit the brick
        Point start = new Point(); //start is LB for crack
        Point end = new Point(); //end is UB for crack

        switch(direction){ //where the ball hits brick
            case LEFT:
                start.setLocation(bounds.x + bounds.width, bounds.y); //to blank point object you assign top right x and y.
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                Point tmp = makeRandomPoint(start,end,VERTICAL); //tmp is the random point
                makeCrack(impact,tmp); //actual point of impact
                break;

            case RIGHT:
                start.setLocation(bounds.getLocation()); //set start to top left of brick
                end.setLocation(bounds.x, bounds.y + bounds.height); //set end to top left of brick plus height
                tmp = makeRandomPoint(start,end,VERTICAL); //some point in between, we make a crack
                makeCrack(impact,tmp);
                break;

            case UP:
                start.setLocation(bounds.x, bounds.y + bounds.height);
                end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                tmp = makeRandomPoint(start,end,HORIZONTAL);
                makeCrack(impact,tmp);
                break;

            case DOWN:
                start.setLocation(bounds.getLocation());
                end.setLocation(bounds.x + bounds.width, bounds.y);
                tmp = makeRandomPoint(start,end,HORIZONTAL);
                makeCrack(impact,tmp);
                break;
        }
    }

    /**
     * Used to generate the design (randomly generated path) of the crack in the brick from start to end. It also allocates height of steps after calling randomInBounds().
     * @param start Refers to the Point of actual impact.
     * @param end Refers to the Point that was found randomly.
     */
    protected void makeCrack(Point start, Point end){ //design of the crack

        GeneralPath path = new GeneralPath();

        path.moveTo(start.x,start.y);

        double w = (end.x - start.x) / (double)steps;
        double h = (end.y - start.y) / (double)steps;

        int bound = crackDepth;

        double x,y;

        for(int i = 1; i < steps;i++){

            x = (i * w) + start.x;
            y = (i * h) + start.y + randomInBounds(bound);

            path.lineTo(x,y);
        }

        path.lineTo(end.x,end.y);
        crack.append(path,true);
    }

    /**
     * Used to randomize the height of the steps in the crack.
     * @param bound Int type, to get affected by randomness.
     * @return Randomized Int subtracted by bound.
     */
    private int randomInBounds(int bound){
        int n = (bound * 2) + 1;
        return rnd.nextInt(n) - bound;
    }

    /**
     * This method is used to create a random location based on the actual impact of the ball on the brick.
     * @param from The lower bound of the actual crack.
     * @param to The upper bound of the actual crack.
     * @param direction The direction in which the crack will be formed.
     * @return Point type, of random point between from and to.
     */
    private Point makeRandomPoint(Point from,Point to, int direction){

        Point out = new Point();
        int pos;

        switch(direction){
            case HORIZONTAL:
                pos = rnd.nextInt(to.x - from.x) + from.x;
                out.setLocation(pos,to.y);
                break;
            case VERTICAL:
                pos = rnd.nextInt(to.y - from.y) + from.y;
                out.setLocation(to.x,pos);
                break;
        }
        return out;
    }

}
