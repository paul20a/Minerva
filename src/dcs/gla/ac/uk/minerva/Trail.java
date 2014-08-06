package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

public class Trail{

	private String title;
	private String description;
	private ArrayList<Integer> idList;
	
	public Trail(String title, String description, ArrayList<Integer> idList) {
		super();
		this.title = title;
		this.description = description;
		this.idList = idList;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Integer> getIdList() {
		return idList;
	}
	public void setIdList(ArrayList<Integer> idList) {
		this.idList = idList;
	}
	
	
}
