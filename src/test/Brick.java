package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

abstract public class Brick  { //this represents all the bricks we see above

    public static final int MIN_CRACK = 1;
    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;

    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

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

        public Crack(int crackDepth, int steps){ //constructor of Crack class
            crack = new GeneralPath(); //crack assigned generalpath object
            this.crackDepth = crackDepth;
            this.steps = steps;
        }

        public GeneralPath draw(){ //returns object generalpath (crack initialized before)
            return crack;
        }

        public void reset(){
            crack.reset();
        } //used to remove any geometric shape drawn using general path

        protected void makeCrack(Point2D point, int direction){ //makes random point to form the crack
            Rectangle bounds = Brick.this.brickFace.getBounds(); //top left coordinate of brick it knocked with w and h

            Point impact = new Point((int)point.getX(),(int)point.getY()); //x, y coordinates on where the ball hit the brick
            Point start = new Point(); //start is LB for crack
            Point end = new Point(); //end is UB for crack


            switch(direction){ //where the ball hits brick
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y); //to blank object you assign top left x and y.
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height); //
                    Point tmp = makeRandomPoint(start,end,VERTICAL);
                    makeCrack(impact,tmp);

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

        private int randomInBounds(int bound){
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound;
        }

        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions); //3 / 35 == 0
            int up = low * (divisions - 1); //0

            return  (i > low) && (i < up); //always false?
        }

        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;
        }

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

    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength){
        rnd = new Random();
        broken = false;
        this.name = name;
        brickFace = makeBrickFace(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;
    }

    protected abstract Shape makeBrickFace(Point pos,Dimension size);

    public  boolean setImpact(Point2D point , int dir){
        if(broken)
            return false;
        impact(); //if impact, then reduce strength
        return  broken;
    }

    public abstract Shape getBrick();

    public Color getBorderColor(){
        return  border;
    }

    public Color getInnerColor(){
        return inner;
    }

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

    public final boolean isBroken(){
        return broken;
    }

    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    public void impact(){
        strength--;
        broken = (strength == 0);
    }

}