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
import processing.core.PImage;

/**
 * A single image or a sprite sheet is used to render the entity. If a sprite
 * sheet is used then they can be used to animate the rendered entity.
 * 
 * @author Peter Lager
 *
 */
public class BitmapPic extends PicturePS {

	PImage[] img;

	// Direction constants for next squence
	final int FORWARD = 1;
	final int PAUSE = 0;
	final int REVERSE = -1;
	
	double timeToChangeFrame = 0;
	double intervalTime = 0;
	int dir = PAUSE;
	int interval = 0;
	int frameNo = 0;
	
	/**
	 * Single image for all frames
	 * @param papp
	 * @param fname the name of the bitmap image file
	 */
	public BitmapPic(PApplet papp, String fname){
		super(papp);
		img = ImageBank.getImage(papp, fname);
	}
	
	/**
	 * An animated image. <br>
	 * The image frames are stored as tiles within a single image
	 *  
	 * @param papp
	 * @param fname the name of the bitmap image file
	 * @param nCols number of tiles horizontally
	 * @param nRows number of rows vertically
	 * @param interval the time (milliseconds) between image frames
	 */
	public BitmapPic(PApplet papp, String fname, int nCols, int nRows){
		super(papp);
		img = ImageBank.getImage(papp, fname, nCols, nRows);
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
		// Determine the angle the entity is facing
		float angle = PApplet.atan2(headY, headX);

		// If this an animated image then update the animation
		if(dir != PAUSE){
			timeToChangeFrame -= etime;
			while(timeToChangeFrame < 0){
				timeToChangeFrame += intervalTime;
				frameNo += (dir + img.length);
				frameNo %= img.length;
			}
		}
		// Prepare to draw the entity		
		app.pushStyle();
		app.imageMode(PApplet.CENTER);
		app.pushMatrix();
		app.translate(posX, posY);
		app.rotate(angle);

		// Draw the entity		
		app.image(img[frameNo],0,0);

		// Finished drawing
		app.popMatrix();
		app.popStyle();
	}

	public void setAnimation(double interval, int dir){
		if(dir == FORWARD || dir == REVERSE || dir == PAUSE){
			if(interval <= 0)
				intervalTime = 0;
			else
				intervalTime = interval;
			this.dir = dir;
		}
	}

	public void pauseAnimation(){
		intervalTime = 0;
		dir = PAUSE;
	}

}
