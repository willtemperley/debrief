package ASSET.Util.XML.Control.Observers;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import ASSET.Models.Decision.TargetType;
import ASSET.Scenario.Observers.ScenarioObserver;
import ASSET.Scenario.Observers.Summary.TimeToLaunchObserver;
import ASSET.Util.XML.Decisions.Util.TargetTypeHandler;

abstract class TimeToLaunchObserverHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
{

  private final static String type = "TimeToLaunchObserver";

  private final static String ACTIVE = "Active";

  private final static String TARGET_TYPE = "Target";

  private TargetType _targetType = null;
  private boolean _isActive;
  private String _name;

  public TimeToLaunchObserverHandler()
  {
    super(type);

    addAttributeHandler(new HandleBooleanAttribute(ACTIVE)
    {
      public void setValue(String name, final boolean val)
      {
        _isActive = val;
      }
    });

    addAttributeHandler(new HandleAttribute("Name")
    {
      public void setValue(String name, final String val)
      {
        _name = val;
      }
    });

    addHandler(new TargetHandler(TARGET_TYPE)
    {
      public void setTargetType(final TargetType type)
      {
        _targetType = type;
      }
    });
  }

  public void elementClosed()
  {
    // create ourselves
    final ScenarioObserver timeO = new TimeToLaunchObserver(_targetType, _name, _isActive);

    setObserver(timeO);

    // and reset
    _name = null;
    _targetType = null;
  }


  abstract public void setObserver(ScenarioObserver obs);

  static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
                                final org.w3c.dom.Document doc)
  {
    // create ourselves
    final org.w3c.dom.Element thisPart = doc.createElement(type);

    // get data item
    final TimeToLaunchObserver bb = (TimeToLaunchObserver) toExport;

    // output it's attributes
    thisPart.setAttribute("Name", bb.getName());
    thisPart.setAttribute(ACTIVE, writeThis(bb.isActive()));

    TargetHandler.exportThis(bb.getTargetType(), thisPart, doc, TARGET_TYPE);

    // output it's attributes
    parent.appendChild(thisPart);

  }

  /**
   * ************************************************************
   * handle our different types of target handler
   * *************************************************************
   */
  abstract private static class TargetHandler extends MWC.Utilities.ReaderWriter.XML.MWCXMLReader
  {
    private TargetType _type = null;

    public TargetHandler(final String myType)
    {
      super(myType);
      addHandler(new TargetTypeHandler()
      {
        public void setTargetType(final TargetType type)
        {
          _type = type;
        }
      });
    }

    public void elementClosed()
    {
      setTargetType(_type);
      _type = null;
    }

    abstract public void setTargetType(TargetType type);

    static public void exportThis(final Object toExport, final org.w3c.dom.Element parent,
                                  final org.w3c.dom.Document doc, final String myType)
    {
      // create ourselves
      final org.w3c.dom.Element thisElement = doc.createElement(myType);

      // get data item
      final TargetType bb = (TargetType) toExport;

      // output it's attributes

      // output the target type
      TargetTypeHandler.exportThis(bb, thisElement, doc);

      // output it's attributes
      parent.appendChild(thisElement);
    }
  }


}