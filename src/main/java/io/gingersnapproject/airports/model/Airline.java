package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Airline extends PanacheEntity {
   public String iata;
   public String icao;
   public String nvls;
   public String publicName;

}
