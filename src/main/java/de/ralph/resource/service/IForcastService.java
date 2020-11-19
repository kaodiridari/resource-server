package de.ralph.resource.service;

import java.util.Optional;

import de.ralph.resource.persistence.model.Forcast;

public interface IForcastService {
    
	Optional<Forcast> findById(Long id);
    
	Iterable<Forcast> findByZipcode(String zipcode);
	
	Optional<Forcast> findByName(String zipcode);

    Forcast save(Forcast forcast);

    Iterable<Forcast> findAll();

}
