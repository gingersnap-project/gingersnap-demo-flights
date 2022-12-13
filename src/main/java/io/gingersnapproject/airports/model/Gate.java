package io.gingersnapproject.airports.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Gate extends PanacheEntity {
   public String name;
   public String terminal;
}
