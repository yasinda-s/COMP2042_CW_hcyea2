package test.drawcomponents;

import test.player.Player;
import test.player.PlayerInterface;
import test.player.SmallPlayer;
import test.brick.Brick;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * DrawPlayer class which implements the Drawable interface. This is used to override the draw(Graphics g2d) method to draw the Player bar used in game.
 */
public class DrawPlayer implements Drawable {
    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private PlayerInterface player;

    /**
     * This is the constructor for DrawPlayer class.
     * @param player We pass in Player object to assign to player.
     */
    public DrawPlayer(Player player, int level, Point2D center) {
        if(level==5){
            this.player = new SmallPlayer((Point) center, new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT)); //player and bar go tgt
            //this.player = new Player((Point) center, 20, 20, new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT));
            //this.player = player;
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
