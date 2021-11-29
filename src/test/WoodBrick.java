package test;

import java.awt.*;

public class WoodBrick extends Brick{ //make interface and change how the brick only breaks from below or above impact
    private static final String NAME = "Wood Brick";
    private static final Color DEF_INNER = new Color(139, 110, 43);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int WOOD_STRENGTH = 2; //breaks in one impact with the ball

    public WoodBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,WOOD_STRENGTH);
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

        if(strength<WOOD_STRENGTH){
            inner = new Color(212, 187, 126);
        }

        broken = (strength == 0);
    }
}
