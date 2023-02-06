package io.gingersnapproject.airports.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@RegisterRestClient(configKey = "gingersnap-api")
@RegisterClientHeaders
public interface GingersnapAPIClient {

   @GET
   @Path("/us-flights")
   List<CacheFlight> flights();

   @GET
   @Path("/us-flights/")
   CacheFlight flight(String code);
}
