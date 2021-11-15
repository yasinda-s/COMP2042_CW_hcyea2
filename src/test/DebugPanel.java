package test;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This is the class for the Debug Panel which helps the user Select the Level, Reset the ball count and to set the ball speed.
 *
 * Refactoring -
 *
 * GamePlay which is a public class is already being passed into the Constructor as a parameter so we do not need to create an instance of it prior
 * to the constructor, hence it has been removed.
 *
 * The private Jbuttons for skipLevel and resetBalls are also refactored to be created in the constructor itself.
 */

public class DebugPanel extends JPanel { //Jpanel is used to organize components, layouts

    private static final Color DEF_BKG = Color.WHITE; //color of background

    private JSlider ballXSpeed; //let us graphically select x ball speed by sliding a knob
    private JSlider ballYSpeed; //let us graphically select y ball speed by sliding a knob

    /**
     * This is the constructor for the DebugPanel.
     * @param gamePlay Takes in GamePlay object.
     */
    public DebugPanel(GamePlay gamePlay){ //pass in gamePlay object

        initialize();

        //labeled button to skip Level when pushed
        JButton skipLevel = makeButton("Skip Level", e -> gamePlay.nextLevel()); //button to skip level
        //labeled button to reset balls when pushed
        JButton resetBalls = makeButton("Reset Balls", e -> gamePlay.resetBallCount()); //button to reset balls

        ballXSpeed = makeSlider(-4,4,e -> gamePlay.setBallXSpeed(ballXSpeed.getValue())); //slider to adjust x speed of ball
        ballYSpeed = makeSlider(-4,4,e -> gamePlay.setBallYSpeed(ballYSpeed.getValue())); //slider to adjust y speed of ball

        this.add(skipLevel); //add features mentioned above
        this.add(resetBalls);

        this.add(ballXSpeed);
        this.add(ballYSpeed);
    }

    /**
     * This method is used to set the background color of the Panel and to set its Grid Layout.
     */
    private void initialize(){
        this.setBackground(DEF_BKG); //set background to white
        this.setLayout(new GridLayout(2,2)); //?
    }

    /**
     * This method is used to create a Button in the Debug Panel.
     * @param title String type, contains the text to be placed on the Button.
     * @param e ActionListener type, contains the action that needs to occur when button is clicked.
     * @return Returns the created Button.
     */
    private JButton makeButton(String title, ActionListener e){
        //return a jbutton type
        JButton out = new JButton(title); //make name of button (title))
        out.addActionListener(e); //what is the action that needs to occur when clicked goes here
        return out;
    }

    /**
     * This method is used to create a Slider in the Debug Panel.
     * @param min Consists of the minimum point in the slider.
     * @param max Consists of the maximum point in the slider.
     * @param e Consists of the effect that needs to take place when slider is moved (Altering speed of ball)
     * @return Returns the created Slider to affect Speed of ball.
     */
    private JSlider makeSlider(int min, int max, ChangeListener e){
        //return Jslider type
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1);
        out.setSnapToTicks(true);
        out.setPaintTicks(true);
        out.addChangeListener(e);
        return out;
    }

    /**
     * This method is used to set the speed of the ball for both x, y axes.
     * @param x The speed of the ball on x-axis.
     * @param y The speed of the ball on y-axis.
     */
    public void setValues(int x,int y){
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }
}