package brickdestroy.player;

import brickdestroy.ball.Ball;

import java.awt.*;

/**
 * This class represents the Player's bar (Rectangle) which he/she controls.
 *
 * Additions -
 * Added "changeWidth" method which is used in level 5 after drop box is picked.
 * Added "resetWidth" method which is used on reset.
 * Added "increaseWidth" method which is used by all levels as a reward system.
 * Added "halveWidth" method which is used in level 5 as a penalty system.
 * Added getter methods to improve encapsulation.
 */
public class Player{ //refers to the player bar

    public static final Color BORDER_COLOR = Color.GREEN.darker().darker(); //outer border
    public static final Color INNER_COLOR = Color.ORANGE; //inner border

    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point ballPoint;
    private int moveAmount;
    private int min;
    private int max;
    private Rectangle container;

    /**
     * This is the constructor for the Player object which refers to the bar he/she controls in-game.
     * @param ballPoint Refers to the position of the ball.
     * @param width Refers to the width of the player ball.
     * @param height Refers to the height of the player ball.
     * @param container Refers to the whole screen used for the game.
     */
    public Player(Point ballPoint,int width,int height,Rectangle container){
        //container refers to the whole box used for the game
        this.ballPoint = ballPoint; //random point object for now, later initial pos of ball
        moveAmount = 0;
        playerFace = makeRectangle(width, height); //face of the player is made after this method which takes in w, h
        //ballPoint refers to the coordinates of the ball
        min = container.x + (width / 2);
        //container.x refers to the top left of the whole box
        max = min + container.width - width;
        this.container = container;
    }

    /**
     * This method makes the rectangle of the player bar.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @return Returns the Rectangle object of the player bar.
     */
    public Rectangle makeRectangle(int width, int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
        return new Rectangle(p,new Dimension(width,height)); //return top left coordinate of point P with w and h passed.
    }

    /**
     * Method to see if Ball made impact with the Player bar.
     * @param b The object of the ball to see if it made impact with the player bar.
     * @return Returns a boolean value if the player bar is touching the bottom of the ball.
     */
    public boolean impact(Ball b){
        return playerFace.contains(b.getPosition()) && playerFace.contains(b.getDown()) ;
    }

    /**
     * This method is used to move the player bar around in game when we press the keys.
     */
    public void move(){ //to move the player brick
        double x = ballPoint.getX() + moveAmount; //getX just to get center of player + move amount
        if(x < min || x > max) //make sure player within container region
            return;
        ballPoint.setLocation(x,ballPoint.getY()); //when brick is moved, change ball to new x coordinate and same Y coordinate
        //ballPoint refers to top mid
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
        //top left of player brick
    }

    /**
     * This method is used to change the width of the player bar to 40.
     */
    public void changeWidth(){
        this.playerFace.width = 40;
        this.min = container.x + (playerFace.width / 2);
        //container.x refers to the top left of the whole box
        this.max = min + container.width - playerFace.width;
    }

    /**
     * This method is used to reset the width of the player bar to 150.
     */
    public void resetWidth(){
        playerFace.width=150;
    }

    /**
     * This method is used to get the width of the player bar.
     * @return returns the width of the player bar.
     */
    public double getWidth(){
        return playerFace.getWidth();
    }

    /**
     * This method is used to increase the width of the player bar.
     */
    public void increaseWidth(){
        if(playerFace.width<300){
            this.playerFace.width += 50;
        }
    }

    /**
     * This method is used to halve the width of the player bar.
     */
    public void halveWidth(){
        this.playerFace.width /= 2;
    }

    /**
     * This method is used to invert the DEF_MOVE_AMOUNT in order to make the player bar move left.
     */
    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     * This method is used to invert the DEF_MOVE_AMOUNT in order to make the player bar move left.
     */
    public void movRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     * This method is used to stop the movement of the player bar.
     */
    public void stop(){
        moveAmount = 0;
    }

    /**
     * Getter for the player bar's Face.
     * @return Returns shape of the playerFace.
     */
    public Shape getPlayerFace(){
        return playerFace;
    }

    /**
     * This method is used to move the player bar to a particular location.
     * @param p The location we want the player bar to be spawned.
     */
    public void moveTo(Point p){
        ballPoint.setLocation(p);
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }
}
