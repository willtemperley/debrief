package org.mwc.debrief.core.wizards.core;

import java.beans.PropertyDescriptor;

import org.eclipse.jface.viewers.ISelection;
import org.mwc.debrief.core.wizards.CoreEditableWizardPage;

import MWC.GUI.Editable;
import MWC.GenericData.WorldDistance;

public class RangeBearingPage extends CoreEditableWizardPage
{

	public static class DataItem implements Editable
	{
		double _bearing = 0;
		WorldDistance _range = new WorldDistance(5, WorldDistance.NM);

		public double getBearing()
		{
			return _bearing;
		}

		public EditorType getInfo()
		{
			return null;
		}

		public String getName()
		{
			return null;
		}

		public WorldDistance getRange()
		{
			return _range;
		}

		public boolean hasEditor()
		{
			return false;
		}

		public void setBearing(double bearing)
		{
			_bearing = bearing;
		}

		public void setRange(WorldDistance range)
		{
			_range = range;
		}
	}

	public static String NAME = "Initial Offset";
	DataItem _myWrapper;
	final private String _rangeTitle;
	final private String _bearingTitle;

	protected RangeBearingPage(ISelection selection, String pageName,
			String pageDescription, String rangeTitle,String bearingTitle)
	{
		super(selection, NAME, pageName, pageDescription, "images/grid_wizard.gif",
				false);
		_rangeTitle = rangeTitle;
		_bearingTitle = bearingTitle;
	}

	protected PropertyDescriptor[] getPropertyDescriptors()
	{
		PropertyDescriptor[] descriptors =
		{
				prop("Range", _rangeTitle,
						getEditable()),
				prop("Bearing", _bearingTitle,
						getEditable()) };
		return descriptors;
	}

	protected Editable createMe()
	{
		if (_myWrapper == null)
			_myWrapper = new DataItem();

		return _myWrapper;
	}

}
