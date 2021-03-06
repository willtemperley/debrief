/**
 * 
 */
package org.mwc.asset.core;

import org.eclipse.ui.*;
import org.mwc.cmap.core.CorePlugin;

/**
 * @author ian.mayo
 * 
 */
public class ASSETPerspectiveFactory implements IPerspectiveFactory
{

	/**
	 * @param layout
	 */
	@SuppressWarnings("deprecation")
	public void createInitialLayout(IPageLayout layout)
	{
		// Get the editor area.
		String editorArea = layout.getEditorArea();

		// Top left: Resource Navigator view and Bookmarks view placeholder
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT,
				0.4f, editorArea);
		topLeft.addView(IPageLayout.ID_RES_NAV);

		
		// split the time one - so we can insert the track tote
		// Top left: Resource Navigator view and Bookmarks view placeholder
//	//				0.2f, "topLeft");

		// Bottom left: Outline view and Property Sheet view
		IFolderLayout upperMidLeft = layout.createFolder("upperMidLeft",
				IPageLayout.BOTTOM, 0.2f, "topLeft");
		upperMidLeft.addView(ASSETPlugin.SCENARIO_CONTROLLER2);
		upperMidLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		upperMidLeft.addPlaceholder(CorePlugin.OVERVIEW_PLOT);
		upperMidLeft.addPlaceholder(CorePlugin.POLYGON_EDITOR);
		upperMidLeft.addPlaceholder(ASSETPlugin.SENSOR_MONITOR);
		
		// Bottom left: Outline view and Property Sheet view
		IFolderLayout bottomLeft = layout.createFolder("bottomLeft",
				IPageLayout.BOTTOM, 0.6f, "upperMidLeft");
		bottomLeft.addView(CorePlugin.LAYER_MANAGER);
		bottomLeft.addView(IPageLayout.ID_PROP_SHEET);

		// bottom: placeholder for the xyplot
		IPlaceholderFolderLayout bottomPanel = layout.createPlaceholderFolder(
				"bottom", IPageLayout.BOTTOM, 0.6f, editorArea);
		bottomPanel.addPlaceholder(CorePlugin.XY_PLOT + ":*");
		bottomPanel.addPlaceholder(CorePlugin.LIVE_DATA_MONITOR);
		bottomPanel.addPlaceholder(IPageLayout.ID_TASK_LIST);
		// bottomPanel.addPlaceholder(CorePlugin.NARRATIVES);

		// and our view shortcuts
		layout.addShowViewShortcut(CorePlugin.LAYER_MANAGER);
		layout.addShowViewShortcut(CorePlugin.NARRATIVES);
		layout.addShowViewShortcut(CorePlugin.OVERVIEW_PLOT);
		layout.addShowViewShortcut(ASSETPlugin.SCENARIO_CONTROLLER2);
		layout.addShowViewShortcut(ASSETPlugin.VESSEL_MONITOR);
		layout.addShowViewShortcut(ASSETPlugin.SENSOR_MONITOR);

		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(CorePlugin.LIVE_DATA_MONITOR);
		
		// and the error log
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		// hey - try to add the 'new plot' to the New menu
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		// layout.addNewWizardShortcut("org.mwc.debrief.core.wizards.NewPlotWizard");

		// ok - make sure the debrief action sets are visible
		layout.addActionSet("org.mwc.debrief.core");
		// layout.addActionSet("org.mwc.debrief.track_shift");
	}

}
