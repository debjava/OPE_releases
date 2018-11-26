
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : Statement1.js                                             *
* Author                      : Kalyani M                                                   *
* Creation Date               : 14-July-2008                                                 *
* Description                 : This file contains all javascript functions.                *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/


function validateRequired(form) {
                var isValid = true;
                var focusField = null;
                var i = 0;
                var fields = new Array();
                oRequired = new required();
                for (x in oRequired) {
                	var field = form[oRequired[x][0]];
                	
                    if (field.type == 'text' ||
                        field.type == 'textarea' ||
                        field.type == 'file' ||
                        field.type == 'select-one' ||
                        field.type == 'radio' ||
                        field.type == 'password') {
                        
                        var value = '';
						// get field's value
						if (field.type == "select-one") {
							var si = field.selectedIndex;
							if (si >= 0) {
								value = field.options[si].value;
							}
						} else {
							value = field.value;
						}
                        
                        if (value == '') {
                        
	                        if (i == 0) {
	                            focusField = field;
	                        }
	                        fields[i++] = oRequired[x][1];
	                        isValid = false;
                        }
                    }
                }
                if (fields.length > 0) {
                   focusField.focus();
                  alert("Mandatory Fields:\n\n"+fields.join('\n'));
                }
                return isValid;
            }


			 var bCancel = false; 

   function validateLoginForm(form) {  
		
        if (bCancel) 
      return true; 
        else 
       return validateRequired(form); 
		
   } 

    function required () { 

	 this.userId = new Array("userId", "User Id", new Function ("varName", " return this[varName];"));
     this.pasword = new Array("password", "Password", new Function ("varName", " return this[varName];"));
	 
    } 
//End --> 

/* This function can be used to change style sheet dynamically.
   Send the id of the elment and the new class to be changed.
   Eg: you can change the background to another on mouseover.
   */

function hover(id, newClass)
{
	identity=document.getElementById(id);
	identity.className=newClass;
}

// added by Anantaraj

//The openWindow array will hold the handles of all open child windows
	var openWindow1 = new Array();

	//Track open adds the new child window handle to the array.
	function trackOpen(winName) {
		openWindow1[openWindow1.length]=winName;
	}

//loop over all known child windows and try to close them.  No error is
	//thrown if a child window(s) was already closed.
	function closeWindows() {
		var openCount = openWindow1.length;
		for(r=0;r<openCount;r++) {
			openWindow1[r].close();
		}
	}

// Ends here