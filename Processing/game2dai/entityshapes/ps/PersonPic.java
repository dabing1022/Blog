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
 * A simple representation of a person viewed from above. <br>
 * The drawing is more complicated so that it will work in all renderers.
 * 
 * @author Peter Lager
 *
 */
public class PersonPic extends PicturePS {

	private float headSize;
	private float[] x = new float[] {-0.5f, 0.2f,  0.2f, -0.5f};
	private float[] y = new float[] {0.6f,  1.0f, -1.0f, -0.6f};

	private int bodyFillCol = 255, headFillCol = 255;;
	private int strokeCol = 0;
	private float strokeWeight = 1.0f;

	/**
	 * Create a person of size 10 with a white body, grey head and thin black border. <br>
	 * 
	 * @param papp
	 */
	public PersonPic(PApplet papp){
		super(papp);
		bodyFillCol = app.color(255);
		headFillCol = app.color(100);
		strokeCol = app.color(0);
		headSize = 10;
		for(int i = 0; i < 4; i++){
			x[i] *= headSize;
			y[i] *= headSize;
		}
	}

	/**
	 * Create a person of size 10 with a white body, grey head and thin black border. <br>
	 * 
	 * @param papp
	 * @param size the overall size (|2x collision radius)
	 */
	public PersonPic(PApplet papp, float size){
		super(papp);
		bodyFillCol = app.color(255);
		headFillCol = app.color(100);
		strokeCol = app.color(0);
		headSize = size;
		for(int i = 0; i < 4; i++){
			x[i] *= headSize;
			y[i] *= headSize;
		}
	}

	/**
	 * Create a person of user defined size and colours.
	 * @param papp
	 * @param size the overall size (|2x collision radius)
	 * @param bodyFill shoulder colour
	 * @param headFill head colour
	 * @param stroke edge colour
	 * @param weight edge thickness
	 */
	public PersonPic(PApplet papp, float size, int bodyFill, int headFill, int stroke, float weight){
		super(papp);
		bodyFillCol = bodyFill;
		headFillCol = headFill;
		strokeCol = stroke;
		strokeWeight = weight;
		headSize = size;
		for(int i = 0; i < 4; i++){
			x[i] *= size;
			y[i] *= size;
		}
	}

	/**
	 * Create a person of user defined size and colours.
	 * @param bodyFill shoulder colour
	 * @param headFill head colour
	 * @param stroke edge colour
	 * @param weight edge thickness
	 */
	public PersonPic appearance(int bodyFill, int headFill, int stroke, float weight){
		bodyFillCol = bodyFill;
		headFillCol = headFill;
		strokeCol = stroke;
		strokeWeight = weight;
		return this;
	}

	/**
	 * Sometimes you might only want to change the body colour.
	 * 
	 * @param col new colour for body (shoulders)
	 */
	public void bodyFill(int col){
		bodyFillCol = col;
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
		// Body first
		fillTriangle(0,1);
		fillTriangle(1,2);
		fillTriangle(2,3);
		fillTriangle(3,0);
		app.noStroke();
		app.fill(bodyFillCol);
		// then head
		app.ellipseMode(PApplet.CENTER);
		app.fill(headFillCol);
		app.ellipse(0,0,headSize,headSize);

		// Finished drawing
		app.popMatrix();
		app.popStyle();	
	}

	private void fillTriangle(int p1, int p2){
		app.noStroke();
		app.fill(bodyFillCol);
		app.beginShape(PApplet.TRIANGLES);
		app.vertex(0,0);
		app.vertex(x[p1], y[p1]);
		app.vertex(x[p2], y[p2]);
		app.endShape(PApplet.CLOSE);

		app.stroke(strokeCol);
		app.strokeWeight(strokeWeight);
		app.line(x[p1], y[p1], x[p2], y[p2]);
	}

}
