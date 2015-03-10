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

import java.util.ArrayList;
import java.util.List;

/**
 * Various routines to work with concave/convex shapes.
 * @author Peter Lager
 *
 */
public class Triangulator {

	static final double EPSILON = 1E-30;

	/**
	 * Create a set of triangles from a concave/convex polygon with no holes and no intersecting sides.
	 * @param contour an open list of vertices that make up a 2D polygon 
	 * @param result list of vertex indices (to contour list) in groups of three for the render triangles
	 * @return true if succeeded else false
	 */
	public static boolean triangulate(List<Vector2D> contour, List<Integer> result){
		result.clear();
		Integer[] r = triangulate(contour);
		for(Integer i : r)
			result.add(i);
		return (result.isEmpty()) ? false : true;
	}
	
	/**
	 * Create a set of triangles from a concave/convex polygon with no holes and no intersecting sides.
	 * @param contour an open list of vertices that make up a 2D polygon 
	 * @return an array of vertex indices (to contour list) in groups of three for the render triangles
	 */
	public static Integer[] triangulate(List<Vector2D> contour){
		Vector2D[] contourArray = contour.toArray(new Vector2D[contour.size()]);
		return triangulate(contourArray);
	}

	/**
	 * Create a set of triangles from a concave/convex polygon with no holes and no intersecting sides.
	 * @param contour an array of vertices that make up an open 2D polygon 
	 * @return an array of vertex indices (to contour list) in groups of three for the render triangles
	 */
	public static Integer[] triangulate(Vector2D[] contour){
		int n = contour.length;
		if ( n < 3 ) 
			return null;

		 List<Integer> result = new ArrayList<Integer>();
		 
		int[] vList = new int[n];

		/* we want a counter-clockwise polygon in V based on computer screen coordinates */
		if ( 0.0f < area(contour) )
			for (int v = 0; v < n; v++) vList[v] = v;
		else
			for(int v = 0; v < n; v++) vList[v] = (n-1)-v;

		int nv = n;

		/*  remove nv-2 Vertices, creating 1 triangle every time */
		int count = 2 * nv;   /* error detection */

		for(int m = 0, v = nv - 1; nv > 2; ){
			/* if we loop, it is probably a non-simple polygon */
			if (0 >= (count--)){
				return null; // Triangulation: ERROR - probable bad polygon!
			}

			/* three consecutive vertices in current polygon, <u,v,w> */
			int u = v;		if (nv <= u) u = 0;		/* previous */
			v = u+1; 		if (nv <= v) v = 0;		/* new v    */
			int w = v+1; 	if (nv <= w) w = 0;		/* next     */

			if (snip(contour, u, v, w, nv, vList)){
				int a,b,c,s,t;

				/* true names of the vertices */
				a = vList[u]; b = vList[v]; c = vList[w];

				/* output Triangle */
				result.add(a);
				result.add(b);
				result.add(c);

				m++;

				/* remove v from remaining polygon */
				for(s = v, t = v+1; t < nv; s++, t++) 
					vList[s] = vList[t]; 
				nv--;

				/* reset error detection counter */
				count = 2 * nv;
			}
		}
		return result.toArray(new Integer[result.size()]);
	}

	/**
	 * Calculate the area of the polygon.
	 * 
	 * @param contour an array of vertices that make up an open 2D polygon 
	 * @return the area of the polygon
	 */
	public static double area(Vector2D[] contour){
		int n = contour.length;
		double areaX2 = 0.0;
		for(int p = n-1, q = 0; q < n; p = q++)
			areaX2 += contour[p].x * contour[q].y - contour[q].x * contour[p].y;
		return areaX2 * 0.5;
	}

	/**
	 * Determine if the point pX/pY is inside triangle defined by triangle ABC whose
	 * vertices are given by [ax,ay] [bx,by] [cx,cy]
	 * @return true if the point is inside
	 */
	public static boolean isInsideTriangle(double aX, double aY,
			double bX, double bY,
			double cX, double cY,
			double pX, double pY){
		double ax, ay, bx, by, cx, cy, apx, apy, bpx, bpy, cpx, cpy;
		double cCROSSap, bCROSScp, aCROSSbp;

		ax = cX - bX;  ay = cY - bY;
		bx = aX - cX;  by = aY - cY;
		cx = bX - aX;  cy = bY - aY;
		apx= pX - aX;  apy= pY - aY;
		bpx= pX - bX;  bpy= pY - bY;
		cpx= pX - cX;  cpy= pY - cY;

		aCROSSbp = ax*bpy - ay*bpx;
		cCROSSap = cx*apy - cy*apx;
		bCROSScp = bx*cpy - by*cpx;

		return ((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f));
	}

	/**
	 * Determine if the point (p) is inside triangle defined by triangle ABC
	 * 
	 * @param a triangle vertex 1
	 * @param b triangle vertex 2
	 * @param c triangle vertex 3
	 * @param p point of interest
	 * @return true if inside triangle else false
	 */
	public static boolean isInsideTriangle(Vector2D a, Vector2D b, Vector2D c, Vector2D p){
		return isInsideTriangle(a.x, a.y, b.x, b.y, c.x, c.y, p.x, p.y);
	}
			
	/**
	 * Determine if the point (p) is inside triangle defined by triangle ABC
	 * 
	 * @param a triangle vertex 1
	 * @param b triangle vertex 2
	 * @param c triangle vertex 3
	 * @param pX x position for point of interest
	 * @param pY x position for point of interest
	 * @return true if inside triangle else false
	 */
	public static boolean isInsideTriangle(Vector2D a, Vector2D b, Vector2D c, double pX, double pY){
		return isInsideTriangle(a.x, a.y, b.x, b.y, c.x, c.y, pX, pY);
	}
			
	private static boolean snip(Vector2D[] contour, int u, int v, int w, int n, int[] vList){
		int p;
		double Ax, Ay, Bx, By, Cx, Cy, Px, Py;

		Ax = contour[vList[u]].x;
		Ay = contour[vList[u]].y;

		Bx = contour[vList[v]].x;
		By = contour[vList[v]].y;

		Cx = contour[vList[w]].x;
		Cy = contour[vList[w]].y;

		if ( EPSILON > (((Bx-Ax)*(Cy-Ay)) - ((By-Ay)*(Cx-Ax))) ) 
			return false;

		for (p = 0; p < n; p++){
			if( (p == u) || (p == v) || (p == w) ) continue;
			Px = contour[vList[p]].x;
			Py = contour[vList[p]].y;
			if (isInsideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) 
				return false;
		}
		return true;
	}
}
