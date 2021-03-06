package Debrief.Tools.Tote.Calculations;

// Copyright MWC 1999, Debrief 3 Project
// $RCSfile: timeCalc.java,v $
// @author $Author: Ian.Mayo $
// @version $Revision: 1.5 $
// $Log: timeCalc.java,v $
// Revision 1.5  2006/07/25 14:48:52  Ian.Mayo
// Handle when this pt has no time
//
// Revision 1.4  2006/03/16 16:01:09  Ian.Mayo
// Override not-applicable statement
//
// Revision 1.3  2005/12/13 09:04:56  Ian.Mayo
// Tidying - as recommended by Eclipse
//
// Revision 1.2  2004/11/25 10:24:40  Ian.Mayo
// Switch to Hi Res dates
//
// Revision 1.1.1.2  2003/07/21 14:49:17  Ian.Mayo
// Re-import Java files to keep correct line spacing
//
// Revision 1.5  2003-03-19 15:36:59+00  ian_mayo
// improvements according to IntelliJ inspector
//
// Revision 1.4  2003-02-10 16:27:47+00  ian_mayo
// Reflect name change of get wrappable data
//
// Revision 1.3  2003-02-07 15:36:08+00  ian_mayo
// Add accessor flag to indicate is this calculation needs special processing (where data crosses through zero)
//
// Revision 1.2  2002-05-28 09:25:12+01  ian_mayo
// after switch to new system
//
// Revision 1.1  2002-05-28 09:11:41+01  ian_mayo
// Initial revision
//
// Revision 1.1  2002-04-23 12:28:37+01  ian_mayo
// Initial revision
//
// Revision 1.0  2001-07-17 08:41:13+01  administrator
// Initial revision
//
// Revision 1.1  2001-01-03 13:40:26+00  novatech
// Initial revision
//
// Revision 1.1.1.1  2000/12/12 20:49:05  ianmayo
// initial import of files
//
// Revision 1.1  2000-09-14 10:24:59+01  ian_mayo
// Initial revision
//

import java.text.DecimalFormat;

import MWC.GenericData.HiResDate;
import MWC.GenericData.Watchable;
import MWC.Utilities.TextFormatting.DebriefFormatDateTime;

public final class timeCalc extends plainCalc
{

  
  /////////////////////////////////////////////////////////////
  // constructor
  ////////////////////////////////////////////////////////////
  public timeCalc()
  {  
    super(new DecimalFormat("000.0"), "Time", "hhmmss");
  }
  
  /////////////////////////////////////////////////////////////
  // member functions
  ////////////////////////////////////////////////////////////
  public final double calculate(Watchable primary, Watchable secondary, HiResDate thisTime)
  {
    double res = 0.0;
    res = thisTime.getMicros();
    return res;
  }
  public final String update(Watchable primary, Watchable secondary, HiResDate time)
  {
		// check we have data
		if(primary == null)
			return NOT_APPLICABLE;
		
		if(time == null)
			return NOT_APPLICABLE;
		
  	return DebriefFormatDateTime.toStringHiRes(time);
  }


  /** does this calculation require special bearing handling (prevent wrapping through 360 degs)
   *
   */
  public final boolean isWrappableData() {
    return false;
  }
}
