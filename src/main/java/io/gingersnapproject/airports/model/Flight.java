package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
//code","name","airline","destination","terminal","scheduleTime","aircraftType","aircraftSubType","direction","dayOfWeek"
//"3O121-2-A","3O121","3O","NDR",1,"18:35:00","32S","320","A",2
public class Flight extends PanacheEntity {
   public String code;
   public String name;

   @OneToOne(fetch = FetchType.LAZY)
   public Airline airline;

   @OneToOne(fetch = FetchType.LAZY)
   public Aircraft aircraft;

   @OneToOne(fetch = FetchType.LAZY)
   public Airport destination;

   public int terminal;
   public String scheduleTime;
   public String aircraftType;
   public String aircraftSubType;
   public String direction;
   public int dayOfWeek;

   public static List<Flight> getSome(int count) {
      PanacheQuery<Flight> flights = Flight.findAll();
      flights.page(Page.ofSize(count));
      return flights.list();
   }
}
