package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class FlightStatus extends PanacheEntity {

   @OneToOne(fetch = FetchType.LAZY)
   public Flight flight;

   @OneToOne(fetch = FetchType.LAZY)
   public Status status;
}
