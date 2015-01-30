package org.uli.springdatajpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uli.springdatajpa.entities.TraditionalPerson;

public interface TraditionalPersonRepository extends JpaRepository<TraditionalPerson, Integer> {

}
