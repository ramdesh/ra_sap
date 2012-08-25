package com.rasap.augmented_reality.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.rasap.augmented_reality.R;
import com.rasap.augmented_reality.ui.IconMarker;
import com.rasap.augmented_reality.ui.Marker;

/**
 * This class should be used as a example local data source. It is an example of
 * how to add data programatically. You can add data either programatically,
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource {
	private List<Marker> cachedMarkers = new ArrayList<Marker>();
	private static Bitmap icon = null;
	InputStream _resourceFile;

	public LocalDataSource(Resources res) {
		if (res == null)
			throw new NullPointerException();

		createIcon(res);
	}

	public LocalDataSource(Resources res, InputStream file) {
		if ( res == null || file == null )
			throw new NullPointerException();
		_resourceFile = file;
		createIcon(res);
	}

	protected void createIcon(Resources res) {
		if (res == null)
			throw new NullPointerException();

		icon = BitmapFactory.decodeResource(res, R.drawable.loc_marker);
	}

	public List<Marker> getMarkers() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(_resourceFile);
			Element root = doc.getDocumentElement();
			NodeList locationList = root.getElementsByTagName("location");
			for ( int i = 0; i < locationList.getLength(); i++ ) {
				Node locationItem = locationList.item(i);
				if ( locationItem.getNodeType() == Node.ELEMENT_NODE ) {
					Element locationElement = (Element)locationItem;
					String name = getTagValue("name",locationElement);
					double latitude = Double.valueOf(getTagValue("latitude",locationElement));
					double longitude = Double.valueOf(getTagValue("longitude",locationElement));
					double altitude = Double.valueOf(getTagValue("altitude",locationElement));
					cachedMarkers.add(new IconMarker(name,latitude,longitude,altitude,Color.DKGRAY,icon));
				}
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		/*
		 * Marker lon = new IconMarker(
		 * "I am a really really long string which should wrap a number of times on the screen."
		 * , 39.95335, -74.9223445, 0, Color.MAGENTA, icon);
		 * cachedMarkers.add(lon); Marker lon2 = new IconMarker(
		 * "2: I am a really really long string which should wrap a number of times on the screen."
		 * , 39.95334, -74.9223446, 0, Color.MAGENTA, icon);
		 * cachedMarkers.add(lon2);
		 */

		/*
		 * float max = 10; for (float i=0; i<max; i++) { Marker marker = null;
		 * float decimal = i/max; if (i%2==0) marker = new Marker("Test-"+i,
		 * 39.99, -75.33+decimal, 0, Color.LTGRAY); marker = new
		 * IconMarker("Test-"+i, 39.99+decimal, -75.33, 0, Color.LTGRAY, icon);
		 * cachedMarkers.add(marker); }
		 */

		return cachedMarkers;
	}
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
}
