package de.ralph.resource.web.dto;

public class ForcastDto {
	
    private long id;
    
    private String name;
   
    private String zipcode;

   	private String temperature;
   	
   	private String wind;
   	
   	private String clouds;
   	
   	private String time;
    
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ForcastDto() {}

	public ForcastDto(long id, String name, String zipcode, String temperature, String wind, String clouds, String time) {
		this.id = id;
		this.name = name;
		this.zipcode = zipcode;
		this.temperature = temperature;
		this.wind = wind;
		this.clouds = clouds;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getClouds() {
		return clouds;
	}

	public void setClouds(String clouds) {
		this.clouds = clouds;
	}
}