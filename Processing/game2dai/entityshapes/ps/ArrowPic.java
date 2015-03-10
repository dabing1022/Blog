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
 * Creates an triangular shape that looks like an arrow.
 * 
 * @author Peter Lager
 *
 */
public class ArrowPic extends PicturePS {

	private float arrowLength = 20;
	private float[] x = new float[] {0.5f * arrowLength, -0.4f * arrowLength, -0.4f * arrowLength};
	private float[] y = new float[] {0, 0.3f * arrowLength, -0.3f * arrowLength};

	private int fillCol;
	private int strokeCol;
	private float strokeWeight;

	/**
	 * Create an arrow 20 units long and 12 units at its thick end with a white body
	 * and thin black border.
	 * @param papp
	 */
	public ArrowPic(PApplet papp){
		super(papp);
		fillCol = app.color(255);
		strokeCol = app.color(0);
		strokeWeight = 1;
	}

	/**
	 * Create an arrow of user specified length and colours.
	 * 
	 * @param papp
	 * @param length the length of the arrow
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thicknesss
	 */
	public ArrowPic(PApplet papp, float length, int fill, int stroke, float weight){
		super(papp);
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
		arrowLength = length;
		x = new float[] {0.5f * arrowLength, -0.4f * arrowLength, -0.4f * arrowLength};
		y = new float[] {0, 0.3f * arrowLength, -0.3f * arrowLength};
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
		// Draw and hints that are specified and relevant
		if(hints != 0){
			Hints.hintFlags = hints;
			Hints.draw(app, owner, velX, velY, headX, headY);
		}
		// Determine the angle the entity is facing
		float angle = PApplet.atan2(headY, headX);

		// Prepare to draw the entity
		app.pushStyle();
		app.pushMatrix();
		app.translate(posX, posY);
		app.rotate(angle);

		// Draw the entity
		app.strokeWeight(1.3f);
		app.stroke(strokeCol);
		app.strokeWeight(strokeWeight);
		app.fill(fillCol);
		app.beginShape(PApplet.TRIANGLES);
		app.vertex(x[0],y[0]);
		app.vertex(x[1],y[1]);
		app.vertex(x[2],y[2]);
		app.endShape(PApplet.CLOSE);

		// Finished drawing
		app.popMatrix();
		app.popStyle();
	}

	/**
	 * Cahneg the appearance of the arrow
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thicknesss
	 */
	public ArrowPic appearance(int fill, int stroke, float weight){
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
		return this;
	}

}
