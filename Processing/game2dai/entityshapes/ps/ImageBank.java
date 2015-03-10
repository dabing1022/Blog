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

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class to load and tile images.
 * 
 * @author Peter Lager
 *
 */
public class ImageBank {

	private static HashMap<String, PImage[]> textures = new HashMap<String, PImage[]>();

	/**
	 * Load a bitmap file and return a reference to an array (size will be 1) 
	 * with the image as the sole frame.
	 * 
	 * @param app the PApplet object (sketch)
	 * @param filename the name of the bitmap file
	 * @return an array containing a single PImages that represent the sole frame to flip through
	 */
	public static PImage[] getImage(PApplet app, String filename){
		return getImage(app, filename, 1, 1);
	}

	/**
	 * Load a bitmap file and create an array of PImages to represent a sequence of frames to flip 
	 * through when drawing. <br>
	 * The frame numbers increase left to right and top down.
	 * 
	 * @param app the PApplet object (sketch)
	 * @param filename the name of the bitmap file
	 * @param nbrCols number of horizontal tiles in the image
	 * @param nbrRows number of vertical tiles in the image
	 * @return an array of PImages that represent the frames to flip through else null if file does not exist
	 */
	public static PImage[] getImage(PApplet app, String filename, int nbrCols, int nbrRows){
		if(textures.containsKey(filename)){
			return textures.get(filename);
		}
		PImage image = app.loadImage(filename);
		if(image != null){
			PImage[] frames = makeImageArray(app, image, nbrCols, nbrRows);
			textures.put(filename, frames);
			return frames;
		}
		PApplet.println("Unable to load image from file '" + filename+"'");
		return null;
	}

	/**
	 * Split the image up into a number of frames
	 * 
	 * @param app
	 * @param img
	 * @param nCols
	 * @param nRows
	 * @return the frames as an array of PImage(s)
	 */
	private static PImage[] makeImageArray(PApplet app, PImage img, int nCols, int nRows){
		int nbrFrames = nCols * nRows;
		PImage[] frames = new PImage[nbrFrames];
		if(nbrFrames == 1){
			frames[0] = img;
		}
		else {
			int idx = 0;
			int tileW = img.width / nCols;
			int tileH = img.height / nRows;
			for(int y = 0; y < nRows; y++){
				for(int x = 0; x < nCols; x++){
					frames[idx] = app.createImage(tileW, tileH, PApplet.ARGB);
					frames[idx].copy(img, x * tileW, y * tileH, tileW, tileH, 0, 0, tileW, tileH);
					idx++;
				}
			}
		}
		return frames;
	}
	
}
