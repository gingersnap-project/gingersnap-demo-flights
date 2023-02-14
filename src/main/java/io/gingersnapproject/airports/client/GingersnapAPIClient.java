package io.gingersnapproject.airports.client;

import io.gingersnapproject.airports.FlightDTO;
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
   @Path("/us-airport/{id}")
   CacheAirport destination(@PathParam("id") int id);

   @GET
   @Path("/us-flight/{code}")
   CacheFlight flight(@PathParam("code") String code);

//   @GET
//   @Path("/us-flight/{code}")
//   List<FlightDTO> departures(Integer day);
}
