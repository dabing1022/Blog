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

package game2dai.entities;

import game2dai.Domain;
import game2dai.maths.FastMath;
import game2dai.maths.Geometry2D;
import game2dai.maths.Triangulator;
import game2dai.maths.Vector2D;
import game2dai.utils.BuildingSAXParser;

import java.io.File;

import processing.core.PApplet;

/**
 * This class is used to represent a building. <br>
 * 
 * @author Peter Lager
 *
 */
public class Building extends BaseEntity {

	/**
	 * This is the one to use with Processing
	 * @param app
	 * @param xmlFilename
	 * @return an array of Buildings
	 */
	public static Building[] makeFromXML(PApplet app, String xmlFilename){
		return new BuildingSAXParser(app, xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFilename name of the file to parse
	 * @return an array of Buildings
	 */
	public static Building[] makeFromXML(String xmlFilename){
		return new BuildingSAXParser(xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFile File to parse
	 * @return an array of Buildings
	 */
	public static Building[] makeFromXML(File xmlFile){
		return new BuildingSAXParser(xmlFile).get();
	}

	private Wall[] walls = null;

	private Vector2D[] contour;
	private Integer[] triangle;
	private double minX, maxX, minY, maxY;

	/**
	 * Create a building with the given contour.
	 * 
	 * @param contour open list (shape is automatically closed) of corners in counter-clockwise order
	 */
	public Building(Vector2D[] contour){
		this("", new Vector2D(0,0), contour);
	}

	/**
	 * Create a building with the given contour.
	 * 
	 * @param offset x/y to offset the contour data
	 * @param contour open list (shape is automatically closed) of corners in counter-clockwise order
	 */
	public Building(Vector2D offset, Vector2D[] contour){
		this("", offset, contour);
	}

	/**
	 * Create a building with the given contour.
	 * 
	 * @param name an optional name for the building
	 * @param offset x/y to offset the contour data
	 * @param contour open list (shape is automatically closed) of corners in counter-clockwise order
	 */
	public Building(String name, Vector2D offset, Vector2D[] contour){
		super(name);
		this.contour = new Vector2D[contour.length];
		minX = minY = Double.MAX_VALUE;
		maxX = maxY = -Double.MAX_VALUE;
		pos.set(offset);
		for(int i = 0; i < contour.length; i++){
			this.contour[i] = new Vector2D(contour[i]);
			minX = FastMath.min(minX, this.contour[i].x);
			maxX = FastMath.max(maxX, this.contour[i].x);
			minY = FastMath.min(minY, this.contour[i].y);
			maxY = FastMath.max(maxY, this.contour[i].y);
		}
		this.triangle = Triangulator.triangulate(this.contour);
		visible = true;
	}

	/**
	 * @return the walls
	 */
	public Wall[] walls() {
		return walls;
	}

	/**
	 * @param walls the walls to set
	 */
	public void walls(Wall[] walls) {
		this.walls = walls;
	}

	/**
	 * Get the rectangle that encompasses the building in world coordinates. <br>
	 */
	public Domain getExtent(){
		return new Domain(minX, minY, maxX, maxY);
	}

	/**
	 * Determines whether a world point is inside the building or not 
	 */
	@Override
	public boolean isOver(double px, double py) {
		return (Geometry2D.isInsidePolygon(contour, px - pos.x, py - pos.y));
	}

	/**
	 * Determines whether two world points are either side of a building. If they are then they 
	 * cannot 'see' each other.
	 * 
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return true if the building is 'inbetween' the 2 points 
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		// Check the bounding box of the building first
		if(Geometry2D.line_box_xyxy(x0, y0, x1, y1, minX, minY, maxX, maxY)){
			// OK so now check against the walls of the building
			for(int i = 1; i < contour.length; i++){
				if(Geometry2D.line_line(x0, y0, x1, y1, contour[i-1].x, contour[i-1].y, contour[i].x, contour[i].y))
					return true;
			}
			if(Geometry2D.line_line(x0, y0, x1, y1, contour[contour.length-1].x, contour[contour.length-1].y, contour[0].x, contour[0].y))
				return true;
		}
		return false;		
	}

	/**
	 * This is needed by any renderer you might want to create.
	 * @return the contour
	 */
	public Vector2D[] contour() {
		return contour;
	}

	/**
	 * Get an expanded contour.
	 * 
	 * @param e the perpendicular distance between existent and expanded walls. 
	 * @return an array of vertices for the expanded contour.
	 */
	public Vector2D[] expandedContour(double e){
		int n = contour.length;
		Vector2D[] ec = new Vector2D[n];

		ELine[] lines = new ELine[n];

		// Get the expanded lines
		for(int i = 0; i < n; i++){
			lines[i] = new ELine(contour[i], contour[(i+1)%n], e);
		}
		for(int i = 0; i < n; i++){
			ELine prevLine = lines[(i-1+n)%n];
			ec[i] = Geometry2D.line_line_infinite(prevLine.start, prevLine.end, lines[i].start, lines[i].end);
		}
		return ec;
	}

	/**
	 * This is needed by any renderer you might want to create.
	 * @return the triangle
	 */
	public Integer[] triangle() {
		return triangle;
	}

	/**
	 * Determine whether this building is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(Domain view) {
		return Geometry2D.box_box(minX, minY, maxX, maxY, view.lowX, view.lowY, view.highX, view.highY);
	}

	public String toString(){
		StringBuilder s = new StringBuilder(name + "  @ " + pos + "\n  ");
		for(Vector2D p : contour)
			s.append(p + " ");
		return new String(s);
	}

	class ELine {
		Vector2D start;
		Vector2D end;
		Vector2D norm;

		public ELine (Vector2D oldStart, Vector2D oldEnd, double e){
			norm = Vector2D.sub(oldEnd, oldStart);
			norm = norm.getPerp();
			norm.normalize().mult(e);
			start = Vector2D.add(oldStart, norm);
			end = Vector2D.add(oldEnd, norm);
		}
	}
}
