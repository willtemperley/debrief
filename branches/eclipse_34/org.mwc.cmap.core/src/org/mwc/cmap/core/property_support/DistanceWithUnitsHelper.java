/**
 * 
 */
package org.mwc.cmap.core.property_support;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

import MWC.GenericData.WorldDistanceWithUnits;

public class DistanceWithUnitsHelper extends EditorHelper
{

	/** constructor..
	 *
	 */
	public DistanceWithUnitsHelper()
	{
		super(WorldDistanceWithUnits.class);
	}

	/** create an instance of the cell editor suited to our data-type
	 * 
	 * @param parent
	 * @return
	 */
	public CellEditor getCellEditorFor(Composite parent)
	{
		return new ValueWithUnitsCellEditor(parent, "Distance", "Units")
		{
			/** the world distance we're editing
			 * 
			 */
			WorldDistanceWithUnits _myVal;
			
			/**
			 * @return
			 */
			protected int getUnitsValue()
			{
				return WorldDistanceWithUnits.getUnitIndexFor(_myVal.getUnitsLabel());
			}

			/**
			 * @return
			 */
			protected double getDoubleValue()
			{
				return _myVal.getDistance();
			}

			/**
			 * @return
			 */
			protected String[] getTagsList()
			{
				return WorldDistanceWithUnits.UnitLabels;
			}
			
			/**
			 * @param dist the value typed in
			 * @param units the units for the value
			 * @return an object representing the new data value
			 */
			protected Object createResultsObject(double dist, int units)
			{
				return new WorldDistanceWithUnits(dist, units);
			}

			/** convert the object to our data units
			 * 
			 * @param value
			 */
			protected void storeMe(Object value)
			{
				_myVal = (WorldDistanceWithUnits) value;
			}
		};
	}

	public ILabelProvider getLabelFor(Object currentValue)
	{
		ILabelProvider label1 = new LabelProvider()
		{
			public String getText(Object element)
			{
				WorldDistanceWithUnits val = (WorldDistanceWithUnits) element;
				return val.toString();
			}

			public Image getImage(Object element)
			{
				return null;
			}

		};
		return label1;
	}
	

	public Control getEditorControlFor(Composite parent, final DebriefProperty property)
	{
		// TODO create the editor
		final Button myCheckbox = new Button(parent, SWT.CHECK);
		myCheckbox.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				Boolean val = new Boolean(myCheckbox.getSelection());
				property.setValue(val);
			}});
		return myCheckbox;
	}	
}