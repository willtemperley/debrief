/**
 * 
 */
package org.mwc.debrief.core.creators.shapes;

import MWC.GUI.Shapes.*;
import MWC.GenericData.*;

/**
 * @author ian.mayo
 *
 */
public class InsertRectangle extends CoreInsertShape
{


	/** produce the shape for the user
	 * 
	 * @param centre the current centre of the screen
	 * @return a shape, based on the centre
	 */
	protected PlainShape getShape(WorldLocation centre)
	{
		// generate the shape
		PlainShape res = new RectangleShape(
						centre, 
						centre.add(new WorldVector(MWC.Algorithms.Conversions.Degs2Rads(45), 0.05, 0)));
		return res;
	}	
	
	/** return the name of this shape, used give the shape an initial name
	 * 
	 * @return the name of this type of shape, eg: rectangle
	 */
	protected String getShapeName()
	{
		return "rectangle";
	}	
	
}