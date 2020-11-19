package de.ralph.resource.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ralph.resource.persistence.model.Forcast;
import de.ralph.resource.persistence.repository.IForcastRepository;
import de.ralph.resource.service.IForcastService;

@Service
public class ForcastServiceImpl implements IForcastService {

    private IForcastRepository forcastRepository;

    public ForcastServiceImpl(IForcastRepository forcastRepository) {
        this.forcastRepository = forcastRepository;
    }

    @Override
    public Optional<Forcast> findById(Long id) {
        return forcastRepository.findById(id);
    }

    @Override
    public Forcast save(Forcast forcast) {
        return forcastRepository.save(forcast);
    }

    @Override
    public Iterable<Forcast> findAll() {
        return forcastRepository.findAll();
    }

	@Override
	public Iterable<Forcast> findByZipcode(String zipcode) {		
		return forcastRepository.findByZipcode(zipcode);
	}

	@Override
	public Optional<Forcast> findByName(String name) {
		return forcastRepository.findByName(name);
	}
}
