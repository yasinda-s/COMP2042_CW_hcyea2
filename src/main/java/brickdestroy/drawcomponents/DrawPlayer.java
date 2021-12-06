package brickdestroy.drawcomponents;

import brickdestroy.player.Player;
import brickdestroy.brick.Brick;

import java.awt.*;

/**
 * DrawPlayer class which implements the Drawable interface. This is used to override the draw(Graphics g2d) method to draw the Player bar used in game.
 */
public class DrawPlayer implements Drawable {
    private Player player;

    /**
     * This is the constructor for DrawPlayer class.
     * @param player We pass in Player object to assign to player.
     */
    public DrawPlayer(Player player, int level) {
        if(level==5 && player.getWidth()==150){
            this.player =  player;
            this.player.changeWidth();
        }else{
            this.player = player;
        }
    }

    /**
     * Overridden method in which the Player bar will be drawn in the g2d Frame.
     * @param g2d Graphics 2d Frame where the player bar will be drawn. XXXX
     */
    @Override
    public void draw(Graphics2D g2d, int level) {
        //to draw player bar
        Color tmp = g2d.getColor();

        Shape s = player.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR); //set inner color of bar
        g2d.fill(s); //fill color using that color

        g2d.setColor(Player.BORDER_COLOR); //set outer color of bar
        g2d.draw(s); //fill outline using that color

        g2d.setColor(tmp);
    }

    @Override
    public void draw(Brick brick, Graphics2D g2d) {
    }
}
