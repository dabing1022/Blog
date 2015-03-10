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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Class to represent a 2D matrix that can be used to create transformed Vector2D
 * objects.
 * 
 * @author Peter Lager
 *
 */
public class Matrix2D {

	private Matrix matrix;

	public Matrix2D(){
		matrix = new Matrix();
		matrix.identity();
	}

	/**
	 * Multiply this matrix by another
	 * @param mIn the multiplying matrix
	 */
	public void matrixMultiply(final Matrix mIn){
		Matrix mat = new Matrix();
		// Row 1
		mat._11 = (matrix._11*mIn._11) + (matrix._12*mIn._21) + (matrix._13*mIn._31);
		mat._12 = (matrix._11*mIn._12) + (matrix._12*mIn._22) + (matrix._13*mIn._32);
		mat._13 = (matrix._11*mIn._13) + (matrix._12*mIn._23) + (matrix._13*mIn._33);
		// Row 2
		mat._21 = (matrix._21*mIn._11) + (matrix._22*mIn._21) + (matrix._23*mIn._31);
		mat._22 = (matrix._21*mIn._12) + (matrix._22*mIn._22) + (matrix._23*mIn._32);
		mat._23 = (matrix._21*mIn._13) + (matrix._22*mIn._23) + (matrix._23*mIn._33);
		// Row 3
		mat._31 = (matrix._31*mIn._11) + (matrix._32*mIn._21) + (matrix._33*mIn._31);
		mat._32 = (matrix._31*mIn._12) + (matrix._32*mIn._22) + (matrix._33*mIn._32);
		mat._33 = (matrix._31*mIn._13) + (matrix._32*mIn._23) + (matrix._33*mIn._33);
		matrix = mat;
	}


	/**
	 * Create a new list of vectors from the provided list after being transformed
	 * by this matrix.
	 * 
	 * @param vList the original list of vectors
	 * @return a list of transformed vectors.
	 */
	public List<Vector2D> transformVector2D(final List<Vector2D> vList){
		List<Vector2D> transformed = new LinkedList<Vector2D>();
		Iterator<Vector2D> iter = vList.iterator();
		while(iter.hasNext()){
			Vector2D v = iter.next();
			double x = (matrix._11 * v.x) + (matrix._21 * v.y) + (matrix._31);
			double y = (matrix._12 * v.x) + (matrix._22 * v.y) + (matrix._32);
			transformed.add(new Vector2D(x,y));
		}
		return transformed;
	}

	/**
	 * Create a new vector from the provided vector after being transformed 
	 * by the matrix.
	 * 
	 * @param vPoint the original vector
	 * @return the transformed vector
	 */
	public Vector2D transformVector2D(final Vector2D vPoint){
		double x =(matrix._11*vPoint.x) + (matrix._21*vPoint.y) + (matrix._31);
		double y = (matrix._12*vPoint.x) + (matrix._22*vPoint.y) + (matrix._32);
		return new Vector2D(x, y);
	}

	/**
	 * Initialise the matrix to the identity matrix. This will erase the previous
	 * matrix element data.
	 */
	public void identity(){
		matrix._11 = 1; matrix._12 = 0; matrix._13 = 0;
		matrix._21 = 0; matrix._22 = 1; matrix._23 = 0;
		matrix._31 = 0; matrix._32 = 0; matrix._33 = 1;
	}

	/**
	 * Translate the matrix by the amount specified in
	 * x and y.
	 * 
	 * @param x x-translation value
	 * @param y y-translation value
	 */
	public void translate(double x, double y){
		Matrix mat = new Matrix();
		mat._11 = 1; 	mat._12 = 0;  mat._13 = 0;
		mat._21 = 0;	mat._22 = 1;  mat._23 = 0;
		mat._31 = x;    mat._32 = y;  mat._33 = 1;
		matrixMultiply(mat);
	}
	
	/**
	 * Scale the matrix in the x and y directions. 
	 * @param xScale scale x by this
	 * @param yScale scale y by this
	 */
	public void scale(double xScale, double yScale){
		Matrix mat = new Matrix();	  
		mat._11 = xScale; 	mat._12 = 0; 		mat._13 = 0;
		mat._21 = 0; 		mat._22 = yScale; 	mat._23 = 0;
		mat._31 = 0; 		mat._32 = 0; 		mat._33 = 1;
		matrixMultiply(mat);
	}

	/**
	 * Rotate the matrix.
	 * 
	 * @param rot angle in radians.
	 */
	public void rotate(final double rot){
		Matrix mat = new Matrix();
		double sinA = FastMath.sin(rot);
		double cosA = FastMath.cos(rot);

		mat._11 = cosA;  	mat._12 = sinA; 	mat._13 = 0;
		mat._21 = -sinA; 	mat._22 = cosA; 	mat._23 = 0;
		mat._31 = 0; 		mat._32 = 0;		mat._33 = 1;
		matrixMultiply(mat);
	}

	/**
	 * Rotate the matrix based an entity's heading and side vectors
	 * @param fwd
	 * @param side
	 */
	public void rotate(final Vector2D fwd, final Vector2D side){
		Matrix mat = new Matrix();
		mat._11 = fwd.x;  	mat._12 = fwd.y; 	mat._13 = 0;
		mat._21 = side.x; 	mat._22 = side.y; 	mat._23 = 0;
		mat._31 = 0; 		mat._32 = 0;		mat._33 = 1;
		matrixMultiply(mat);
	}


	// setters for the matrix elements
	public void _11(double val){matrix._11 = val;}
	public void _12(double val){matrix._12 = val;}
	public void _13(double val){matrix._13 = val;}

	public void _21(double val){matrix._21 = val;}
	public void _22(double val){matrix._22 = val;}
	public void _23(double val){matrix._23 = val;}

	public void _31(double val){matrix._31 = val;}
	public void _32(double val){matrix._32 = val;}
	public void _33(double val){matrix._33 = val;}


	/**
	 * Handy inner class to hold the intermediate transformation matrices.
	 * 
	 */

	class Matrix {
		private double _11, _12, _13;
		private double _21, _22, _23;
		private double _31, _32, _33;

		/**
		 * Ctor initialises to the zero matrix
		 */
		public Matrix(){
			_11=0.0; 	_12=0.0; 	_13=0.0;
			_21=0.0; 	_22=0.0; 	_23=0.0;
			_31=0.0; 	_32=0.0; 	_33=0.0;
		}

		/**
		 * Set to the identity matrix. Erases previous matrix data.
		 */
		public void identity(){
			_11=1.0; 	_12=0.0; 	_13=0.0;
			_21=0.0; 	_22=1.0; 	_23=0.0;
			_31=0.0; 	_32=0.0; 	_33=1.0;
		}

	}
}
