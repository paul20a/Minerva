package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * @author Paul Cairney
 * 
 * This class extracts information required by Minerva from an Xml document
 *
 */
public class XmlPointParser extends XmlParser {
	// Currently unused parameter
	private static final String ns = null;
	String image;
	String audio;
	Double lat;
	Double lon;
	int id;
	/**
	 * @param in - An InputStream consisting of XML data
	 * @return ArrayList - returns an ArrayList of Objects
	 * @throws XmlPullParserException - parsing errors
	 * @throws IOException - File reader errors
	 */
	public ArrayList<Object> parse(InputStream in) throws XmlPullParserException,
			IOException {
		
		try {
			XmlPullParser parser = Xml.newPullParser();
			//namespace processing turned off
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			
			parser.setInput(in, null);
			parser.nextTag();
			
			//return ArrayList processed by ReadFeed
			return readFeed(parser);
			
		} finally {
			//close stream
			in.close();
		}
	}

	private ArrayList<Object> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		id=0;
		ArrayList<Object> entries = new ArrayList<Object>();
		//define start of list
		parser.require(XmlPullParser.START_TAG, ns, "audio_tour");
		while (parser.next() != XmlPullParser.END_TAG) {
			//Search for entries
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// locate an entry in the document
			if (name.equals("point")) {
				//parse entry			
				image=parser.getAttributeValue(ns, "image");
				audio=parser.getAttributeValue(ns, "audio");
				id=Integer.parseInt(parser.getAttributeValue(ns, "id"));
				try{
				lon= Double.parseDouble(parser.getAttributeValue(ns, "longitude"));
				lat= Double.parseDouble(parser.getAttributeValue(ns, "latitude"));
				}catch(NumberFormatException e){
					lon=null;
					lat=null;
				}
				POI p=readEntry(parser);
				entries.add(p);
			} else {
				//ignore data
				skip(parser);
			}
		}
		return entries;
	}

	public POI readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException {

		String name = null;
		String description = null;
		//define start of an entry
		parser.require(XmlPullParser.START_TAG, ns, "point");
		//until closing tag
		while (parser.next() != XmlPullParser.END_TAG) {
			//ensure on start
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			//process tag name to collect name and description fields or skip
			String tag = parser.getName();
			if (tag.equals("title")) {
				name = readTag(parser,"title");
			} else if (tag.equals("summary")) {
				description = readTag(parser,"summary");
			} else {
				skip(parser);
			}
		}
		return new POI(name, description,lat,lon,image, id,audio);
	}

}
