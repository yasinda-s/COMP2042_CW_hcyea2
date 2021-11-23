package test;

import java.awt.*;
import java.awt.font.FontRenderContext;

public class PauseMenu{

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final Color MENU_COLOR = new Color(0,255,0);
    private static final int TEXT_SIZE = 30;

    private Font menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;

    private int strLen;

    private GameBoard gameBoard;

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
        Font tmpFont = g2d.getFont(); //get font saved in g2d
        Color tmpColor = g2d.getColor(); //get color save in g2d

        g2d.setFont(menuFont); //set font to menuFont
        g2d.setColor(MENU_COLOR); //set g2d color to MENU_COLOR

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width; //get width of the PAUSE and assign to strLen
        }

        int x = (gameBoard.getWidth() - strLen) / 2; //x-coordinate of where PAUSE will be
        int y = gameBoard.getHeight() / 10; //y-coordinate of where PAUSE will be

        g2d.drawString(PAUSE,x,y); //draw PAUSE string on frame

        //change x and y coordinates
        x = gameBoard.getWidth() / 8;
        y = gameBoard.getHeight() / 4;

        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds(); //set the string bounds to continueButtonRectangle
            continueButtonRect.setLocation(x,y-continueButtonRect.height); //set its location to whats mentioned above
        }

        g2d.drawString(CONTINUE,x,y); //draw CONTINUE string on frame

        //change y coordinate
        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone(); //clone continue button rect
            restartButtonRect.setLocation(x,y-restartButtonRect.height); //change its location
        }

        g2d.drawString(RESTART,x,y); //draw it on screen

        y *= 3.0/2; //change y again

        if(exitButtonRect == null){ //make for exit button
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y); //draw exit button
        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);

    }

    public Rectangle getContinueButtonRect() {
        return continueButtonRect;
    }

    public Rectangle getExitButtonRect() {
        return exitButtonRect;
    }

    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
    }
}
