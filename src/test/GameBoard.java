package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class draws all of the 2d Components required to load the home screen and to play the game.
 *
 * Additions -
 * This class is responsible for opening up the text file and reading in the previous high scores by the user. It then refers to this text file
 * and updates it when the user has achieved a new high score! The txt file is then referred to display a leader board when the game is completed.
 * It shows the top 5 permanent high scores in descending order.
 * Added a method that draws the permanent leader board of the high scores when the game is ended.
 *
 * Refactoring -
 * Removed the methods that painted the Screen for the Pause Menu when Esc is pressed and gave it a new Class called PauseMenu.
 * Removed the methods that painted the High Score Screen when the game is done (either game over or completed) and moved it to a new class called HighScore.
 * Removed the draw() methods - drawBall(), drawPlayer() and drawBrick() from this class and created a factory design to generate these components when needed.
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
    private DrawBall drawBall;
    private DrawBrick drawBrick;
    private DrawPlayer drawPlayer;

    private String message;
    private String detailMessage;

    Writer writer;
    Writer writerLvlOne;
    Writer writerLvlTwo;
    Writer writerLvlThree;
    Writer writerLvlFour;
    Writer writerLvlFive;

    private boolean showPauseMenu;
    private boolean scoreExitClicked;
    private boolean gameOver;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private Rectangle scoreExitButtonRect;

    private java.util.List<Integer> lvlOneScoresFromFile;
    private java.util.List<Integer> lvlTwoScoresFromFile;
    private java.util.List<Integer> lvlThreeScoresFromFile;
    private java.util.List<Integer> lvlFourScoresFromFile;
    private java.util.List<Integer> lvlFiveScoresFromFile;

    /**
     * This is the constructor for the GameBoard.
     * @param owner JFrame type container which works like a window where you have 2d components set up.
     */
    public GameBoard(JFrame owner) throws IOException {
        super();

        showPauseMenu = false;
        gameOver = false;

        this.initialize(); //set dimension, focus, and listeners from Component

        message = "";
        detailMessage = "";
        gamePlay = new GamePlay(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2);
        gamePlay.makeComponents(new Point(300,430)); //gamePlay sets up the whole game frame with the game layout (bricks, ballpos (300x430)...)

        writer = new BufferedWriter(new FileWriter("src/test/highscore.txt", true));

        writerLvlOne = new BufferedWriter(new FileWriter("src/test/levelOneScore.txt", true));
        lvlOneScoresFromFile = new ArrayList<Integer>();

        writerLvlTwo = new BufferedWriter(new FileWriter("src/test/levelTwoScore.txt", true));
        lvlTwoScoresFromFile = new ArrayList<Integer>();

        writerLvlThree = new BufferedWriter(new FileWriter("src/test/levelThreeScore.txt", true));
        lvlThreeScoresFromFile = new ArrayList<Integer>();

        writerLvlFour = new BufferedWriter(new FileWriter("src/test/levelFourScore.txt", true));
        lvlFourScoresFromFile = new ArrayList<Integer>();

        writerLvlFive = new BufferedWriter(new FileWriter("src/test/levelFiveScore.txt", true));
        lvlFiveScoresFromFile = new ArrayList<Integer>();

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
                        saveLevelScores();
                        saveTotalScore();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        popUpLevelScore();
                    } catch (FileNotFoundException ex) {
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
                        saveTotalScore();
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

    private void popUpLevelScore() throws FileNotFoundException { //make function for this
        if(gamePlay.getLevel()==1){
            Scanner inputLevelScore = new Scanner(new File("src/test/levelOneScore.txt"));

            while(inputLevelScore.hasNext()){
                lvlOneScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
            }
            lvlOneScoresFromFile.sort(Collections.reverseOrder());

            if(lvlOneScoresFromFile.size()>5){
                lvlOneScoresFromFile = lvlOneScoresFromFile.subList(0,6);
            }

            StringBuilder displayScore = new StringBuilder("<html>");

            if(lvlOneScoresFromFile.size()>5){
                for(int i=0;i<5;i++){
                    String scoreString = String.valueOf(lvlOneScoresFromFile.get(i));
                    displayScore.append(" ").append(scoreString).append("<br>");
                }
            }else{
                for (Integer integer : lvlOneScoresFromFile){
                    displayScore.append(" ").append(integer).append("<br>");
                }
            }
            //System.out.println(displayScore);
            JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
        }else if(gamePlay.getLevel()==2){
            Scanner inputLevelScore = new Scanner(new File("src/test/levelTwoScore.txt"));

            while(inputLevelScore.hasNext()){
                lvlTwoScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
            }
            lvlTwoScoresFromFile.sort(Collections.reverseOrder());

            if(lvlTwoScoresFromFile.size()>5){
                lvlTwoScoresFromFile = lvlTwoScoresFromFile.subList(0,6);
            }

            StringBuilder displayScore = new StringBuilder("<html>");

            if(lvlTwoScoresFromFile.size()>5){
                for(int i=0;i<5;i++){
                    String scoreString = String.valueOf(lvlTwoScoresFromFile.get(i));
                    displayScore.append(" ").append(scoreString).append("<br>");
                }
            }else{
                for (Integer integer : lvlTwoScoresFromFile){
                    displayScore.append(" ").append(integer).append("<br>");
                }
            }
            //System.out.println(displayScore);
            JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
        }else if(gamePlay.getLevel()==3){
            Scanner inputLevelScore = new Scanner(new File("src/test/levelThreeScore.txt"));

            while(inputLevelScore.hasNext()){
                lvlThreeScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
            }
            lvlThreeScoresFromFile.sort(Collections.reverseOrder());

            if(lvlThreeScoresFromFile.size()>5){
                lvlThreeScoresFromFile = lvlThreeScoresFromFile.subList(0,6);
            }

            StringBuilder displayScore = new StringBuilder("<html>");

            if(lvlThreeScoresFromFile.size()>5){
                for(int i=0;i<5;i++){
                    String scoreString = String.valueOf(lvlThreeScoresFromFile.get(i));
                    displayScore.append(" ").append(scoreString).append("<br>");
                }
            }else{
                for (Integer integer : lvlThreeScoresFromFile){
                    displayScore.append(" ").append(integer).append("<br>");
                }
            }
            //System.out.println(displayScore);
            JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
        }else if(gamePlay.getLevel()==4){
            Scanner inputLevelScore = new Scanner(new File("src/test/levelFourScore.txt"));

            while(inputLevelScore.hasNext()){
                lvlFourScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
            }
            lvlFourScoresFromFile.sort(Collections.reverseOrder());

            if(lvlFourScoresFromFile.size()>5){
                lvlFourScoresFromFile = lvlFourScoresFromFile.subList(0,6);
            }

            StringBuilder displayScore = new StringBuilder("<html>");

            if(lvlFourScoresFromFile.size()>5){
                for(int i=0;i<5;i++){
                    String scoreString = String.valueOf(lvlFourScoresFromFile.get(i));
                    displayScore.append(" ").append(scoreString).append("<br>");
                }
            }else{
                for (Integer integer : lvlFourScoresFromFile){
                    displayScore.append(" ").append(integer).append("<br>");
                }
            }
            //System.out.println(displayScore);
            JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
        }else if(gamePlay.getLevel()==5){
            Scanner inputLevelScore = new Scanner(new File("src/test/levelFiveScore.txt"));

            while(inputLevelScore.hasNext()){
                lvlFiveScoresFromFile.add(Integer.parseInt(inputLevelScore.next()));
            }
            lvlFiveScoresFromFile.sort(Collections.reverseOrder());

            if(lvlFiveScoresFromFile.size()>5){
                lvlFiveScoresFromFile = lvlFiveScoresFromFile.subList(0,6);
            }

            StringBuilder displayScore = new StringBuilder("<html>");

            if(lvlFiveScoresFromFile.size()>5){
                for(int i=0;i<5;i++){
                    String scoreString = String.valueOf(lvlFiveScoresFromFile.get(i));
                    displayScore.append(" ").append(scoreString).append("<br>");
                }
            }else{
                for (Integer integer : lvlFiveScoresFromFile){
                    displayScore.append(" ").append(integer).append("<br>");
                }
            }
            JOptionPane.showMessageDialog(null, displayScore, "Level " + gamePlay.getLevel() + " High Scores", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * This method is used to save the scores permanently into the text file.
     * @throws IOException In case the file cannot be found in the directory.
     */
    private void saveTotalScore() throws IOException {
        if(writer==null){
            assert false;
            writer.write(gamePlay.getScore() + " ");
        }else{
            writer.write(" " + gamePlay.getScore() + " ");
        }
        writer.close();
    }

    private void saveLevelScores() throws IOException{
        if(gamePlay.getLevel()==1){
            if(writerLvlOne==null){
                assert false;
                writerLvlOne.write(gamePlay.getScore() + " ");
            }else{
                writerLvlOne.write(" " + gamePlay.getScore() + " ");
            }
            writerLvlOne.close();
        }else if(gamePlay.getLevel()==2){
            if(writerLvlTwo==null){
                assert false;
                writerLvlTwo.write(gamePlay.getScore() + " ");
            }else{
                writerLvlTwo.write(" " + gamePlay.getScore() + " ");
            }
            writerLvlTwo.close();
        }else if(gamePlay.getLevel()==3){
            if(writerLvlThree==null){
                assert false;
                writerLvlThree.write(gamePlay.getScore() + " ");
            }else{
                writerLvlThree.write(" " + gamePlay.getScore() + " ");
            }
            writerLvlThree.close();
        }else if(gamePlay.getLevel()==4){
            if(writerLvlFour==null){
                assert false;
                writerLvlFour.write(gamePlay.getScore() + " ");
            }else{
                writerLvlFour.write(" " + gamePlay.getScore() + " ");
            }
            writerLvlFour.close();
        }else if(gamePlay.getLevel()==5){
            if(writerLvlFive==null){
                assert false;
                writerLvlFive.write(gamePlay.getScore() + " ");
            }else{
                writerLvlFive.write(" " + gamePlay.getScore() + " ");
            }
            writerLvlFive.close();
        }
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
        int level = gamePlay.getLevel();
        Point2D p = gamePlay.ball.getPosition();

        DrawFactory drawFactory = new DrawFactory();
        drawBall = (DrawBall) drawFactory.getDraw(gamePlay.ball, level, p); //must be new ball
        drawBrick = (DrawBrick) drawFactory.getDraw();
        drawPlayer = (DrawPlayer) drawFactory.getDraw(gamePlay.player);

        Graphics2D g2d = (Graphics2D) g; //get more control over geometry, coordinate transformations, color management, and text
        clear(g2d);
        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225); //whenever we print something on screen, we use this with the color we set earlier
        g2d.drawString(detailMessage,180,200); //for score things

        drawBall.draw(g2d,level);

        for(Brick b : gamePlay.bricks) //draws the bricks using 2dgraphics
            if(!b.isBroken())
                drawBrick.draw(b,g2d);

        drawPlayer.draw(g2d, level); //draws the player bar using 2dgraphics

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
     * This method is used to checked if the exit button pressed from other classes.
     * @return Returns the current boolean value if the button is pressed.
     */
    public boolean isScoreExitClicked() {
        return scoreExitClicked;
    }
}