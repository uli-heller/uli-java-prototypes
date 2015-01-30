package org.uli.springdatajpa.entities;

import java.util.List;

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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@Entity
@Table(name="PERSON")
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Person {
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
    private List<Address> addresses;
}
