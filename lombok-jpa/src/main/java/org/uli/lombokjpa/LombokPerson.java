package org.uli.lombokjpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="PERSON")
@EqualsAndHashCode
@ToString
public class LombokPerson {
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
    
    public LombokPerson() {
        ;
    }

}
