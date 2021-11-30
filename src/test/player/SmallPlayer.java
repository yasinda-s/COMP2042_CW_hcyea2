package test.player;

import test.player.Player;

import java.awt.*;

public class SmallPlayer extends Player {

    private static final int DEF_WIDTH = 30;
    private static final int DEF_HEIGHT = 20;

    /**
     * This is the constructor for the Player object which refers to the bar he/she controls in-game.
     *
     * @param ballPoint Refers to the position of the ball.
     * @param container Refers to the whole screen used for the game.
     */
    public SmallPlayer(Point ballPoint, Rectangle container) {
        super(ballPoint, DEF_WIDTH, DEF_HEIGHT, container);
    }
}
