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
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//use alt+shift+f1 to open debugpanel console

/**
 * This class is used to XXXX.
 */
public class DebugConsole extends JDialog implements WindowListener{
    //window listener to be notified when we change the window state - windowed/focus/fullscreen

    private static final String TITLE = "Debug Console";

    private JFrame owner; //works like a main window where components you put together like labels, buttons, fields to make a GUI
    private DebugPanel debugPanel;
    private GameBoard gameBoard;
    private Wall wall;

    /**
     * This is the constructor for DebugConsole Class. XXXX
     * @param owner
     * @param wall
     * @param gameBoard
     */

    public DebugConsole(JFrame owner,Wall wall,GameBoard gameBoard){ //constructor for this takes main window, wall, gameboard
        this.wall = wall;
        this.owner = owner;
        this.gameBoard = gameBoard;
        initialize();

        debugPanel = new DebugPanel(wall);
        this.add(debugPanel,BorderLayout.CENTER);

        this.pack();
    }

    /**
     *
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
    private void setLocation(){ //to set the frame of the debug console
        //owner refers to whole frame and this refers to console frame
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
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
     * XXXX
     * @param windowEvent The status of the window.
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        Ball b = wall.ball;
        debugPanel.setValues(b.getSpeedX(),b.getSpeedY());
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
    }
}
