package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class Airport extends PanacheEntity {
   public String iata;
   public String name;
   public String city;
   public double elevation_ft;
   public double latitude_deg;
   public double longitude_deg;

   @OneToOne(fetch = FetchType.LAZY)
   public Country country;
}
