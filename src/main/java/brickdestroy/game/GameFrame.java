/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package brickdestroy.game;
import brickdestroy.homemenu.HomeMenu;
import brickdestroy.homemenu.InfoScreen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

/**
 * This class is what holds the whole frame of the Game to be displayed.
 *
 * Additions -
 *
 * Added a method called enableInfoScreen() which allows the info screen to be displayed by removing the home menu screen.
 * Added a method called enableHomeScreen() which allows the home menu screen to be displayed when the menu button is clicked from info screen.
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    private static final String DEF_TITLE = "Brick Destroy"; //name on the game frame header
    private GameBoard gameBoard;
    private HomeMenu homeMenu;
    private InfoScreen infoScreen;
    private boolean gaming;

    /**
     * This is the constructor for the GameFrame class. It sets the border layout and initiates the home menu, gameboard and info screen.
     */
    public GameFrame() throws IOException {
        super(); //JFrame
        gaming = false; //originally set to false
        this.setLayout(new BorderLayout());
        gameBoard = new GameBoard(this);
        homeMenu = new HomeMenu(this,new Dimension(600,450));
        infoScreen = new InfoScreen(this, new Dimension(600,450));
        this.add(homeMenu,BorderLayout.CENTER);
        this.setUndecorated(true);
    }

    /**
     * This method is used to set up the Game Frame which will hold "Brick Destroy"
     */
    public void initialize(){
        this.setTitle(DEF_TITLE); //title on top of game frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //close when cross button pressed on game frame
        this.pack();
        this.autoLocate(); //load gameframe to center of screen
        this.setVisible(true); //make the loaded gameframe visible to the screen
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
     * This method is responsible in setting up the How to Play screen when Info button is clicked.
     */
    public void enableInfoScreen(){
        this.dispose();
        this.remove(homeMenu);
        this.add(infoScreen, BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        this.addWindowFocusListener(this);
    }

    /**
     * This method is responsible in setting up the Main menu when Menu button is clicked.
     */
    public void enableHomeScreen(){
        this.dispose();
        this.remove(infoScreen);
        this.add(homeMenu, BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        this.addWindowFocusListener(this);
    }

    /**
     * This method is used to load the game Frame on the center of the monitor screen.
     */
    private void autoLocate(){ //get x,y coordinate of the screen and set the location to that
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
    }

    /**
     * This method is used when the window is in focused mode (receiving keyboard events), sets gaming to true.
     * @param windowEvent pass in the WindowEvent object.
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
     * Method to repaint the game if the focus was lost after gaming had begun.
     * @param windowEvent The windowEvent object to be passed.
     */
    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if(gaming)
            gameBoard.onLostFocus();

    }
}