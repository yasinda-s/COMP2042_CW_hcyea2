package brickdestroy.homemenu;

import brickdestroy.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * This Class is responsible for displaying the How to Play screen when user clicks on Info in Main Menu.
 *
 * Additions -
 * Consists of the background image which is available in the home screen.
 */

public class InfoScreen extends JComponent implements MouseListener, MouseMotionListener{

    private static final String INFO_TEXT = "HOW TO PLAY";
    private static final String MENU_TEXT = "RETURN TO MENU";
    private static final String MOVE_LEFT = "Use A to move to the Left.";
    private static final String MOVE_RIGHT = "Press D to move to the Right.";
    private static final String DEBUG_PANEL = "Press Alt + Shift + F1 to Open Debug Panel";
    private static final String ESCAPE = "Press Esc to Pause the Game.";

    private GameFrame owner;
    private Rectangle infoFace;

    private Rectangle infoMenuButton;

    private Font headFont;
    private Font menuFont;

    private Image backGroundImage;

    private boolean infoMenuClicked;

    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(141, 50, 5); //brown color - for border
    private static final Color TEXT_COLOR = new Color(141, 50, 5);//brown color - for text
    private static final int BORDER_SIZE = 5;
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    private BasicStroke borderStoke_noDashes;

    /**
     * This is the constructor for the info screen.
     * @param owner The game frame where the info screen will be setup.
     * @param area The dimension in which the info screen will be setup.
     */
    public InfoScreen(GameFrame owner, Dimension area){

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        backGroundImage = new ImageIcon("src/main/resources/backGroundImage.jpg").getImage();

        this.owner = owner;

        infoFace = new Rectangle(new Point(0,0), area);
        this.setPreferredSize(area);

        //for the border deco
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        Dimension btnDim = new Dimension(area.width/3, area.height/12);
        infoMenuButton = new Rectangle(btnDim);

        headFont = new Font("Noto Mono",Font.BOLD,40);
        menuFont = new Font("Noto Mono",Font.BOLD,20);
    }

    /**
     * Overriding method to paint the 2d Components onto the screen.
     * @param g The Graphics frame in which we want to draw the game components.
     */
    public void paint(Graphics g){
        drawInfoScreen((Graphics2D)g); //type cast graphics object to 2d and pass to drawMenu
    }

    /**
     * This method is used to call the methods that draw the components in the Info Screen.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing on the screen.
     */
    public void drawInfoScreen(Graphics2D g2d){

        drawContainer(g2d);

        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = infoFace.getX();
        double y = infoFace.getY();

        g2d.translate(x,y);

        drawText(g2d);
        drawButton(g2d);

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * This method is used to draw the background and border (container) of the Info Screen.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing.
     */
    private void drawContainer(Graphics2D g2d){
        Color prev = g2d.getColor();

        g2d.setColor(BG_COLOR);
        g2d.fill(infoFace);

        g2d.drawImage(backGroundImage, 0, 0, null);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStoke_noDashes);
        g2d.draw(infoFace);

        g2d.setColor(BORDER_COLOR);
        g2d.draw(infoFace);

        g2d.setStroke(tmp);
        g2d.setColor(prev);
    }

    /**
     * This method is used to draw the Text we show in the Info screen.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing the text.
     */
    private void drawText(Graphics2D g2d){
        g2d.setColor(Color.BLACK);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D headingRect = headFont.getStringBounds(INFO_TEXT,frc);

        int sX,sY;

        sX = (int)(infoFace.getWidth() - headingRect.getWidth()) / 2;
        sY = (int)(infoFace.getHeight() / 6);

        g2d.setFont(headFont);
        g2d.drawString(INFO_TEXT,sX,sY);

        g2d.setColor(TEXT_COLOR);
        Rectangle2D textBodyRect = menuFont.getStringBounds(MOVE_LEFT,frc);

        sX = (int)(infoFace.getWidth() - textBodyRect.getWidth()) / 2;
        sY = (int)(infoFace.getHeight() / 3);

        g2d.setFont(menuFont);

        g2d.drawString(MOVE_LEFT, sX, sY);

        g2d.drawString(MOVE_RIGHT, sX-12, sY+50);

        g2d.drawString(DEBUG_PANEL, sX - 75, sY+100);

        g2d.drawString(ESCAPE, sX-14, sY+150);
    }

    /**
     * This method is used to add the return to menu button in the info screen.
     * @param g2d Graphics2D to draw the 2d components.
     */
    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();
        g2d.setColor(Color.BLACK);

        Rectangle2D menuTxtRect = menuFont.getStringBounds(MENU_TEXT,frc);

        g2d.setFont(menuFont); //set font for buttons

        int x = (infoFace.width - infoMenuButton.width) / 2;
        int y =(int) ((infoFace.height - infoMenuButton.height) * 0.8);

        infoMenuButton.setLocation(x,y); //set the button rectangle to above coordinates

        x = (int)(infoMenuButton.getWidth() - menuTxtRect.getWidth()) / 2;
        y = (int)(infoMenuButton.getHeight() - menuTxtRect.getHeight() - 40) / 2;

        x += infoMenuButton.x;
        y += infoMenuButton.y + (infoMenuButton.height * 0.9);

        if(infoMenuClicked){ //change color, redraw button and more...
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(infoMenuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y+5);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(infoMenuButton); //leave as it is
            g2d.drawString(MENU_TEXT,x,y+5);
        }
    }

    /**
     * This method is used to enable the menu screen when the user clicks return to menu button.
     * @param e MouseEvent to do the action for mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if(infoMenuButton.contains(p)){ //if user clicks menu... call back menu screen
            owner.enableHomeScreen();
        }
    }

    /**
     * This method is used to repaint the menu button to give a flashy look when clicked.
     * @param e MouseEvent to do the action for the mouse press.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (infoMenuButton.contains(p)){
            infoMenuClicked = true;
            repaint(infoMenuButton.x,infoMenuButton.y,infoMenuButton.width+1,infoMenuButton.height+1);
        }
    }

    /**
     * This method is used to repaint the menu button when the click is released to completed the flashy look.
     * @param e MouseEvent to do the action after the mouse is released.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(infoMenuClicked ){
            infoMenuClicked = false;
            repaint(infoMenuButton.x,infoMenuButton.y,infoMenuButton.width+1,infoMenuButton.height+1);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }
}