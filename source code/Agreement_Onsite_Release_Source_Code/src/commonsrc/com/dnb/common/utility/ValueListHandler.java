/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ValueListHandler.java					                    *
 * Author                      : Boopathy													*
 * Creation Date               : 26-May-2006				                                *
 * Description                 : Implemention of Value List Handler Pattern					*
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
import java.util.ListIterator;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Collections;




/**
 * Implementation of Value List Handler Pattern
 * Used to Iterator the list based on the index
 *
 */
public class ValueListHandler implements ValueListIterator {

	// source list 
  protected List list;
  // list iterator used to iteration
  protected ListIterator listIterator;

  public ValueListHandler() {
  }

 /**
  *@param List
  * sets the list to be iterated
  *
  */
  public void setList(List list)   {
	  
	  System.out.println("the coming list"+list);
    this.list = list;
    if(list != null)
      listIterator =  list.listIterator();
  }

  /**
   *@return Collection
   * returns the source list
   */
  public Collection getList(){
    return list;
  }
    
  /**
   *@return int 
   * returns size of the list
   */

  public int getSize() {
    int size = 0;
    if (list != null)
      size = list.size();
    return size;
  }
   
   /**
    *@param int - current index 
	* set the current index of list to  the specified index
	*/

  public void setCurrentIndex(int currentIndex){
	  while(listIterator.hasNext() && (currentIndex>=1)){
		  currentIndex--; 
		  listIterator.next();
	  }
 
   }

   /**
    *@return Object
	*returns the element referred by the current index
	*/

  public Object getCurrentElement()  {

    Object obj = null;
    if (list != null) {
      int currIndex = listIterator.nextIndex();
      obj = list.get(currIndex);
    }
    return obj;

  }
    
  
  /**
   *@param int - count 
   *@return List
   * returns specified previous elements starting from 
   * current index
   *
   */
  public List getPreviousElements(int count){
    int i = 0;
    Object object = null;
    LinkedList list = new LinkedList();

    if (listIterator != null) {
      while (listIterator.hasPrevious() && (i < count)){
		 object = listIterator.previous();
        list.add(object);
        i++;
      }
    }// end if

	Collections.reverse(list);
	 System.out.println("list"+list);
    return list;
   
  }

  /**
   *@param int - count 
   *@return List
   * returns specified next elements starting from 
   * current index
   *
   */

  public List getNextElements(int count){
    int i = 0;
    Object object = null;
    LinkedList list = new LinkedList();
    if(listIterator != null){
      while(  listIterator.hasNext() && (i < count) ){
        object = listIterator.next();
        list.add(object);
        i++;
      }
    } // end if

    return list;
  }
    
  /**
   * Used to reset the index
   */
  public void resetIndex() {
    if(listIterator != null){
      listIterator = list.listIterator();
    }
  }
}