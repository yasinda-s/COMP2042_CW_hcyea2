package test;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * This class is responsible for the general game play of the Brick Game (interactions between components and it keeps track of in game scores).
 *
 * Addition -
 *
 * Added methods and variables which are used to calculate the user's score for the ongoing game. Higher levels give more points for each brick that is broken.
 *
 * Refactoring -
 *
 * The methods that formed the formation of bricks for each level has been removed from this class and created in LevelSetup Class.
 * To set up the wall formation will be the single responsibility of WallSetup.
 *
 * Instead of setting the speed of the ball (for both axes) in the GamePlay constructor, it has been moved to the Ball class's constructor so
 * that the initial speed is randomly assigned from the parent class.
 *
 * In addition, the original code had the same lines of code repeating when the ball was to be reset in the "ballReset" method, I have refactored
 * the code by replacing those lines with one method call "getSpeedsXY" which reassigned the x, y speed of the ball randomly.
 *
 * rnd variable has been removed as the speed of the ball is randomized within the Ball class itself.
 */

public class GamePlay {
    private Rectangle area;
    private Point ballPos;

    Brick[] bricks; //array of bricks
    Ball ball;
    Player player;

    private Brick[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    private int score;
    private int scoreToAdd;
    private int scoreLvlOne;
    private int scoreLvlTwo;
    private int scoreLvlThree;
    private int scoreLvlFour;
    private int scoreLvlFive;
    private int scoreLvlSix;
    private int timePlayed;

    /**
     * This is the constructor for the GamePlay class.
     * @param drawArea drawArea is the Rectangle in which the whole game is being setup.
     * @param brickCount this represents the total number of bricks on the brick wall.
     * @param lineCount this represents the number of brick lines on the wall.
     * @param brickDimensionRatio this represents the height to width ratio of a singular brick.
     */
    public GamePlay(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio){

        WallSetup wallsetup = new WallSetup(drawArea,brickCount,lineCount,brickDimensionRatio);
        //returns a brick[][] object which can be indexed to get one of the 4 levels

        levels = wallsetup.levelsMade;
        level = 0; //original level is 0

        score = 0;
        scoreToAdd = 0;
        scoreLvlOne = 0;
        scoreLvlTwo = 0;
        scoreLvlThree = 0;
        scoreLvlFour = 0;
        scoreLvlFive = 0;
        scoreLvlSix = 0;

        ballCount = 1; //we get 3 lives
        ballLost = false; //originally no balls are lost

        area = drawArea;
    }

    /**
     * This method is used to make the rubber ball object.
     * @param ballPos Represents the Point2D position of the ball.
     */
    public void makeComponents(Point2D ballPos){
        this.startPoint = new Point((Point) ballPos); //takes in the position of the ball to begin with (the player bar is based on this too)
        this.ballPos = (Point) ballPos;
        if(level==5){ //when player plays the game properly and reaches level 5, we change the looks of player and ball, but to get with debug panel it should not be edited here
            player = new SmallPlayer((Point) ballPos.clone(), area);
            ball = new BigBall(ballPos);
        }else{
            player = new Player((Point) ballPos.clone(),150,10, area);
            ball = new RubberBall(ballPos); //use the position of the ball to make a rubber ball object
        }
    }

    /**
     * This method enables both the ball and player bar to move around.
     */
    public void move(){
        player.move();
        ball.move();
    }

    /**
     * This method is used to identify where the ball makes an impact (The player bar, wall, Frame Borders) and how the ball's movement is affected.
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
            calculateScore(level);
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
     * This method is used to calculate the ongoing score of the player for one game.
     * @param level Takes in the level which the users is playing currently.
     */
    public void calculateScore(int level){
        switch (level){
            case 1:
                scoreToAdd = 100 / checkScoreDenominator(getTimePlayed());
                scoreLvlOne += scoreToAdd; //based on how soon you break the bricks in that level as sooner means higher points
                score+=100; //based on how many solid points you gain for each brick in a level (cumulative)
                break;
            case 2:
                scoreToAdd = 200 / checkScoreDenominator(getTimePlayed());
                scoreLvlTwo += scoreToAdd;
                score+=200;
                break;
            case 3:
                scoreToAdd = 300 / checkScoreDenominator(getTimePlayed());
                scoreLvlThree += scoreToAdd;
                score+=300;
                break;
            case 4:
                scoreToAdd = 400 / checkScoreDenominator(getTimePlayed());
                scoreLvlFour += scoreToAdd;
                score+=400;
                break;
            case 5:
                scoreToAdd = 500 / checkScoreDenominator(getTimePlayed());
                scoreLvlFive += scoreToAdd;
                score+=500;
                break;
            case 6:
                scoreToAdd = 600 / checkScoreDenominator(getTimePlayed());
                scoreLvlSix += scoreToAdd;
                score += 600;
                break;
        }
    }

    private int checkScoreDenominator(int timePlayed){
        if(timePlayed<10){
            return 1;
        }else if(timePlayed>10 && timePlayed<30){
            return 2;
        }else if(timePlayed>30 && timePlayed<50){
            return 3;
        }else{
            return 4;
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
     * Getter for the number of bricks available on the Wall above.
     * @return Returns the number of bricks available on the wall above.
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
        //same function called from ball, not efficient to rewrite the same LOC, so we recall the method in Ball class.
        int[] speedsXY = ball.getSpeedsXY();
        ball.setSpeed(speedsXY[0],speedsXY[1]);
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
        ballCount = 1;
    }

    /**
     * This method is used to increment the time the user had played the game.
     */
    public void incrementTime(){
        timePlayed++;
    }

    /**
     * This method is used to access the user's time played from any class.
     * @return Returns the time in seconds.
     */
    public int getTimePlayed() {
        int seconds = timePlayed/100;
        return seconds;
    }

    /**
     * This method is used as a setter to set the time the user had played the game.
     * @param timePlayed The time the user had played the game.
     */
    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    /**
     * This method is used to get the user's current score.
     * @return Int type score of user.
     */
    public int getScore() {
        return score;
    }

    /**
     * This method is used to set the user's score.
     * @param score New score to set score to.
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void skipLevel(){
        ballReset();
        wallReset();
        nextLevel();
        ballReset();
        setScore(0);
        System.out.println("Current Level - " + level);
    }

    public int getLevel() {
        return level;
    }

    public int getScoreLvlOne() {
        return scoreLvlOne;
    }

    public int getScoreLvlTwo() {
        return scoreLvlTwo;
    }

    public int getScoreLvlThree() {
        return scoreLvlThree;
    }

    public int getScoreLvlFour() {
        return scoreLvlFour;
    }

    public int getScoreLvlFive() {
        return scoreLvlFive;
    }

    public int getScoreLvlSix() {return scoreLvlSix;}

}