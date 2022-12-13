package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Country extends PanacheEntity {
   public String isoCode;
   public String name;
   public String continent;
}
