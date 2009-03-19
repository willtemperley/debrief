/**
 * Debrief.Wrappers.SensorContactWrapper
 */
// / Copyright MWC 1999, Debrief 3 Project
// $RCSfile: SensorContactWrapper.java,v $
// @author $Author: ian.mayo $
// @version $Revision: 1.13 $
// $Log: SensorContactWrapper.java,v $
// Revision 1.13  2007/04/25 09:32:44  ian.mayo
// Prevent highlight being plotted for sensor & TMA data
//
// Revision 1.12 2006/02/13 16:19:07 Ian.Mayo
// Sort out problem with creating sensor data
//
// Revision 1.11 2006/01/06 10:37:42 Ian.Mayo
// Reflect tidying of sensor wrapper naming
//
// Revision 1.10 2005/12/13 09:04:59 Ian.Mayo
// Tidying - as recommended by Eclipse
//
// Revision 1.9 2005/06/07 08:38:30 Ian.Mayo
// Provide efficiency to stop millions of popup menu items representing hidden
// tma solutions.
//
// Revision 1.8 2005/06/06 14:45:05 Ian.Mayo
// Refactor how we support tma & sensor data
//
// Revision 1.7 2005/03/10 09:44:23 Ian.Mayo
// Tidy implementation where we have sensor/tua data beyond time period of
// parent track. Prevent error being thrown.
//
// Revision 1.6 2005/02/22 09:31:57 Ian.Mayo
// Refactor snail plotting sensor & tma data - so that getting & managing valid
// data points are handled in generic fashion. We did have two very similar
// implementations, tracking errors introduced after hi-res-date changes was
// proving expensive/unreliable. All fine now though.
//
// Revision 1.5 2004/12/17 15:53:59 Ian.Mayo
// Get on top of some problems plotting sensor & tma data.
//
// Revision 1.4 2004/12/16 11:33:03 Ian.Mayo
// Handle when sensor data is outside period of parent track - and we can't find
// parent fixes to attached sensor contact line to
//
// Revision 1.3 2004/11/25 10:24:47 Ian.Mayo
// Switch to Hi Res dates
//
// Revision 1.2 2003/10/27 12:59:47 Ian.Mayo
// Tidy up duplicate comments
//
// Revision 1.1.1.2 2003/07/21 14:49:24 Ian.Mayo
// Re-import Java files to keep correct line spacing
//
// Revision 1.14 2003-07-16 12:50:11+01 ian_mayo
// Improve text in multi-line tooltip
//
// Revision 1.13 2003-07-04 10:59:23+01 ian_mayo
// reflect name change in parent testing class
//
// Revision 1.12 2003-06-30 09:14:15+01 ian_mayo
// Improve labels
//
// Revision 1.11 2003-06-25 10:44:21+01 ian_mayo
// Provide multi-line tooltip
//
// Revision 1.10 2003-06-16 11:58:52+01 ian_mayo
// Correctly implement compareTo method (we weren't returning equality value for
// when same object is being compared).
//
// Revision 1.9 2003-03-19 15:36:53+00 ian_mayo
// improvements according to IntelliJ inspector
//
// Revision 1.8 2002-10-30 16:27:26+00 ian_mayo
// tidy up (shorten) display names of editables
//
// Revision 1.7 2002-10-01 15:41:41+01 ian_mayo
// make final methods & classes final (following IDEA analysis)
//
// Revision 1.6 2002-07-10 14:59:26+01 ian_mayo
// handle correct returning of nearest points - zero length list instead of null
// when no matches
//
// Revision 1.5 2002-07-08 09:47:45+01 ian_mayo
// <>
//
// Revision 1.4 2002-06-05 12:56:29+01 ian_mayo
// unnecessarily loaded
//
// Revision 1.3 2002-05-31 16:18:44+01 ian_mayo
// Don't store the far end any more
//
// Revision 1.2 2002-05-28 09:25:13+01 ian_mayo
// after switch to new system
//
// Revision 1.1 2002-05-28 09:11:39+01 ian_mayo
// Initial revision
//
// Revision 1.0 2002-04-30 09:14:54+01 ian
// Initial revision
//
// Revision 1.1 2002-04-23 12:28:26+01 ian_mayo
// Initial revision
//
// Revision 1.8 2002-02-26 15:49:08+00 administrator
// Reflect new TextLabel signature
//
// Revision 1.7 2001-10-02 10:08:41+01 administrator
// remove d-line
//
// Revision 1.6 2001-10-01 12:49:49+01 administrator
// the getNearest method of WatchableList now returns an array of points (since
// a contact wrapper may contain several points at the same DTG). We have had to
// reflect this across the application
//
// Revision 1.5 2001-10-01 11:22:15+01 administrator
// Change COMPARABLE so that multiple contacts with the same DTG are not
// rejected
//
// Revision 1.4 2001-08-29 19:17:01+01 administrator
// <>
//
// Revision 1.3 2001-08-21 15:19:54+01 administrator
// Tidy up constructor code
//
// Revision 1.2 2001-08-21 12:05:33+01 administrator
// General tidying up, plus extension of testing to tidily manage missing
// origins for sensor data
//
// Revision 1.1 2001-08-14 14:07:38+01 administrator
// Finishing the implementation
//
// Revision 1.0 2001-08-09 14:16:51+01 administrator
// Initial revision
//

package Debrief.Wrappers;

import MWC.GUI.CanvasType;
import MWC.GUI.Plottable;
import MWC.GenericData.*;
import MWC.Utilities.TextFormatting.DebriefFormatDateTime;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import Debrief.GUI.Tote.Painters.SnailDrawTMAContact;
import Debrief.GUI.Tote.Painters.SnailPainter.DoNotHighlightMe;

public final class SensorContactWrapper extends
		SnailDrawTMAContact.PlottableWrapperWithTimeAndOverrideableColor implements
		MWC.GUI.Plottable, Debrief.Tools.Tote.Watchable,
		CanvasType.MultiLineTooltipProvider, DoNotHighlightMe
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ///////////////////////////////////////////
	// member variables
	/**
	 * /////////////////////////////////////////////
	 */
	private String _trackName;

	/**
	 * long _DTG
	 */
	private HiResDate _DTG;

	/**
	 * Range to target (in Degrees)
	 */
	private WorldDistance _range;

	/**
	 * Bearing to target (in Radians)
	 */
	private double _bearing;

	/**
	 * origin of the target, or null to read origin from host vessel
	 */
	private WorldLocation _origin;

	/**
	 * whether to show the label
	 */
	private boolean _showLabel = false;

	/**
	 * our editor
	 */
	transient private MWC.GUI.Editable.EditorType _myEditor;

	/**
	 * the style to plot this line
	 */
	private int _myLineStyle = 0;

	/**
	 * the parent object (which supplies our colour, should we need it)
	 */
	private SensorWrapper _mySensor;

	/**
	 * the label describing this contact
	 */
	private final MWC.GUI.Shapes.TextLabel _theLabel;

	/**
	 * whereabouts on the line where we plot the label
	 */
	private int _theLineLocation = MWC.GUI.Properties.LineLocationPropertyEditor.END;

	private String _sensorName;

	// ///////////////////////////////////////////
	// constructor
	// ///////////////////////////////////////////

	/**
	 * default constructor, used when we read in from XML
	 */
	public SensorContactWrapper()
	{
		// create the label
		_theLabel = new MWC.GUI.Shapes.TextLabel(new WorldLocation(0, 0, 0), null);

		setVisible(true);
		setLabelVisible(false);
	}

	/**
	 * build a new sensor contact wrapper
	 * 
	 * @param trackName
	 * @param dtg
	 * @param rangeYds
	 * @param bearingDegs
	 * @param origin
	 * @param color
	 * @param label
	 * @param style
	 * @param sensorName
	 *          TODO
	 * @param sensorName
	 */
	public SensorContactWrapper(final String trackName, final HiResDate dtg,
			final WorldDistance range, final double bearingDegs, final WorldLocation origin,
			final java.awt.Color color, final String label, final int style, String sensorName)
	{
		this();
		_trackName = trackName;
		_DTG = dtg;
		_range = range;
		_bearing = MWC.Algorithms.Conversions.Degs2Rads(bearingDegs);

		// store the origin, and update the far end if required
		setOrigin(origin);

		// and the gui parameters
		setColor(color);
		_myLineStyle = style;
		_theLabel.setLocation(origin);
		_theLabel.setString(label);
		_sensorName = sensorName;
	}

	// ///////////////////////////////////////////
	// accessor methods
	// ///////////////////////////////////////////

	/**
	 * set the origin for this object
	 */
	public final void setOrigin(final WorldLocation val)
	{
		_origin = val;
	}

	public WorldLocation getOrigin()
	{
		return _origin;
	}
		
	/**
	 * return the coordinates for the start of the line
	 */
	public final WorldLocation getOrigin(final Debrief.Tools.Tote.WatchableList parent)
	{
		if ((_origin == null) && (parent != null))
		{
			// get the origin
			final Debrief.Tools.Tote.Watchable[] list = parent.getNearestTo(_DTG);
			Debrief.Tools.Tote.Watchable wa = null;
			if (list.length > 0)
				wa = list[0];

			// did we find it?
			if (wa != null)
			{
				// yes, store it
				_origin = wa.getLocation();

			}

		}

		return _origin;
	}

	/**
	 * return the coordinates of the end of hte line
	 */
	public final WorldLocation getFarEnd()
	{
		WorldLocation res = null;

		if (_origin != null)
		{
			// also do the far end
			res = _origin.add(new WorldVector(_bearing, _range.getValueIn(WorldDistance.DEGS), 0d));
		}

		return res;
	}

	/**
	 * getTrackName
	 * 
	 * @return the returned String
	 */
	public final String getTrackName()
	{
		return _trackName;
	}

	/**
	 * getDTG
	 * 
	 * @return the returned long
	 */
	public final HiResDate getDTG()
	{
		return _DTG;
	}

	/**
	 * set the time
	 */
	public final void setDTG(final HiResDate val)
	{
		_DTG = val;
	}

	

	public final void setColor(final Color val)
	{
		super.setColor(val);
	}

	/**
	 * member function to meet requirements of comparable interface *
	 */
	public final int compareTo(Plottable o)
	{
		final SensorContactWrapper other = (SensorContactWrapper) o;
		int res = 0;
		if (_DTG.lessThan(other._DTG))
			res = -1;
		else if (_DTG.greaterThan(other._DTG))
			res = 1;
		else
		{
			// just check if this is actually the same object (in which case return 0)
			if (o == this)
			{
				// we need a correct implementation of compare to for when we're finding
				// the position
				// of an item which is actually in the list - otherwise it won't get
				// found and we can't
				// delete it.
				res = 0;
			}
			else
			{
				// same times, make the newer item appear later. This is to overcome the
				// problem we experience where only the first contact at a particular
				// DTG gets recorded for a sensor
				res = 1;
			}
		}

		return res;

	}

	// ///////////////////////////////////////////
	// member methods to meet requirements of Plottable interface
	// ///////////////////////////////////////////

	/**
	 * paint this object to the specified canvas
	 */
	public final void paint(final MWC.GUI.CanvasType dest)
	{
		// DUFF METHOD TO MEET INTERFACE REQUIREMENTS
	}

	/**
	 * paint this object to the specified canvas
	 * 
	 * @param track
	 *          the parent list (from which we calculate origins if required)
	 * @param dest
	 *          where we're painting it to
	 * @param keep_simple
	 *          whether to allow a change in line style
	 */
	public final void paint(final Debrief.Tools.Tote.WatchableList track,
			final MWC.GUI.CanvasType dest, final boolean keep_simple)
	{
		if (!getVisible())
			return;

		// do we know our track?
		if (track == null)
		{
			MWC.Utilities.Errors.Trace.trace("failed to find track for sensor data:"
					+ this.getLabel());
			return;
		}

		// do we need an origin
		final WorldLocation origin = getOrigin(track);

		final TimePeriod trackPeriod = new TimePeriod.BaseTimePeriod(track.getStartDTG(),
				track.getEndDTG());
		if (!trackPeriod.contains(this.getTime()))
		{
			// don't bother trying to plot it, we're outside the parent period
			return;
		}

		// ok, we have the start - convert it to a point
		final Point pt = new Point(dest.toScreen(origin));

		// and convert to screen coords
		final WorldLocation theFarEnd = getFarEnd();
		final Point farEnd = dest.toScreen(theFarEnd);

		// set the colour
		dest.setColor(getColor());

		// only use line styles if we are allowed to (it is a particular problem
		// when in snail mode)
		if (!keep_simple)
		{
			// set the line style
			dest.setLineStyle(_myLineStyle);
		}

		// draw the line
		dest.drawLine(pt.x, pt.y, farEnd.x, farEnd.y);

		if (!keep_simple)
		{
			// restore the solid line style, for the next poor bugger
			dest.setLineStyle(MWC.GUI.CanvasType.SOLID);
		}

		// only plot the label if we don't want it simple
		if (!keep_simple)
		{

			// now draw the label
			if (getLabelVisible())
			{
				WorldLocation labelPos = null;

				// sort out where to plot it
				if (_theLineLocation == MWC.GUI.Properties.LineLocationPropertyEditor.START)
				{
					// use the start
					labelPos = origin;
				}
				else if (_theLineLocation == MWC.GUI.Properties.LineLocationPropertyEditor.END)
				{
					// put it at the end
					labelPos = theFarEnd;
				}
				else if (_theLineLocation == MWC.GUI.Properties.LineLocationPropertyEditor.MIDDLE)
				{
					// calculate the centre point
					final WorldArea tmpArea = new WorldArea(origin, theFarEnd);
					labelPos = tmpArea.getCentre();
				}

				// update it's location
				_theLabel.setLocation(labelPos);
				_theLabel.setColor(getColor());
				_theLabel.paint(dest);
			}
		}
	}

	/**
	 * method to reset the colour, so that we take that of our parent
	 */
	public final void resetColor()
	{
		setColor(null);
	}

	/**
	 * find the name of the sensor which recorded this contact
	 */
	public final String getSensorName()
	{
		String res;
		if (_mySensor != null)
		{
			res = _mySensor.getName();
		}
		else
		{
			res = _sensorName;
		}
		return res;
	}

	/**
	 * find the data area occupied by this item
	 */
	public final MWC.GenericData.WorldArea getBounds()
	{
		WorldArea res = null;
		final WorldLocation origin = getOrigin(null);

		// do we know our origin?
		if (origin != null)
			res = new WorldArea(getOrigin(null), getFarEnd());

		// done.
		return res;
	}

	/**
	 * get the bearing (in degrees)
	 */
	public final double getBearing()
	{
		return MWC.Algorithms.Conversions.Rads2Degs(_bearing);
	}

	/**
	 * set the bearing (in degrees)
	 */
	public final void setBearing(final double degs)
	{
		_bearing = MWC.Algorithms.Conversions.Degs2Rads(degs);
	}

	/**
	 * get the range (in yards)
	 */
	public final WorldDistance getRange()
	{
		return _range;
	}

	/**
	 * set the range (in yards)
	 */
	public final void setRange(WorldDistance dist)
	{
		_range = dist;
	}
	

	/**
	 * it this Label item currently visible?
	 */
	public final boolean getLabelVisible()
	{
		return _showLabel;
	}

	/**
	 * set the Label visibility
	 */
	public final void setLabelVisible(final boolean val)
	{
		_showLabel = val;
	}

	/**
	 * return the location of the label
	 */
	public final void setLabelLocation(final Integer loc)
	{
		_theLabel.setRelativeLocation(loc);
	}

	/**
	 * update the location of the label
	 */
	public final Integer getLabelLocation()
	{
		return _theLabel.getRelativeLocation();
	}

	/**
	 * return the location of the label
	 */
	public final void setPutLabelAt(final Integer loc)
	{
		_theLineLocation = loc.intValue();
	}

	/**
	 * update the location of the label
	 */
	public final Integer getPutLabelAt()
	{
		return new Integer(_theLineLocation);
	}

	/**
	 * update the line style
	 */
	public final void setLineStyle(final Integer style)
	{
		_myLineStyle = style.intValue();
	}

	/**
	 * retrieve the line style
	 */
	public final Integer getLineStyle()
	{
		return new Integer(_myLineStyle);
	}

	/**
	 * inform us of our sensor
	 */
	public final void setSensor(final SensorWrapper sensor)
	{
		_mySensor = sensor;
	}

	/**
	 * get the label for this data item
	 */
	public final String getLabel()
	{
		return _theLabel.getString();
	}

	/**
	 * set the label for this data item
	 */
	public final void setLabel(final String val)
	{
		_theLabel.setString(val);
	}

	/**
	 * how far away are we from this point? or return null if it can't be
	 * calculated
	 */
	public final double rangeFrom(final MWC.GenericData.WorldLocation other)
	{
		// return the distance from each end
		double res = Double.MAX_VALUE;

		if (getVisible())
		{
			// get the range from the origin
			if (_origin != null)
				res = _origin.rangeFrom(other);

			final WorldLocation farEnd = getFarEnd();

			// and get the range from the far end
			if (farEnd != null)
				res = Math.min(res, farEnd.rangeFrom(other));

			// lastly determine the range from the nearest point on the track
			if ((_origin != null) && (farEnd != null))
			{
				WorldDistance dist = other.rangeFrom(_origin, farEnd);
				res = Math.min(res, dist.getValueIn(WorldDistance.DEGS));
			}
		}

		return res;
	}

	/**
	 * getInfo
	 * 
	 * @return the returned MWC.GUI.Editable.EditorType
	 */
	public final MWC.GUI.Editable.EditorType getInfo()
	{
		if (_myEditor == null)
			_myEditor = new SensorContactInfo(this);

		return _myEditor;
	}

	/**
	 * method to provide the actual colour value stored in this fix
	 * 
	 * @return fix colour, including null if applicable
	 */
	public final Color getActualColor()
	{
		return super.getColor();
	}

	/**
	 * get the colour (or that of our parent, if we don't have one
	 */
	public final java.awt.Color getColor()
	{
		java.awt.Color res = super.getColor();

		// has our colour been set?
		if (res == null)
		{
			// no, get the colour from our parent
			res = _mySensor.getColor();
		}

		return res;
	}

	/**
	 * whether there is any edit information for this item this is a convenience
	 * function to save creating the EditorType data first
	 * 
	 * @return yes/no
	 */
	public final boolean hasEditor()
	{
		return true;
	}

	/**
	 * get the name of this entry, using the formatted DTG
	 */
	public final String getName()
	{
		return DebriefFormatDateTime.toStringHiRes(_DTG);
	}

	/**
	 * get the data name in multi-line format (for tooltips)
	 * 
	 * @return multi-line text label
	 */
	public String getMultiLineName()
	{
		return "Sensor:" + getSensorName() + "\nDTG:" + getName() + "\nTrack:" + getLabel();
	}

	/**
	 * toString
	 * 
	 * @return the returned String
	 */
	public final String toString()
	{
		return getName();
	}

	// ////////////////////////////////////////////////////////////
	// methods to support Watchable interface
	// ////////////////////////////////////////////////////////////
	/**
	 * get the current location of the watchable
	 * 
	 * @return the location
	 */
	public final WorldLocation getLocation()
	{
		return this.getOrigin(null);
	}

	/**
	 * get the current course of the watchable (rads)
	 * 
	 * @return course in radians
	 */
	public final double getCourse()
	{
		return -1;
	}

	/**
	 * get the current speed of the watchable (kts)
	 * 
	 * @return speed in knots
	 */
	public final double getSpeed()
	{
		return -1;
	}

	/**
	 * get the current depth of the watchable (m)
	 * 
	 * @return depth in metres
	 */
	public final double getDepth()
	{
		return 0;
	}

	/**
	 * find out the time of this watchable
	 */
	public final HiResDate getTime()
	{
		return this.getDTG();
	}

	// //////////////////////////////////////////////////////////////////////////
	// embedded class, used for editing the projection
	// //////////////////////////////////////////////////////////////////////////
	/**
	 * the definition of what is editable about this object
	 */
	public static final class SensorContactInfo extends MWC.GUI.Editable.EditorType
	{

		/**
		 * constructor for editable details of a set of Layers
		 * 
		 * @param data
		 *          the Layers themselves
		 */
		public SensorContactInfo(final SensorContactWrapper data)
		{
			super(data, data.getName(), "Sensor");
		}

		/**
		 * The things about these Layers which are editable. We don't really use
		 * this list, since we have our own custom editor anyway
		 * 
		 * @return property descriptions
		 */
		public final PropertyDescriptor[] getPropertyDescriptors()
		{
			try
			{
				final PropertyDescriptor[] res = {
						prop("Label", "the label for this data item"),
						prop("Visible", "whether this sensor contact data is visible"),
						prop("LabelVisible", "whether the label for this contact is visible"),
						prop("Color", "the color for this sensor contact"),
						longProp("LabelLocation", "the label location",
								MWC.GUI.Properties.LocationPropertyEditor.class),
						longProp("PutLabelAt", "whereabouts on the line to position the label",
								MWC.GUI.Properties.LineLocationPropertyEditor.class),
						longProp("LineStyle", "style to use to plot the line",
								MWC.GUI.Properties.LineStylePropertyEditor.class) };


				// see if we need to add rng/brg or origin data
				SensorContactWrapper tc = (SensorContactWrapper) getData();
				final PropertyDescriptor[] res1; 
				if(tc.getOrigin() == null)
				{
					// has origin
					final PropertyDescriptor[] res2 = 
					{
							prop("Range", "range to centre of solution", SPATIAL),
							prop("Bearing", "bearing to centre of solution", SPATIAL)
					};
					res1 = res2;					
				}
				else
				{
					// rng, brg data
					final PropertyDescriptor[] res2 = 
					{
							prop("Origin", "centre of solution", SPATIAL)
					};
					res1 = res2;
				}
				
				PropertyDescriptor[] res3 = new PropertyDescriptor[res.length + res1.length];
				System.arraycopy(res, 0, res3, 0, res.length);
				System.arraycopy(res1, 0, res3, res.length, res1.length);
								
				return res3;				
				
			}
			catch (IntrospectionException e)
			{
				return super.getPropertyDescriptors();
			}
		}

		/**
		 * getMethodDescriptors
		 * 
		 * @return the returned MethodDescriptor[]
		 */
		public final MethodDescriptor[] getMethodDescriptors()
		{
			// just add the reset color field first
			final Class<SensorContactWrapper> c = SensorContactWrapper.class;
			final MethodDescriptor[] mds = { method(c, "resetColor", null, "Reset Color"), };
			return mds;
		}

	}

	// ////////////////////////////////////////////////////
	// nested class
	// /////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// testing for this class
	// ////////////////////////////////////////////////////////////////////////////////////////////////

	static public final class testSensorContact extends junit.framework.TestCase
	{
		static public final String TEST_ALL_TEST_TYPE = "UNIT";

		public testSensorContact(final String val)
		{
			super(val);
		}

		public final void testMyCode()
		{
			// setup our object to be tested
			final WorldLocation origin = new WorldLocation(2, 2, 0);
			final SensorContactWrapper ed = new SensorContactWrapper("blank track",
					new HiResDate(new java.util.Date().getTime()), new WorldDistance(3000, WorldDistance.YARDS), 55, origin,
					java.awt.Color.red, "my label", 1, "theSensorName");

			// check the editable parameters
			editableTesterSupport.testParams(ed, this);

			/**
			 * test the distance calcs
			 */
			final WorldVector test_vector = new WorldVector(MWC.Algorithms.Conversions
					.Degs2Rads(335), MWC.Algorithms.Conversions.Yds2Degs(4000), 0);
			final WorldLocation test_end = origin.add(test_vector);
			final WorldVector test_other_vector = new WorldVector(MWC.Algorithms.Conversions
					.Degs2Rads(55), MWC.Algorithms.Conversions.Yds2Degs(4000), 0);
			final WorldLocation test_other_end = origin.add(test_other_vector);

			// ok, now test that we find the distance from the indicated point
			double dist = MWC.Algorithms.Conversions.Degs2Yds(ed.rangeFrom(test_end));
			assertEquals("find nearest from origin", dist, 4000d, 0.001);

			dist = MWC.Algorithms.Conversions.Degs2Yds(ed.rangeFrom(test_other_end));
			assertEquals("find nearest from far end", dist, 1000d, 0.001);

		}

	}

}