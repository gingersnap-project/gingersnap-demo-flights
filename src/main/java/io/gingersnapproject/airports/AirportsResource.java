package io.gingersnapproject.airports;

import io.gingersnapproject.airports.model.Airport;
import io.smallrye.common.annotation.Blocking;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/airports")
public class AirportsResource {

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Blocking
   public List<Airport> airports() {
      return Airport.getSome(50);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/{iata}")
   @Blocking
   public Airport airport(@PathParam("iata") String iata) {
      return  Airport.<Airport>find("iata", iata)
            .firstResultOptional().orElseThrow(() -> new NotFoundException("Airport not found"));
   }
}
