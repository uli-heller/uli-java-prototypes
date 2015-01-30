package org.uli.springdatajpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uli.springdatajpa.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByLastName(String lastName);
}
