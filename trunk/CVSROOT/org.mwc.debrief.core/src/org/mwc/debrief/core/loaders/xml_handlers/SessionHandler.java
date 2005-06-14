package org.mwc.debrief.core.loaders.xml_handlers;

import java.util.Vector;

import org.eclipse.core.runtime.IAdaptable;
import org.mwc.cmap.core.DataTypes.TrackData.TrackManager;
import org.mwc.cmap.core.interfaces.IControllableViewport;
import org.mwc.debrief.core.editors.PlotEditor;

import Debrief.ReaderWriter.XML.DebriefLayersHandler;
import MWC.Algorithms.PlainProjection;
import MWC.GUI.Layers;

/**
 * Title:        Debrief 2000
 * Description:  Debrief 2000 Track Analysis Software
 * Copyright:    Copyright (c) 2000
 * Company:      MWC
 * @author Ian Mayo
 * @version 1.0
 */


public class SessionHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{
  public SessionHandler(Layers _theLayers, 
  		final IControllableViewport  view)
  {
    // inform our parent what type of class we are
    super("session");

    // define our handlers
    addHandler(new ProjectionHandler()
    		{
					public void setProjection(PlainProjection proj)
					{
						view.setProjection(proj);
					}
    		});
    addHandler(new GUIHandler()
    		{
					public void assignTracks(String primaryTrack, Vector secondaryTracks)
					{
						// see if we have our track data listener
						if(view instanceof IAdaptable)
						{
							IAdaptable ad = (IAdaptable) view;
							Object adaptee = ad.getAdapter(org.mwc.cmap.core.DataTypes.TrackData.TrackManager.class);
							if(adaptee != null)
							{
								TrackManager tl = (TrackManager) adaptee;
								tl.assignTracks(primaryTrack, secondaryTracks);
							}
						}
					}
    		});
    
    addHandler(new DebriefLayersHandler(_theLayers));

  }

  public final void elementClosed()
  { 	
  	// and the GUI details
  //	setGUIDetails(null);
  }

  public static void exportThis(PlotEditor thePlot, org.w3c.dom.Element parent,
                                org.w3c.dom.Document doc)
  {
    org.w3c.dom.Element eSession = doc.createElement("session");

    // ok, get the layers
    Layers theLayers = (Layers) thePlot.getAdapter(Layers.class);
    
    // now the Layers
    DebriefLayersHandler.exportThis(theLayers, eSession, doc);

    // now the projection
    PlainProjection proj =  (PlainProjection) thePlot.getAdapter(PlainProjection.class);
    ProjectionHandler.exportProjection(proj, eSession, doc);

    // now the GUI
    GUIHandler.exportThis(thePlot, eSession, doc);

    // send out the data
    parent.appendChild(eSession);
  }

}