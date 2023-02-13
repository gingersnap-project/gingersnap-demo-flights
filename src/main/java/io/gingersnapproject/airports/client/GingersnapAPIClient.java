package io.gingersnapproject.airports.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(configKey = "gingersnap-api")
@RegisterClientHeaders
public interface GingersnapAPIClient {

   @GET
   @Path("/us-flight/{code}")
   CacheFlight flight(@PathParam("code") String code);
}
