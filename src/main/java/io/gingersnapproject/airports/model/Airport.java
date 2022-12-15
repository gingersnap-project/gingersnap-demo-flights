package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Airport extends PanacheEntity {
   public String iata;
   public String name;
   public String city;
   public double elevation_ft;
   public double latitude_deg;
   public double longitude_deg;

   @ManyToOne
   public Country country;

   public static List<Airport> getSome(int count) {
      PanacheQuery<Airport> airports = Airport.findAll();
      airports.page(Page.ofSize(count));
      return airports.list();
   }
}
