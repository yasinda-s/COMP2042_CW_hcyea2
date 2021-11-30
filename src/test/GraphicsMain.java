package test;

import test.game.GameFrame;

import java.awt.*;
import java.io.IOException;

/**
 * This is the GraphicsMain Class which consists of the main() method to run the entire Brick Game.
 */
public class GraphicsMain {

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            try {
                new GameFrame().initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
