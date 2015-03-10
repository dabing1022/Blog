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

import game2dai.entities.BaseEntity;
import processing.core.PApplet;

/**
 * A simple circle for a round obstacle. <br>
 * 
 * @author Peter Lager
 *
 */
public class ObstaclePic extends PicturePS {

	private int fillCol;
	private int strokeCol;
	private float strokeWeight;

	/**
	 * A simple white circle with thin black border.	
	 * @param papp
	 */
	public ObstaclePic(PApplet papp){
		super(papp);
		fillCol = app.color(255);
		strokeCol = app.color(255);
		strokeWeight = 1;
	}

	/**
	 * Picture with user defined colours
	 * @param papp
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thickness
	 */
	public ObstaclePic(PApplet papp, int fill, int stroke, float weight){
		super(papp);
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
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
	public void draw(BaseEntity owner, float posX, float posY, float velX, float velY,
			float headX, float headY, float etime) {
		app.pushStyle();
		app.pushMatrix();
		app.translate(posX, posY);

		app.stroke(strokeCol);
		app.fill(fillCol);	
		app.strokeWeight(strokeWeight);
		app.ellipseMode(PApplet.CENTER);

		double cr = owner.colRadius();
		app.ellipse(0, 0, 2*(float)cr, 2*(float)cr);

		app.popMatrix();
		app.popStyle();		
	}

}
