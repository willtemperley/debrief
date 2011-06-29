package org.mwc.asset.netassetserver.core;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import ASSET.NetworkParticipant;
import ASSET.NetworkScenario;

public class STest
{
	private SPresenter _pres;

	public STest(SView view)
	{
		final Vector<NetworkScenario> scens = createScenarios();
		 _pres = new SPresenter(view)
		{

			@Override
			public Vector<NetworkScenario> getScenarios()
			{
				return scens;
			}
		};
		
		
		
	}

	private Vector<NetworkScenario> createScenarios()
	{
		Vector<NetworkScenario> res = new Vector<NetworkScenario>();
		res.add(new NetworkScenario()
		{
			public String getName()
			{
				return "aaa";
			}

			@Override
			public Integer[] getListOfParticipants()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public NetworkParticipant getThisParticipant(int id)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
		res.add(new NetworkScenario()
		{
			public String getName()
			{
				return "bbbb";
			}

			@Override
			public Integer[] getListOfParticipants()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public NetworkParticipant getThisParticipant(int id)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
		return res;
	}

	public static void main(String[] args) throws IOException
	{
		SView view = new SView()
		{

			@Override
			public void showMessage(Date date, String msg)
			{
				System.out.println("LOG:" + msg);
			}
		};
		@SuppressWarnings("unused")
		STest tst = new STest(view);
		
		System.out.println("Pausing...");
		System.in.read();

	}
}