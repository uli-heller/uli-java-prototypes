package org.uli.springdatajpa.entities;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@Entity
@Table(name="PERSON")
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class CacheablePerson {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer personId;
    
    @Column(name="FIRST_NAME")
    @Getter @Setter
    private String firstName;
    
    @Column(name="LAST_NAME")
    @Getter @Setter
    private String lastName;
    
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy="personId")
    @Getter @Setter
    private List<CacheableAddress> addresses;
    
    @Getter
    static private final AtomicInteger noArgsConstructorCallCounter = new AtomicInteger(0);
    
    public CacheablePerson() {
        noArgsConstructorCallCounter.incrementAndGet();
    }
    
    public static void clearNoArgsConstructorCallCounter() {
        noArgsConstructorCallCounter.set(0);
    }
}
