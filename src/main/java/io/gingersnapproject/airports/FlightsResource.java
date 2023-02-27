package io.gingersnapproject.airports;

import io.gingersnapproject.airports.client.GingersnapAPIClient;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/flights")
public class FlightsResource {

    @Inject
    @RestClient
    GingersnapAPIClient gingersnapAPIClient;

   /*

      Gingersnap REST calls

    */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    @Path("cache/{code}")
    public Response flights(@PathParam("code") String code) {
        return gingersnapAPIClient.flight(code);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("departures/{day}")
    @Blocking
    public Response flights(@PathParam("day") Integer day) {

        String query = "select * from us-flight where departure=1 and dayOfWeek=" + day + " order by id desc limit 100";
        return gingersnapAPIClient.flightsQuery(query);
    }
}
