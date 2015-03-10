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


package game2dai;

import game2dai.maths.FastMath;
import game2dai.maths.Vector2D;
import game2dai.utils.Message;

/**
 * Objects of this class represent a rectangular region of the world. It
 * can also used to store the viewport or display area of the screen. They
 * can be used to constrain moving entities to particular regions. <br>
 * <strong>WARNING</strong> although the attributes are public, this is 
 * for speed of reading and they should only be changed using the methods 
 * provided in this class.
 * 
 * @author Peter Lager
 *
 */
public class Domain {

	public double lowX;
	public double highX;
	public double lowY;
	public double highY;
	public final Vector2D centre;
	public double width;
	public double height;

	/**
	 * Create a Domain object given the top-left and bottom-right coordinates.
	 * @param lowX
	 * @param lowY
	 * @param highX
	 * @param highY
	 */
	public Domain(double lowX, double lowY, double highX, double highY) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
		centre = new Vector2D((lowX + highX)/2, (lowY + highY)/2);
		width = highX - lowX;
		height = highY - lowY;
	}

	/**
	 * Create a Domain that is a copy of another one.
	 * @param d domain to be copied
	 */
	public Domain(Domain d) {
		lowX = d.lowX;
		lowY = d.lowY;
		highX = d.highX;
		highY = d.highY;
		width = highX - lowX;
		height = highY - lowY;
		centre = new Vector2D(d.centre);
	}

	/**
	 * Set the domain size.
	 * 
	 * @param lowX top-left x coordinate
	 * @param lowY top-left y coordinate
	 * @param width domain width
	 * @param height domain height
	 */
	public void setDomain_xywh(double lowX, double lowY, double width, double height) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.width = width;
		this.height = height;
		highX = lowX + width;
		highY = lowY + height;
		centre.set((lowX + highX)/2, (lowY + highY)/2);
	}

	/**
	 * Centre the domain about the given world position.
	 * @param wx world x position
	 * @param wy world y position
	 */
	public void move_centre_xy_to(double wx, double wy){
		centre.set(wx, wy);
		lowX = centre.x - width/2;
		lowY = centre.y - height/2;
		highX = lowX + width;
		highY = lowY + height;				
	}

	/**
	 * Centre the domain about the given horizontal position.
	 * @param wx world x position
	 */
	public void move_centre_x_to(double wx){
		centre.x = wx;
		lowX = centre.x - width/2;
		highX = lowX + width;
	}

	/**
	 * Centre the domain about the given vertical position.
	 * @param wy world y position
	 */
	public void move_centre_y_to(double wy){
		centre.y = wy;
		lowY = centre.y - height/2;
		highY = lowY + height;				
	}

	/**
	 * Centre the domain about the given position.
	 * @param wx world x centre position
	 * @param wy world y centre position
	 */
	public void move_centre_xy_by(double wx, double wy){
		centre.x -= wx;
		centre.y -= wy;
		lowX -= wx;
		lowY -= wy;
		highX = lowX + width;
		highY = lowY + height;				
	}

	/**
	 * Move the domain centre horizontally by the world distance given.
	 * @param wx world x centre position
	 */
	public void move_centre_x_by(double wx){
		centre.x -= wx;
		lowX -= wx;
		highX = lowX + width;
	}

	/**
	 * Move the domain centre vertically by the world distance given.
	 * @param wy world y centre position
	 */
	public void move_centre_y_by(double wy){
		centre.y -= wy;
		lowY -= wy;
		highY = lowY + height;				
	}

	/**
	 * See if this point is within the domain
	 * @param p the point to test
	 * @return true if the point is on or inside the boundary of this domain
	 */
	public boolean contains(Vector2D p){
		return (p.x >= lowX && p.x <= highX && p.y >= lowY && p.y <= highY);
	}

	/**
	 * See if this point is within a box scaled by the second parameter. <br>
	 * fraction must be >0 otherwise the function always returns false. A value 
	 * of 1 will test against the full size domain and a value of 0.5 means the
	 * point p must be in the middle half both horizontally and vertically. <br>
	 * 
	 * @param p the point to test
	 * @param scale the scale of the domain to consider
	 * @return true if the point is on or inside the boundary of this scaled domain
	 */
	public boolean contains(Vector2D p, double scale){
		double dx = FastMath.abs(p.x - centre.x)/width;
		double dy = FastMath.abs(p.y - centre.y)/height;
		return (dx < scale && dy < scale);
	}

	/**
	 * See if this point is within the domain
	 * @param px x position of point to test
	 * @param py  position of point to test
	 * @return true if the point is on or inside the boundary of this domain
	 */
	public boolean contains(double px, double py){
		return (px >= lowX && px <= highX && py >= lowY && py <= highY);		
	}


	/**
	 * Return the Domain as a String
	 */
	public String toString(){
		return Message.build("Position from {0},{1} to {2},{3}  Size {4} x {5}", lowX,lowY,highX,highY,width,height);
	}
}
