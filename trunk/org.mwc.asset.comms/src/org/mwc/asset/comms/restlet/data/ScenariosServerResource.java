package org.mwc.asset.comms.restlet.data;

import java.util.List;
import java.util.Vector;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * The server side implementation of the Restlet resource.
 */
public class ScenariosServerResource extends ServerResource implements
		ScenariosResource
{

	@Get
	public List<Scenario> retrieve()
	{
		final Vector<Scenario> res = new Vector<Scenario>();
		res.add(new Scenario("Scott", 44));
		res.add(new Scenario("Scott", 22));
		res.add(new Scenario("Scott", 33));
		res.add(new Scenario("Scott", 11));

		return res;
	}
}