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
import game2dai.maths.Geometry2D;
import game2dai.maths.Vector2D;
import game2dai.utils.ObstacleSAXParser;

import java.io.File;

import processing.core.PApplet;

/**
 * This class represents a circular obstacle. The user must ensure that the 
 * collision radius and the obstacles renderer size are appropriately matched.
 * 
 * @author Peter Lager
 *
 */
public class Obstacle extends BaseEntity { 

	/**
	 * This is the one to use with Processing
	 * 
	 * @param app
	 * @param xmlFilename
	 * @return an array of Obstacles
	 */
	public static Obstacle[] makeFromXML(PApplet app, String xmlFilename){
		return new ObstacleSAXParser(app, xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFilename name of the file to parse
	 * @return an array of Obstacles
	 */
	public static Obstacle[] makeFromXML(String xmlFilename){
		return new ObstacleSAXParser(xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFile File to parse
	 * @return an array of Obstacles
	 */
	public static Obstacle[] makeFromXML(File xmlFile){
		return new ObstacleSAXParser(xmlFile).get();
	}

	/**
	 * Create an obstacle with a name
	 * @param name optional name for this obstacle
	 * @param pos world position of obstacle centre
	 * @param colRadius collision radius
	 */
	public Obstacle(String name, Vector2D pos, double colRadius) {
		super(name, pos, colRadius);
		visible = true;
	}

	/**
	 * Create an obstacle without a name
	 * @param pos world position of obstacle centre
	 * @param colRadius collision radius
	 */
	public Obstacle(Vector2D pos, double colRadius) {
		this("", pos, colRadius);
	}

	/**
	 * Determines whether two points are either side of this obstacle. If they are 
	 * then they cannot 'see' each other.
	 * 
	 * @param x0 x position of first point of interest
	 * @param y0 y position of first point of interest
	 * @param x1 x position of second point of interest
	 * @param y1 y position of second point of interest
	 * @return true if the points are either side else false
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return Geometry2D.line_circle(x0, y0, x1, y1, pos.x, pos.y, colRadius);
	}

	/**
	 * Determine whether this obstacle is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(Domain view) {
		return Geometry2D.box_box(pos.x - colRadius, pos.y - colRadius, pos.x + colRadius, pos.y + colRadius, view.lowX, view.lowY, view.highX, view.highY);
	}

	/**
	 * Determine whether the given world position is over this obstacle.
	 * @param px x position for point of interest 
	 * @param py y position for point of interest 
	 * @return true if over the obstacle
	 */
	@Override
	public boolean isOver(double px, double py) {
		return ((pos.x - px)*(pos.x - px) + (pos.y - py)*(pos.y - py)) <= (colRadius * colRadius);
	}

	public String toString(){
		return (name + "  @ " + pos + "  R:" + colRadius);
	}


}
