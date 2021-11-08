package test;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class DebugPanel extends JPanel { //Jpanel is used to organize components, layouts

    private static final Color DEF_BKG = Color.WHITE; //color of background

    private JButton skipLevel; //labeled button to skip Level when pushed
    private JButton resetBalls; //labeled button to reset balls when pushed

    private JSlider ballXSpeed; //let us graphically select x ball speed by sliding a knob
    private JSlider ballYSpeed; //let us graphically select y ball speed by sliding a knob

    private Wall wall; //create wall object

    public DebugPanel(Wall wall){ //pass in wall object

        this.wall = wall; //assign specific wall to this.wall

        initialize();

        skipLevel = makeButton("Skip Level",e -> wall.nextLevel()); //button to skip level
        resetBalls = makeButton("Reset Balls",e -> wall.resetBallCount()); //button to reset balls

        ballXSpeed = makeSlider(-4,4,e -> wall.setBallXSpeed(ballXSpeed.getValue())); //slider to adjust x speed of ball
        ballYSpeed = makeSlider(-4,4,e -> wall.setBallYSpeed(ballYSpeed.getValue())); //slider to adjust y speed of ball

        this.add(skipLevel); //add features mentioned above
        this.add(resetBalls);

        this.add(ballXSpeed);
        this.add(ballYSpeed);

    }

    private void initialize(){
        this.setBackground(DEF_BKG); //set background to white
        this.setLayout(new GridLayout(2,2)); //?
    }

    private JButton makeButton(String title, ActionListener e){
        //return a jbutton type
        JButton out = new JButton(title); //make name of button (title))
        out.addActionListener(e); //what is the action that needs to occur when clicked goes here
        return out;
    }

    private JSlider makeSlider(int min, int max, ChangeListener e){
        //return Jslider type
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1);
        out.setSnapToTicks(true);
        out.setPaintTicks(true);
        out.addChangeListener(e);
        return out;
    }

    public void setValues(int x,int y){
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }
}