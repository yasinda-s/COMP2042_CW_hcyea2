package test;

import java.awt.*;

/**
 * This is the GraphicsMain Class which consists of the main() method to run the entire Brick Game.
 */
public class GraphicsMain {

    public static void main(String[] args){
        EventQueue.invokeLater(() -> new GameFrame().initialize());
    }

}
