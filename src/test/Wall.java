package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

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

    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){
        //drawArea will later refer to the rectangle where the game is being setup (whole thing)
        //brickCount is the total number of bricks = 30
        //lineCount is 3
        //ratio is 6/2
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

        do{ //set speedX and speedY to random stuff
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

    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //use the position of the ball to make a rubber ball object
    }

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

    public void move(){
        player.move();
        ball.move();
    }

    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY(); //change direction of ball when it hits player bar to go back up
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
             * because for every brick program checks for horizontal and vertical impacts
             */
            brickCount--;
        }
        else if(impactBorder()) {
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

    private boolean impactWall(){ //to handle how ball moves and causes cracks on impact with brick wall
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY(); //reverse Y movement
                    return b.setImpact(ball.down, Brick.Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY(); //reverse Y movement
                    return b.setImpact(ball.up,Brick.Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.right,Brick.Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.left,Brick.Crack.LEFT);
            }
        }
        return false;
    }

    private boolean impactBorder(){ //to handle how ball moves and causes cracks on impact with surrounding wall
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    public int getBrickCount(){
        return brickCount;
    }

    public int getBallCount(){
        return ballCount;
    }

    public boolean isBallLost(){
        return ballLost;
    }

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

    public void wallReset(){ //to bring back wall formation
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    public boolean ballEnd(){
        return ballCount == 0;
    } //returns true if no more balls left

    public boolean isDone(){
        return brickCount == 0;
    } //returns true if all bricks broken

    public void nextLevel(){
        bricks = levels[level++]; //advance in level (increase index)
        this.brickCount = bricks.length;
    }

    public boolean hasLevel(){ //see if user has more levels to complete
        return level < levels.length;
    }

    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    public void resetBallCount(){
        ballCount = 3;
    }

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
