package de.ralph.resource.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.ralph.resource.persistence.model.Forcast;
import de.ralph.resource.service.IForcastService;
import de.ralph.resource.web.dto.ForcastDto;

@RestController
@RequestMapping(value = "/api/foos")
public class ForcastController {

	private static final Logger logger = LoggerFactory.getLogger(ForcastController.class);
	
    private IForcastService forcastService;

    public ForcastController(IForcastService forcastService) {
        this.forcastService = forcastService;
    }

    // http://localhost:8081/sso-resource-server/api/foos/2
    //@CrossOrigin(origins = "http://localhost:8089")
    @GetMapping("/{id}")
    public Collection<ForcastDto> findOne(@PathVariable Long id) {
    	logger.info("findOne: " + id);
        Forcast entity = forcastService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ForcastDto> forcastDtos = new ArrayList<>();
        forcastDtos.add(convertToDto(entity));
        return forcastDtos;
    }
    
    @GetMapping("/zipcode/{zipcode}")
    public Collection<ForcastDto> findByZipcode(@PathVariable String zipcode) {
    	logger.info("findByZipcode: " + zipcode);
//        Forcast entity = forcastService.findByZipcode(zipcode)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        List<ForcastDto> forcastDtos = new ArrayList<>();
//        forcastDtos.add(convertToDto(entity));
    	Iterable<Forcast> forcasts = this.forcastService.findByZipcode(zipcode);
        List<ForcastDto> forcastDtos = new ArrayList<>();
        forcasts.forEach(p -> forcastDtos.add(convertToDto(p)));
        return forcastDtos;
    }
    
    @GetMapping()
    public Collection<ForcastDto> findAll() {
    	logger.info("findAll");
        Iterable<Forcast> forcasts = this.forcastService.findAll();
        List<ForcastDto> forcastDtos = new ArrayList<>();
        forcasts.forEach(p -> forcastDtos.add(convertToDto(p)));
        return forcastDtos;
    }
    
    @GetMapping("/name/{name}")
    public Collection<ForcastDto> findByname(@PathVariable String name) {
    	logger.info("findByname: " + name);
        Forcast entity = forcastService.findByName(name)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ForcastDto> forcastDtos = new ArrayList<>();
        forcastDtos.add(convertToDto(entity));
        return forcastDtos;
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public void create(@RequestBody FooDto newFoo) {
//    	logger.info("create: " + newFoo.getId());
//        Foo entity = convertToEntity(newFoo);
//        this.fooService.save(entity);
//    }

//    @PutMapping("/{id}")
//    public FooDto updateFoo(@PathVariable("id") Long id, @RequestBody FooDto updatedFoo) {
//    	logger.info("updateFoo:  " + id);
//        Foo fooEntity = convertToEntity(updatedFoo);
//        return this.convertToDto(this.fooService.save(fooEntity));
//    }

    protected ForcastDto convertToDto(Forcast entity) {
        ForcastDto dto = new ForcastDto(
        		entity.getId(),
        		entity.getName(),
        		entity.getZipcode(),
        		entity.getTemperature(),
        		entity.getWind(),
        		entity.getClouds(),
        		entity.getTime()
        		);
        return dto;
    }

    protected Forcast convertToEntity(ForcastDto dto) {
        Forcast forcast = new Forcast(dto.getName());
        if (!StringUtils.isEmpty(dto.getId())) {
            forcast.setId(dto.getId());
        }
        return forcast;
    }
}