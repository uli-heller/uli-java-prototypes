package org.uli.springdatajpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uli.springdatajpa.entities.LombokPerson;

public interface LombokPersonRepository extends JpaRepository<LombokPerson, Integer> {
    LombokPerson findByLastName(String lastName);
}
