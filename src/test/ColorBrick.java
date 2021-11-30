package test;

import java.awt.*;
import java.util.Random;

public class ColorBrick extends Brick{ //make interface and change how the brick only breaks from below or above impact
    Random rand = new Random();
    private static final String NAME = "Color Brick";
    private static final Color DEF_INNER = new Color(0, 0, 0);
    private static final Color DEF_BORDER = Color.WHITE;
    private static final int COLOR_STRENGTH = 3; //breaks in one impact with the ball

    public ColorBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,COLOR_STRENGTH);
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    @Override
    public Shape getBrick() {
        return super.brickFace;
    }

    /**
     * Method used to decrease the strength of a brick on impact.
     */
    public void impact(){
        strength--;
        if(strength==2){
            inner = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }else if(strength==1){
            inner = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        broken = (strength == 0);
    }
}
