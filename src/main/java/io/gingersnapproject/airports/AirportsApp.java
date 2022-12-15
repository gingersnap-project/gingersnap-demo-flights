package io.gingersnapproject.airports;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AirportsApp {
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
}

