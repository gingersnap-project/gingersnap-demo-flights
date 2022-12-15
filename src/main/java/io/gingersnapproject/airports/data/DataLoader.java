package io.gingersnapproject.airports.data;

import io.gingersnapproject.airports.model.Aircraft;
import io.gingersnapproject.airports.model.Airline;
import io.gingersnapproject.airports.model.Airport;
import io.gingersnapproject.airports.model.Country;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class DataLoader {
   public static final String COUNTRIES_FILE_NAME = "countries.csv";
   public static final String AIRPORTS_FILE_NAME = "airports.csv";
   public static final String AIRCRAFTS_FILE_NAME = "aircrafts.csv";
   public static final String AIRLINES_FILE_NAME = "airlines.csv";
   public static final String FLIGHTS_FILE_NAME = "flights.csv";

   void onStart(@Observes StartupEvent ev) {
      Log.info("Airports Demo App is starting Powered by Quarkus");
      Log.info("  _   _   _   _   _   _   _   _");
      Log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      Log.info("( A | i | r | p | o | r | t | s )");
      Log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
   }

   void onStop(@Observes ShutdownEvent ev) {
      Log.info("Airports Demo App is shutting down...");
   }

   public void loadAll() {
      // Ref data
      loadCountries();
      loadAircrafts();
      loadAirlines();
      loadAirports();

      // Daily data
      loadFlights();
   }


   public void loadFlights() {
      load(FLIGHTS_FILE_NAME, line -> {
         String airlineIata = line[2].trim();
         String airportIata = line[3].trim();
         Optional<Airline> airline = Airline.find("iata", airlineIata).firstResultOptional();
         Optional<Airport> airport = Airport.find("iata", airportIata).firstResultOptional();
         Optional<Aircraft> aircraft = Aircraft.find("iataMain = :iataMain and iataSub = :iataSub",
                           Parameters.with("iataMain", line[6].trim())
                           .and("iataSub", line[7].trim())).firstResultOptional();
         if (airline.isEmpty() || airport.isEmpty() || aircraft.isEmpty()) {
            return null;
         }

         Flight flight = new Flight();
         flight.code = line[0].trim();
         flight.name = line[1].trim();
         flight.airline = airline.get();
         flight.destination = airport.get();
         flight.terminal = Integer.valueOf(line[4].trim());
         flight.scheduleTime = line[5].trim();
         flight.aircraft = aircraft.get();
         flight.direction = line[8].trim();
         flight.dayOfWeek = Integer.valueOf(line[9].trim());
         return flight;
      });
      Log.info("Flights loaded");
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
      Log.info("Airlines loaded");
   }

   public void loadAirports() {
      load(AIRPORTS_FILE_NAME, line -> {
         Airport airport = new Airport();
         airport.iata = line[0].trim();
         airport.name = line[1].trim();
         airport.country = Country.find("isoCode", line[2].trim()).firstResult();
         airport.city =  line[3].trim();
         int add = line.length - 7;
         airport.elevation_ft = Double.valueOf(line[4 + add].trim().trim());
         airport.latitude_deg = Double.valueOf(line[5 + add].trim());
         airport.longitude_deg = Double.valueOf(line[6 + add].trim());
         return airport;
      });
      Log.info("Airports loaded");
   }


   public void loadCountries() {
      load(COUNTRIES_FILE_NAME, line -> {
         Country country = new Country();
         country.isoCode = line[0].trim();
         country.name = line[1].trim();
         country.continent =  line[2].trim();
         return country;
      });
      Log.info("Countries loaded");
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
      Log.info("Aircrafts loaded");
   }

   public void load(String fileName, Function<String[], PanacheEntity> function) {
      InputStream resourceAsStream = this.getClass().getClassLoader()
            .getResourceAsStream("data/" + fileName);
      List<PanacheEntity> entitiesBatch = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
         String line;
         int id = 1;
         // Head
         br.readLine();
         while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            PanacheEntity panacheEntity = function.apply(values);
            if (entitiesBatch.size() == 100) {
               // Persist batch
               persistList(entitiesBatch);
               entitiesBatch.clear();
            } else if (panacheEntity != null){
               entitiesBatch.add(panacheEntity);
            }
            id++;
         }
         // Persist remaining
         persistList(entitiesBatch);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   @Transactional
   public void persistList(List<PanacheEntity> entitiesToPersist) {
      PanacheEntity.persist(entitiesToPersist);
   }

   @Transactional
   public void cleanup() {
      Log.info("Clean all data");
      Flight.deleteAll();
      Aircraft.deleteAll();
      Airline.deleteAll();
      Airport.deleteAll();
      Country.deleteAll();
   }
}
