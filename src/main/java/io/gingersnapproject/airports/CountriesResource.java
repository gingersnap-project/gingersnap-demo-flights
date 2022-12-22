package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.CacheCountry;
import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.gingersnapproject.airports.model.Country;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/countries")
public class CountriesResource {

   @Inject
   @RestClient
   GingersnapAPIClient gingersnapAPIClient;

   @GET
   @Blocking
   @Path("gingersnap")
   public List<String> countriesCache() {
      return gingersnapAPIClient.countries();
   }

   @GET
   @Blocking
   @Path("gingersnap/{isoCode}")
   public CacheCountry countryFromCache(@PathParam("isoCode") String isoCode) {
      return gingersnapAPIClient.country(isoCode);
   }

   @GET
   @Blocking
   public List<Country> countriesDB() {
      return Country.listAll();
   }
}
