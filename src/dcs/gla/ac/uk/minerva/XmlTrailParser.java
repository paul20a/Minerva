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
public class XmlTrailParser extends XmlParser {
	// Currently unused parameter
	private static final String ns = null;
	String image;
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
	
		
		ArrayList<Object> entries = new ArrayList<Object>();
		//define start of list
		parser.require(XmlPullParser.START_TAG, ns, "data");
		while (parser.next() != XmlPullParser.END_TAG) {
			//Search for entries
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// locate an entry in the document
			if (name.equals("trail")) {
			image=parser.getAttributeValue(ns, "image");
				//parse entry
				entries.add(readEntry(parser));
			} else {
				//ignore data
				skip(parser);
			}
		}
		return entries;
	}

	public Trail readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException {

		String title = null;
		String description = null;
		String file = null;
		//define start of an entry
		parser.require(XmlPullParser.START_TAG, ns, "trail");
		//until closing tag
		while (parser.next() != XmlPullParser.END_TAG) {
			//ensure on start
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			//process tag name to collect name and description fields or skip
			String tag = parser.getName();	
			if (tag.equals("title")) {
				title = readTag(parser,"title");
			} else if (tag.equals("summary")) {
				description = readTag(parser,"summary");
			} else if (tag.equals("file")) {
				file = readTag(parser,"file");
			} else {
				skip(parser);
			}
		}
		return new Trail(title, description,file,image);
	}

}
