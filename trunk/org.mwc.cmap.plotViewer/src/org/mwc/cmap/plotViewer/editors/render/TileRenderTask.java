package org.mwc.cmap.plotViewer.editors.render;

import java.awt.Color;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.mwc.cmap.core.ui_support.swt.SWTCanvasAdapter;

/**
 * A RenderTask implementation for loading a particular tile. asynchronously.
 * 
 * @author Jesse
 * 
 */
public class TileRenderTask extends AbstractRenderTask
{
	private PositionedTile _tile;
	private int _tileXIndex;
	private int _tileYIndex;

	/**
	 * Set the parameters for the tile that needs to be loaded.
	 * 
	 * @param tileXIndex
	 *          the x index of the tile to load
	 * @param tileYIndex
	 *          the y index of the tile to load
	 * @param tile
	 *          a tile that has been bound to a screen location so that it can be
	 *          rendered.
	 */
	public void setTile(int tileXIndex, int tileYIndex, PositionedTile tile)
	{
		this._tile = tile;
		this._tileXIndex = tileXIndex;
		this._tileYIndex = tileYIndex;
	}

	@Override
	public RenderTaskResult call() throws Exception
	{
		Image load = _tile.load();
		String tilePos = _tileXIndex + ", " + _tileYIndex;
		if (TileCache.isDebug())
		{
			GC gc = new GC(load);
			try
			{
				SWTCanvasAdapter canv = new SWTCanvasAdapter(_projection);
				canv.startDraw(gc);
				int quarterHeight = _tile.getSize().height / 4;
				canv.setColor(Color.WHITE);
				canv.drawText(tilePos, 5, quarterHeight);
			}
			finally
			{
				gc.dispose();
			}
		}
		RenderTaskResult result = new RenderTaskResult(load, _tile.getDrawArea(),
				tilePos)
		{

			@Override
			public void dispose()
			{
				// allow tilecache to manage image disposal
			}
		};
		return result;
	}

}