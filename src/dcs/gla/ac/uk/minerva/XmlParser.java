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
public class XmlParser {
	// Currently unused parameter
	private static final String ns = null;

	/**
	 * @param in - An InputStream consisting of XML data
	 * @return ArrayList - returns an ArrayList of Objects
	 * @throws XmlPullParserException - parsing errors
	 * @throws IOException - File reader errors
	 */
	public ArrayList<POI> parse(InputStream in) throws XmlPullParserException,
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

	private ArrayList<POI> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		
		ArrayList<POI> entries = new ArrayList<POI>();
		//define start of list
		parser.require(XmlPullParser.START_TAG, ns, "data");
		while (parser.next() != XmlPullParser.END_TAG) {
			//Search for entries
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// locate an entry in the document
			if (name.equals("entry")) {
				//parse entry
				entries.add(readEntry(parser));
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
		double lat = 0;
		double lon=0;
		//define start of an entry
		parser.require(XmlPullParser.START_TAG, ns, "entry");
		//until closing tag
		while (parser.next() != XmlPullParser.END_TAG) {
			//ensure on start
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			//process tag name to collect name and description fields or skip
			String tag = parser.getName();
			if (tag.equals("name")) {
				name = readTag(parser,"name");
			} else if (tag.equals("description")) {
				description = readTag(parser,"description");
			} else if (tag.equals("lat")) {
				lat = Double.parseDouble(readTag(parser,"lat"));
			} else if (tag.equals("lon")) {
				lon = Double.parseDouble(readTag(parser,"lon"));
			} else {
				skip(parser);
			}
		}
		return new POI(name, description,lat,lon);
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		//ensure skipping only occurs on start tags
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		//until depth = 0 continue to skip data
		while (depth != 0) {
			switch (parser.next()) {
			//decrement depth on closing tag
			case XmlPullParser.END_TAG:
				depth--;
				break;
			//increment on start tag
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

	private String readTag(XmlPullParser parser, String tagName) throws IOException,
			XmlPullParserException {
		//parse information within name tag
		parser.require(XmlPullParser.START_TAG, ns, tagName);
		String name = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tagName);
		return name;
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		//process strings
		String s = "";
		if (parser.next() == XmlPullParser.TEXT) {
			s = parser.getText();
			parser.nextTag();
		}
		return s;
	}

}
