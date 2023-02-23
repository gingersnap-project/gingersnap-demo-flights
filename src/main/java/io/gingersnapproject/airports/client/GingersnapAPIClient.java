package io.gingersnapproject.airports.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.gingersnapproject.airports.model.Flight;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.gingersnapproject.airports.DashboardFlightDTO;

@RegisterRestClient(configKey = "gingersnap-api")
@RegisterClientHeaders
public interface GingersnapAPIClient {

   @GET
   @Path("/us-flight/{code}")
   String flight(@PathParam("code") String code);

   @GET
   @Path("/dashboard-departure/{code}")
   String departure(@PathParam("code") String code);
}
