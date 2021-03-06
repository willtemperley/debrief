package com.borlander.rac525791.dashboard.layout.data;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.borlander.rac525791.dashboard.layout.ControlUIModel;

class Depth320x183 implements ControlUIModel {
	
	public Point getControlCenter() {
		return new Point(256, 118);
	}

	public Dimension getUnitsAndMultipliersSize() {
		return new Dimension(28, 11);
	}

	public Point getUnitsPosition() {
		return new Point(-28, 15);

	}
	public int getRedSectorRadius() {
		return 42;
	}
	
	public Dimension getZeroMark() {
		return new Dimension(-28, -37);
	}

	public Dimension getMaximumMark() {
		return new Dimension(12, 44);
	}
	
	public boolean isFullCircleMapped() {
		return false;
	}
	
	public Dimension getValueTextSize() {
		return new Dimension(19, 17);
	}
	
	public Point getValueTextPosition() {
		return new Point(-28, -8);
	}

}
