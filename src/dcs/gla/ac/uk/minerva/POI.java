package dcs.gla.ac.uk.minerva;

public class POI {
	//fields to hold data on points of interest
	private String name;
	private String description;
	private double lat;
	private double lon;
	
	public POI(String name, String description,double lat,double lon) {
		this.name = name;
		this.description = description;
		this.lat=lat;
		this.lon=lon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
}