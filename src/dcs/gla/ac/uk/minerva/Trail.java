package dcs.gla.ac.uk.minerva;


public class Trail{

	private String title;
	private String description;
	private	String image;
	private String file;
	
	public Trail(String title, String description,String file,String image) {
		super();
		this.title = title;
		this.description = description;
		this.file=file;
		this.image=image;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}


	
}
