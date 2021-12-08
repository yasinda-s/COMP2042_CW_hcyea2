package brickdestroy.game;

import brickdestroy.levels.*;
import brickdestroy.brick.Brick;
import brickdestroy.debug.DebugConsole;
import brickdestroy.drawcomponents.DrawBall;
import brickdestroy.drawcomponents.DrawBrick;
import brickdestroy.drawcomponents.DrawFactory;
import brickdestroy.drawcomponents.DrawPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;

/**
 * This class draws all of the 2d Components required to load the home screen and to play the game.
 *
 * Additions -
 * Creates an object of HighScore class to call a method that saves the final high score onto a text file for permanent record tracking. This is later used by HighScore class to show the leader board in descending order at the end of game (either game over or victory).
 * Creates an object of LevelScore class to call a method that saves the individual level scores onto text files for permanent record tracking. This is later used by LevelScore class to show the leader board in descending order at the end of each level (either level complete or defeat).
 * Added a method getScoreFromGamePlay() so that the current ongoing score can be accessed from other classes.
 * Creates an object of class IncreaseSpeedDrop to display the box that increases ball speed in additional level (Level 5) - Penalty Drop.
 * Creates an object of class HalvePlayerDrop to display the box that halves the width of the player bar (Level 5) - Penalty Drop.
 * Added the time played to be shown on screen as an extra exciting features, this is used to reward the players for score.
 * High score and level score are also displayed in the game screen via GameBoard.
 *
 * Refactoring -
 * Removed the draw() methods - drawBall(), drawPlayer() and drawBrick() from this class and created a factory design pattern to generate these components when needed (drawcomponents package).
 * Removed the methods and variables that painted the Screen for the Pause Menu when Esc is pressed and gave it a new Class called PauseMenu to break down large class into smaller class and to promote single responsibility.
 * Removed the methods that painted the High Score Screen when the game is done (either game over or completed) and moved it to a new class called HighScore to break down large class into smaller class and to promote single responsibility.
 * Added getters and used encapsulation to promote data hiding from other classes.
 */

public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;
    private static final Color BG_COLOR = Color.WHITE;

    private Timer gameTimer;
    private GamePlay gamePlay;
    private DebugConsole debugConsole;
    private PauseMenu pauseMenu;
    private HighScore highScore;
    private LevelScore levelScore;
    private SaveScores saveScores;
    private IncreaseSpeedDrop increaseSpeedDrop;
    private HalvePlayerDrop halvePlayerDrop;
    private String detailMessage;
    private String scoreMessage;

    private boolean showPauseMenu;
    private boolean scoreExitClicked;
    private boolean gameOver;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private Rectangle scoreExitButtonRect;

    /**
     * This is the constructor for the GameBoard.
     * @param owner JFrame type container which works like a window where you have 2d components set up.
     */
    public GameBoard(JFrame owner) throws IOException {
        super();
        showPauseMenu = false;
        gameOver = false;

        this.initialize(); //set dimension, focus, and listeners from Component
        detailMessage = "";
        scoreMessage = "";
        increaseSpeedDrop = new IncreaseSpeedDrop();
        halvePlayerDrop = new HalvePlayerDrop();
        gamePlay = new GamePlay(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2, increaseSpeedDrop, halvePlayerDrop);
        gamePlay.makeComponents(new Point(300,430));
        levelScore = new LevelScore(gamePlay);

        pauseMenu = new PauseMenu(this);
        debugConsole = new DebugConsole(owner, gamePlay,this); //setup debug console
        highScore = new HighScore(this);
        saveScores = new SaveScores(gamePlay, highScore);

        //initialize the first level
        gamePlay.nextLevel();
        gamePlay.setLevelTimePlayed(0);

        gameTimer = new Timer(10,e ->{
            gamePlay.move(); //moving of player and ball from GamePlay
            gamePlay.findImpacts(); //look for impacts from GamePlay
            gamePlay.incrementTime();
            detailMessage = String.format("Bricks: %d Balls: %d Time Played: %d", gamePlay.getBrickCount(), gamePlay.getBallCount(), gamePlay.getLevelTimePlayed());
            scoreMessage = String.format("Level Score : %d Total Score : %d", levelScore.getLevelScore(), gamePlay.getScore());
            if(gamePlay.isBallLost()){
                if(gamePlay.ballEnd()){
                    try {
                        saveScores.saveLevelScores(); //save final score for that level only
                        saveScores.saveTotalScore(); //save total final score
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        levelScore.popUpLevelScore(); //trigger pop up score for that level only
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    gamePlay.wallReset();
                    gameOver = true; //this will trigger final high score screen
                }
                gamePlay.ballReset(); //if user hasnt used all 3 balls
                gameTimer.stop();
            }
            else if(gamePlay.isDone()){ //if all bricks broken
                gamePlay.setLevelTimePlayed(0);
                if(gamePlay.hasLevel()){ //if user has more levels left
                    gameTimer.stop();
                    try {
                        saveScores.saveLevelScores(); //save score for that level as its completed
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        levelScore.popUpLevelScore(); //trigger pop up score for that level only
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    gamePlay.ballReset();
                    gamePlay.playerReset();
                    gamePlay.wallReset();
                    gamePlay.nextLevel();
                }
                else{ //if no more levels
                    try {
                        saveScores.saveTotalScore();  //save total final score
                        saveScores.saveLevelScores(); //save score for that level as its completed
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        levelScore.popUpLevelScore(); //trigger pop up score for that level only
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    gameTimer.stop();
                    gameOver = true;
                }
            }
            repaint();
        });
    }

    /**
     * This method is used to initialize the dimensions of the Game Board and Listeners so the system responds to user's actions.
     */
    private void initialize(){ //set dimension, focus, and listeners from Component
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT)); //set size of game screen to whats mentioned above 600x450
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Overriding method to paint the 2d Components onto the screen. We use this to draw the ball, wall and player whilst inside the game loop.
     * @param g The Graphics frame in which we want to draw the game components.
     */
    public void paint(Graphics g){
        int level = gamePlay.getLevel();
        Point2D p = gamePlay.getBall().getPosition();

        DrawFactory drawFactory = new DrawFactory();
        DrawBall drawBall = (DrawBall) drawFactory.getDraw(gamePlay.getBall(), level, p);
        DrawBrick drawBrick = (DrawBrick) drawFactory.getDraw();
        DrawPlayer drawPlayer = (DrawPlayer) drawFactory.getDraw(gamePlay.player, level);

        Graphics2D g2d = (Graphics2D) g; //get more control over geometry, coordinate transformations, color management, and text
        clear(g2d);
        g2d.setColor(Color.BLUE);
        g2d.drawString(detailMessage,220,200); //for score things
        g2d.drawString(scoreMessage, 220, 230);

        drawBall.draw(g2d,level);

        for(Brick b : gamePlay.bricks) //draws the bricks using 2dgraphics
            if(!b.isBroken())
                drawBrick.draw(b,g2d);

        drawPlayer.draw(g2d, level); //draws the player bar using 2dgraphics

        increaseSpeedDrop.showBox(g2d, level);
        halvePlayerDrop.showBox(g2d, level);

        if(showPauseMenu) { //if user presses esc
            pauseMenu.drawMenu(g2d);
            continueButtonRect = pauseMenu.getContinueButtonRect();
            restartButtonRect = pauseMenu.getRestartButtonRect();
            exitButtonRect = pauseMenu.getExitButtonRect();
        }

        if(gameOver){
            clear(g2d);
            try {
                highScore.findHighScore();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            highScore.drawHighScoreScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * This method is used to clear the game board.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    public void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    /**
     * This method identifies the keys the user presses into order to cause a reaction.
     * @param keyEvent KeyEvent object used to get KeyCode of pressed Key.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) { //in charge of reacting to user keybinds
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                gamePlay.player.moveLeft();
                break;
            case KeyEvent.VK_D:
                gamePlay.player.movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu; //originally set to False
                repaint();
                gameTimer.stop();
                break;
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(gameTimer.isRunning())
                        gameTimer.stop();
                    else
                        gameTimer.start();
                break;
            case KeyEvent.VK_F1: //alt+shift+f1 for debug console
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                gamePlay.player.stop();
        }
    }

    /**
     * This method is used to make sure the player does not move when no key is pressed.
     * @param keyEvent KeyEvent object, when a key is released.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        gamePlay.player.stop();
    }

    /**
     * This method identifies certain button's the user clicks in order to perform some actions.
     * @param mouseEvent MouseEvent object used to get Point to see if it is on any of the buttons.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {  //in charge of reacting to mouse commands
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }
        else if(restartButtonRect.contains(p)){
            gamePlay.setLevelTimePlayed(0);
            gamePlay.ballReset();
            gamePlay.wallReset();
            gamePlay.setScore(0);
            gamePlay.resetLevelScores();
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)) {
            System.exit(0);
        }
    }

    /**
     * This method repaints the buttons when they are clicked.
     * @param mouseEvent MouseEvent object used to see if the button is being pressed.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        scoreExitButtonRect = highScore.getScoreExitButtonRect();
        if (scoreExitButtonRect.contains(p)){
            scoreExitClicked = true;
            repaint(scoreExitButtonRect.x,scoreExitButtonRect.y,scoreExitButtonRect.width+1,scoreExitButtonRect.height+1);
        }
    }

    /**
     * This method repaints the buttons when they are released.
     * @param e MouseEvent object used to see if the button is being released.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(scoreExitClicked){
            scoreExitClicked = false;
            repaint(scoreExitButtonRect.x,scoreExitButtonRect.y,scoreExitButtonRect.width+1,scoreExitButtonRect.height+1);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    /**
     * This method identifies if the user is hovering any buttons on the Pause Screen to change the cursor symbol.
     * It also checks if the exit button is clicked in the high score screen.
     * @param mouseEvent MouseEvent button to see if a button is being hovered over.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        scoreExitButtonRect = highScore.getScoreExitButtonRect();
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //make cursor hand cursor if hovering over buttons
            else
                this.setCursor(Cursor.getDefaultCursor()); //else leave normal cursor
        }else if(scoreExitButtonRect.contains(p)){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //make cursor hand cursor if hovering over buttons
        }
        else{
            this.setCursor(Cursor.getDefaultCursor()); //else leave normal cursor
        }
    }

    /**
     * Method to repaint the components of the gameboard incase the game loses its focus.
     */
    public void onLostFocus(){
        gameTimer.stop();
        repaint();
    }

    /**
     * This method is used to checked if the exit button pressed in high score screen from other classes.
     * @return Returns the current boolean value if the button is pressed.
     */
    public boolean isScoreExitClicked() {
        return scoreExitClicked;
    }

    /**
     * This method is used as a getter in order to access the score from other classes.
     * @return Returns the current ongoing score.
     */
    public int getScoreFromGameplay(){
        return gamePlay.getScore();
    }
}