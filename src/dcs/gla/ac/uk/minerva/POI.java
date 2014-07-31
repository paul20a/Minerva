package dcs.gla.ac.uk.minerva;

public class POI {
private String name;
private String description;

public POI(String name, String description) {
	this.name = name;
	this.description = description;
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
}// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
//to their respective "read" methods for processing. Otherwise, skips the tag.

}