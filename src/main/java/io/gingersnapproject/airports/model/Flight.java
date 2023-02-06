package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Flight extends PanacheEntity {
   public String code;
   public String name;

   @ManyToOne
   public Airline airline;

   @ManyToOne
   public Aircraft aircraft;

   @ManyToOne
   public Airport destination;

   public String state;

   public int terminal;
   public String scheduleTime;
   public boolean departure;
   public int dayOfWeek;

   public static List<Flight> getSome(int count) {
      PanacheQuery<Flight> flights = Flight.findAll();
      flights.page(Page.ofSize(count));
      return flights.list();
   }
}
