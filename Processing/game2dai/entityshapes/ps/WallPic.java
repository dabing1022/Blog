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
import game2dai.entities.Wall;
import processing.core.PApplet;

/**
 * A very basic wall picture created using a single line. <br>
 * 
 * 
 * @author Peter Lager
 *
 */
public class WallPic extends PicturePS {
	
	private int strokeCol;
	private float strokeWeight;

	/**
	 * A thin black line.
	 * @param papp
	 */
	public WallPic(PApplet papp){
		super(papp);
		strokeCol = app.color(0);
		strokeWeight = 1f;
	}

	/**
	 * User selected line colour and thickness.
	 * @param papp
	 * @param stroke the line colour
	 * @param weight the line thickness
	 */
	public WallPic(PApplet papp, int stroke, float weight){
		super(papp);
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
		Wall w = (Wall) owner;

		// Prepare to draw entity
		app.pushStyle();
		app.pushMatrix();

		// Draw the wall
		app.strokeWeight(strokeWeight);
		app.stroke(strokeCol);
		app.line(posX, posY, (float)w.getEndPos().x, (float)w.getEndPos().y);

		// Finished drawing
		app.popMatrix();
		app.popStyle();		
	}

	/**
	 * Set wall colour and thickness (stroke weight)
	 * 
	 * @param col
	 * @param thickness
	 */
	public void wallDetails(int col, float thickness){
		strokeCol = col;
		strokeWeight = thickness;
	}

}
