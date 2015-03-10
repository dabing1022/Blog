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

package game2dai.entityshapes.ps;

import processing.core.PApplet;
import game2dai.entities.BaseEntity;

/**
 * A simple circular picture with user defined size and colour.
 * 
 * @author Peter Lager
 *
 */
public class CirclePic extends PicturePS {

	private float diam = 10;

	private int fillCol;
	private int strokeCol;
	private float strokeWeight;

	/**
	 * Create a white circle with a thin black border of size 10.
	 * @param papp
	 */
	public CirclePic(PApplet papp){
		super(papp);	
		fillCol = app.color(255);
		strokeCol = app.color(255);
		strokeWeight = 1;
	}
	
	
	/**
	 * Create a circle picture
	 * @param papp
	 * @param size the diameter (2x collision radius)
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thickness
	 */
	public CirclePic(PApplet papp, float size, int fill, int stroke, float weight){
		super(papp);
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
		diam = size;
	}

	/**
	 * Change the drawing factors.
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thickness
	 */
	public CirclePic appearance(int fill, int stroke, float weight){
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
		return this;
	}

	/**
	 * Draw the entity.
	 * @param owner the entity that owns this renderer.
	 * @param posX real world position (x)
	 * @param posY real world position (x)
	 * @param velX magnitude of the velocity vector in the x direction
	 * @param velY magnitude of the velocity vector in the y direction
	 * @param headX magnitude of the heading vector in the x direction
	 * @param headY magnitude of the heading vector in the y direction
	 * @param etime the elapsed time in seconds since last update
	 */
	@Override
	public void draw(BaseEntity owner, float posX, float posY, float velX,
			float velY, float headX, float headY, float etime) {
		// Draw and hints that are specified and relevant
		if(hints != 0){
			Hints.hintFlags = hints;
			Hints.draw(app, owner, velX, velY, headX, headY);
		}

		// Prepare to draw the entity
		app.pushStyle();
		app.ellipseMode(PApplet.CENTER);
		app.pushMatrix();
		app.translate(posX, posY);
		
		// Draw the entity
		app.strokeWeight(1.3f);
		app.stroke(strokeCol);
		app.strokeWeight(strokeWeight);
		app.fill(fillCol);
		app.ellipse(0,0,diam,diam);

		// Finished drawing
		app.popMatrix();
		app.popStyle();		
	}

}
