package org.uli.springdatajpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@Entity
@Table(name="ADDRESS")
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
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
    
}
