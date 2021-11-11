package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * The Cement Brick refers to bricks that are grey in color and from cracks on impact. It inherits from the Brick Class directly.
 */
public class CementBrick extends Brick {

    //properties of cement brick shown below - has cracks before it breaks
    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = new Color(147, 147, 147);
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private Crack crack;
    private Shape brickFace;

    /**
     * This is the constructor for the CementBrick, it inherits most from the super constructor.
     * @param point Refers to the top left coordinates of the Clay Brick.
     * @param size Refers to the dimension of the brick - width and height.
     */
    public CementBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH); //set all the super constructor variables
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickFace = super.brickFace;
    }

    /**
     * Method to make the Clay Brick's Face (Rectangle Shape)
     * @param pos The top left position (coordinates) of the brick.
     * @param size The width and height of the brick (Dimension).
     * @return
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    } //make a brick face instantly with the pos and dimension of brick passed

    /**
     * This method handles strength reduction of Cement Bricks on impact. It also checks whether the cement brick is going to crack or break completely.
     * @param point Refers to the edge of the ball making contact with the brick.
     * @param dir Refers to the direction (edge) of the brick that is making contact with the ball.
     * @return Returns boolean value based on whether the brick makes or not.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) { //when ball makes impact with cement brick - crack or broken?
        if(super.isBroken())
            return false;
        super.impact();
        if(!super.isBroken()){
            crack.makeCrack(point,dir);
            updateBrick();
            return false;
        }
        return true;
    }

    /**
     * Getter for a singular Cement Brick.
     * @return Returns the Shape of the brick.
     */
    @Override
    public Shape getBrick() {
        return brickFace;
    }

    /**
     * This method is used to draw the cracks on the Cement Brick.
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.draw();
            gp.append(super.brickFace,false);
            brickFace = gp;
        }
    }

    /**
     * Resets the strength of a brick to its full state.
     */
    public void repair(){
        super.repair();
        crack.reset();
        brickFace = super.brickFace;
    }
}
