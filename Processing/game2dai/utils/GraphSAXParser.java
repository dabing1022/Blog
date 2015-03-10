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

import game2dai.graph.Graph;
import game2dai.graph.GraphNode;

import java.io.File;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import processing.core.PApplet;

/**
 * 
 * @author Peter Lager
 *
 */
public class GraphSAXParser extends EntitySAXParser {

	
	private Graph graph = null;
	
	private int nodeid, from, to;
	private double x, y;
	private double cost_out, cost_back;


	public Graph get(){
		if(!error) {	
			try {
				saxParser.parse(this.istream, this);
			} catch (SAXException e) {
				error = true;
				Message.println("General SAX parser error for file {0}", filename);
			} catch (IOException e) {
				error = true;
				Message.println("IO error for file {0}", filename);
			}
		}
		if(error)
			return null;
		else
			return graph;
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
		if(qName.equals("graph")){
			graph = new Graph();
		}
		else if (qName.equals("node")){   // Set default values
			nodeid = -1;
			x = y = 0;
		}
		else if (qName.equals("edge")){   // Set default values
			cost_out = cost_back = -1;
			from = to = -1;
		}
		// Process any attributes
		for (int i = 0 ; i < attr.getLength(); i++){
			storeElementValue(attr.getQName(i), attr.getValue(i));
		}
		buffer.reset();
	}
	
	public void endElement(String namespceURI, String localName, String qName) throws SAXException {
		storeElementValue(qName, buffer.toString());
		if(qName.equals("node")){
			if(nodeid >= 0){
				graph.addNode(new GraphNode(nodeid, x, y));
			}
		}
		else if(qName.equals("edge")){
			if(from >= 0  && cost_out >= 0){
				graph.addEdge(from, to, cost_out);
				if(cost_back >= 0){
					graph.addEdge(to, from, cost_back);
				}
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.write(ch, start, length);
	}

	// This function is called when the closing tag is found
	private void storeElementValue(String elementName, String elementValue){
		if (elementName.equals("x")){
			x = getDouble(elementValue);
		}
		else if (elementName.equals("y")){
			y = getDouble(elementValue);
		}
		else if (elementName.equals("id")){
			nodeid = getInteger(elementValue);
		}
		else if (elementName.equals("from")){
			from = getInteger(elementValue);
		}
		else if (elementName.equals("to")){
			to = getInteger(elementValue);
		}
		else if (elementName.equals("cost_out")) {
			cost_out = getDouble(elementValue);
		}
		else if (elementName.equals("cost_back")) {
			cost_back = getDouble(elementValue);
		}
	}

	
	
	
	/**
	 * @param aFile
	 */
	public GraphSAXParser(File aFile) {
		super(aFile);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param app
	 * @param fname
	 */
	public GraphSAXParser(PApplet app, String fname) {
		super(app, fname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fname
	 */
	public GraphSAXParser(String fname) {
		super(fname);
		// TODO Auto-generated constructor stub
	}

}
