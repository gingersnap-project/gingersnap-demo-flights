package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Aircraft extends PanacheEntity {
   public String iataMain;
   public String iataSub;
   public String longDescription;
   public String shortDescription;
}
