package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;

public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
    //Keylistener can focus on 3 things - keyPressed, keyReleased and keyTyped
    //MouseListener also has methods based on mouse interactions
    //MouseMotionListener - methods to track mouse movement (dragging or moving)

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);

    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Color.WHITE;

    private Timer gameTimer;

    private Wall wall;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private DebugConsole debugConsole;

    public GameBoard(JFrame owner){ //Jframe is a container which works like a window where you have components like labels, buttons, text
        //constructor
        super();

        strLen = 0;
        showPauseMenu = false;

        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);
        //type Font for Menu screen that has font mentioned and font size mentioned above

        this.initialize(); //set dimension, focus, and listeners from Component
        message = "";
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));
        //wall sets up the whole game frame with the game layout (bricks, ballpos (300x430)...)

        debugConsole = new DebugConsole(owner,wall,this); //setup debug console

        //initialize the first level
        wall.nextLevel();

        gameTimer = new Timer(10,e ->{
            wall.move(); //moving of player and ball from Wall
            wall.findImpacts(); //look for impacts from Wall
            message = String.format("Bricks: %d Balls %d",wall.getBrickCount(),wall.getBallCount());
            if(wall.isBallLost()){
                if(wall.ballEnd()){
                    wall.wallReset();
                    message = "Game over"; //if all balls lost
                }
                wall.ballReset(); //if user hasnt used all 3 balls
                gameTimer.stop();
            }
            else if(wall.isDone()){ //if all bricks broken
                if(wall.hasLevel()){ //if user has more levels left
                    message = "Go to Next Level";
                    gameTimer.stop();
                    //reset everything
                    wall.ballReset();
                    wall.wallReset();
                    wall.nextLevel();
                }
                else{ //if no more levels
                    message = "ALL WALLS DESTROYED";
                    gameTimer.stop();
                }
            }

            repaint();
        });

    }

    private void initialize(){ //set dimension, focus, and listeners from Component
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT)); //set size of game screen to whats mentioned above 600x450
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g; //get more control over geometry, coordinate transformations, color management, and text

        clear(g2d);

        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225); //whenever we print something on screen, we use this with the color we set earlier

        drawBall(wall.ball,g2d); //draws the ball using 2dgraphics

        for(Brick b : wall.bricks) //draws the bricks using 2dgraphics
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(wall.player,g2d); //draws the player bar using 2dgraphics

        if(showPauseMenu) //if user presses esc
            drawMenu(g2d); //draw the menu screen

        Toolkit.getDefaultToolkit().sync();
    }

    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    private void drawBrick(Brick brick,Graphics2D g2d){
        //draw and color the brick
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor()); //set color based on what we assigned before
        g2d.fill(brick.getBrick());  //fill the brick based on color we set

        g2d.setColor(brick.getBorderColor()); //set outline color based on what we assigned before
        g2d.draw(brick.getBrick()); //outline the brick based on color we set

        g2d.setColor(tmp);
    }

    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor(); //set tmp color to color in g2d

        Shape s = ball.getBallFace(); //set face of ball as shape

        g2d.setColor(ball.getInnerColor()); //set color of g2d as ball color (inner)
        g2d.fill(s); //use this color and fill shape of ball face

        g2d.setColor(ball.getBorderColor()); //set outline color based on what we assigned before
        g2d.draw(s); //draw only does the outline of a shape

        g2d.setColor(tmp); //set to tmp color
    }

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

    private void drawMenu(Graphics2D g2d){ //pass in g2d frame
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        //alpha refers to the opacity when in pause menu
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK); //set color to black
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT); //fill all of the screen rectangle with black

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont(); //get font saved in g2d
        Color tmpColor = g2d.getColor(); //get color save in g2d

        g2d.setFont(menuFont); //set font to menuFont
        g2d.setColor(MENU_COLOR); //set g2d color to MENU_COLOR

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width; //get width of the PAUSE and assign to strLen
        }

        int x = (this.getWidth() - strLen) / 2; //x-coordinate of where PAUSE will be
        int y = this.getHeight() / 10; //y-coordinate of where PAUSE will be

        g2d.drawString(PAUSE,x,y); //draw PAUSE string on frame

        //change x and y coordinates
        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


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

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) { //in charge of reacting to user keybinds
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                wall.player.moveLeft();
                break;
            case KeyEvent.VK_D:
                wall.player.movRight();
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
                wall.player.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        wall.player.stop();
    }

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
            message = "Restarting Game...";
            wall.ballReset();
            wall.wallReset();
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

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
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //make cursor hand cursor if hovering over buttons
            else
                this.setCursor(Cursor.getDefaultCursor()); //else leave normal cursor
        }
        else{
            this.setCursor(Cursor.getDefaultCursor()); //else leave normal cursor
        }
    }

    public void onLostFocus(){
        gameTimer.stop();
        message = "Focus Lost";
        repaint();
    }
}
