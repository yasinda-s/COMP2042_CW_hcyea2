package brickdestroy.levels;

import brickdestroy.ball.Ball;

import java.awt.*;

public class HalvePlayerDrop {
    private Rectangle boxFace = new Rectangle(200, 150, 20, 20);

    private boolean impactOnce;

    public HalvePlayerDrop(){
        impactOnce = false;
    }

    public void showBox(Graphics2D g2d, int level){
        if(level==5 && !this.impactOnce){
            g2d.setColor(Color.GREEN);
            g2d.fillRect(200, 150, 20, 20);
            g2d.draw(boxFace);
        }
    }

    public boolean impact(Ball b){
        return (boxFace.contains((b.getDown())) || boxFace.contains((b.getUp())) || boxFace.contains((b.getLeft())) || boxFace.contains((b.getRight())));
    }

    public boolean isImpactOnce() {
        return impactOnce;
    }

    public void setImpactOnce(boolean impactOnce) {
        this.impactOnce = impactOnce;
    }
}
