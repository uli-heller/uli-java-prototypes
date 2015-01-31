package org.uli.springdatajpa.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.uli.springdatajpa.entities.CacheablePerson;
import org.uli.springdatajpa.repositories.CacheablePersonRepository;

@Slf4j
public class CacheablePersonService {
    @Autowired
    CacheablePersonRepository cacheablePersonRepository;
    
    @Cacheable("CacheablePersonService.byLastName")
    CacheablePerson findByLastName(String lastName) {
        log.trace("-> lastName={}", lastName);
        CacheablePerson result = this.cacheablePersonRepository.findByLastName(lastName);
        log.trace("<- result={}", result);
        return result;
    }
}
