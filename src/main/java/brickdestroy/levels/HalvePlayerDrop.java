package brickdestroy.levels;

import brickdestroy.ball.Ball;

import java.awt.*;

/**
 * This class is responsible for the level 5 power drop that halves the width of the player bar - Penalty to game more challenging and exciting.
 */

public class HalvePlayerDrop {
    private Rectangle boxFace = new Rectangle(200, 150, 20, 20);

    private boolean impactOnce;

    /**
     * This is the constructor for the class, it sets impactOnce as zero as it is not broken yet.
     */
    public HalvePlayerDrop(){
        impactOnce = false;
    }

    /**
     * This method is responsible for displaying the box in the game frame.
     * @param g2d The frame to show the box.
     * @param level The level being played currently.
     */
    public void showBox(Graphics2D g2d, int level){
        if(level==5 && !this.impactOnce){
            g2d.setColor(Color.GREEN);
            g2d.fillRect(200, 150, 20, 20);
            g2d.draw(boxFace);
        }
    }

    /**
     * This method checks if the ball had impacted the drop.
     * @param b The ball that is being used in the game.
     * @return Returns true if the ball was impacted, else returns false.
     */
    public boolean impact(Ball b){
        return (boxFace.contains((b.getDown())) || boxFace.contains((b.getUp())) || boxFace.contains((b.getLeft())) || boxFace.contains((b.getRight())));
    }

    /**
     * A getter method to see if the drop was impacted once.
     * @return Returns true if the drop was impacted, else returns false.
     */
    public boolean isImpactOnce() {
        return impactOnce;
    }

    /**
     * A setter method to adjust the value of impactOnce.
     * @param impactOnce Whether the drop was impacted by the ball or not.
     */
    public void setImpactOnce(boolean impactOnce) {
        this.impactOnce = impactOnce;
    }
}
