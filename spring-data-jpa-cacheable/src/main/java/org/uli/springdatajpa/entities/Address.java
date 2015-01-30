package org.uli.springdatajpa.entities;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@Entity
@Table(name="ADDRESS")
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class Address {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer addressId;
    
    @Getter @Setter
    private Integer personId;
    
    @Column(name="STREET")
    @Getter @Setter
    private String street;
    
    @Column(name="CITY")
    @Getter @Setter
    private String city;

    @Getter
    static private final AtomicInteger noArgsConstructorCallCounter = new AtomicInteger(0);
    
    public Address() {
        noArgsConstructorCallCounter.incrementAndGet();
    }

    public static void clearNoArgsConstructorCallCounter() {
        noArgsConstructorCallCounter.set(0);
    }
}
