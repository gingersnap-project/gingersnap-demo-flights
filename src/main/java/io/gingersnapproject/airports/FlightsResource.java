package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.gingersnapproject.airports.model.Flight;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/flights")
public class FlightsResource {

   @Inject
   @RestClient
   GingersnapAPIClient gingersnapAPIClient;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Blocking
   @Path("{iata}/cache")
   public List<FlightDTO> flights() {
      return gingersnapAPIClient.flights().stream().map(cf -> {
         FlightDTO flightDTO = new FlightDTO();
         flightDTO.name = cf.name;
         flightDTO.code = cf.code;
         return flightDTO;
      }).collect(Collectors.toList());
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{iata}")
   @Blocking
   public List<FlightDTO> flights(@PathParam("iata") String iata) {
      return  Flight.<Flight>find("destination.iata", iata)
            .list().stream().map(fdb -> {
               FlightDTO flightDTO = new FlightDTO();
               flightDTO.code = fdb.code;
               flightDTO.name = fdb.name;
               return flightDTO;
            }).collect(Collectors.toList());
   }

}
