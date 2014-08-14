package dcs.gla.ac.uk.minerva;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Paul Cairney
 * 
 * This class extracts information required by Minerva from an Xml document
 *
 */
public abstract class XmlParser {
	// Currently unused parameter
	private static final String ns = null;


	protected void skip(XmlPullParser parser) throws XmlPullParserException,
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

	protected String readTag(XmlPullParser parser, String tagName) throws IOException,
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
		s=s.trim();
		return s;
	}

}
