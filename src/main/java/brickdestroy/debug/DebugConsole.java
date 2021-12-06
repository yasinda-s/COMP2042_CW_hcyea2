package brickdestroy.debug;

import brickdestroy.game.GameBoard;
import brickdestroy.game.GamePlay;
import brickdestroy.ball.Ball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * This class is used to setup the DebugConsole as a frame. Open by using Alt + Shift + F1.
 */
public class DebugConsole extends JDialog implements WindowListener{
    private static final String TITLE = "Debug Console";
    private JFrame owner; //works like a main window where components you put together like labels, buttons, fields to make a GUI
    private DebugPanel debugPanel;
    private GameBoard gameBoard;
    private GamePlay gamePlay;

    /**
     * This is the constructor for DebugConsole Class.
     * @param owner Consists of the JFrame where the components will be set up.
     * @param gamePlay GamePlay object.
     * @param gameBoard GameBoard object where all the gaming components are drawn.
     */
    public DebugConsole(JFrame owner, GamePlay gamePlay, GameBoard gameBoard){ //constructor for this takes main window, gamePlay, gameboard
        this.gamePlay = gamePlay;
        this.owner = owner;
        this.gameBoard = gameBoard;
        initialize();
        debugPanel = new DebugPanel(gamePlay);
        this.add(debugPanel,BorderLayout.CENTER);
        this.pack();
    }
    /**
     * This method sets up the Debug Console Frame with a fixed set of parameters.
     */
    private void initialize(){
        this.setModal(true); //jdialog method to see if dialog is modal or not
        this.setTitle(TITLE); //set a name to the dialog box
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.addWindowListener(this);
        this.setFocusable(true);
    }

    /**
     * This method is used to set the frame of the debug panel in the provided location.
     */
    private void setLocation(){ //to set the frame of the debug console -
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX(); //owner refers to whole frame and this refers to console frame
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
    }

    /**
     * This method is used to repaint the content on the Game Board when the Window has changed its status.
     * @param windowEvent The status of the window.
     */
    @Override
    public void windowClosing(WindowEvent windowEvent) {
        gameBoard.repaint();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    /**
     * This method is used to set the location of the frame and to set the slider to the current speed of the ball.
     * @param windowEvent The status of the window.
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        Ball b = gamePlay.getBall();
        debugPanel.setValues(b.getSpeedX(),b.getSpeedY());
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
    }
}