package io.gingersnapproject.airports;

import io.gingersnapproject.airports.model.DepartureFlightState;
import io.gingersnapproject.airports.model.Flight;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/update")
public class UpdateFlightsResource {


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/flight/{code}")
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
            try {
                Flight.update("update from Flight set state = ?1 where code = ?2", state.name(), code);
             } catch (Exception e ) {
                Log.error(e);
            }

            return Response.accepted().build();
        }

        return Response.notModified().build();
    }
}
