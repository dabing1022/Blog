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
import game2dai.entities.Building;
import game2dai.maths.Vector2D;
import processing.core.PApplet;

/**
 * Simple solid colour picture to represent a building.
 * 
 * @author Peter Lager
 *
 */
public class BuildingPic extends PicturePS {

	// Absolute positions
	private Vector2D[] contour = null;
	private Integer[] triangle = null;

	private int fillCol;
	private int strokeCol;
	private float strokeWeight;

	/**
	 * Simple black and white with thin border design.
	 * @param papp
	 */
	public BuildingPic(PApplet papp){
		super(papp);
		fillCol = app.color(255);
		strokeCol = app.color(255);
		strokeWeight = 1;
	}

	/**
	 * Use specified design.
	 * 
	 * @param papp
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thickness
	 */
	public BuildingPic(PApplet papp, int fill, int stroke, float weight){
		super(papp);
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
	}

	/**
	 * Change the drawing factors.
	 * @param fill the fill colour
	 * @param stroke the border colour
	 * @param weight the border thickness
	 */
	public BuildingPic appearance(float size, int fill, int stroke, float weight){
		fillCol = fill;
		strokeCol = stroke;
		strokeWeight = weight;
		return this;
	}

	
	/**
	 * Set the centre colour
	 */
	public BuildingPic fill(int col){
		fillCol = col;
		return this;
	}

	/**
	 * Set the border colour
	 */
	public BuildingPic stroke(int col){
		strokeCol = col;
		return this;		
	}
	
	/**
	 * Set the border thickness
	 */
	public BuildingPic strokeWeight(float w){
		strokeWeight = w;
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
	public void draw(BaseEntity owner, float posX, float posY, float velX, float velY,
			float headX, float headY, float etime) {
		Building b = (Building)owner;
		contour = b.contour();
		// Safety net in case the building contour has not been set yet
		if(contour == null) 
			return;
		triangle = b.triangle();

		// Prepare to draw the building
		app.pushStyle();
		app.pushMatrix();
		app.translate(posX, posY);
		
		// Draw the entity
		// draw building inside first
		if(triangle != null){
			for(int i = 0 ; i < triangle.length; i+= 3){
				app.beginShape(PApplet.TRIANGLES);
				app.noStroke();
				app.fill(fillCol);
				app.vertex((float)contour[triangle[i+2]].x, (float)contour[triangle[i+2]].y);
				app.vertex((float)contour[triangle[i+1]].x, (float)contour[triangle[i+1]].y);
				app.vertex((float)contour[triangle[i+0]].x, (float)contour[triangle[i+0]].y);
				app.endShape();
			}
		} // end of centre fill
		// Now draw the walls
		app.noFill();
		app.stroke(strokeCol);
		app.strokeWeight(strokeWeight);
		app.beginShape();
		for(int i = 0; i < contour.length; i++)
			app.vertex((float)contour[i].x, (float)contour[i].y);
		app.endShape(PApplet.CLOSE);

		// Finished drawing
		app.popMatrix();
		app.popStyle();
	}

}
