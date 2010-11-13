package org.mwc.asset.comms.restlet.host;

import java.io.File;

import org.mwc.asset.comms.restlet.host.ASSETHost.HostProvider;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

abstract public class HostServer extends Application implements HostProvider {

		abstract public ASSETHost getHost();
	

		
    /**
     * When launched as a standalone application.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getClients().add(Protocol.FILE);
        component.getServers().add(Protocol.HTTP, 8080);
        component.getDefaultHost().attach(new HostServer(){

        	MockHost host = new MockHost();
					@Override
					public ASSETHost getHost()
					{
						return host;
					}});
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        getConnectorService().getClientProtocols().add(Protocol.FILE);

        // Serve the files generated by the GWT compilation step.
        File warDir = new File("");
        if (!"war".equals(warDir.getName())) {
            warDir = new File(warDir, "war/");
        }

        Directory dir = new Directory(getContext(), LocalReference
                .createFileReference(warDir));
        router.attachDefault(dir);
        router.attach("/v1/scenario", ScenariosHandler.class);
        router.attach("/v1/scenario/{scenario}/listener", ScenarioListenerHandler.class);
        router.attach("/v1/scenario/{scenario}/listener/{listener}", ScenarioListenerHandler.class);
        router.attach("/v1/scenario/{scenario}/participant", ParticipantsHandler.class);
        router.attach("/v1/scenario/{scenario}/participant/{participant}/state", StatusHandler.class);
        return router;
    }


}
