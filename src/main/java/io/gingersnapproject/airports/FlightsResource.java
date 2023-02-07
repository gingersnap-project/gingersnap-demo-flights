package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.gingersnapproject.airports.model.Airport;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static io.quarkus.panache.common.Parameters.with;

@Path("/flights")
public class FlightsResource {

   @Inject
   @RestClient
   GingersnapAPIClient gingersnapAPIClient;


   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public CompletionStage<String> checkResourceUp() {
      return CompletableFuture.completedFuture("Flights resource is up");
   }

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
   @Path("departures/{day}")
   @Blocking
   public List<Flight> flights(@PathParam("day") Integer day, @QueryParam("iata") Optional<String> iataOpt) {
      Log.infof("Request flights for day %d and destination %s", day, iataOpt);
      if (iataOpt.isPresent()) {
         String iata = iataOpt.get();
         Airport airport = Airport.<Airport>find("iata", iata)
               .firstResultOptional().orElseThrow(() -> new NotFoundException("Airport not found " + iata));
         return  Flight.find("destination = :destination and dayOfWeek = :dayOfWeek",
                     with("destination", airport).and("dayOfWeek", day).map())
               .list();
      }

      return Flight.find("dayOfWeek",day).list();



//            .list().stream().map(fdb -> {
//               FlightDTO flightDTO = new FlightDTO();
//               flightDTO.code = fdb.code;
//               flightDTO.name = fdb.name;
//               return flightDTO;
//            }).collect(Collectors.toList());
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{code}")
   @Blocking
   public Flight getByCode(@PathParam("code") String code) {
      Log.infof("Request flight code %s", code);
      return  Flight.find("code", code).firstResult();
   }

}
