package io.gingersnapproject.airports.data;

import io.gingersnapproject.airports.model.Aircraft;
import io.gingersnapproject.airports.model.Airline;
import io.gingersnapproject.airports.model.Airport;
import io.gingersnapproject.airports.model.Country;
import io.gingersnapproject.airports.model.Flight;
import io.gingersnapproject.airports.model.Gate;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class DataLoader {

   public static final String COUNTRIES_FILE_NAME = "countries.csv";
   public static final String GATES_FILE_NAME = "gates.csv";
   public static final String AIRPORTS_FILE_NAME = "airports.csv";
   public static final String AIRCRAFTS_FILE_NAME = "aircrafts.csv";
   public static final String AIRLINES_FILE_NAME = "airlines.csv";
   public static final String FLIGHTS_FILE_NAME = "flights.csv";

   private Map<String, Airline> airlinesMap = new HashMap<>();
   private Map<String, Aircraft> aircraftsMap = new HashMap<>();
   private Map<String, Country> countryMap = new HashMap<>();
   private Map<String, Airport> airportsMap = new HashMap<>();

   void onStart(@Observes StartupEvent ev) {
      Log.info("Airports Demo App is starting Powered by Quarkus");
      Log.info("  _   _   _   _   _   _   _   _");
      Log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      Log.info("( A | i | r | p | o | r | t | s )");
      Log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");

      loadCountries();
      loadAircrafts();
      loadAirports();
      loadGates();
      loadAirlines();
      loadFlights();
   }

   void onStop(@Observes ShutdownEvent ev) {
      Log.info("Airports Demo App is shutting down...");
   }

   public void loadFlights() {
      load(FLIGHTS_FILE_NAME, line -> {
         Flight flight = new Flight();
         flight.code = line[0].trim();
         flight.name = line[1].trim();
         flight.airline = airlinesMap.get(line[2].trim());
         flight.destination = Airport.find("iata", line[3].trim()).<Airport>firstResultOptional().orElse(null);
         flight.terminal = Integer.valueOf(line[4].trim());
         flight.scheduleTime = line[5].trim();
         flight.aircraft = aircraftsMap.get(line[6].trim() + line[7].trim());
         flight.direction = line[8].trim();
         flight.dayOfWeek = Integer.valueOf(line[9].trim());
         return flight;
      });
   }

   public void loadAirlines() {
      load(AIRLINES_FILE_NAME, line -> {
         Airline airline = new Airline();
         airline.iata = line[0].trim();
         airline.icao = line[1].trim();
         airline.nvls = line[2].trim();
         airline.publicName = line[3].trim();
         return airline;
      });
      List<Airline> airlines = Airline.listAll();
      for (Airline airline: airlines) {
         airlinesMap.put(airline.nvls, airline);
      }
   }

   public void loadAirports() {
      load(AIRPORTS_FILE_NAME, line -> {
         Airport airport = new Airport();
         airport.iata = line[0].trim();
         airport.name = line[1].trim();
         airport.country = countryMap.get(line[2].trim());
         airport.city =  line[3].trim();
         int add = line.length - 7;
         airport.elevation_ft = Double.valueOf(line[4 + add].trim().trim());
         airport.latitude_deg = Double.valueOf(line[5 + add].trim());
         airport.longitude_deg = Double.valueOf(line[6 + add].trim());
         return airport;
      });
      List<Airport> airports = Airport.listAll();
      for (Airport airport: airports) {
         airportsMap.put(airport.iata, airport);
      }

   }

   public void loadCountries() {
      load(COUNTRIES_FILE_NAME, line -> {
         Country country = new Country();
         country.isoCode = line[0].trim();
         country.name = line[1].trim();
         country.continent =  line[2].trim();
         return country;
      });
      List<Country> countries = Country.listAll();
      for (Country country: countries) {
         countryMap.put(country.isoCode, country);
      }
   }

   public void loadGates() {
      load(GATES_FILE_NAME, line -> {
         Gate gate = new Gate();
         gate.name = line[0].trim();
         gate.terminal = line[1].trim();
         return gate;
      });
   }

   public void loadAircrafts() {
      load(AIRCRAFTS_FILE_NAME, line -> {
         Aircraft aircraft = new Aircraft();
         aircraft.iataMain = line[0].trim();
         aircraft.iataSub = line[1].trim();
         aircraft.longDescription =  line[2].trim();
         aircraft.shortDescription =  line[3].trim();
         return aircraft;
      });
      List<Aircraft> aircrafts = Aircraft.listAll();
      for (Aircraft aircraft: aircrafts) {
         aircraftsMap.put(aircraft.iataMain + aircraft.iataSub, aircraft);
      }
   }

   @Transactional
   public void load(String fileName, Function<String[], PanacheEntity> function) {
      InputStream resourceAsStream = this.getClass().getClassLoader()
            .getResourceAsStream("data/" + fileName);

      try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
         String line;
         int id = 1;
         // Head
         br.readLine();
         while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            function.apply(values).persist();
            id++;
         }
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
