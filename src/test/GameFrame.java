package test;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

/**
 * This class is what holds the whole frame of the Game to be displayed.
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    private static final String DEF_TITLE = "Brick Destroy"; //title screen text

    private GameBoard gameBoard;
    private HomeMenu homeMenu;

    private boolean gaming;

    /**
     * This is the constructor for the GameFrame class. It uses gameBoard and homeMenu to get the layout setup.
     */
    public GameFrame(){ //constructor
        super(); //JFrame

        gaming = false; //originally set to false

        this.setLayout(new BorderLayout());

        gameBoard = new GameBoard(this);

        homeMenu = new HomeMenu(this,new Dimension(450,300));

        this.add(homeMenu,BorderLayout.CENTER);

        this.setUndecorated(true);
    }

    /**
     *
     */
    public void initialize(){
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate();
        this.setVisible(true);
    }

    /**
     *  This method removes the home menu screen and loads up the game board.
     */
    public void enableGameBoard(){
        this.dispose();
        this.remove(homeMenu); //remove home screen
        this.add(gameBoard,BorderLayout.CENTER); //add game board
        this.setUndecorated(false);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
    }

    /**
     * This method is used to locate the coordinates of the screen and set the location to those coordinates.
     */
    private void autoLocate(){ //get x,y coordinate of the screen and set the location to that
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
    }

    /**
     *
     * @param windowEvent
     */
    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        /*
            the first time the frame loses focus is because
            it has been disposed to install the GameBoard,
            so went it regains the focus it's ready to play.
            of course calling a method such as 'onLostFocus'
            is useful only if the GameBoard as been displayed
            at least once
         */
        gaming = true;
    }

    /**
     *
     * @param windowEvent
     */
    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if(gaming)
            gameBoard.onLostFocus();

    }
}
