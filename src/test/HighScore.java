package test;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HighScore {

    private static final Color MENU_COLOR = new Color(0,255,0);
    private Font menuFont;
    private static final String HIGH_SCORE_TEXT = "High Score Board";
    private static final String SCORE_EXIT_TEXT = "Exit Game";
    private static final int TEXT_SIZE = 30;

    private static final int TOP_SCORES = 5;

    private java.util.List<Integer> scoresFromFile;
    private GameBoard gameBoard;

    public Rectangle getScoreExitButtonRect() {
        return scoreExitButtonRect;
    }

    private Rectangle scoreExitButtonRect;

    public HighScore(GameBoard gameBoard){
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);
        scoresFromFile = new ArrayList<Integer>();
        this.gameBoard = gameBoard;
        Dimension btnDim = new Dimension(gameBoard.getWidth()/3, gameBoard.getHeight()/12); //XXXX
        scoreExitButtonRect = new Rectangle(btnDim); //button draws when it is inside method
    }

    /**
     * This method is responsible for drawing the High Score Screen that is shown when the user either completes or loses the game.
     * @param g2d The Graphics 2d frame in which we want to draw the game components.
     */
    public void drawHighScoreScreen(Graphics2D g2d) { //XXXX - try to break from class
        //high score title
        g2d.setColor(MENU_COLOR); //assign text color in home screen
        FontRenderContext frc = g2d.getFontRenderContext();
        g2d.setFont(menuFont); //set the font

        Rectangle2D headingRect = menuFont.getStringBounds(HIGH_SCORE_TEXT,frc);

        int sX,sY;
        sX = (int)(gameBoard.getWidth() - headingRect.getWidth()) / 2; //x coordinate of where we want the box to be in
        sY = gameBoard.getHeight() / 6; //y coordinate of where we want the box to be in

        g2d.drawString(HIGH_SCORE_TEXT,sX,sY); //draw the string "High Score"

        //displaying high scores
        int score_x, score_y;

        score_x = sX + 110;
        score_y = sY + 50;

        if (scoresFromFile.size()>TOP_SCORES){
            for(int i=0;i<TOP_SCORES;i++){
                String scoreString = String.valueOf(scoresFromFile.get(i));
                g2d.drawString(scoreString, score_x, score_y);
                score_y += 30;
            }
        } else{
            for (Integer integer : scoresFromFile) {
                String scoreString = String.valueOf(integer);
                g2d.drawString(scoreString, score_x, score_y);
                score_y += 30;
            }
        }

        //exit button
        Rectangle2D menuTxtRect = menuFont.getStringBounds(SCORE_EXIT_TEXT,frc);

        Dimension btnDim = new Dimension(gameBoard.getWidth()/3, gameBoard.getHeight()/12);
        scoreExitButtonRect = new Rectangle(btnDim); //button draws when it is inside method

        //coordinates for exit button
        int x = (gameBoard.getWidth() - scoreExitButtonRect.width) / 2;
        int y =(int) ((gameBoard.getHeight() - scoreExitButtonRect.height) * 0.8);

        scoreExitButtonRect.setLocation(x, y);

        //get the location of the string for start button
        x = (int)(scoreExitButtonRect.getWidth() - menuTxtRect.getWidth()) / 2;
        y = (int)(scoreExitButtonRect.getHeight() - menuTxtRect.getHeight() - 40) / 2;

        x += scoreExitButtonRect.x;
        y += scoreExitButtonRect.y + (scoreExitButtonRect.height * 0.9);

        if(gameBoard.isScoreExitClicked()){ //change color, redraw button and more...
            Color tmp = g2d.getColor();
            g2d.setColor(Color.WHITE);
            g2d.draw(scoreExitButtonRect);
            g2d.setColor(Color.BLACK);
            g2d.drawString(SCORE_EXIT_TEXT,x,y+20);
            g2d.setColor(tmp);
            System.exit(0);
        }
        else{
            g2d.draw(scoreExitButtonRect); //leave as it is
            g2d.drawString(SCORE_EXIT_TEXT,x,y+20); //with normal coordinate found above
        }
    }

    /**
     * This method is used to find the top 5 permanent high scores in the game.
     * @throws FileNotFoundException This is incase the method is unable to access the file highscore.txt which records the scores.
     */
    public void findHighScore() throws FileNotFoundException { //read values, save on array/list and then sort, then print
        Scanner inputScore = new Scanner(new File("src/test/highscore.txt"));

        while(inputScore.hasNext()){
            scoresFromFile.add(Integer.parseInt(inputScore.next()));
        }
        scoresFromFile.sort(Collections.reverseOrder());

        if(scoresFromFile.size()>TOP_SCORES){
            scoresFromFile = scoresFromFile.subList(0,6);
        }
    }

}
