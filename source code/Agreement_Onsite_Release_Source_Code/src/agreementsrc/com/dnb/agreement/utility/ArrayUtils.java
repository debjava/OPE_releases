package com.dnb.agreement.utility;

/**
 * As the name implies - generic Array functionality provided
 * by static methods.
 */
public class ArrayUtils
{
	 /**
	  * Increases the array size for the defined size
	  * @param oldArray
	  * @param newSize
	  * @return
	  */
	 public static Object resizeArray (Object oldArray, int newSize) {
		   int oldSize = java.lang.reflect.Array.getLength(oldArray);
		   Class elementType = oldArray.getClass().getComponentType();
		   Object newArray = java.lang.reflect.Array.newInstance(
		         elementType,newSize);
		   int preserveLength = Math.min(oldSize,newSize);
		   if (preserveLength > 0)
		      System.arraycopy (oldArray,0,newArray,0,preserveLength);
		   return newArray; }
}
