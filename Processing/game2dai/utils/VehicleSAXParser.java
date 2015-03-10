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

import game2dai.entities.Vehicle;
import game2dai.maths.Vector2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import processing.core.PApplet;



public class VehicleSAXParser extends EntitySAXParser {

	private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
	
	private String name = "";
	private Vector2D pos = null;
	private Vector2D velocity = null;
	private Vector2D heading = null;
	private double col_radius;
	private double mass;
	private double max_speed;
	private double max_turn_rate;
	private double max_force;	
	
	private double x, y, value;

	private Vehicle[] vehicles = null;

	public Vehicle[] get(){
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
			return new Vehicle[0];
		else
			return vehicles;
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
		if(qName.equals("vehicles")){
			vehicleList.clear();
		}
		else if (qName.equals("vehicle")){   // Set default values
			name = "";
			pos = velocity = heading = null;
			col_radius = 2;
			mass = 1;
			max_speed = 40;
			max_turn_rate = 1.0;
			max_force = 200;
			x = y = 0;
		}
		else if(qName.equals("pos") || qName.equals("velocity") || qName.equals("heading")){
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
		else if(qName.equals("velocity")){
			velocity = new Vector2D(x, y);
		}
		else if(qName.equals("heading")){
			heading = new Vector2D(x, y);
		}
		else if(qName.equals("collision_radius")){
			col_radius = value;
		}
		else if(qName.equals("mass")){
			mass = value;
		}
		else if(qName.equals("max_speed")){
			max_speed = value;
		}
		else if(qName.equals("max_turn_rate")){
			max_turn_rate = value;
		}
		else if(qName.equals("max_force")){
			max_force = value;
		}
		else if(qName.equals("vehicle")){
			vehicleList.add(new Vehicle(name, pos, col_radius, velocity, max_speed, heading, mass, max_turn_rate, max_force));
		}
		else if (qName.equals("vehicles")){
			vehicles = vehicleList.toArray(new Vehicle[vehicleList.size()]);
			vehicleList.clear();
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
	public VehicleSAXParser(File aFile) {
		super(aFile);
	}

	/**
	 * @param app
	 * @param fname
	 */
	public VehicleSAXParser(PApplet app, String fname) {
		super(app, fname);
	}

	/**
	 * @param fname
	 */
	public VehicleSAXParser(String fname) {
		super(fname);
	}

}
