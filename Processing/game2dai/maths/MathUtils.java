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

package game2dai.maths;


public class MathUtils {


	public static Integer getInteger(Object obj){
		if(obj == null) return null;
		try {
			Integer i = Integer.parseInt(obj.toString());
			return i;
		}
		catch(Exception e){
			return null;
		}
	}

	public static Float getFloat(Object obj){
		if(obj == null) return null;
		try {
			Float f = Float.parseFloat(obj.toString());
			return f;
		}
		catch(Exception e){
			return null;
		}
	}

	public static Double getDouble(Object obj){
		if(obj == null) return null;
		try {
			Double i = Double.parseDouble(obj.toString());
			return i;
		}
		catch(Exception e){
			return null;
		}
	}

	public static boolean isEqual(double a, double b){
		return (FastMath.abs(a-b) < 1.0E-12f) ? true : false;
	}
	
	public static boolean isEqual(double a, double b, double err){
		return (FastMath.abs(a-b) < err) ? true : false;
	}
	
	public static double randomClamped(){
		return (Math.random() - Math.random());
	}
	
	public static double randomInRange(double r0, double r1){
		return (r1 - r0) * Math.random() + r0;
	}
}
