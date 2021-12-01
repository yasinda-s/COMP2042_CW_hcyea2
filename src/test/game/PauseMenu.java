package test.game;

import java.awt.*;
import java.awt.font.FontRenderContext;

/**
 * This class is responsible for displaying the pause menu in screen when Esc is pressed mid game.
 *
 * Addition - Changed the look of the pause menu to make it look more appealing whilst holding back the same functionality.
 */
public class PauseMenu{
    private static final String CONTINUE = "CONTINUE";
    private static final String RESTART = "RESTART";
    private static final String EXIT = "EXIT";
    private static final String PAUSE = "PAUSE MENU";
    private static final int TEXT_SIZE = 30;

    private Font pauseHeading = new Font("Noto Mono",Font.BOLD,TEXT_SIZE);
    private Font buttonFont = new Font("Monospaced",Font.BOLD,24);

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private GameBoard gameBoard;

    /**
     * Constructor for PauseMenu.
     * @param gameBoard Takes in gameboard object where the pause menu will be set up.
     */
    public PauseMenu(GameBoard gameBoard){
        strLen = 0;
        this.gameBoard = gameBoard;
    }

    /**
     * This method is used to draw the Screen when the user presses Esc.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    public void drawMenu(Graphics2D g2d){ //pass in g2d frame
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * This method refers to the drawing of the dark screen we see when we press Esc to open the Pause Menu.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    public void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        //alpha refers to the opacity when in pause menu
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK); //set color to black
        g2d.fillRect(0,0,gameBoard.getWidth(),gameBoard.getHeight()); //fill all of the screen rectangle with black

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * This method is used to draw the Pause Menu content we see when we press Esc.
     * @param g2d Graphics2D frame type to allow more control over coloring and drawing over 2d components.
     */
    private void drawPauseMenu(Graphics2D g2d){

        g2d.setFont(pauseHeading); //set font to menuFont
        g2d.setColor(new Color(255, 255, 255)); //set g2d color to MENU_COLOR

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = pauseHeading.getStringBounds(PAUSE,frc).getBounds().width; //get width of the PAUSE and assign to strLen
        }

        int x = (gameBoard.getWidth() - strLen) / 2; //x-coordinate of where PAUSE will be
        int y = gameBoard.getHeight() / 10; //y-coordinate of where PAUSE will be

        g2d.drawString(PAUSE,x,y); //draw PAUSE string on frame

        FontRenderContext frc = g2d.getFontRenderContext();
        g2d.setColor(new Color(141, 50, 5));

        //make rectangles for continue, restart and exit texts
        continueButtonRect = pauseHeading.getStringBounds(CONTINUE,frc).getBounds();
        restartButtonRect = continueButtonRect.getBounds();
        exitButtonRect = continueButtonRect.getBounds();

        g2d.setFont(buttonFont);

        //coordinates for continue button
        x = (int) (gameBoard.getWidth() - continueButtonRect.getWidth()) / 2;
        y = gameBoard.getHeight() / 3;
        continueButtonRect.setLocation(x,y);

        g2d.draw(continueButtonRect); //leave as it is
        g2d.drawString(CONTINUE, (float) (x + continueButtonRect.getWidth()/7), y+continueButtonRect.height-10);

        //coordinates for restart button
        y += 100;
        restartButtonRect.setLocation(x, y);

        g2d.draw(restartButtonRect); //leave as it is
        g2d.drawString(RESTART, (float) (x + restartButtonRect.getWidth()/6), y+restartButtonRect.height-10);

        //coordinates for exit button
        y += 100; //change y again
        exitButtonRect.setLocation(x, y);

        g2d.draw(exitButtonRect); //leave as it is
        g2d.drawString(EXIT, (float) (x + exitButtonRect.getWidth()/3.25), y+exitButtonRect.height-10);
    }

    /**
     * Getter for the continue button in the pause menu.
     * @return Returns the rectangle for the button.
     */
    public Rectangle getContinueButtonRect() {
        return continueButtonRect;
    }
    /**
     * Getter for the exit button in the pause menu.
     * @return Returns the rectangle for the button.
     */
    public Rectangle getExitButtonRect() {
        return exitButtonRect;
    }
    /**
     * Getter for the restart button in the pause menu.
     * @return Returns the rectangle for the button.
     */
    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
    }
}
