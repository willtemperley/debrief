package org.pml.debrief.KMLTransfer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostGet_Presenter
{

	private static final String DATABASE_ROOT = "jdbc:postgresql://127.0.0.1/ais";
	private static Connection _conn;
	private static PreparedStatement sql;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// check we have a database
			connectToDatabase();

			String query = "select dateval, nameval, mmsi, longval, latval, courseval, speedval, dataset, color from wednesday;";
			System.out.println("query will be:" + query);
			sql = _conn.prepareStatement(query);

			FileWriter outFile = new FileWriter("portland6.rep");

			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");

			ResultSet res = sql.executeQuery();
			while (res.next())
			{
				int ctr = 1;
				Timestamp date = res.getTimestamp(ctr++);
				String name = res.getString(ctr++);
				int mmsi = res.getInt(ctr++);
				double longVal = res.getDouble(ctr++);
				double latVal = res.getDouble(ctr++);
				double course = res.getDouble(ctr++);
				double speed = res.getDouble(ctr++);
				int dataset = res.getInt(ctr++);
				String color = res.getString(ctr++);

				String symbol = getSymFor(color);

				String vName = name + "_" + mmsi + "_" + dataset;

				// debrief format: YYMMDD HHMMSS.SSS XXXXXX SY DD MM SS.SS H DDD MM
				// SS.SS H CCC.C SS.S DDD
				Date jDate = new Date(date.getTime());
				String line = "";
				line += sdf.format(jDate) + " ";
				line += "\"" + vName + "\" ";
				line += "@" + symbol + " ";
				line += latVal + " 0 0.0 N ";
				line += longVal + " 0 0.0 E ";
				line += course + " ";
				line += speed + " ";
				line += " 0";
				line += "\n";

				outFile.write(line);
			}

			outFile.close();

		}
		catch (RuntimeException re)
		{
			re.printStackTrace();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{

			// close the databse
			if (_conn != null)
			{
				try
				{
					System.out.println("closing database");
					_conn.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

	private static String getSymFor(String color)
	{
		final String res;
		if (color.equals("yellow"))
			res = "D";
		else if (color.equals("lightgreen"))
			res = "B";
		else if (color.equals("blue"))
			res = "A";
		else if (color.equals("red"))
			res = "C";
		else if (color.equals("pink"))
			res = "K";
		else if (color.equals("lightgray"))
			res = "F";
		else if (color.equals("cyan"))
			res = "H";
		else if (color.equals("magenta"))
			res = "E";
		else 
			res = "";

		return res;
	}

	private static void connectToDatabase()
	{
		// driver first
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException("Failed to load database driver");
		}

		try
		{
			String url = DATABASE_ROOT;
			final String password = System.getenv("pg_pwd");
			if (password == null)
				throw new RuntimeException("database password missing");
			_conn = DriverManager.getConnection(url, "postgres", password);

			// also tell the connection about our new custom data types
			((org.postgresql.PGConnection) _conn).addDataType("geometry",
					org.postgis.PGgeometry.class);
			((org.postgresql.PGConnection) _conn).addDataType("box3d",
					org.postgis.PGbox3d.class);
		}
		catch (SQLException e)
		{
			throw new RuntimeException("failed to create connection");
		}

	}
}
