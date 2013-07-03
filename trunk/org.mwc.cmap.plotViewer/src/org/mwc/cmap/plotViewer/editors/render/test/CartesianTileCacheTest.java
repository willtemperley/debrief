package org.mwc.cmap.plotViewer.editors.render.test;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.Before;
import org.junit.Test;
import org.mwc.cmap.plotViewer.editors.render.Tile;
import org.mwc.cmap.plotViewer.editors.render.TileCache;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class CartesianTileCacheTest
{
	private TileCache _tileCache;

	double[] scales = new double[]
	{ 1, 10, 100, 1000, 10000, 100000, 1000000 };

	@Before
	public void before()
	{

		_tileCache = new TileCache(new Dimension(10, 10), scales, new Coordinate(
				-100, -100), 100  / 2.54, 2, DefaultEngineeringCRS.CARTESIAN_2D,
				new TestTileLoader(), null);
	}

	@Test
	public void testCalculateBounds_EqualLengths() {

		_tileCache = new TileCache(new Dimension(10, 10), scales, new Coordinate(
				-100, -100), 100  / 2.54, 0, DefaultEngineeringCRS.CARTESIAN_2D,
				new TestTileLoader(), null);
		Envelope bounds = _tileCache.calculateBounds(new Dimension(200, 200), 1, new Coordinate(0,0));
		assertEquals (200, (int)Math.floor(bounds.getWidth()));
		assertEquals (200, (int)Math.ceil(bounds.getWidth()));

		assertEquals (200, (int)Math.floor(bounds.getHeight()));
		assertEquals (200, (int)Math.ceil(bounds.getHeight()));
		
		assertEquals (-100, (int)bounds.getMinX());
		assertEquals (-100, (int)bounds.getMinY());
		assertEquals (100, (int)bounds.getMaxX());
		assertEquals (100, (int)bounds.getMaxY());
	}
	
	@Test
	public void testCalculateBounds_UnequalLengths() {
		Envelope bounds = _tileCache.calculateBounds(new Dimension(30, 50), 1, new Coordinate(-70,-70));
		assertEquals (30, bounds.getWidth(), 0.001);
		assertEquals (50, bounds.getHeight(), 0.001);
		
		
		assertEquals (-70 - 15, bounds.getMinX(), 0.001);
		assertEquals (-70 - 25, bounds.getMinY(), 0.001);
		assertEquals (-70 + 15, bounds.getMaxX(), 0.001);
		assertEquals (-70 + 25, bounds.getMaxY(), 0.001);
	}
	
	@Test
	public void testGetClosestScaleExceptionalCases()
	{
		double scale = _tileCache.getClosestScale(new Envelope(-100, 100, -10, 10),
				new Dimension(10, 10));

		assertEquals(10, scale, 0.0001);

		scale = _tileCache.getClosestScale(new Envelope(0, 0.00001, 0, 0.000001),
				new Dimension(5000, 5000));

		assertEquals(1, scale, 0.0001);
	}

	@Test
	public void testGetClosestScale()
	{
		double scale = _tileCache.getClosestScale(new Envelope(10, 10, 110, 110),
				new Dimension(100, 100));

		assertEquals(1, scale, 0.0001);
	}

	@Test
	public void testGetTiles_TilesFitBounds()
	{
		Dimension screenSize = new Dimension(200, 200);
		Coordinate center = new Coordinate(100, 100);

		

		Tile[][] tiles = _tileCache.getTiles(screenSize, 1, center);

		assertEquals(20, tiles.length);
		for (Tile[] column : tiles)
		{
			assertEquals(20, column.length);
		}
		
		assertBounds(0,10,0,10, DefaultEngineeringCRS.CARTESIAN_2D, tiles[0][0].getBounds());
		assertBounds(190,200,190,200, DefaultEngineeringCRS.CARTESIAN_2D, tiles[19][19].getBounds());
		
		for (int i = 0; i < tiles.length; i++)
		{
			Tile[] column = tiles[i];
			int minx = 10 * i;
			int maxx = minx + 10;
			for (int j = 0; j < column.length; j++)
			{
				int miny = 10 * j;
				int maxy = miny + 10;

				assertBounds(minx,maxx,miny,maxy, DefaultEngineeringCRS.CARTESIAN_2D, tiles[i][j].getBounds());
			}
		}
	}
	
	@Test
	public void testGetTiles_TilesNonEvenDimension()
	{
		Dimension screenSize = new Dimension(15, 25);
		Coordinate center = new Coordinate(-100 + 30, -100 + 30);
		
		
		Tile[][] tiles = _tileCache.getTiles(screenSize, 1, center);
		
		assertEquals(2, tiles.length);
		for (Tile[] column : tiles)
		{
			assertEquals(4, column.length);
		}
		
		assertBounds(-80,-70,-90,-80, DefaultEngineeringCRS.CARTESIAN_2D, tiles[0][0].getBounds());
		assertBounds(-70,-60,-60,-50, DefaultEngineeringCRS.CARTESIAN_2D, tiles[1][3].getBounds());
		
		for (int i = 0; i < tiles.length; i++)
		{
			Tile[] row = tiles[i];
			int minx = -80 + (10 * i);
			int maxx = minx + 10;
			for (int j = 0; j < row.length; j++)
			{
				int miny = -90 + (10 * j);
				int maxy = miny + 10;
				
				assertBounds(minx,maxx,miny,maxy, DefaultEngineeringCRS.CARTESIAN_2D, tiles[i][j].getBounds());
			}
		}
	}

	@Test
	public void testGetTiles_TilesDontFitBounds()
	{
		Dimension screenSize = new Dimension(30, 50);
		Coordinate center = new Coordinate(-100 + 30, -100 + 30);


		Tile[][] tiles = _tileCache.getTiles(screenSize, 1, center);

		assertEquals(4, tiles.length);
		for (Tile[] row : tiles)
		{
			assertEquals(6, row.length);
		}
		
		assertBounds(-90,-80,-100,-90, DefaultEngineeringCRS.CARTESIAN_2D, tiles[0][0].getBounds());
		assertBounds(-60,-50,-50,-40, DefaultEngineeringCRS.CARTESIAN_2D, tiles[3][5].getBounds());
		
		for (int i = 0; i < tiles.length; i++)
		{
			Tile[] row = tiles[i];
			int minx = -90 + (10 * i);
			int maxx = minx + 10;
			for (int j = 0; j < row.length; j++)
			{
				int miny = -100 + (10 * j);
				int maxy = miny + 10;

				assertBounds(minx,maxx,miny,maxy, DefaultEngineeringCRS.CARTESIAN_2D, tiles[i][j].getBounds());
			}
		}
	}
	
	@Test
	public void testGetTiles_TilesAtBottomLeft()
	{
		Dimension screenSize = new Dimension(30, 50);
		Coordinate center = new Coordinate(-100 + 15, -100 + 25);
		
		
		Tile[][] tiles = _tileCache.getTiles(screenSize, 1, center);
		
		assertEquals(3, tiles.length);
		for (Tile[] row : tiles)
		{
			assertEquals(5, row.length);
		}
		
		assertBounds(-100,-90,-100,-90, DefaultEngineeringCRS.CARTESIAN_2D, tiles[0][0].getBounds());
		assertBounds(-80,-70,-60,-50, DefaultEngineeringCRS.CARTESIAN_2D, tiles[2][4].getBounds());
		
		for (int i = 0; i < tiles.length; i++)
		{
			Tile[] row = tiles[i];
			int minx = -100 + (10 * i);
			int maxx = minx + 10;
			for (int j = 0; j < row.length; j++)
			{
				int miny = -100 + (10 * j);
				int maxy = miny + 10;
				
				assertBounds(minx,maxx,miny,maxy, DefaultEngineeringCRS.CARTESIAN_2D, tiles[i][j].getBounds());
			}
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testOutOfBoundsGetTiles() {
		Dimension screenSize = new Dimension(30, 50);
		Coordinate center = new Coordinate(-100, -100);
		_tileCache.getTiles(screenSize, 1, center);
	}
	private void assertBounds(double minx, double maxx, double miny, double maxy,
			CoordinateReferenceSystem crs, ReferencedEnvelope actualBounds)
	{
		final double precision = 0.0000001;
		assertEquals(minx, actualBounds.getMinX(), precision );
		assertEquals(miny, actualBounds.getMinY(), precision );
		assertEquals(maxx, actualBounds.getMaxX(), precision );
		assertEquals(maxy, actualBounds.getMaxY(), precision );
		
		assertTrue (CRS.equalsIgnoreMetadata(crs, actualBounds.getCoordinateReferenceSystem()));
	}

	@Test
	public void testCalculateScales()
	{
		TileCache tc = new TileCache(new Dimension(10, 10), 10, new Envelope(0,
				10, 0, 10), new Envelope(0, 100, 0, 100), 100  / 2.54, 2, DefaultEngineeringCRS.CARTESIAN_2D, new TestTileLoader(), null);
		
		double[] scales = tc.getScales();
		
		assertEquals(10, scales.length);
		
		assertArrayEquals(new double[]{1,2,3,4,5,6,7,8,9,10}, scales, 0.0001);
	}
}
