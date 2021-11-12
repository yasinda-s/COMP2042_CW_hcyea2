package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * This class sets up the Wall (collection of bricks), PLayer Bar and Ball we see in game.
 */

public class Wall {

    private static final int LEVELS_COUNT = 4; //number of levels in the game

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;

    private Random rnd;
    private Rectangle area;

    Brick[] bricks; //array of bricks
    Ball ball;
    Player player;

    private Brick[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    /**
     * This is the constructor for the Wall class. XXXX
     * @param drawArea drawArea is the Rectangle in which the whole game is being setup.
     * @param brickCount this represents the total number of bricks on the brick wall.
     * @param lineCount this represents the XXXX
     * @param brickDimensionRatio this represents the height to width ratio of a singular brick.
     * @param ballPos this represents the position of the ball.
     */
    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){
        //drawArea will later refer to the rectangle where the game is being setup (whole thing)
        //brickCount is the total number of bricks = 30
        //lineCount is 3
        //ratio is 6/2  - height to width ratio
        //ballPos is original point of ball

        this.startPoint = new Point(ballPos); //takes in the position of the ball to begin with (the player bar is based on this too)

        levels = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio);
        //returns a brick[][] object which can be indexed to get one of the 4 levels

        level = 0; //original level is 0

        ballCount = 3; //we get 3 lives
        ballLost = false; //originally no balls are lost

        rnd = new Random(); //create an object of random

        makeBall(ballPos); //take in the initial ball coordinates and make rubber ball object

        int speedX,speedY;

        do{ //set speedX and speedY to random speeds
            speedX = rnd.nextInt(5) - 2;
        } while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        } while(speedY == 0);

        ball.setSpeed(speedX,speedY); //set speed of ball

        player = new Player((Point) ballPos.clone(),150,10, drawArea);
        //make the player bar by referring to the ball position (center)
        //drawArea refers to the whole screen where the game is being played
        area = drawArea;

    }

    /**
     * This method is used to form the Brick Wall for a particular level where only one type of Brick is used.
     * @param drawArea drawArea is the Rectangle in which the level is being setup.
     * @param brickCnt this represents the total number of bricks on the brick wall.
     * @param lineCnt XXXX
     * @param brickSizeRatio this represents the height to width ratio of a singular brick.
     * @param type this Integer represents the corresponding level we want to set up.
     * @return Returns an array of type Brick to setup the wall.
     */
    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        //returns Brick[], not Brick[][]
        //brickCnt - total number of bricks (30)
        //lineCnt - total number of lines (3)
        //brickSizeRatio - 6/2
        //type - 1-4, which refers to type of brick

        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */

        brickCnt -= brickCnt % lineCnt; //see how many bricks we can use

        int brickOnLine = brickCnt / lineCnt; //how many bricks on one line

        double brickLen = drawArea.getWidth() / brickOnLine; //length of one brick
        //divide width of whole frame by 10 to get size of one brick

        double brickHgt = brickLen / brickSizeRatio; //height of one brick

        brickCnt += lineCnt / 2; //30 += 3/2 => 31

        Brick[] tmp  = new Brick[brickCnt]; //make array to store brick objects, stores 31 bricks in array

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt); //width and height
        //dimension encapsulates width and height of object in one object

        Point p = new Point();

        int i;

        //below is to see if we lay full size bricks or half size bricks

        for(i = 0; i < tmp.length; i++){ //going from 0 to number of bricks in array
            int line = i / brickOnLine;
            if(line == lineCnt) //if line == 3 (so when i = 30) which is the last iteration
                break;
            double x = (i % brickOnLine) * brickLen; //before i==30, do all these
            //x will have a value until i>=11 (then multiply by length of brick)
            //brickLen is length of one brick
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            //x only holds x (true) if i == 20 so that 20/10 is 2, else it holds x - (bricklen/2)
            double y = (line) * brickHgt;
            //y will only have a value as long as i>=11, else its 0
            p.setLocation(x,y); //use x, y to set coordinates to point p
            tmp[i] = makeBrick(p,brickSize,type); //then we pass the p, size of brick, and type of brick to make the brick at the point p
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){  //setting bricks to clay type
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = new ClayBrick(p,brickSize); //set this brick to clay
        }
        return tmp;

    }

    /**
     * This method is used to form the Brick Wall for a particular level where two types of Bricks are being used.
     * @param drawArea drawArea is the Rectangle in which the level is being setup.
     * @param brickCnt this represents the total number of bricks on the brick wall.
     * @param lineCnt XXXX
     * @param brickSizeRatio this represents the height to width ratio of a singular brick.
     * @param typeA this represents the first type of Brick being used.
     * @param typeB this represents the second type of Brick being used.
     * @return Returns an array of Bricks with the different types of bricks to be set up as the Wall.
     */
    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        //return Brick[] type
        //called when you have more than one type of brick

        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */

        //same definitions as for first level
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / 2 - 1;
        int centerRight = brickOnLine / 2 + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            //based on above condition, set the brick types
            tmp[i] = b ?  makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
            //typeA refers to first type passed and B refers to second
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
            //by default have first passed bricks as default
        }
        return tmp;
    }

    /**
     * This method is used to make the rubber ball object.
     * @param ballPos Represents the Point2D position of the ball.
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //use the position of the ball to make a rubber ball object
    }

    /**
     * This method initializes all of the different levels we have in the game.
     * @param drawArea drawArea is the Rectangle in which the level is being setup.
     * @param brickCount this represents the total number of bricks on the brick wall.
     * @param lineCount XXXX
     * @param brickDimensionRatio this represents the height to width ratio of a singular brick.
     * @return Returns Brick[][] type which consists of all the different levels we have in the game which can be accessed by indexing.
     */
    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        //this returns a Brick[][]
        Brick[][] tmp = new Brick[LEVELS_COUNT][];
        //tmp is Brick[][]
        //tmp[0] is (Brick[][])[0]
        //for each level we set a different set of params based on what we want to edit (the brick type)
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        return tmp;
    }

    /**
     * This method enables both the ball and player bar to move around.
     */
    public void move(){
        player.move();
        ball.move();
    }

    /**
     * This method is used to identify where the ball makes an impact (The player bar, Wall, Frame Borders) and how the ball's movement is affected.
     */
    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY(); //change direction of ball when it hits player bar to go back up -reflection along Y axis
        }
        else if(impactWall()){ //how ball reacts and causes cracks on the wall
            /*for efficiency reverse is done into method impactWall
             * because for every brick program checks for horizontal and vertical impacts
             */
            brickCount--;
        }
        else if(impactBorder()) { //if the ball hits the corner walls (left and right)
            ball.reverseX();
        }
        else if(ball.getPosition().getY() < area.getY()){ //if ball hits top frame
            ball.reverseY();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){ //if ball goes out (below)
            ballCount--;
            ballLost = true;
        }
    }

    /**
     * This method is used to understand the ball movement and how it causes cracks on the bricks on impact.
     * @return Returns a boolean value depending on whether the ball made impact with the wall or not (true if impact, else false).
     */
    private boolean impactWall(){ //to handle how ball moves and causes cracks on impact with brick wall
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY(); //reverse Y movement
                    return b.setImpact(ball.down, Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY(); //reverse Y movement
                    return b.setImpact(ball.up, Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.right, Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.left, Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * XXXX
     * @return
     */
    private boolean impactBorder(){ //to handle how ball moves and causes cracks on impact with surrounding wall
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /**
     * Getter for the number of bricks available on the Wall.
     * @return Returns the number of bricks available on the Wall.
     */
    public int getBrickCount(){
        return brickCount;
    }

    /**
     * Getter for the number of balls available for the level.
     * @return Returns Int, the number of balls available for the level.
     */
    public int getBallCount(){
        return ballCount;
    }

    /**
     * Method to identify if a ball was lost in a level.
     * @return Returns true if ball was lost, else false.
     */
    public boolean isBallLost(){
        return ballLost;
    }

    /**
     * This method is used to respawn a ball and re-randomize the balls speed when a ball is lost.
     */
    public void ballReset(){ //what happens when ball is lost
        player.moveTo(startPoint);
        ball.moveTo(startPoint);
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);
        ballLost = false;
    }

    /**
     * This method is used to Reset the wall structure and form the bricks again, it also resets the number of balls to its original value.
     */
    public void wallReset(){ //to bring back wall formation
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    /**
     * This method is used to check if all the balls are lost.
     * @return Returns true if all balls are lost, else returns false.
     */
    public boolean ballEnd(){
        return ballCount == 0;
    } //returns true if no more balls left

    /**
     * This method is used to check if all the bricks are broken.
     * @return Returns true if all bricks are broken, else returns false.
     */
    public boolean isDone(){
        return brickCount == 0;
    } //returns true if all bricks broken

    /**
     * This method is used to advance to the next level.
     */
    public void nextLevel(){
        bricks = levels[level++]; //advance in level (increase index)
        this.brickCount = bricks.length;
    }

    /**
     * This method is used to check if there are more levels left to advance.
     * @return Returns true if there are more levels available, else returns false.
     */
    public boolean hasLevel(){ //see if user has more levels to complete
        return level < levels.length;
    }

    /**
     * Setter to set the speed of the ball in X-axis.
     * @param s refers to the speed of ball we want to set in X-axis.
     */
    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    /**
     * Setter to set the speed of the ball in Y-axis.
     * @param s refers to the speed of ball we want to set in Y-axis.
     */
    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    /**
     * This method is used to reset the ball count for a given level back to its original value.
     */
    public void resetBallCount(){
        ballCount = 3;
    }

    /**
     * This method is used to make any type of brick for the levels.
     * @param point Represents the top left coordinate of the rectangle (Brick).
     * @param size Represents the width and height of the Brick.
     * @param type Represents the type of brick being used (Clay, Cement, SteeL).
     * @return Returns a Brick type to be created.
     */
    private Brick makeBrick(Point point, Dimension size, int type){ //for making new bricks/brick types
        Brick out;
        switch(type){
            case CLAY:
                out = new ClayBrick(point,size);
                break;
            case STEEL:
                out = new SteelBrick(point,size);
                break;
            case CEMENT:
                out = new CementBrick(point, size);
                break;
            default:
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
        return  out;
    }

}
