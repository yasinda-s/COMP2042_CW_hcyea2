package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * This class is responsible for the impacts between game components and how they affect certain scores.
 */

public class Wall {

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
     * This is the constructor for the Wall class.
     * @param drawArea drawArea is the Rectangle in which the whole game is being setup.
     * @param brickCount this represents the total number of bricks on the brick wall.
     * @param lineCount this represents the number of brick lines on the wall.
     * @param brickDimensionRatio this represents the height to width ratio of a singular brick.
     * @param ballPos this represents the position of the ball.
     */
    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){
        this.startPoint = new Point(ballPos); //takes in the position of the ball to begin with (the player bar is based on this too)

        LevelSetUp levelsetup = new LevelSetUp(drawArea,brickCount,lineCount,brickDimensionRatio);
        //returns a brick[][] object which can be indexed to get one of the 4 levels

        levels = levelsetup.levelsMade;

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
     * This method is used to make the rubber ball object.
     * @param ballPos Represents the Point2D position of the ball.
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos); //use the position of the ball to make a rubber ball object
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
     * This is to see if the ball had gone beyond the range of the border.
     * @return Returns true if the ball had gone beyond the range of the border.
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
}