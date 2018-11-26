/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ValueListIterator.java					                    *
 * Author                      : Boopathy													*
 * Creation Date               : 26-May-2006				                                *
 * Description                 : Methods used for iterating the List						*
 * Modification History        :                                                            *
 *																						    *
 *     Version No.               Date               Brief description of change             *
 *  --------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/


package com.dnb.common.utility;
import java.util.List;

/**
 * Methods used for implementing 
 * ValueListHandler  pattern
 * used to iterator the list based on the index
 *
 */
public interface ValueListIterator {

	  /**
	   * returns size of the list
	   */
	  public int getSize() ;

	  /**
	   * returns current element
	   */
	  public Object getCurrentElement() ;

	  /**
	   * returns specified previous elements starting from current index
	   */
	   
	  public List getPreviousElements(int count) ;
	  
	  /**
	   * returns specified next elements starting from current index
	   */
	  
	  public List getNextElements(int count) ;
	  
	  /**
	   * resets index to 0
	   */
	   public void resetIndex();
 
}