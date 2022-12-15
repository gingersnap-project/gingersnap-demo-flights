package io.gingersnapproject.airports;

import io.gingersnapproject.airports.data.DataLoader;
import io.gingersnapproject.airports.model.Aircraft;
import io.gingersnapproject.airports.model.Airline;
import io.gingersnapproject.airports.model.Airport;
import io.gingersnapproject.airports.model.Country;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/demo")
public class FlightsResource {

    @Inject
    DataLoader dataLoader;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String loadData(@QueryParam("reload") Boolean reload) {
        if (reload != null && reload) {
            Log.info("Reloading all data");
            dataLoader.cleanup();
            dataLoader.loadAll();
            Log.info("Data reloaded");
        }
        return "Airports demo is up";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/flight")
    public List<Flight> allFlights() {
        return Flight.getSome(20);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/airline")
    public List<Airline> allAirlines() {
        return Airline.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/airport")
    public List<Airport> allAirports() {
        return Airport.getSome(50);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/country")
    public List<Country> allCountries() {
        return Country.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/aircraft")
    public List<Aircraft> allAircrafts() {
        return Aircraft.listAll();
    }

}