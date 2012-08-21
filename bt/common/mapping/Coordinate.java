/**
 * Created on 14/11/2005
 * <p>Title: Miradan Phedd</p>
 *
 * <p>Description: Library for all Miradan related work</p>
 *
 * <p>Copyright: Copyright (c) 2004-2005</p>
 *
 * <p>Company: Miradan Phedd</p>
 *
 * @author Daniel Cleyne
 * @version 0.1
 */
package bt.common.mapping;

import java.io.Serializable;

/**
 * Coords stores x and y values.  Since these are hexes,
 * coordinates with odd x values are a half-hex down.
 * 
 * Directions work clockwise around the hex, starting with
 * zero at the top.
 * 
 *          -y
 *          0
 *        _____
 *     5 /     \ 1
 * -x   /       \   +x
 *      \       /
 *     4 \_____/ 2
 *          3
 *          +y
 * Code in this class copyright BMazur and curtesy of the MegaMek project
 * http://megamek.sourceforge.org
 * portions based off of http://www.rossmack.com/ab/RPG/traveller/AstroHexDistance.asp
 */

public class Coordinate implements Serializable
{
    static final long serialVersionUID = 1;

    public int            x;
    public int            y;
    

    public static final double HEXSIDE = Math.PI / 3.0;
    /**
     * Constructs a new coordinate pair at (0, 0).
     */
    public Coordinate() {
        this(0, 0);
    }

    /**
     * Constructs a new coordinate pair at (x, y).
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructs a new coordinate pair that is a duplicate of the
     * parameter.
     */
    public Coordinate(Coordinate c) {
        this(c.x, c.y);
    }
    
    /**
     * Add by Daniel Cleyne. Allows the Coordinate object to be used as a reliable key in HashMap.
     */
    public int hashCode()
    {
    	return getBoardNum().hashCode();
    }
    
    public void setCoordinate(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    /**
     * Returns a new coordinate that represents the coordinate 1 unit
     * in the specified direction.
     * 
     * @return the new coordinate, if the direction is valid;
     *  otherwise, a new copy of this coordinate.
     * @param dir the direction.
     */
    public final Coordinate translated(int dir) {
        return new Coordinate(xInDir(x, y, dir), yInDir(x, y, dir));
    }
    
    /**
     * Returns the x parameter of the coordinates in the direction
     */
    public final static int xInDir(int x, int y, int dir) {
         switch (dir) {
             case 1 :
             case 2 :
                 return x + 1;
             case 4 :
             case 5 :
                 return x - 1;
             default :
                 return x;
         }
    }
    
    /**
     * Returns the y parameter of the coordinates in the direction
     */
    public final static int yInDir(int x, int y, int dir) {
        switch (dir) {
            case 0 : 
                return y - 1;
            case 1 : 
            case 5 :
                return y - ((x + 1) & 1);
            case 2 : 
            case 4 : 
                return y + (x & 1);
            case 3 : 
                return y + 1;
            default :
                return y;
        }
    }
    
    /**
     * Tests whether the x coordinate of this coordinate is odd.  This
     * is significant in determining where this coordinate lies in
     * relation to other coordinates.
     */
    public boolean isXOdd() {
        return (x & 1) == 1;
    }
    
    /**
     * Returns a string representing a coordinate in "board number" format.
     */
    public final String getBoardNum() {
        StringBuffer num = new StringBuffer();
        
        num.append((x > -1 && x < 9 ? "0" : "") + (x + 1));
        num.append((y > -1 && y < 9 ? "0" : "") + (y + 1));

        return num.toString(); 
    }
    
    /**
     * Coordinate are equal if their x and y components are equal
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Coordinate other = (Coordinate)object;
        return other.x == this.x && other.y == this.y;
    }

    public String toString() {
        return "Coordinate (" + x + ", " + y + "); " + getBoardNum();
    }

    public String toShortString() 
    {
    	String xVal = Integer.toString(x + 1);
    	if (x + 1 < 10) 
    		xVal = "0" + xVal; 
    	String yVal = Integer.toString(y + 1);
    	if (y + 1 < 10) 
    		yVal = "0" + yVal; 
        return "(" + xVal + "," + yVal + ")";
    }
}
