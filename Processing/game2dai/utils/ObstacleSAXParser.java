/*
 * Copyright (c) 2014 Peter Lager
 * <quark(a)lagers.org.uk> http:www.lagers.org.uk
 * 
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented;
 * you must not claim that you wrote the original software.
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 * 
 * 2. Altered source versions must be plainly marked as such,
 * and must not be misrepresented as being the original software.
 * 
 * 3. This notice may not be removed or altered from any source distribution.
 */

package game2dai.utils;

import game2dai.entities.Obstacle;
import game2dai.maths.Vector2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import processing.core.PApplet;

/**
 * @author Peter
 *
 */
public class ObstacleSAXParser extends EntitySAXParser {

	private ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();

	private String name = "";
	private Vector2D pos = null;
	private double radius;

	private double x, y, value;

	private Obstacle[] obstacles;
	
	public Obstacle[] get(){
		if(!error) {	
			try {
				saxParser.parse(this.istream, this);
			} catch (SAXException e) {
				error = true;
				Message.println("General SAX parser for file {0}", filename);
			} catch (IOException e) {
				error = true;
				Message.println("IO error for file {0}", filename);
			}
		}
		if(error)
			return new Obstacle[0];
		else
			return obstacles;
	}
	
	public void startDocument() throws SAXException {
		Message.println("Parsing document {0}", filename);
	}
	
	public void endDocument() throws SAXException {
		System.out.println("End of document");
	}
	
	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes attr) throws SAXException
	{
		if(qName.equals("obstacles")){
			obstacleList.clear();
		}
		else if (qName.equals("obstacle")){   // Set default values
			name = "";
			pos = null;
			radius = 3;
			value =	x = y = 0;
		}
		else if (qName.equals("pos")){
			x = y = 0;
		}
		// Process any attributes
		for (int i = 0 ; i < attr.getLength(); i++){
			storeElementValue(attr.getQName(i), attr.getValue(i));
		}
		buffer.reset();
	}
	
	public void endElement(String namespceURI, String localName, String qName) throws SAXException {
		storeElementValue(qName, buffer.toString());
		if(qName.equals("pos")){
			pos = new Vector2D(x, y);
		}
		else if(qName.equals("radius")){
			radius = value;
		}
		else if(qName.equals("obstacle")){
			obstacleList.add(new Obstacle(name, pos, radius));
		}
		else if (qName.equals("obstacles")){
			obstacles = obstacleList.toArray(new Obstacle[obstacleList.size()]);
			obstacleList.clear();
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.write(ch, start, length);
	}

	// This function is called when the closing tag is found
	private void storeElementValue(String elementName, String elementValue){
		if (elementName.equals("x")){
			x = this.getDouble(elementValue);
		}
		else if (elementName.equals("y")){
			y = this.getDouble(elementValue);
		}
		else if (elementName.equals("value")){
			value = this.getDouble(elementValue);
		}
		else if (elementName.equals("name")) {
			name = elementValue;
		}
	}


	
	/**
	 * @param aFile
	 */
	public ObstacleSAXParser(File aFile) {
		super(aFile);
	}

	/**
	 * @param app
	 * @param fname
	 */
	public ObstacleSAXParser(PApplet app, String fname) {
		super(app, fname);
	}

	/**
	 * @param fname
	 */
	public ObstacleSAXParser(String fname) {
		super(fname);
	}

}
