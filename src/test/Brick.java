package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * The Brick Class refers to a singular brick we see above the game.
 */

abstract public class Brick  { //this represents all the bricks we see above

    public static final int MIN_CRACK = 1;
    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    /**
     *  The Crack Class focuses on the cracks that are placed on the bricks (Cement)
     */
    public class Crack{ //**class inside abstract class

        private static final int CRACK_SECTIONS = 3;
        private static final double JUMP_PROBABILITY = 0.7;

        public static final int LEFT = 10;
        public static final int RIGHT = 20;
        public static final int UP = 30;
        public static final int DOWN = 40;
        public static final int VERTICAL = 100;
        public static final int HORIZONTAL = 200;

        private GeneralPath crack; //generalPath type called crack
        private int crackDepth;
        private int steps;

        /**
         * This is a constructor that represents a singular crack we see in a brick after the ball hits it.
         * @param crackDepth
         * @param steps This refers to the number of steps that are formed in a singular crack on a brick.
         */

        public Crack(int crackDepth, int steps){ //constructor of Crack class
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
            Rectangle bounds = Brick.this.brickFace.getBounds(); //top left coordinate of brick it knocked with w and h

            Point impact = new Point((int)point.getX(),(int)point.getY()); //x, y coordinates on where the ball hit the brick
            Point start = new Point(); //start is LB for crack
            Point end = new Point(); //end is UB for crack

            switch(direction){ //where the ball hits brick
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y); //to blank object you assign top left x and y.
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
         * Used to generate the design of the crack in the brick and height of steps after calling randomInBounds().
         * @param start Refers to the Point of actual impact.
         * @param end Refers to the Point that was found randomly.
         */
        protected void makeCrack(Point start, Point end){ //design of the crack

            GeneralPath path = new GeneralPath();

            path.moveTo(start.x,start.y);

            double w = (end.x - start.x) / (double)steps;
            double h = (end.y - start.y) / (double)steps;

            int bound = crackDepth;
            int jump  = bound * 5;

            double x,y;

            for(int i = 1; i < steps;i++){

                x = (i * w) + start.x;
                y = (i * h) + start.y + randomInBounds(bound);

                if(inMiddle(i,CRACK_SECTIONS,steps)) //no use
                    y += jumps(jump,JUMP_PROBABILITY);

                path.lineTo(x,y);
            }

            path.lineTo(end.x,end.y);
            crack.append(path,true);
        }

        /**
         * Used to randomize the height of the steps in the crack.
         * @param bound Int type, to get affected by randomness.
         * @return Randomized Int.
         */
        private int randomInBounds(int bound){
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound;
        }

        /**
         * XXXX
         * @param i
         * @param steps
         * @param divisions
         * @return
         */
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions); //3 / 35 == 0
            int up = low * (divisions - 1); //0

            return  (i > low) && (i < up); //always false?
        }

        /**
         * XXXX
         * @param bound
         * @param probability
         * @return
         */
        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;
        }

        /**
         * This method is used to create a random location based on the actual impact of the ball on the brick.
         * @param from The lower bound of the actual crack.
         * @param to The upper bound of the actual crack.
         * @param direction The direction in which the crack will be formed.
         * @return
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

    private static Random rnd;

    private String name;
    Shape brickFace;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private boolean broken;

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
        this.name = name;
        brickFace = makeBrickFace(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;
    }

    /**
     * This method is used to make the face of the brick (Rectangle).
     * @param pos The top left position (coordinates) of the brick.
     * @param size The width and height of the brick (Dimension).
     * @return
     */
    protected abstract Shape makeBrickFace(Point pos,Dimension size);

    /**
     * This method handles strength reduction of bricks on impact. It also checks if the brick is broken.
     * @param point Refers to the edge of the ball making contact with the brick.
     * @param dir Refers to the direction (edge) of the brick that is making contact with the ball.
     * @return Returns boolean value based on whether the brick makes or not.
     */
    public  boolean setImpact(Point2D point , int dir){
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
    public final int findImpact(Ball b){ //see how the ball hit the brick (direction)
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

}