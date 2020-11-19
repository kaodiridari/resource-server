package de.ralph.resource.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.ralph.resource.persistence.model.Forcast;

public interface IForcastRepository extends PagingAndSortingRepository<Forcast, Long> {

	Iterable<Forcast> findByZipcode(String zipcode);

	Optional<Forcast> findByName(String name);
}
