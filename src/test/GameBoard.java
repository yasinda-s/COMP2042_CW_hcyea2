package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 * This class draws all of the 2d Components required to load the home screen and to play the game.
 *
 * Additions -
 *
 * This class is responsible for opening up the text file and reading in the previous high scores by the user. It then refers to this text file
 * and updates it when the user has achieved a new high score! The txt file is then referred to display a leader board when the game is completed.
 * It shows the top 5 permanent high scores in descending order.
 *
 * Added a method that draws the permanent leaderboard of the high scores when the game is ended.
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

    private String message;
    private String detailMessage;

    Writer writer; //XXXX

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

        //type Font for Menu screen that has font mentioned and font size mentioned above
        this.initialize(); //set dimension, focus, and listeners from Component

        message = "";
        detailMessage = "";
        gamePlay = new GamePlay(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));
        //gamePlay sets up the whole game frame with the game layout (bricks, ballpos (300x430)...)

        writer = new BufferedWriter(new FileWriter("src/test/highscore.txt", true));

        pauseMenu = new PauseMenu(this);
        debugConsole = new DebugConsole(owner, gamePlay,this); //setup debug console
        highScore = new HighScore(this);

        //initialize the first level
        gamePlay.nextLevel();
        gamePlay.setTimePlayed(0);

        gameTimer = new Timer(10,e ->{
            gamePlay.move(); //moving of player and ball from GamePlay
            gamePlay.findImpacts(); //look for impacts from GamePlay
            gamePlay.incrementTime();
            detailMessage = String.format("Bricks: %d Balls: %d Time Played: %d Score: %d", gamePlay.getBrickCount(), gamePlay.getBallCount(), gamePlay.getTimePlayed(), gamePlay.getScore());
            if(gamePlay.isBallLost()){
                if(gamePlay.ballEnd()){
                    gamePlay.wallReset();
                    message = "Game over, your final score is " + gamePlay.getScore(); //if all balls lost XXXX add this to the high score screen
                    try {
                        saveScores();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    gameOver = true;
                }
                gamePlay.ballReset(); //if user hasnt used all 3 balls
                gameTimer.stop();
            }
            else if(gamePlay.isDone()){ //if all bricks broken
                if(gamePlay.hasLevel()){ //if user has more levels left
                    message = "Go to Next Level";
                    gameTimer.stop();
                    gamePlay.ballReset();
                    gamePlay.wallReset();
                    gamePlay.nextLevel();
                }
                else{ //if no more levels
                    message = "ALL WALLS DESTROYED";
                    try {
                        saveScores();
                    } catch (IOException ex) {
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
     * This method is used to save the scores permanently into the text file.
     * @throws IOException In case the file cannot be found in the directory.
     */
    private void saveScores() throws IOException {
        if(writer==null){
            assert false;
            writer.write(gamePlay.getScore() + " ");
        }else{
            writer.write(" " + gamePlay.getScore() + " ");
        }
        writer.close();
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
     * Overriding method to paint the 2d Components onto the screen. We use this to draw the ball, wall and player.
     * @param g The Graphics frame in which we want to draw the game components.
     */
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g; //get more control over geometry, coordinate transformations, color management, and text
        clear(g2d);
        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225); //whenever we print something on screen, we use this with the color we set earlier
        g2d.drawString(detailMessage,180,200); //for score things

        drawBall(gamePlay.ball,g2d); //draws the ball using 2dgraphics

        for(Brick b : gamePlay.bricks) //draws the bricks using 2dgraphics
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(gamePlay.player,g2d); //draws the player bar using 2dgraphics

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
     * This method is used to clear the gameboard.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    /**
     * This method is used to draw a singular brick on the game board.
     * @param brick Takes in type Brick based on what brick we want to draw.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    private void drawBrick(Brick brick,Graphics2D g2d){
        //draw and color the brick
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor()); //set color based on what we assigned before
        g2d.fill(brick.getBrick());  //fill the brick based on color we set

        g2d.setColor(brick.getBorderColor()); //set outline color based on what we assigned before
        g2d.draw(brick.getBrick()); //outline the brick based on color we set

        g2d.setColor(tmp);
    }

    /**
     * This method is used to draw the ball on the game board.
     * @param ball The ball object to be drawn.
     * @param g2d Graphics2D frame type to allow more control over coloring the and drawing the ball.
     */
    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor(); //set tmp color to color in g2d

        Shape s = ball.getBallFace(); //set face of ball as shape

        g2d.setColor(ball.getInnerColor()); //set color of g2d as ball color (inner)
        g2d.fill(s); //use this color and fill shape of ball face

        g2d.setColor(ball.getBorderColor()); //set outline color based on what we assigned before
        g2d.draw(s); //draw only does the outline of a shape

        g2d.setColor(tmp); //set to tmp color
    }

    /**
     * This method is used to draw the PLayer bar on the game board.
     * @param p The player object to be drawn.
     * @param g2d  Graphics2D frame type to allow more control over coloring the and drawing the ball.
     */
    private void drawPlayer(Player p,Graphics2D g2d){
        //to draw player bar
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR); //set inner color of bar
        g2d.fill(s); //fill color using that color

        g2d.setColor(Player.BORDER_COLOR); //set outer color of bar
        g2d.draw(s); //fill outline using that color

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
            gamePlay.setTimePlayed(0);
            gamePlay.ballReset();
            gamePlay.wallReset();
            gamePlay.setScore(0);
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)) {
            System.exit(0);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        scoreExitButtonRect = highScore.getScoreExitButtonRect();
        if (scoreExitButtonRect.contains(p)){
            scoreExitClicked = true;
            repaint(scoreExitButtonRect.x,scoreExitButtonRect.y,scoreExitButtonRect.width+1,scoreExitButtonRect.height+1);
        }
    }

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

    public boolean isScoreExitClicked() {
        return scoreExitClicked;
    }
}