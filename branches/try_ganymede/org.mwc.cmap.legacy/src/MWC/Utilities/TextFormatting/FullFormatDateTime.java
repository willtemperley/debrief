// Copyright MWC 1999, Generated by Together
// @Author : Ian Mayo
// @Project: Debrief 3
// @File   : FormatRNDateTime.java

package MWC.Utilities.TextFormatting;

import java.text.*;
import java.util.*;

public class FullFormatDateTime 
{

  
  static public String toString(long theVal)
  {
		return toStringLikeThis(theVal, 
														"dd/MMM/yy HH:mm:ss");
  }
  
	static public String toShortString(long theVal)
	{
		return toStringLikeThis(theVal, 
														"HHmm");
	}

  static public String toDetailedShortString(long theVal)
	{
		return toStringLikeThis(theVal,
														"HHmm:ss");
	}


	static public String toStringLikeThis(long theVal,
																				String thePattern)
	{
    java.util.Date theTime = new java.util.Date(theVal);
    String res;

  
    DateFormat df = new SimpleDateFormat(thePattern);    
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    res = df.format(theTime);

    return res;
	}
	
  static public String getExample(){
    return "dd/MMM/yy HH:mm:ss";
  }
}


