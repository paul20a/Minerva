package dcs.gla.ac.uk.minerva;

import android.os.Parcel;
import android.os.Parcelable;

public class POI implements Parcelable {
	// fields to hold data on points of interest
	private int id;
	private String name;
	private String description;
	private double lat;
	private double lon;
	private String image;
	private String audio;

	public POI(String name, String description, double lat, double lon,
			String image, int id,String audio) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.lat = lat;
		this.lon = lon;
		this.image = image;
		this.setAudio(audio);
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeString(image);
		dest.writeString(audio);
	}

	public static final Parcelable.Creator<POI> CREATOR = new Parcelable.Creator<POI>() {
		public POI createFromParcel(Parcel in) {
			return new POI(in);
		}

		public POI[] newArray(int size) {
			return new POI[size];
		}

	};

	public POI(Parcel in) {
		id=in.readInt();
		name=in.readString();
		description=in.readString();
		lat=in.readDouble();
		lon=in.readDouble();
		image=in.readString();
		audio=in.readString();
	}

}