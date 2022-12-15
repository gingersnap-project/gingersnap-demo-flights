package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Path("/flights")
public class FlightsResource {

   @Inject
   @RestClient
   GingersnapAPIClient gingersnapAPIClient;

   @GET
   @Blocking
   public String test() {
      Log.info(gingersnapAPIClient.countries());
      return "hello";
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{destination}")
   public List<Flight> flights(@PathParam("destination") String destination) {
      if (destination == null || destination.isEmpty()) {
         return Flight.getSome(50);
      }

      return  Flight.find("destination.city", destination).list();
   }

}
