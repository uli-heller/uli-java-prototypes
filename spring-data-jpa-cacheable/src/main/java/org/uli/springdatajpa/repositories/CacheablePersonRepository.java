package org.uli.springdatajpa.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.uli.springdatajpa.entities.CacheablePerson;

public interface CacheablePersonRepository extends JpaRepository<CacheablePerson, Integer> {
    @Cacheable("cacheablePersonRepository.byLastName")
    CacheablePerson findByLastName(String lastName);
}
