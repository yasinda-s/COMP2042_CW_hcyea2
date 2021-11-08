package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Player { //refers to the player brick

    public static final Color BORDER_COLOR = Color.GREEN.darker().darker(); //outer border
    public static final Color INNER_COLOR = Color.GREEN; //inner border

    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point ballPoint;
    private int moveAmount;
    private int min;
    private int max;


    public Player(Point ballPoint,int width,int height,Rectangle container) {
        //container refers to the whole box used for the game
        this.ballPoint = ballPoint; //random point object for now, later initial pos of ball
        moveAmount = 0;
        playerFace = makeRectangle(width, height); //face of the player is made after this method which takes in w, h
        //ballPoint refers to the coordinates of the ball

        min = container.x + (width / 2);
        //container.x refers to the top left of the whole box
        max = min + container.width - width;
        //not sure what these refer to yet

    }

    private Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
        return new Rectangle(p,new Dimension(width,height)); //return top left coordinate of point P with w and h passed.
    }

    public boolean impact(Ball b){
        return playerFace.contains(b.getPosition()) && playerFace.contains(b.down) ;
    }
    //if condition true then impact is true

    public void move(){ //to move the player brick
        double x = ballPoint.getX() + moveAmount; //getX just to get center of player + move amount
        if(x < min || x > max)
            return;
        ballPoint.setLocation(x,ballPoint.getY()); //when brick is moved, change ball to new x coordinate and same Y coordinate
        //ballPoint refers to top mid
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
        //top left of player brick
    }

    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    public void movRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    public void stop(){
        moveAmount = 0;
    }

    public Shape getPlayerFace(){
        return  playerFace;
    }

    public void moveTo(Point p){
        ballPoint.setLocation(p);
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }
}
