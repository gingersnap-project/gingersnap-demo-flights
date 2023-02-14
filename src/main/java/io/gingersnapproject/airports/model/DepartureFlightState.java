package io.gingersnapproject.airports.model;

public enum DepartureFlightState {
   SCH, // Scheduled to take place.
   DEL, // The deviation between the expected time of departure and the scheduled time exceeds 10 minutes.
   WIL, // Not available yet for boarding. Passengers need to wait in the lounge
   GTO, // When the gate is opened/released by the handler.
   BRD, // The actual boarding of the passengers starts.
   GCL, // The flight handler of the flight is closing the gate of the departing flight shortly.
   GTD, // This flight state indicates that the gate for a departing flight is closed and passenger boarding is not possible anymore.
   DEP, // Departing aircraft is taxiing to the runway.
   CNX, // A scheduled flight that will not be operated.
   GCH, // The gate of departure for a scheduled flight changes to another gate.
   TOM // When the date of an expected departure exceeds the initial date.
}
