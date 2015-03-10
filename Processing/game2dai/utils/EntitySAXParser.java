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

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import processing.core.PApplet;

public class EntitySAXParser extends DefaultHandler {
	
	protected SAXParserFactory factory = SAXParserFactory.newInstance();
	protected SAXParser saxParser;

	protected CharArrayWriter buffer = new CharArrayWriter();

	protected String filename;
	protected InputStream istream = null;

	protected boolean error = false;
	
	
	protected double getDouble(String s){
		double n = 0;
		try {
			n = Double.parseDouble(s);
		}
		catch(NumberFormatException nfe){
			Message.println("Number format error '{0}' in file {1}", s, filename);
		}
		return n;
	}
	
	protected int getInteger(String s){
		int n = 0;
		try {
			n = Integer.parseInt(s);
		}
		catch(NumberFormatException nfe){
			Message.println("Number format error '{0}' in file {1}", s, filename);
		}
		return n;
	}
	
	
	/**
	 * Use this version with Processing
	 * 
	 * @param app
	 * @param fname
	 */
	public EntitySAXParser(PApplet app, String fname){
		filename = fname;
		try {
			saxParser = factory.newSAXParser();
		}
		catch (ParserConfigurationException e) {
			error = true;
			Message.println("Unable to configure parser for file {0}", filename);
			//e.printStackTrace();
		} catch (SAXException e) {
			error = true;
			Message.println("General SAX parser for file {0}", filename);
		}
		istream = app.createInput(fname);
		if(istream == null){
			error = true;
			Message.println("Unable to create input stream for file {0}", filename);
		}
	}

	/**
	 * Use this when not working in Processing.
	 * 
	 * @param fname
	 */
	public EntitySAXParser(String fname){
		filename = fname;
		try{
			saxParser = factory.newSAXParser();
			URL fileURL = getClass().getClassLoader().getResource(fname);
			if(fileURL == null) {
				throw new FileNotFoundException(fname);
			}
			File ifile = new File(fileURL.toURI());

			istream = new FileInputStream(ifile);
			if (ifile.getName().toLowerCase().endsWith(".gz")) {
				istream = new GZIPInputStream(istream);
			}
		} 
		catch (IOException e) {
			error = true;
			Message.println("Could not create input stream for file {0}", filename);
		}
		catch(URISyntaxException e) {
			error = true;
			Message.println("Bad URI for file: {0} ", filename);
		} 
		catch (ParserConfigurationException e) {
			error = true;
			Message.println("Unable to cofigure parser for file {0}", filename);
			//e.printStackTrace();
		} catch (SAXException e) {
			error = true;
			Message.println("General SAX parser for file {0}", filename);
		}
	}	


	public EntitySAXParser(File aFile){
		filename = aFile.getName();
		try{
			saxParser = factory.newSAXParser();
			istream = new FileInputStream(aFile);
		} 
		catch (IOException e) {
			Message.println("Could not create input stream for file {0}", filename);
			e.printStackTrace();
		}
		catch(IllegalArgumentException e) {
			Message.println("Bad URI for file: {0}", filename);
		} 
		catch (ParserConfigurationException e) {
			Message.println("Unable to cofigure parser for file {0}", filename);
			error = true;
		} catch (SAXException e) {
			error = true;
			Message.println("General SAX parser for file {0}", filename);
		}
	}
	
}
