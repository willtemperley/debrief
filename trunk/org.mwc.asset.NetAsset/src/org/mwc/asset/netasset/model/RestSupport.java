package org.mwc.asset.netasset.model;

import java.util.List;

import org.mwc.asset.comms.restlet.data.ListenerResource;
import org.mwc.asset.comms.restlet.data.Scenario;
import org.mwc.asset.comms.restlet.data.ScenarioStateResource;
import org.mwc.asset.comms.restlet.data.ScenariosResource;
import org.mwc.asset.comms.restlet.host.ASSETGuest;
import org.mwc.asset.comms.restlet.host.GuestServer;
import org.mwc.asset.comms.restlet.test.MockHost;
import org.restlet.Restlet;
import org.restlet.resource.ClientResource;

public class RestSupport
{

	private static final int NULL_INT = -1;
	final private ASSETGuest _myGuest;
	private int _scenarioListenerId = NULL_INT;
	private GuestServer guestS;

	public RestSupport(ASSETGuest guest)
	{
		_myGuest = guest;
	}

	/**
	 * connect to a server
	 * 
	 */
	public boolean doConnect()
	{
		// find some data
		ClientResource cr = new ClientResource("http://localhost:8080/v1/scenario");

		// does it have a scenario?
		ScenariosResource scenR = cr.wrap(ScenariosResource.class);
		List<Scenario> sList = scenR.retrieve();
		boolean res = (sList != null);

		if (res)
			_myGuest.newScenarioEvent(0, "Setup", "scenarios found:" + sList.size());
		else
			_myGuest.newScenarioEvent(0, "Setup", "scenarios not found");

		// ok, register ourselves as a listener
		if (guestS == null)
		{
			guestS = new GuestServer()
			{
				@Override
				public ASSETGuest getGuest()
				{
					return _myGuest;
				}
			};
		}

		if (guestS.isStopped())
			try
			{
				GuestServer.go(guestS);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		// start listening to time events
		// right, now try to register it.
		cr = new ClientResource("http://localhost:8080/v1/scenario/"
				+ MockHost.SCENARIO_ID + "/listener");
		ListenerResource lr = cr.wrap(ListenerResource.class);
		_scenarioListenerId = lr.accept("http://localhost:8081/v1/scenario/"
				+ MockHost.SCENARIO_ID + "/event");

		return res;
	}

	public void doDisconnect()
	{
		if (_scenarioListenerId != NULL_INT)
		{
			ClientResource cr = new ClientResource(
					"http://localhost:8080/v1/scenario/" + MockHost.SCENARIO_ID
							+ "/listener/" + _scenarioListenerId);
			ListenerResource lr = cr.wrap(ListenerResource.class);
			lr.remove();
		}
	}

	public void play(boolean play)
	{
		// find some data
		ClientResource cr = new ClientResource("http://localhost:8080/v1/scenario/"
				+ MockHost.SCENARIO_ID + "/state");

		// does it have a scenario?
		ScenarioStateResource scenR = cr.wrap(ScenarioStateResource.class);
		String theState;
		if (play)
			theState = ScenarioStateResource.START;
		else
			theState = ScenarioStateResource.STOP;

		scenR.store(theState);
	}

}
