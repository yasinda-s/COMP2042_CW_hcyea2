package brickdestroy.homemenu;

import brickdestroy.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * HomeMenu class is used to design the Home Screen we see when we first load the game.
 *
 * Additions -
 * Added a background image to the home screen to make it look more pleasant.
 * Added a new button which shows the Info (How to Play) screen which explains to the user how to play the game.
 *
 * Refactoring -
 * Changed the variable name from MENU_TEXT to EXIT_TEXT because it represents the String we place on the EXIT button in the Menu Screen.
 * Changed the variable name from "menubutton" to "exitbutton" as it refers to the Exit button on the screen.
 * The content of the info screen (How to play screen) has been moved to its own class called Info Screen to promote single responsibility.
 */
public class HomeMenu extends JComponent implements MouseListener, MouseMotionListener { //need to add javadocs

    private static final String GREETINGS = "Welcome to";
    private static final String GAME_TITLE = "BRICK DESTROY";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "START";
    private static final String EXIT_TEXT = "EXIT";
    private static final String INFO_TEXT = "HOW TO PLAY";

    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(141, 50, 5); //brown color - for border
    private static final Color GREETINGS_COLOR = new Color(141, 50, 5); //brown color - for welcome to
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.BLACK;
    private static final int BORDER_SIZE = 5;

    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle exitButton;
    private Rectangle infoButton;

    private BasicStroke borderStoke_noDashes;

    private Font greetingsFont;
    private Font gameTitleFont;
    private Font creditsFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean startClicked;
    private boolean exitClicked;
    private boolean infoClicked;

    private Image backGroundImage;

    /**
     * This is the constructor for the HomeMenu.
     * @param owner This is the game frame window where the components will be set up.
     * @param area This is the dimension of the game frame/home menu (width and height).
     */
    public HomeMenu(GameFrame owner,Dimension area){
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area); //make Rectangle object for home screen
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(300, area.height / 12); //default dimension for buttons
        startButton = new Rectangle(btnDim); //make rectangle for start
        exitButton = new Rectangle(btnDim); //make rectangle for exit button
        infoButton = new Rectangle(btnDim); //make rectangle for the into button

        //for the border deco
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        backGroundImage = new ImageIcon("src/main/resources/backGroundImage.jpg").getImage();

        //assigning fonts for greetings, font, credits and buttons
        greetingsFont = new Font("Noto Mono",Font.PLAIN,35);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,50);
        creditsFont = new Font("Monospaced",Font.PLAIN,10);
        buttonFont = new Font("Monospaced",Font.BOLD,startButton.height-2);
    }

    /**
     * Overriding method to paint the 2d Components onto the screen.
     * @param g The Graphics frame in which we want to draw the game components.
     */
    public void paint(Graphics g){
        drawMenu((Graphics2D)g); //type cast graphics object to 2d and pass to drawMenu
    }

    /**
     * This method is used to call the methods that draw the components in the Menu Screen.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing on the screen.
     */
    public void drawMenu(Graphics2D g2d){
        drawContainer(g2d);

        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x,y);

        drawText(g2d);
        drawButton(g2d);

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * This method is used to draw the background and border (container) of the Home Menu.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing.
     */
    private void drawContainer(Graphics2D g2d){ //drawing the background and border of container
        Color prev = g2d.getColor();

        g2d.setColor(BG_COLOR); //set bg color
        g2d.fill(menuFace); //draw interior of passed in rectangle (menu frame)

        Stroke tmp = g2d.getStroke();

        g2d.drawImage(backGroundImage, 0, 0, null);

        g2d.setStroke(borderStoke_noDashes); //set border of of whole container
        g2d.draw(menuFace); //draw outline of passed rectangle (menu frame)

        g2d.setColor(BORDER_COLOR); //set red
        g2d.draw(menuFace); //rerun to apply 2nd border

        g2d.setStroke(tmp);
        g2d.setColor(prev);
    }

    /**
     * This method is used to draw the Text we show in the Menu Screen.
     * @param g2d Graphics2D type frame to allow more control over coloring and drawing the text.
     */
    private void drawText(Graphics2D g2d){
        g2d.setColor(GREETINGS_COLOR); //everything below becomes brown
        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2; //x coordinate of where we want the box to be in
        sY = (int)(menuFace.getHeight() / 4); //y coordinate of where we want the box to be in

        g2d.setFont(greetingsFont); //set the font
        g2d.drawString(GREETINGS,sX,sY); //draw the greetings font (string) in the coordinates we found

        //coordinates for game title string
        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        Color TITLE_COLOR = new Color(0,0,0);
        g2d.setColor(TITLE_COLOR); //make everything below greetings black
        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY); //draw the title string

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;  //same for credits - version
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(creditsFont);  //draw the string for version
        g2d.drawString(CREDITS,sX,sY);
    }

    /**
     * This method is used to add the text and buttons for the Start, Info and Exit on the Home Menu.
     * @param g2d Graphics2D to draw the 2d components.
     */
    private void drawButton(Graphics2D g2d){
        FontRenderContext frc = g2d.getFontRenderContext();
        g2d.setColor(GREETINGS_COLOR);

        //make rectangles for start and exit texts
        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(EXIT_TEXT,frc);
        Rectangle2D infoTxtRect = buttonFont.getStringBounds(INFO_TEXT,frc);

        g2d.setFont(buttonFont); //set font for buttons

        //coordinates for start button
        int x = (menuFace.width - startButton.width) / 2;
        int y =(int) ((menuFace.height - startButton.height) * 0.8);

        startButton.setLocation(x,y); //set the button rectangle to above coordinates

        //get the location of the string for start button
        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);

        if(startClicked){ //change color, redraw button and more...
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(startButton); //leave as it is
            g2d.drawString(START_TEXT,x,y); //with normal coordinate found above
        }

        //set button for INFO
        x = (menuFace.width - startButton.width) / 2;
        y =(int) ((menuFace.height - startButton.height) * 0.65);

        infoButton.setLocation(x, y);

        //string things
        x = (int)(infoButton.getWidth() - infoTxtRect.getWidth()) / 2;
        y = (int)(infoButton.getHeight() - infoTxtRect.getHeight()) / 2;

        x += infoButton.x;
        y += infoButton.y + (infoButton.height * 0.9);

        if(infoClicked){ //change color, redraw button and more...
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(infoButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(INFO_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(infoButton); //leave as it is
            g2d.drawString(INFO_TEXT,x,y); //with normal coordinate found above
        }

        //reset to originals
        x = startButton.x;
        y = startButton.y;

        y *= 1.2;

        exitButton.setLocation(x,y); //use these coordinates to set coordinates for exit button

        x = (int)(exitButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(exitButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += exitButton.x;
        y += exitButton.y + (startButton.height * 0.9);

        //same as before to set the menu text in the right coordinates with what was found above
        if(exitClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(exitButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(EXIT_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(exitButton);
            g2d.drawString(EXIT_TEXT,x,y);
        }
    }

    /**
     * This method either starts the game when user clicks START, shows info screen when user clicks HOW TO PLAY and exits the game when user presses EXIT.
     * @param mouseEvent MouseEvent object used to see if the button is being clicked.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){ //if user clicks start, enable the game by calling game board
            owner.enableGameBoard();

        }
        else if(infoButton.contains(p)){
            owner.enableInfoScreen();
        }
        else if(exitButton.contains(p)){ //if in exit button, then exit the game
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
    }

    /**
     * This method repaints the buttons when they are clicked.
     * @param mouseEvent MouseEvent object used to see if the button is being pressed.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) { //other for when start/exit is clicked
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        }
        else if(exitButton.contains(p)){
            exitClicked = true;
            repaint(exitButton.x,exitButton.y,exitButton.width+1,exitButton.height+1);
        }
        else if(infoButton.contains(p)){
            infoClicked = true;
            repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
        }
    }

    /**
     * This method repaints the buttons when they are released.
     * @param mouseEvent MouseEvent object used to see if the button is being released.
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        }
        else if(exitClicked){
            exitClicked = false;
            repaint(exitButton.x,exitButton.y,exitButton.width+1,exitButton.height+1);
        }
        else if(infoClicked){
            infoClicked = false;
            repaint(infoButton.x,infoButton.y,infoButton.width+1,infoButton.height+1);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    /**
     * This method identifies if the user is hovering any buttons on the Menu Screen to change the cursor symbol.
     * @param mouseEvent MouseEvent button to see if a button is being hovered over.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p) || exitButton.contains(p) || infoButton.contains(p)) //if user hovering any button, then change to hand cursor, else default
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());
    }
}