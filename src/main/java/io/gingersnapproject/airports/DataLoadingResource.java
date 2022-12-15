package io.gingersnapproject.airports;

import io.gingersnapproject.airports.service.DataLoader;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/demo")
public class DataLoadingResource {

    @Inject
    DataLoader dataLoader;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String loadData(@QueryParam("reload") Boolean reload, @QueryParam("all") Boolean all) {
        if (reload != null && reload) {
            Log.info("Reloading data ...");
            if (all != null && all) {
                Log.info("Clean all");
                dataLoader.cleanup();
                Log.info("Load all");
                dataLoader.loadAll();
            } else {
                Log.info("Clean flights");
                dataLoader.cleanupFlights();
                Log.info("Loading flights");
                dataLoader.loadFlights();
            }
            Log.info("Data reloaded");
        }
        return "Airports demo is up";
    }
}
