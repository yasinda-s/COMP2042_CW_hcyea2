package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class HomeMenu extends JComponent implements MouseListener, MouseMotionListener {

    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "Start";
    private static final String MENU_TEXT = "Exit";

    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = new Color(16, 52, 166);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle menuButton;

    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font greetingsFont;
    private Font gameTitleFont;
    private Font creditsFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean startClicked;
    private boolean menuClicked;


    public HomeMenu(GameFrame owner,Dimension area){

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area); //make Rectangle object for home screen
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12); //default dimension for buttons
        startButton = new Rectangle(btnDim); //make rectangle for start
        menuButton = new Rectangle(btnDim); //make rectangle for exit button

        //for the border deco
        borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //assigning fonts for greetings, font, credits and buttons
        greetingsFont = new Font("Noto Mono",Font.PLAIN,25);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
        creditsFont = new Font("Monospaced",Font.PLAIN,10);
        buttonFont = new Font("Monospaced",Font.PLAIN,startButton.height-2);

    }


    public void paint(Graphics g){
        drawMenu((Graphics2D)g); //type cast graphics object to 2d and pass to drawMenu
    }


    public void drawMenu(Graphics2D g2d){

        drawContainer(g2d);

        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x,y);

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    private void drawContainer(Graphics2D g2d){ //drawing the background of container
        Color prev = g2d.getColor();

        g2d.setColor(BG_COLOR); //set bg color
        g2d.fill(menuFace); //draw interior of passed in rectangle (menu frame)

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStoke_noDashes); //set border of of whole container
        g2d.setColor(DASH_BORDER_COLOR); //set color '' (yellow) - dashed
        g2d.draw(menuFace); //draw outline of passed rectangle (menu frame)

        g2d.setStroke(borderStoke); //set other border with no dashes (red)
        g2d.setColor(BORDER_COLOR); //set red
        g2d.draw(menuFace); //rerun to apply 2nd border

        g2d.setStroke(tmp);

        g2d.setColor(prev);
    }

    private void drawText(Graphics2D g2d){
        g2d.setColor(TEXT_COLOR); //assign text color in home screen

        FontRenderContext frc = g2d.getFontRenderContext();

        //set fonts to the 2d rectangles used for the text
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

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY); //draw the title string

        //same for credits - version
        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        //draw the string for version
        g2d.setFont(creditsFont);
        g2d.drawString(CREDITS,sX,sY);
    }

    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        //make rectangles for start and exit texts
        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);

        g2d.setFont(buttonFont); //set font

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

        //reset to originals
        x = startButton.x;
        y = startButton.y;

        y *= 1.2;

        menuButton.setLocation(x,y); //use these coordinates to set coordinates for menu button

        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        //same as before to set the menu text in the right coordinates with what was found above
        if(menuClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT,x,y);
        }

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){ //if user clicks start, enable the game by calling game board
            owner.enableGameBoard();

        }
        else if(menuButton.contains(p)){ //if in exit button, then exit the game
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) { //other for when start/exit is clicked
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        }
        else if(menuButton.contains(p)){
            menuClicked = true;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        }
        else if(menuClicked){
            menuClicked = false;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p) || menuButton.contains(p)) //if user hovering any button, then change to hand cursor, else default
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());

    }
}
