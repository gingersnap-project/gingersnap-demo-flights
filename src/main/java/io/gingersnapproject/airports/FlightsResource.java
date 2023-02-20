package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.CacheAirport;
import io.gingersnapproject.airports.client.CacheFlight;
import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.gingersnapproject.airports.model.Airport;
import io.gingersnapproject.airports.model.DepartureFlightState;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{code}")
   @Blocking
   public Flight getByCode(@PathParam("code") String code) {
      Log.infof("Request flight code %s", code);
      return  Flight.find("code", code).firstResult();
   }

   @PUT
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{code}")
   @Blocking
   @Transactional
   public Response updateFlightState(@PathParam("code") String code, @QueryParam("state") Optional<DepartureFlightState> stateOpt) {
      Log.infof("Change status for flight %s to %s", code, stateOpt);
      if (stateOpt.isPresent()) {
         Flight flight = Flight.find("code", code).<Flight>firstResultOptional()
               .orElseThrow(() -> new NotFoundException(String.format("Flight %s not found", code)));
         if (!flight.departure) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(),
                  String.format("Flight %s is not departure type", code)).build();
         }

         DepartureFlightState state = stateOpt.get();
         Flight.update("update from Flight set state = ?1 where code = ?2", state.name(), code);
         return Response.accepted().build();
      }

      return Response.notModified().build();
   }

   // TODO: Lazy Query
//
//   @GET
//   @Produces(MediaType.APPLICATION_JSON)
//   @Blocking
//   @Path("cache/departures/{day}")
//   public List<FlightDTO> flightsFromCache(@PathParam("day") Integer day) {
//      List<FlightDTO> departures = gingersnapAPIClient.departures(day);
//
//      return new ArrayList<>();
//   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Blocking
   @Path("cache/departures/get/{code}")
   public DashboardFlightDTO flightFromCache(@PathParam("code") String code) {
      return gingersnapAPIClient.departure(code);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Blocking
   @Path("cache/{code}")
   public FlightDTO flights(@PathParam("code") String code) {
      CacheFlight flight = gingersnapAPIClient.flight(code);

      FlightDTO flightDTO = new FlightDTO();
      flightDTO.code = flight.code;
      flightDTO.name = flight.name;
      flightDTO.scheduleTime = flight.scheduleTime;
      flightDTO.state = flight.state;
      flightDTO.destinationCity = flight.destination_id.name;

      return flightDTO;
   }

}
