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


public final class Geometry2D {

	private static final double 	ACCY 	= 1E-30;
	private static final double[] 	NONE 	= new double[0];

	public static final int ON_PLANE 		= 16;
	public static final int PLANE_INSIDE 	= 17;
	public static final int PLANE_OUTSIDE 	= 18;

	public static final int OUT_LEFT 		= 1;
	public static final int OUT_TOP 		= 2;
	public static final int OUT_RIGHT 		= 4;
	public static final int OUT_BOTTOM 		= 8;

	/**
	 * Sees if a line intersects with the circumference of a circle.
	 * 
	 * @param x0 
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param cx centre of circle x position
	 * @param cy centre of circle y position
	 * @param r radius of circle
	 * @return true if the line intersects the circle else false
	 */
	public static boolean line_circle(double x0, double y0, double x1, double y1, double cx, double cy, double r){
		double f = (x1 - x0);
		double g = (y1 - y0);
		double fSQ = f*f;
		double gSQ = g*g;
		double fgSQ = fSQ + gSQ;
		double rSQ = r*r;

		double xc0 = cx - x0;
		double yc0 = cy - y0;
		double xc1 = cx - x1;
		double yc1 = cy - y1;


		boolean lineInside = xc0*xc0 + yc0*yc0 < rSQ && xc1*xc1 + yc1*yc1 < rSQ;

		double fygx = f*yc0 - g*xc0;
		double root = r*r*fgSQ - fygx*fygx;

		if(root > ACCY && !lineInside){
			double fxgy = f*xc0 + g*yc0;
			double t = fxgy / fgSQ;
			if(t >= 0 && t <= 1)
				return true;
			// Circle intersects with one end then return true
			if( (xc0*xc0 + yc0*yc0 < rSQ) || (xc1*xc1 + yc1*yc1 < rSQ) )
				return true;
		}
		return false;
	}

	/**
	 * Calculate the points of intersection between a line and a circle. <br>
	 * An array is returned that contains the intersection points in x, y order.
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 there is just one intersection (the line is a tangent to the circle) <br>
	 * 4 there are two intersections <br>
	 * 
	 * @param x0 start of line
	 * @param y0 start of line
	 * @param x1 end of line
	 * @param y1 end of line
	 * @param cx centre of circle x position
	 * @param cy centre of circle y position
	 * @param r radius of circle
	 * @return the intersection points as an array (2 elements per intersection)
	 */
	public static double[] line_circle_p(double x0, double y0, double x1, double y1, double cx, double cy, double r){
		double[] result = NONE;
		double f = (x1 - x0);
		double g = (y1 - y0);
		double fSQ = f*f;
		double gSQ = g*g;
		double fgSQ = fSQ + gSQ;

		double xc0 = cx - x0;
		double yc0 = cy - y0;

		double fygx = f*yc0 - g*xc0;
		double root = r*r*fgSQ - fygx*fygx;
		if(root > -ACCY){
			double[] temp = null;
			int np = 0;
			double fxgy = f*xc0 + g*yc0;
			if(root < ACCY){		// tangent so just one point
				double t = fxgy / fgSQ;
				if(t >= 0 && t <= 1)
					temp = new double[] { x0 + f*t, y0 + g*t};
				np = 2;
			}
			else {	// possibly two intersections
				temp = new double[4];
				root = Math.sqrt(root);
				double t = (fxgy - root)/fgSQ;
				if(t >= 0 && t <= 1){
					temp[np++] = x0 + f*t;
					temp[np++] = y0 + g*t;
				}
				t = (fxgy + root)/fgSQ;
				if(t >= 0 && t <= 1){
					temp[np++] = x0 + f*t;
					temp[np++] = y0 + g*t;
				}
			}
			if(temp != null){
				result = new double[np];
				System.arraycopy(temp, 0, result, 0, np);
			}
		}
		return result;
	}

	/**
	 * See if two lines intersect <br>
	 * @param x0 start of line 1
	 * @param y0 start of line 1
	 * @param x1 end of line 1
	 * @param y1 end of line 1
	 * @param x2 start of line 2
	 * @param y2 start of line 2
	 * @param x3 end of line 2
	 * @param y3 end of line 2
	 * @return true if the lines intersect
	 */
	public static boolean line_line(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		double f2 = (x3 - x2);
		double g2 = (y3 - y2);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;
		if(Math.abs(det) > ACCY){
			double s = (f2*(y2 - y0) - g2*(x2 - x0))/ det;
			double t = (f1*(y2 - y0) - g1*(x2 - x0))/ det;
			return (s >= 0 && s <= 1 && t >= 0 && t <= 1);
		}
		return false;
	}

	/**
	 * Find the point of intersection between two lines. <br>
	 * This method uses Vector2D objects to represent the line end points.
	 * @param v0 start of line 1
	 * @param v1 end of line 1
	 * @param v2 start of line 2
	 * @param v3 end of line 2
	 * @return a Vector2D object holding the intersection coordinates else null if no intersection 
	 */
	public static Vector2D line_line_p(Vector2D v0, Vector2D v1, Vector2D v2, Vector2D v3){
		Vector2D intercept = null;

		double f1 = (v1.x - v0.x);
		double g1 = (v1.y - v0.y);
		double f2 = (v3.x - v2.x);
		double g2 = (v3.y - v2.y);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(v2.y - v0.y) - g2*(v2.x - v0.x))/ det;
			double t = (f1*(v2.y - v0.y) - g1*(v2.x - v0.x))/ det;
			if(s >= 0 && s <= 1 && t >= 0 && t <= 1)
				intercept = new Vector2D(v0.x + f1 * s, v0.y + g1 * s);
		}
		return intercept;
	}

	/**
	 * Find the intersection point between two infinite lines that 
	 * pass through the points (v0,v1) and (v2,v3)
	 * @return a Vector2D object of the intercept or null if parallel
	 */
	public static Vector2D line_line_infinite(Vector2D v0, Vector2D v1, Vector2D v2, Vector2D v3){
		Vector2D intercept = null;

		double f1 = (v1.x - v0.x);
		double g1 = (v1.y - v0.y);
		double f2 = (v3.x - v2.x);
		double g2 = (v3.y - v2.y);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(v2.y - v0.y) - g2*(v2.x - v0.x))/ det;
			intercept = new Vector2D(v0.x + f1 * s, v0.y + g1 * s);
		}
		return intercept;
	}

	/**
	 * Find the point of intersection between two lines. <br>
	 * An array is returned that contains the intersection points in x, y order.
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 these are the x/y coordinates of the intersection point. <br>
	 * @param x0 start of line 1
	 * @param y0 start of line 1
	 * @param x1 end of line 1
	 * @param y1 end of line 1
	 * @param x2 start of line 2
	 * @param y2 start of line 2
	 * @param x3 end of line 2
	 * @param y3 end of line 2
	 * @return an array of coordinates for the intersection if any
	 */
	public static double[] line_line_p(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
		double[] result = NONE;
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		double f2 = (x3 - x2);
		double g2 = (y3 - y2);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(y2 - y0) - g2*(x2 - x0))/ det;
			double t = (f1*(y2 - y0) - g1*(x2 - x0))/ det;
			if(s >= 0 && s <= 1 && t >= 0 && t <= 1)
				result = new double[] { x0 + f1 * s, y0 + g1 * s };
		}
		return result;
	}

	/**
	 * Find the point of intersection between two infinite lines that pass through the points
	 * ([x0,y0],[x1,y1]) and ([x2,y2],[x3,y3]). <br>
	 * An array is returned that contains the intersection points in x, y order.
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 these are the x/y coordinates of the intersection point. <br>
	 * @return an array of coordinates for the intersection if any
	 */
	public static double[] line_line_infinite(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
		double[] result = NONE;
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		double f2 = (x3 - x2);
		double g2 = (y3 - y2);
		double f1g2 = f1 * g2;
		double f2g1 = f2 * g1;
		double det = f2g1 - f1g2;

		if(Math.abs(det) > ACCY){
			double s = (f2*(y2 - y0) - g2*(x2 - x0))/ det;
			result = new double[] { x0 + f1 * s, y0 + g1 * s };
		}
		return result;
	}

	/**
	 * Calculate the intersection points between a line and a collection of lines. <br>
	 * This will calculate all the intersection points between a given line
	 * and the lines formed from the points in the array xy. <br>
	 * If the parameter continuous = true the points form a continuous line so the <br>
	 * <pre>
	 * line 1 is from xy[0],xy[1] to xy[2],xy[3] and
	 * line 2 is from xy[2],xy[3] to xy[4],xy[5] and so on
	 * </pre>
	 * and if continuous is false then each set of four array elements form their 
	 * own line <br>
	 * <pre>
	 * line 1 is from xy[0],xy[1] to xy[2],xy[3] and
	 * line 2 is from xy[4],xy[5] to xy[6],xy[7] and so on
	 * </pre>
	 * 
	 * @param x0 x position of the line start
	 * @param y0 y position of the line start
	 * @param x1 x position of the line end
	 * @param y1 y position of the line end
	 * @param xy array of x/y coordinates
	 * @param continuous if true the points makes a continuous line
	 * @return an array with all the intersection coordinates
	 */
	public static double[] line_lines_p(double x0, double y0, double x1, double y1, double[] xy, boolean continuous){
		double[] result = NONE;
		int stride = continuous ? 2 : 4;
		int np = 0;
		double[] temp = new double[xy.length];
		double f2, g2, f1g2, f2g1, det;
		double f1 = (x1 - x0);
		double g1 = (y1 - y0);
		for(int i = 0; i < xy.length - stride; i += stride){
			f2 = (xy[i+2] - xy[i]);
			g2 = (xy[i+3] - xy[i+1]);
			f1g2 = f1 * g2;
			f2g1 = f2 * g1;
			det = f2g1 - f1g2;
			if(Math.abs(det) > ACCY){
				double s = (f2*(xy[i+1] - y0) - g2*(xy[i] - x0))/ det;
				double t = (f1*(xy[i+1] - y0) - g1*(xy[i] - x0))/ det;
				if(s >= 0 && s <= 1 && t >= 0 && t <= 1){
					temp[np++] = x0 + f1 * s;
					temp[np++] = y0 + g1 * s;
				}
			}
		}
		if(np > 0){
			result = new double[np];
			System.arraycopy(temp, 0, result, 0, np);
		}
		return result;
	}

	/**
	 * Determine if two circles overlap
	 * @param cx0 centre of first circle x position
	 * @param cy0 centre of first circle y position
	 * @param r0 radius of first circle
	 * @param cx1 centre of second circle x position
	 * @param cy1 centre of second circle y position
	 * @param r1 radius of second circle
	 * @return true if the circles overlap else false
	 */
	public static boolean circle_circle(double cx0, double cy0, double r0, double cx1, double cy1, double r1){
		double dxSQ = (cx1 - cx0)*(cx1 - cx0);
		double dySQ = (cy1 - cy0)*(cy1 - cy0);
		double rSQ = (r0 + r1)*(r0 + r1);
		double drSQ = (r0 - r1)*(r0 - r1);
		return (dxSQ + dySQ <= rSQ && dxSQ + dySQ >= drSQ);
	}

	/**
	 * Calculate the intersection points between two circles. <br>
	 * If the array is of length: <br>
	 * 0 then there is no intersection <br>
	 * 2 there is just one intersection (the circles are touching) <br>
	 * 4 there are two intersections <br>
	 * 
	 * @param cx0 centre of first circle x position
	 * @param cy0 centre of first circle y position
	 * @param r0 radius of first circle
	 * @param cx1 centre of second circle x position
	 * @param cy1 centre of second circle y position
	 * @param r1 radius of second circle
	 * @return an array with the intersection points
	 */
	public static double[] circle_circle_p(double cx0, double cy0, double r0, double cx1, double cy1, double r1){
		double[] result = NONE;
		double dx = cx1 - cx0;
		double dy = cy1 - cy0;
		double distSQ = dx*dx + dy*dy;

		if(distSQ > ACCY){
			double r0SQ = r0 * r0;
			double r1SQ = r1 * r1;
			double diffRSQ = (r1SQ - r0SQ);
			double root = 2 * (r1SQ + r0SQ) * distSQ - distSQ * distSQ - diffRSQ * diffRSQ;
			if(root > -ACCY){
				double distINV = 0.5f/ distSQ;
				double scl = 0.5f - diffRSQ * distINV;
				double x = dx * scl + cx0;
				double y = dy * scl + cy0;
				if(root < ACCY){
					result = new double[] { x, y };
				}
				else {
					root = distINV * Math.sqrt(root);
					double xfac = dx * root;
					double yfac = dy * root;
					result = new double[] { x-yfac, y+xfac,  x+yfac, y-xfac };
				}
			}
		}
		return result;
	}

	/**
	 * Calculate the tangents from a point. <br>
	 * If the array is of length: <br>
	 * 0 then there is no tangent the point is inside the circle <br>
	 * 2 there is just one intersection (the point is on the circumference) <br>
	 * 4  there are two points.
	 * 
	 * @param x x position for point of interest
	 * @param y y position for point of interest
	 * @param cx centre of circle x position
	 * @param cy centre of circle y position
	 * @param r radius of circle
	 * @return an array of the tangent point coordinates
	 */
	public static double[] tangents_to_circle(double x, double y, double cx, double cy, double r){
		double[] result = NONE;
		double dx = cx - x;
		double dy = cy - y;
		double dxSQ = dx * dx;
		double dySQ = dy * dy;
		double denom = dxSQ + dySQ;

		double root = denom - r*r;

		if(root > -ACCY){
			double denomINV = 1.0f/denom;
			double A, B;

			if(root < ACCY){ // point is on circle
				A = -r*dx*denomINV;
				B = -r*dy*denomINV;
				result = new double[] { cx + A*r, cy + B*r };
			}
			else {
				root = Math.sqrt(root);
				result = new double[4];
				A = (-dy*root - r*dx)*denomINV;
				B = (dx*root - r*dy)*denomINV;
				result[0] = cx + A*r;
				result[1] = cy + B*r;
				A = (dy*root - r*dx)*denomINV;
				B = (-dx*root - r*dy)*denomINV;
				result[2] = cx + A*r;
				result[3] = cy + B*r;	
			}
		}
		return result;
	}



	/**
	 * Will calculate the contact points for both outer and inner tangents. <br>
	 * There are no tangents if one circle is completely inside the other.
	 * If the circles interact only the outer tangents exist. When the circles
	 * do not intersect there will be 4 tangents (outer and inner), the array
	 * has the outer pair first.
	 * 
	 * @param cx0 x position for the first circle
	 * @param cy0 y position for the first circle
	 * @param r0 radius of the first circle
	 * @param cx1 x position for the second circle
	 * @param cy1 y position for the second circle
	 * @param r1 radius of the second circle
	 * @return an array of tangent contact points
	 */
	public static double[] tangents_between_circles(double cx0, double cy0, double r0, double cx1, double cy1, double r1) {
		double[] result = NONE;
		double dxySQ = (cx0 - cx1) * (cx0 - cx1) + (cy0 - cy1) * (cy0 - cy1);

		if (dxySQ <= (r0-r1)*(r0-r1)) return result;

		double d = Math.sqrt(dxySQ);
		double vx = (cx1 - cx0) / d;
		double vy = (cy1 - cy0) / d;

		double[] temp = new double[16];
		int np = 0;
		double c, h, nx, ny;
		for (int sign1 = +1; sign1 >= -1; sign1 -= 2) {
			c = (r0 - sign1 * r1) / d;
			if (c*c > 1) continue;

			h = Math.sqrt(Math.max(0.0, 1.0 - c*c));

			for (int sign2 = +1; sign2 >= -1; sign2 -= 2) {
				nx = vx * c - sign2 * h * vy;
				ny = vy * c + sign2 * h * vx;

				temp[np++] = cx0 + r0 * nx;
				temp[np++] = cy0 + r0 * ny;
				temp[np++] = cx1 + sign1 * r1 * nx;
				temp[np++] = cy1 + sign1 * r1 * ny;
			}
		}
		if(np > 0){
			result = new double[np];
			System.arraycopy(temp, 0, result, 0, np);
		}

		return result;
	}

	/**
	 * Outside is in the same direction of the plane normal. <br>
	 * The first four parameters represent the start and end position
	 * for a line segment (finite plane).  
	 * 
	 * @param x0 x start of the line
	 * @param y0 y start of the line
	 * @param x1 x end of the line
	 * @param y1 y end of the line
	 * @param px x position of the point to test
	 * @param py y position of the point to test
	 * @return returns either PLANE_INSIDE, PLANE_OUTSIDE or ON_PLANE
	 */
	public static int which_side_pp(double x0, double y0, double x1, double y1, double px, double py){
		int side;
		double dot = (y0 - y1)*(px - x0) + (x1 - x0)*(py - y0);

		if(dot < -ACCY)
			side = PLANE_INSIDE;
		else if(dot > ACCY)
			side = PLANE_OUTSIDE;
		else 
			side = ON_PLANE;
		return side;
	}

	/**
	 * Outside is in the same direction of the plane normal. <br>
	 * This version requires a single point on the plane and the normal 
	 * direction. Useful for an infinite plane or for testing many
	 * points against a single plane when the plane normal does not have
	 * to be calculated each time.
	 * 
	 * @param x0 x position point of a point on the plane
	 * @param y0 y position point of a point on the plane
	 * @param nx x value of normal vector
	 * @param ny y value of normal vector
	 * @param px x position of the point to test
	 * @param py y position of the point to test
	 * @return returns either PLANE_INSIDE, PLANE_OUTSIDE or ON_PLANE
	 */
	public static int which_side_pn(double x0, double y0, double nx, double ny, double px, double py){
		int side;
		double dot = nx*(px - x0) + ny*(py - y0);

		if(dot < -ACCY)
			side = PLANE_INSIDE;
		else if(dot > ACCY)
			side = PLANE_OUTSIDE;
		else 
			side = ON_PLANE;
		return side;
	}

	/**
	 * Code copied from {@link java.awt.geom.Rectangle2D#intersectsLine(double, double, double, double)}
	 */
	private static int outcode(double pX, double pY, double rectX, double rectY, double rectWidth, double rectHeight) {
		int out = 0;
		if (rectWidth <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (pX < rectX) {
			out |= OUT_LEFT;
		} else if (pX > rectX + rectWidth) {
			out |= OUT_RIGHT;
		}
		if (rectHeight <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (pY < rectY) {
			out |= OUT_TOP;
		} else if (pY > rectY + rectHeight) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	/**
	 * Determine whether a line intersects with a box. <br>
	 * The box is represented by the top-left and bottom-right corner coordinates. 
	 * @param lx0 start of line
	 * @param ly0 start of line
	 * @param lx1 end of line
	 * @param ly1 end of line
	 * @param rx0 top-left corner of rectangle
	 * @param ry0 top-left corner of rectangle
	 * @param rx1 bottom-right corner of rectangle
	 * @param ry1  bottom-right corner of rectangle
	 * @return true if they intersect else false
	 */
	public static boolean line_box_xyxy(double lx0, double ly0, double lx1, double ly1, double rx0, double ry0, double rx1, double ry1) {
		int out1, out2;
		double rectWidth = rx1 - rx0;
		double rectHeight = ry1 - ry0;
		
		if ((out2 = outcode(lx1, ly1, rx0, ry0, rectWidth, rectHeight)) == 0) {
			return true;
		}
		while ((out1 = outcode(lx0, ly0, rx0, ry0, rectWidth, rectHeight)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				double x = rx0;
				if ((out1 & OUT_RIGHT) != 0) {
					x += rectWidth;
				}
				ly0 = ly0 + (x - lx0) * (ly1 - ly0) / (lx1 - lx0);
				lx0 = x;
			} else {
				double y = ry0;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += rectHeight;
				}
				lx0 = lx0 + (y - ly0) * (lx1 - lx0) / (ly1 - ly0);
				ly0 = y;
			}
		}
		return true;
	}

	/**
	 * Determine whether a line intersects with a box. <br>
	 * The box is represented by the top-left corner coordinates and the box width and height. 
	 * @param lx0 start of line
	 * @param ly0 start of line
	 * @param lx1 end of line
	 * @param ly1 end of line
	 * @param rx0 top-left corner of rectangle
	 * @param ry0 top-left corner of rectangle
	 * @param rWidth width of rectangle
	 * @param rHeight height of rectangle
	 * @return true if they intersect else false
	 */
	public static boolean line_box_xywh(double lx0, double ly0, double lx1, double ly1, double rx0, double ry0, double rWidth, double rHeight) {
		int out1, out2;
		if ((out2 = outcode(lx1, ly1, rx0, ry0, rWidth, rHeight)) == 0) {
			return true;
		}
		while ((out1 = outcode(lx0, ly0, rx0, ry0, rWidth, rHeight)) != 0) {
			if ((out1 & out2) != 0) {
				return false;
			}
			if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
				double x = rx0;
				if ((out1 & OUT_RIGHT) != 0) {
					x += rWidth;
				}
				ly0 = ly0 + (x - lx0) * (ly1 - ly0) / (lx1 - lx0);
				lx0 = x;
			} else {
				double y = ry0;
				if ((out1 & OUT_BOTTOM) != 0) {
					y += rHeight;
				}
				lx0 = lx0 + (y - ly0) * (lx1 - lx0) / (ly1 - ly0);
				ly0 = y;
			}
		}
		return true;
	}

	/**
	 * Determine whether two boxes intersect. <br>
	 * The boxes are represented by the top-left and bottom-right corner coordinates. 
	 * 
	 * @param ax0 top-left corner of rectangle A
	 * @param ay0 top-left corner of rectangle A
	 * @param ax1 bottom-right corner of rectangle A
	 * @param ay1 bottom-right corner of rectangle A
	 * @param bx0 top-left corner of rectangle B
	 * @param by0 top-left corner of rectangle B
	 * @param bx1 bottom-right corner of rectangle B
	 * @param by1 bottom-right corner of rectangle B
	 * @return true if the boxes intersect
	 */
	public static boolean box_box(double ax0, double ay0, double ax1, double ay1, double bx0, double by0, double bx1, double by1){
		double topA = FastMath.min(ay0, ay1);
		double botA = FastMath.max(ay0, ay1);
		double leftA = FastMath.min(ax0, ax1);
		double rightA = FastMath.max(ax0, ax1);
		double topB = FastMath.min(by0, by1);
		double botB = FastMath.max(by0, by1);
		double leftB = FastMath.min(bx0, bx1);
		double rightB = FastMath.max(bx0, bx1);

		if(botA <= topB  || botB <= topA || rightA <= leftB || rightB <= leftA)
			return false;

		return true;		
	}
	
	/**
	 * If two boxes overlap then the overlap region is another box. This method is used to 
	 * calculate the coordinates of the overlap. <br>
	 * The boxes are represented by the top-left and bottom-right corner coordinates. 
	 * If the returned array has a length:
	 * 0 then they do not overlap <br>
	 * 4 then these are the coordinates of the top-left and bottom-right corners of the overlap region.
	 *  
	 * @param ax0 top-left corner of rectangle A
	 * @param ay0 top-left corner of rectangle A
	 * @param ax1 bottom-right corner of rectangle A
	 * @param ay1 bottom-right corner of rectangle A
	 * @param bx0 top-left corner of rectangle B
	 * @param by0 top-left corner of rectangle B
	 * @param bx1 bottom-right corner of rectangle B
	 * @param by1 bottom-right corner of rectangle B
	 * @return an array with the overlap box coordinates (if any)
	 */
	public static double[] box_box_p(double ax0, double ay0, double ax1, double ay1, double bx0, double by0, double bx1, double by1){
		double[] result = NONE;
		double topA = FastMath.min(ay0, ay1);
		double botA = FastMath.max(ay0, ay1);
		double leftA = FastMath.min(ax0, ax1);
		double rightA = FastMath.max(ax0, ax1);
		double topB = FastMath.min(by0, by1);
		double botB = FastMath.max(by0, by1);
		double leftB = FastMath.min(bx0, bx1);
		double rightB = FastMath.max(bx0, bx1);
		
		if(botA <= topB  || botB <= topA || rightA <= leftB || rightB <= leftA)
			return result;

		double leftO = (leftA < leftB) ? leftB : leftA;
		double rightO = (rightA > rightB) ? rightB : rightA;
		double botO = (botA > botB) ? botB : botA;
		double topO = (topA < topB) ? topB : topA;
		result =  new double[] {leftO, topO, rightO, botO};
		return result;
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
	 * Determine if the point pX/pY is inside triangle defined by triangle ABC
	 * 
	 * @param a triangle vertex 1
	 * @param b triangle vertex 2
	 * @param c triangle vertex 3
	 * @param pX x position for point of interest
	 * @param pY y position for point of interest
	 * @return true if inside triangle else false
	 */
	public static boolean isInsideTriangle(Vector2D a, Vector2D b, Vector2D c, double pX, double pY){
		return isInsideTriangle(a.x, a.y, b.x, b.y, c.x, c.y, pX, pY);
	}
	
	
	/**
	 * See if a point is inside the rectangle defined by top-left and bottom right coordinates
	 * @param x0 top-left corner of rectangle 
	 * @param y0 top-left corner of rectangle 
	 * @param x1 bottom-right corner of rectangle
	 * @param y1 bottom-right corner of rectangle
	 * @param pX x position of point of interest
	 * @param pY y position of point of interest
	 * @return true if inside rectangle else false
	 */
	public static boolean isInsideRectangle_xyxy(double x0, double y0, double x1, double y1,
			double pX, double pY){
		return (pX >= x0 && pY >= y0 && pX <= x1 && pY <= y1);
	}

	/**
	 * See if this a is inside the rectangle defined by top-left and bottom right coordinates
	 * @param v0 top-left corner of rectangle 
	 * @param v1 bottom-right corner of rectangle
	 * @param p point of interest
	 * @return true if inside rectangle else false
	 */
	public static boolean isInsideRectangle_xyxy(Vector2D v0, Vector2D v1, Vector2D p){
		return isInsideRectangle_xyxy(v0.x, v0.y, v1.x, v1.y, p.x, p.y);
	}

	/**
	 * See if a point is inside the rectangle defined by top-left and bottom right coordinates
	 * @param x0 top-left corner of rectangle 
	 * @param y0 top-left corner of rectangle 
	 * @param width width of rectangle
	 * @param height height of rectangle
	 * @param pX x position of point of interest
	 * @param pY y position of point of interest
	 * @return true if inside rectangle else false
	 */
	public static boolean isInsideRectangle_xywh(double x0, double y0, double width, double height,
			double pX, double pY){
		return (pX >= x0 && pY >= y0 && pX <= x0 + width && pY <= y0 + height);
	}
	
	/**
	 * See if this a is inside the rectangle defined by top-left and bottom right coordinates
	 * @param v0 top-left corner of rectangle 
	 * @param width width of rectangle
	 * @param height height of rectangle
	 * @param p point of interest
	 * @return true if inside rectangle else false
	 */
	public static boolean isInsideRectangle_xywh(Vector2D v0, double width, double height, Vector2D p){
		return isInsideRectangle_xyxy(v0.x, v0.y, v0.x + width, v0.y + height, p.x, p.y);
	}

	/**
	 * See if the given point is inside a polygon defined by the vertices provided. 
	 * 
	 * @param verts the vertices of the shape
	 * @param x0 
	 * @param y0
	 * @return true if x0, y0 is inside polygon else returns false
	 */
	public static boolean isInsidePolygon(Vector2D[] verts, double x0, double y0){
		  boolean oddNodes = false;
		  for (int i = 0, j = verts.length - 1; i < verts.length; j = i, i++) {
			  Vector2D vi = verts[i];
			  Vector2D vj = verts[j];
		    if ((vi.y < y0 && vj.y >= y0 || vj.y < y0 && vi.y >= y0) && (vi.x + (y0 - vi.y) / (vj.y - vi.y) * (vj.x - vi.x) < x0))
		      oddNodes = !oddNodes;
		  }
		  return oddNodes;
	}
	
	/**
	 * Calculates the squared distance between 2 points
	 * @param x0 point 1
	 * @param y0 point 1
	 * @param x1 point 2
	 * @param y1 point 2
	 * @return the distance between the points squared
	 */
	public static double distance_sq(double x0, double y0, double x1, double y1){
		return (x1-x0)*(x1-x0) + (y1-y0)*(y1-y0);
	}
	
	/**
	 * Calculates the distance between 2 points
	 * @param x0 point 1
	 * @param y0 point 1
	 * @param x1 point 2
	 * @param y1 point 2
	 * @return the distance between the points squared
	 */
	public static double distance(double x0, double y0, double x1, double y1){
		return FastMath.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0));
	}
	
}

