
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : ProcessMate1.js                                             *
* Author                      : Biju K R                                                    *
* Creation Date               : 16-Feb-2006                                                 *
* Description                 : This file contains all javascript functions.                *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/



/* This is used to include any newly added js, vbs,css file into all the html pages */

//document.write('<script language="javascript" type="text/javascript" src="../../javascript/ProcessMate2.js"></script>');
document.write('<script language="javascript" type="text/javascript" src="../js/agreement3.js"></script>');


/* DEFINITIONS STARTS HERE-
   These Definitions contain constants which are used in this file.	
*/

	//var digits = "0123456789";	            // Allowed NUMBERS 

	var phoneNumberDelimiters = "+";	    // Non-digit characters which are allowed in PHONE NUMBERS.

	var phoneNumberDelimitersKey = 43;	    // Key Code for phoneNumberDelimiters Eg : 43 for '+'

	var minDigitsInPhoneNumber = 15;	    // Minimum no of digits in PHONE NUMBERS.
	
	var forNumber=/(^\d+$)|(^\d+\.\d+$)/	// This is used to check whether the given string is number or not.	

    var emailReg = "^[\\w-_\.]*[\\w-_\.]\@[\\w]\.+[\\w]+[\\w]$"; // expression for validating email address
    
    var urlVal ="^[\\w-_\.]*[\\w-_\.]\://[\\w]\.+[\\w]\.+[\\w]+[\\w]"; //expression for validating url.

	var disp_text="";						// This variable is used for storing mesg that are displayed



/*
--------------------------------------------------------------------------------------------
              CONVENTIONS USED FOR VALIDATION

	* : Suffix '*' with id for all the Mandatory fields. Eg. id="Interface Name*"
	# : Suffix '#' with id for Decimal Check
	@ : Suffix '@' with id for Email Validation	
	& : Suffix '&' with id for Number or Integer Check
	+ : Suffix '+' with id for Phone Number Validation
	
	Note : Label for id with the above convention for all validation fields shud be given. 
		   This will then be printed as message alert in UI

----------------------------------------------------------------------------------------------
*/

/* This function can be used to call all the validation functions.
   It checks for all the controls in the UI whose id field contain a '*,#,@..etc' suffixed.
      
   Return :				
				Alert Message.
*/

function validate(nav,editorHold,status)
{

	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	
	editorHold.value=status;
	
	var go= eval("document."+nav);
		

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[2].submit();
		}
	}
	else
	{
		var flag=false;		

		if(checkMandatory())
		
			flag=true;
		else
			return false;				

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;
		
		go.submit();
	}	
}


/* 
	This is used to show popup window for editing entities
*/

function doSomeAction(nextAction,ver,id)
{
	if(nextAction==1)
	{
		showPopUp("../jsp/ServiceBureauDisplay.do?internalBureauId="+id+"&versionNo="+ver,424,720,'no','no');
	}else if(nextAction==2){
        
		showPopUp("../jsp/agreementdisplay.do?agreementId="+id+"&versionNo="+ver,480,720,'no','no');
	
	}else {
		alert("Next Action Not Specified"); 	
	}
}


/* This function can be used to check for Mandatory Fields.
   If the value is null alert is sent to the UI.
   It checks for all the controls in the UI whose id field contain a '*' suffixed.
      
   Return :
   				Alert for Mandatory Fields along with the list of Control Labels.
*/

function checkMandatory()
{
	var list_input = document.getElementsByTagName ('input');
		validateMe(list_input);
	var list_select = document.getElementsByTagName ('select');		
		validateMe(list_select);
		return displayMesg(2);
}

function checkMandatoryCTM()
{
	// alert('Validate');
	var list_input = document.getElementsByTagName ('input');
		validateCTM(list_input);
	var list_select = document.getElementsByTagName ('select');		
		validateCTM(list_select);
		return displayMesg(2);
}

/* This function can be used to check for Mandatory Fields.
   If the value is null alert is sent to the UI.
   It checks for input  controls in the UI whose id field contain a '*' suffixed.
      
   Return :
   				Alert for Mandatory Fields along with the list  of Control Labels.
*/


/** checking to allow numbers only */
 function numbersOnlycust()
{
	var key;
	if (window.event)
	   key = window.event.keyCode;
	if(key<48 || key>57)
		return false;
	else
		return true;
}


/*Function to validate YEAR filed in Baseinsuranceamount entity under customedefinitions HOLD*/

function validatebase(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD with mandatory field
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	if(	status=="HOLD1" || status=="HOLD2")
	{	
		var flag=false;
		if(checkMandatoryWhenHoldbase(go))
			flag=true;
		else
			return false;
		
		
		go.submit();
	}
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

/* function can be used to show a pop up javascript window.
   parameters :- url : specifies the target page link
                 ht  : window ht
				 wt  : window wt
				 sb  : scrollbar = yes / no
				 rs  : resizable = yes / no 
*/		

function showPopUp(url,ht,wt,sb,rs)
{
	
	var left_pos = (screen.width) ? (screen.width-wt)/2 : 0;
	var top_pos = (screen.height) ? (screen.height-ht)/2 : 0;
	var pp='height='+ht+',width='+wt+',top='+top_pos+',left='+left_pos+',status=no,scrollbars='+sb+',resizable='+rs;
	
	var name=url.replace(/\=/g,"");
	name=name.replace(/\&/g,"");
	name=name.replace(/\./g,"");
	name=name.replace(/\?/g,"");
	name=name.replace(/\//g,"");
	name=name.replace(/\ /g,"");
	name=name.replace(/\-/g,"");
	name=name.replace(/\@/g,"");
	name=name.replace(/\|/g,"");

	// var randomNumber = Math.random();

	var randomNumber = 	Math.floor(Math.random()*10000001);
	name = name + randomNumber;

	newwindow=window.open(url,name,pp);
	trackOpen(newwindow);
}

function showPopUp1(url,ht,wt,sb,rs)
{
	
	var left_pos = (screen.width) ? (screen.width-wt)/2 : 0;
	var top_pos = (screen.height) ? (screen.height-ht)/2 : 0;
	var pp='height='+ht+',width='+wt+',top='+top_pos+',left='+left_pos+',status=no,scrollbars='+sb+',resizable='+rs;
	
	newwindow=window.open(url,'name',pp);
	trackOpen(newwindow);
}


/* This function can be used to check if value is a number or decimal or a string 
   It checks for the fields whose id contains a '#' suffixed in it. 
   If the value is a not a number or decimal an alert is sent to the UI.
   Parameter : 

	Return :
   				True / False, Alert.
*/

function checkNumber()
{	
	var list_input = document.getElementsByTagName ('input');
	if (list_input)
		for (var i = 0; i < list_input.length; ++i)
		{
			if(list_input[i].value!="")
			{
	    		if(list_input[i].id.indexOf('#')!=-1 && !forNumber.test(list_input[i].value))
					disp_text+=list_input[i].id.replace('#','') + "\n"
			}
		}
	return displayMesg(3);
}

/* This function can be used to check if value is a Integer or a string 
   It checks for the fields whose id contains a '&' suffixed in it. 
   If the value is a not a number an alert is sent to the UI.
   Parameter : 

	Return :
   				True / False, Alert.
*/

function isInteger()
{
	var st=false;
	var list_input = document.getElementsByTagName ('input');
	if (list_input)
		for (var i = 0; i < list_input.length; ++i)
    	{
			if(list_input[i].id.indexOf('&')!=-1)
		 	   for (var j = 0; j < list_input[i].value.length; j++)
		    	{   
        			// Check that current character is number.
			        var c = list_input[i].value.charAt(j);
    			    if (((c < "0") || (c > "9" ))) 
    					st=true;		
				}
			if(st==true)
				disp_text+=list_input[i].id.replace('&','') + "\n"
			st=false;
		}
		return displayMesg(3);					
}


/* This function can be used to remove certain chars from a string 
   If the value is a not a number an alert is sent to the UI.
   Parameter : 
				c : Character/s to replace
				str : String from which the char shud be removed
	Return :
   				Return string after striping characters
*/

function removeChars(c, str)
{   var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < c.length; i++)
    {   
        // Check that current character isn't whitespace.
        var chr = c.charAt(i);
        if (str.indexOf(chr) == -1) returnString += chr;
    }
    return returnString;
}

/* This function is used to check whether the given value is a valid phone number
   If the value is a not valid an alert is sent to the UI.
   The total number of digits in phone number is defined in global definition at the top.
   A Valid phone number : +003917724524241

   Parameter : 

   Return :
   				True / False, Alert.
*/


function checkPhone()
{

	var list_input = document.getElementsByTagName ('input');

	if (list_input)
		for (var i = 0; i < list_input.length; ++i)
    	if(list_input[i].id.indexOf('+')!=-1)
		{

			var s=removeChars(list_input[i].value,phoneNumberDelimiters);
			if(isInteger(s) && (parseInt(s.length) <= parseInt(minDigitsInPhoneNumber)))
				disp_text+=list_input[i].id.replace('+','') + "\n"
		}
		return displayMesg(1);
}

/* Function to get the name of the form  */
function getFormName()
{
	document.EnterpriseUnits.test_var.value=opener.document.forms[0].test_var.value;

	if(opener.document.UserEdit.checkOk.value=="Y")
	{
		loadSelections(document.getElementById('EnterpriseUnits'));
	}
}


/*Function to validate fileds when HOLD*/

function validateWhenHold(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD with mandatory field
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	if(	status=="HOLD1" || status=="HOLD2" || status=="HOLD")
	{	
		var flag=false;
		if(checkMandatoryWhenHold())
			flag=true;
		else
			return false;
			
		if(checkNumber())
			flag=true;
		else
			return false;
		go.submit();
	}
}

function checkMandatoryWhenHold()
{
	var list_input = document.getElementsByTagName ('input');
		validateMeWhenHold(list_input);
	var list_select = document.getElementsByTagName ('select');		
		validateMeWhenHold(list_select);	
	return displayMesg(2);
}

/* This function is used in the above function */

function validateMeWhenHold(list_array)
{
	if (!list_array)
	   	return;
	for (var i = 0; i < list_array.length; ++i)
    	if(list_array[i].id.indexOf('?')!=-1 && list_array[i].value=="" || list_array[i].value=="select" )
			disp_text+=list_array[i].id.replace('?','') + "\n"
}

/* This function is used to limit the number of text entry in a field

   Parameter : control name eg : Form.TextArea
			   limit  eg : 255 chars
*/

function textCounter(field, maxlimit) 
{
	if (field.value.length > maxlimit) // if too long...trim it!
		field.value = field.value.substring(0, maxlimit);
}

/* This function is used to filter non numeric characters
*/

function numbersOnly()
{
	var key;
	if (window.event)
	   key = window.event.keyCode;
	if(key<48 || key>57)
		return false;
	else
		return true;
}


function pincodeOnly()
{
	var key;
	if (window.event)
	   key = window.event.keyCode;
	if((key<48 || key>57) && key!=32)
		return false;
	else
		return true;
}


/** This function is used to force selecting in selecting roles to store in the database */

function selectAllRoles(selObj)
{
selectedFlag = selObj.options.selectedIndex == -1;
for (i = 0; i < selObj.options.length; i ++)
{
selObj.options[i].selected = true;
}
}
 
/*Function to validate Ageto, Agefrom and Multiplication factor fileds in Multiplicationfactor entity under customedefinitions HOLD*/

function validatemult(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD with mandatory field
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	if(	status=="HOLD")
	{	
		var flag=false;
		if(checkMandatoryWhenHoldmult(go))
			flag=true;
		else
			return false;

		go.submit();
	}
	else
		if(	status=="HOLD1")
	{	
		var flag=false;
		if(checkMandatoryWhenHoldmult(go))
			flag=true;
		else
			return false;

		go.submit();
	}
}

/*

this fuction should allow uppercase letters only
*/

function ucharactersOnly1()
{
  	
	var key = window.event.keyCode;

	
	if(key<=64 || key>=91)
	    return false;
	else
		return true;
}

/** This function is used to enter only valid telephone or fax
 * Delimiter is defined above
*/

function chkTelFax(obj)
{
	var key;
	if (window.event)
	   key = window.event.keyCode;

	if(key == phoneNumberDelimitersKey)
		if(obj.value.indexOf(phoneNumberDelimiters)==-1)
			return true;
		else
			return false;

	if((key<48 || key>57) && key!=32 && key!=45)
		return false;
	else
		return true;
}

/* Functions to get and set value for contributing collateral in collateral entity */
function setValue1()
	    {
		if (document.getElementById('Check').checked)
		{	
			document.getElementById('Contributing').value = "Y" ;
			//alert(document.getElementById('Contributing').value);
		}
		else
		{
			document.getElementById('Contributing').value = "N";
			//alert(document.getElementById('Contributing').value);
		}
		//return alphanumeric(document.getElementById('UserID*?').value);
	
        } 


function getValue1()
	    {
		if (document.getElementById('Active').checked)
		{	
			document.getElementById('Contributing').value = "Y" ;
		}
		else
		{
			document.getElementById('Contributing').value = "N";
		}
		
        } 


function RefreshWin()
{
	newWind=window.opener.location;
		if(newWind!=null)
	{	
		window.opener.location.reload();
		return true;
	}
	else		
		return false;

}


/** This function is used to move selected roles from Available roles list to Selected roles list 
in Usergrouproles entity*/
     <!--
     function MoveOption(objSourceElement, objTargetElement)
     {
  		 var aryTempSourceOptions = new Array();
   		 var aryTempTargetOptions = new Array();
   		 var x = 0;
			   
		//looping through source element to find selected options
			for (var i = 0; i < objSourceElement.length; i++) {
				if (objSourceElement.options[i].selected) {
					//need to move this option to target element
					 var intTargetLen = objTargetElement.length++;
					 objTargetElement.options[intTargetLen].text = objSourceElement.options[i].text;
					 objTargetElement.options[intTargetLen].value = objSourceElement.options[i].value;
				}
			   else {
					 //storing options that stay to recreate select element
					 var objTempValues = new Object();
					 objTempValues.text = objSourceElement.options[i].text;
					 objTempValues.value = objSourceElement.options[i].value;
					 aryTempSourceOptions[x] = objTempValues;
					 x++;
			   }
			 }

			   //sorting and refilling target list
			   for (var i = 0; i < objTargetElement.length; i++) {
				var objTempValues = new Object();
				objTempValues.text = objTargetElement.options[i].text;
				objTempValues.value = objTargetElement.options[i].value;
				aryTempTargetOptions[i] = objTempValues;
			   }

			   aryTempTargetOptions.sort(sortByText);

			   for (var i = 0; i < objTargetElement.length; i++) {
				objTargetElement.options[i].text = aryTempTargetOptions[i].text;
				objTargetElement.options[i].value = aryTempTargetOptions[i].value;
				objTargetElement.options[i];
			   }
			   
			   //resetting length of source
			   objSourceElement.length = aryTempSourceOptions.length;
			   //looping through temp array to recreate source select element
			   for (var i = 0; i < aryTempSourceOptions.length; i++) {
				objSourceElement.options[i].text = aryTempSourceOptions[i].text;
				objSourceElement.options[i].value = aryTempSourceOptions[i].value;
				objSourceElement.options[i];
			   }
     }// end of moveoption

     function sortByText(a, b)
     {
   if (a.text < b.text) {return -1}
   if (a.text > b.text) {return 1}
   return 0;
     }

     function selectAll(objTargetElement)
     {
   for (var i = 0; i < objTargetElement.length; i++) {
    objTargetElement.options[i];
   }
   return false;
  }
  //-->

/** This function is used to move all roles from Available roles list to Selected roles list 
in Usergrouproles entity*/

  function MoveOption1(objSourceElement, objTargetElement)
     {
			   var aryTempSourceOptions = new Array();
			   var aryTempTargetOptions = new Array();
			   var x = 0;
			   
			   //looping through source element to find selected options
			   for (var i = 0; i < objSourceElement.length; i++) {
				if (objSourceElement.options[i]) {
				 //need to move this option to target element
				 var intTargetLen = objTargetElement.length++;
				 objTargetElement.options[intTargetLen].text = objSourceElement.options[i].text;
				 objTargetElement.options[intTargetLen].value = objSourceElement.options[i].value;
				}
				else {
				 //storing options that stay to recreate select element
				 var objTempValues = new Object();
				 objTempValues.text = objSourceElement.options[i].text;
				 objTempValues.value = objSourceElement.options[i].value;
				 aryTempSourceOptions[x] = objTempValues;
				 x++;
				}
			   }

			   //sorting and refilling target list
			   for (var i = 0; i < objTargetElement.length; i++) {
				var objTempValues = new Object();
				objTempValues.text = objTargetElement.options[i].text;
				objTempValues.value = objTargetElement.options[i].value;
				aryTempTargetOptions[i] = objTempValues;
			   }

			   aryTempTargetOptions.sort(sortByText);

			   for (var i = 0; i < objTargetElement.length; i++) {
				objTargetElement.options[i].text = aryTempTargetOptions[i].text;
				objTargetElement.options[i].value = aryTempTargetOptions[i].value;
				objTargetElement.options[i];
			   }
			   
			   //resetting length of source
			   objSourceElement.length = aryTempSourceOptions.length;
			   //looping through temp array to recreate source select element
			   for (var i = 0; i < aryTempSourceOptions.length; i++) {
				objSourceElement.options[i].text = aryTempSourceOptions[i].text;
				objSourceElement.options[i].value = aryTempSourceOptions[i].value;
				objSourceElement.options[i];
			   }
     }

     function sortByText(a, b)
     {
   if (a.text < b.text) {return -1}
   if (a.text > b.text) {return 1}
   return 0;
     }

     function selectAll(objTargetElement)
     {
   for (var i = 0; i < objTargetElement.length; i++) {
    objTargetElement.options[i];
   }
   return false;
  }


/* Function can be used to enable controls used in an HTML page.
   You can set enable/disable for single control or all the controls in the hmtl.
   To enable for a single / few control just pass the control names seperated by comas.
   To enable for controls for whole html just send parameter as all.
   To display any html tags after enabling - place a div tag and pass as parameter its div id and text to display.
   parameters :- al      : all or ''
                 few     : controlname1,controlname2,...
				 disp    : display text
				 dispid  : div id
*/				 

function enableButtons(al,few,disable_me)
{
	if(al!='') // to enable all the input types
	{
		var list_input = document.getElementsByTagName ('input');
			enableThis(list_input);
		var list_select = document.getElementsByTagName ('select');		
			enableThis(list_select);
		var list_select = document.getElementsByTagName ('textarea');		
			enableThis(list_select);
		var list_id = document.getElementsByTagName ('td');		
			enableThis(list_id);
		var list_li = document.getElementsByTagName ('li');		
			enableThis(list_li);
		var list_a = document.getElementsByTagName ('a');		
			enableThis(list_a);
	}
	else if(few!='')
	{
	//put code here- to enable for only few controls
			var list_select = document.getElementsByTagName ('textarea');		
			enableThis(list_select);
	}	
	if(disable_me!='')
	{
		//hover(disable_me,'clsTB_Icons clsLabel clsTableTBDiv');
		var array_ids=disable_me.split(",");
		for(i=0;i<array_ids.length;i++)
		{
			document.getElementById(array_ids[i]).disabled=true;
		}
		//document.getElementById(disable_me).disabled=true;
	}	
	return false;
}

function enableThis(list_array)
{
	if (! list_array)
	   	return;
	for (var i = 0; i < list_array.length; ++i)
    	list_array[i].disabled=false;		

}


/* This function can be used to change style sheet dynamically.
   Send the id of the elment and the new class to be changed.
   Eg: you can change the background to another on mouseover.
   */

function hover(id, newClass)
{
	identity=document.getElementById(id);
	identity.className=newClass;
}

/* This function is used to hide any content / control.
   You need to specify the id of the element.
   */
function hideMe(id)
{
	document.getElementById(id).style.visibility="hidden";
}

/* This function is used to show any hidden content / control.
   You need to specify the id of the element.
   */
function showMe(id)
{
	document.getElementById(id).style.visibility="visible";
}


/* function can be used to check or uncheck all checkboxes in a web page.
   If the checkboxes is already checked, the function unchecks it or
   if unchecked the function checks it.
   Note: Use form-name as form2 only in your html page. 
         [This is only a temporary solution- needs to be sorted out.] 
*/
function checkAll(curobj)
{
	var list_input = document.getElementsByTagName ('input');

	if (list_input )
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			var e = list_input[i]; 
			if (e.type == "checkbox")
				if(curobj.checked==true)
					e.checked=true;
				else
					e.checked=false;
		}
	}
}

/* function can be used to check if any rights have been checked for
 * any entity. If no false is returned
*/
function getNumCheckedRoles()
{
	//page=eval("document."+page);
	var list_input = document.getElementsByTagName ('input');
	var count=0;
	var disp="";
	var caught="";

	if (list_input)
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			
			var e = list_input[i]; 
			
			if (e.type == "checkbox")
				if(e.checked==true)
				{
					disp+=e.name+',';
					count++;				
				}			
		}		
		if(parseInt(count)==0 && parseInt(list_input.length-6)>0) return 6;
		else if(parseInt(count)==0 )	return 7; 
			 else return true;
    }	
	else return false;
}


/* function can be used to put dynamic text between a span or div tag 
   Pass the id of the tag as first parameter and the text to print as the 
   second parameter.
*/

function putText(id,txt)
{
	parent.topright.document.getElementById(id).innerHTML=txt;
}


/* function can be used to put dynamic text between a span or div tag 
   Pass the id of the tag as first parameter and the text to print as the 
   second parameter.
*/

function hideIntf(id1,id2,id3,txt)
{
	parent.leftt.document.getElementById(id2).visible=false;
parent.leftt.document.getElementById(id1).innerHTML=txt;

	parent.leftt.document.getElementById(id3).height='5%';
}


/* Show messages in status bar of the window 
   Pass the refno and version no as parameter
*/
function showStatus(refno,verno)
{
	window.status="Version : "+verno;
}


/* Function is used to populate related contents of one list
   to another list box dynamically */

/* Populate the select boxes for entity code.
	    This is only a temp solution */
		
     var items = new Array();
     var itemLabels = new Array();
	 items[0] = ['Select'];   
     itemLabels[0] = ['----Select----'];  
     
     items[1] = ['Select','Org1','Org2'];  
     itemLabels[1] = ['----Select----','Org1','Org2']; 
     
     items[2] = ['Select','User1','User2']; 
     itemLabels[2] = ['----Select----','User1','User2'];
     
     items[3] = ['Select','Div1','Div2']; 
     itemLabels[3] = ['----Select----','Div1','Div2'];   

function addSelected(theIndex)
{
	var selectObj = document.forms[0].ecode;
    selectObj.options.length = 0;
    for(var no=0;no<items[theIndex].length;no++)
	{
    	selectObj.options[selectObj.options.length] = new Option(itemLabels[theIndex][no],items[theIndex][no]);     
	}          
}

/* Function can be used to enable and disable process and hold alternatively. 
*/

function disable_enable(obj1,obj2)
{
	    obj1.disabled=false ; 
		obj2.disabled=true ; 

}


function getCloseAndReload()
{
	window.opener.close();
	
}

/* Function to popup a window to reply message*/
function doSomeActionForReply(nextAction,ver,id,winName,features)
			
{ 	
	if(nextAction==1)
	{	
		window.open("MessageReply.jsp?ver="+ver+"&id="+id,winName,features);		 

	}	
	else
		alert("Next Action Not Specified");
}


function setVal()
	    {
		if (document.getElementById('Check').checked)
		{	
			document.getElementById('UserStatus').value = "Y" ;
			//alert(document.getElementById('UserStatus').value);
		}
		else
		{
			document.getElementById('UserStatus').value = "N";
			//alert(document.getElementById('UserStatus').value);
		}
		//return alphanumeric(document.getElementById('UserID*?').value);
	
        } 


function getVal()
	    {
		if (document.getElementById('Active').checked)
		{	
			document.getElementById('UserStatus').value = "Y" ;
		}
		else
		{
			document.getElementById('UserStatus').value = "N";
		}
		
        } 

/* Functions to Trim,LTrim,RTrim the given string */

	function Trim(TRIM_VALUE){
		if(TRIM_VALUE.length < 1){
		return"";
		}
		TRIM_VALUE = RTrim(TRIM_VALUE);
		TRIM_VALUE = LTrim(TRIM_VALUE);
		if(TRIM_VALUE==""){
		return "";
		}
		else{
		return TRIM_VALUE;
		}
		} //End Function

	function RTrim(VALUE){
	var w_space = String.fromCharCode(32);
	var v_length = VALUE.length;
	var strTemp = "";
	if(v_length < 0){
	return"";
	}
	var iTemp = v_length -1;

	while(iTemp > -1){
	if(VALUE.charAt(iTemp) == w_space){
	}
	else{
	strTemp = VALUE.substring(0,iTemp +1);
	break;
	}
	iTemp = iTemp-1;

	} //End While
	return strTemp;

	} //End Function

	function LTrim(VALUE){
	var w_space = String.fromCharCode(32);
	if(v_length < 1){
	return"";
	}
	var v_length = VALUE.length;
	var strTemp = "";

	var iTemp = 0;

	while(iTemp < v_length){
	if(VALUE.charAt(iTemp) == w_space){
	}
	else{
	strTemp = VALUE.substring(iTemp,v_length);
	break;
	}
	iTemp = iTemp + 1;
	} //End While
	return strTemp;
	} //End Function

/**
Function to check maxlength and not to allow special characters in 
textarea field
*/
function checkMaxLength (obj,evt,max_chars) { 
 var allowKey = false; 
 if (max_chars > 0) {
  var keyCode = (navigator.appName == "Netscape") ? evt.which : evt.keyCode; 
 
  //if((keyCode == 32 ||keyCode == 44 || keyCode ==46)||(keyCode > 64 && keyCode < 91)||(keyCode > 96 && keyCode <123)){
  if (keyCode > 31){
   allowKey = obj.value.length < max_chars; 
  } 
  if (keyCode == 8 || keyCode == 13) allowKey=true; // Destructive Backspace 
 } 
 return allowKey; 
} 


/* Function to get and set value for check boxes*/
function setActiveValue(obj1,obj2,status1,status2)
	    {
		if (document.getElementById(obj1).checked)
		{	
			document.getElementById(obj2).value =status1 ;				
		}
		else
		{
			document.getElementById(obj2).value =status2;		
		}		
	
        } 

function makeEnable()
{
	
	document.getElementById('stdate_trigger').disabled=false;
	document.getElementById('stdate_trigger1').disabled=false;
}

// this function is used for Date field disabling when edit


function makeEnableDate()
{
	
	document.getElementById('stdate_trigger').disabled=false;
	
}


function validateInterfaceWhenHold(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD with mandatory field
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	if(	status=="HOLD1" || status=="HOLD2")
	{	
		var flag=false;
		if(checkMandatoryWhenHold())
			flag=true;
		else
			return false;

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;	
		
		if(validatePassword())
			flag=true;
		else
			return false;	
		go.submit();
	}
}


/*Function is used to restict space and only accept alphanumeric characters*/
function alphanumericWithoutSpace(src)
{
	 var key;
	if (window.event)
	   key = window.event.keyCode;

	if((key > 47 && key <58) || (key > 64 && key < 91) || (key > 96 && key<123))
		return true;
	else
		
	return false;
}



function validatemarket(nav)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	
	var go= eval("document."+nav);

			go.submit();
		
}
function checkAllCheckBox(curobj)
{
	var list_input = document.getElementsByTagName ('input');

	if (list_input)
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			var e = list_input[i]; 
			if (e.type == "checkbox" && e.disabled==false && e.value!='dontCheck')
				if(curobj.checked==true)
					e.checked=true;
				else
					e.checked=false;
		}
	}
}
function showDeletedRecords()
{
	if(document.getElementById('checkDelete').checked==true)
	{
		document.getElementById('checkStatus').value="Y";
	}
	else
	{
		document.getElementById('checkStatus').value="N";
	}
}

function deleteRecords(nav,editorHold,status,objForm)
{
	/**
	 *	Set status for DELETE
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	var objNumber = parseInt(objForm);

	if(status=="DELETE")
	{
		try{
			document.getElementById('deleteMode').value="D";
		}catch(ex)
		{
		}
		if(getNumChecked(editorHold))
		{
			//closeWindows();  // closing child windows
			document.forms[objNumber].submit();	
			
		}
	}else if(status=="REOPEN")
	{
		document.getElementById('deleteMode').value="R";
		if(getNumCheckedToReopen(editorHold,status))
		{	
			
			document.forms[objNumber].submit();	
			
		}
	}
}

/* Function to erase Link Tree Values */

function eraseLinkTreeValues(sessionId)
{	
	try{
		eraseCookie(document.getElementById(sessionId));
	}catch(ex)
	{
		
	}
}

/* This function is used to open the window with position parameters and re-opening the minimized window  */
function MM_openBrWindow(theURL,winName,features) { //v2.0  

window.open(theURL,winName,features);
   /*if (!window.OpenWindow) {
        // has not yet been defined
        OpenWindow = window.open(theURL,winName,features);
    }
    else {
        // has been defined
        if (!OpenWindow.closed) {
            // still open
            OpenWindow.focus();
        }
        else {
            OpenWindow = window.open(theURL,winName,features);
        }
    }*/

}

/* Function is used to submit the page after pressing the enter key */
function submitPageAfterEnterKey(objForm)
{	
	var go= eval("document."+objForm);
	
	var key;
	if (window.event)
	   key = window.event.keyCode;
	if(key==13)
	{
		go.submit();
	}
}


function validateKPI(nav,editorHold,status)
{
	
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;
	
	var go= eval("document."+nav);
		
	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[3].submit();
		}
	}
	else
	{
		var flag=false;

		if(checkMandatory())
			flag=true;
		else
			return false;

		if(status=="EDIT")
		{
			
			if(validateForm(go)){		
				flag=true;
			}else{ 
				return false;
			}
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;	

		if(DoDatesCompare())
			flag=true;
		else
			return false;	
		go.submit();
	}	
}


function MoveOptionPromotion(objSourceElement, objSourceElementPrd, objTargetElement)
{
            var num = new Array();
            var num1 = new Array();
			 var num2 = new Array();
			var l = objSourceElementPrd.length;
			var l2 = objSourceElement.length;
			    
			var msg = "";
			var csg = "";
			var msg1 = "";
			var csg1 = "";
			var chid = "";
			var chid1 = "";
			var prod = new Array();
			var prods= "";
			for (x = 0; x < l2; x++)
			{
			if (objSourceElement.options[x].selected) {
			   num[x] = objSourceElement.options[x].text;
			   num1[x] = objSourceElement.options[x].value;
			  
			   csg = csg + num[x]+ ' ';
			   csg1 = csg1 + num1[x] + ' ';
			   chid = csg;
			   chid1 = csg1;
			}
		  }
		  
		   for (x = 0; x < l; x++)
		   {
			if (objSourceElementPrd.options[x].selected) {
			   num[x] = objSourceElementPrd.options[x].text;
			   num1[x] = objSourceElementPrd.options[x].value;
			   //alert("num1[x]  : "+num1[x]);
               var vv = num1[x];
			   var cc = vv.substring(0,8);
			   msg = msg + chid;
			   msg = msg + num[x]+ '~';
			   msg1 = msg1 + chid1;
			   msg1 = msg1 + cc +'~';
				
  		   }
		}


	var abc = msg.split('~');
	var abc1 = msg1.split('~');
    	
   
	
	var a = objTargetElement.length;
	var p = a;

  for(i=0;i<abc.length-1;i++)
	{
	  //alert("   abc  :"+abc[i]);
      //alert("   abc1 :"+abc1[i]);
	        objTargetElement.options[p] = new Option(abc[i], abc1[i]);
			p++;
	}

	return objTargetElement;
  }



 function MoveOptionBack(objSourceElement, objTargetElement)
 {
   //looping through source element to find selected options
   for (var i = 0; i < objSourceElement.length; i++) {
    if (objSourceElement.options[i].selected) {
     //need to move this option to target element
     objSourceElement.options[i] = null;
    }
   }
 }

 /********************Function to check if the value is changed in edit page or not ***************************/

var currVals = new Array();		// store curr values of contol in form
var prevVals = new Array();		// store starting values of control in form

	function validateForm(form){

		// store prev values
		populateArray(form, prevVals);
			
		var result = compareArray(currVals, prevVals);

		if(result == true){	
            // Do wish to save ?					
			if(confirm(agmtMesg[10])==true){			
				return true;
			}else{
				return false;
			}
			
		}else{
            // Do wish to save ?
			if(confirm(agmtMesg[10])==true){			
				return true;
			}else{
				return false;
			}			
		}
	}//validateForm Ends

	/*
	fill values of control in form to arr
	form - whose cotrol values req. to store
	arr - cotrol values req. to store in this array
	*/
	function populateArray(form, arr){

		cInd=0;
		for(i=0; i<form.elements.length; i++){
	
			var cType = form.elements[i].type;

			switch(cType){
				case "checkbox":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "text":
					var cValue = form.elements[i].value;
					arr[cInd++]=cValue;
					break;

				case "radio":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "select-one":
					var cValue = form.elements[i].value;
					arr[cInd++]=cValue;
					break;
					
				case "select-multiple":
					var control = form.elements[i];
					for(j=0; j<control.length; j++){
						var cValue = control.options[j].selected;
						arr[cInd++]=cValue;
					}
					break;

				case "textarea":
					var cValue = form.elements[i].value;			
					arr[cInd++]=cValue;
					break;
			}	
		}

	}

	/*
	copare content of two arrya
	lArr - left
	rArr - right
	return true if both are exactly same
	return false if any difference
	*/
	function compareArray(lArr, rArr){
		var size=lArr.length;

		for(i=0; i<size; i++){
			if(lArr[i] != rArr[i])
				return false;
		}
		return true;
	}

	/*
	called when user has changed values
	it will reset original values what user has actually entered
	*/

	function resetOriginalValues(form, arr){

		cInd=0;
		for(i=0; i<form.elements.length; i++){
	
			var cType = form.elements[i].type;

			switch(cType){
				case "checkbox":
					var control = form.elements[i];
					control.checked = arr[cInd++];
					break;

				case "text":
					var control = form.elements[i];
					control.value = arr[cInd++];
					break;

				case "radio":
					var control = form.elements[i];
					control.checked = arr[cInd++];
					break;

				case "select-one":
					var control = form.elements[i];
					control.value = arr[cInd++];
					break;

					
				case "select-multiple":
					var control = form.elements[i];
					for(j=0; j<control.length; j++){
						control.options[j].selected = arr[cInd++];
					}
					break;

				case "textarea":
					form.elements[i].value = arr[cInd++];					
					break;
			}	
		}

	}


function validateAction(nav,editorHold,status)
{	
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[2].submit();
		}
	}
	else if(status=="EDIT")
	{
		
		var flag=false;

		if(status=="ROLE-HOLD" || status=="ROLE-NEW" || status=="ROLE-EDIT")
		{
			var roleRet=getNumCheckedRoles(editorHold);
			if(roleRet==false)
				return false
			else if(roleRet==true)
				flag=true;
			else
				disp_text+=mesg[roleRet]+"\n";
		}
		
		if(checkMandatory())
			flag=true;
		else
			return false;

		if(validateForm(go)){		
			flag=true;
		}else{ 
			return false;
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
		
	}
	else
	{
		var flag=false;

		if(status=="ROLE-HOLD" || status=="ROLE-NEW" || status=="ROLE-EDIT")
		{
			var roleRet=getNumCheckedRoles(editorHold);
			if(roleRet==false)
				return false
			else if(roleRet==true)
				flag=true;
			else
				disp_text+=mesg[roleRet]+"\n";
		}

		if(checkMandatory())
			flag=true;
		else
			return false;
			
		if(status=="ROLE-EDIT"){
				if(validateForm(go)){		
				flag=true;
			}else{ 
			return false;
			}
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
	}	
}

/* 
 *	This is used to show popup window for COPYING entities
 */

function doCopyAction(nextAction,ver,id)
{
	if(nextAction==1)
	{			
		showPopUp("../../pmactions/generic-definitions/EnterpriseCopy.do?enterpriseId="+id+"&versionNo="+ver,570,720,'no','no');

	}else if(nextAction==6)
	{		
		showPopUp("../../pmactions/generic-definitions/UserCopy.do?userId="+id+"&versionNo="+ver,550,720,'no','no');

	}else if(nextAction==7)
	{		
		showPopUp("../../pmactions/generic-definitions/DepartmentCopy.do?departmentId="+id+"&versionNo="+ver,250,720,'no','no');

	} else if(nextAction==3){
		
		showPopUp("../../pmactions/generic-definitions/DivisionCopy.do?divisionCode="+id+"&versionNo="+ver,538,720,'no','no');

	} else if(nextAction==2)
	{
		showPopUp("../../pmactions/generic-definitions/UnitCopy.do?unitId="+id+"&versionNo="+ver,294,720,'no','no');

	}else if(nextAction==22)
	{
		showPopUp("../../pmactions/generic-definitions/InterfaceCopy.do?interfaceId="+id+"&versionNo="+ver,390,720,'no','no');

	}else if(nextAction==5)
	{
		showPopUp("../../pmactions/generic-definitions/LevelCopy.do?levelId="+id+"&versionNo="+ver,270,720,'no','no');

	}else if(nextAction==8)
	{
		showPopUp("../../pmactions/generic-definitions/ExchangeRateCopy.do?exchangeRateId="+id+"&versionNo="+ver,274,720,'no','no');

	}
	else if(nextAction==13)
	{
		showPopUp("../../pmactions/generic-definitions/CurrencyCopy.do?currencyId="+id+"&versionNo="+ver,272,720,'no','no');

	}else if(nextAction==27)
	{
		showPopUp("../../pmactions/generic-definitions/InterestRateCodeCopy.do?rateCode="+id+"&versionNo="+ver,414,720,'no','no');

	}else if(nextAction==36)
	{			
		showPopUp("../../pmactions/generic-definitions/TemplateCopy.do?templateId="+id+"&versionNo="+ver,310,720,'no','no');
	}else if(nextAction==9) 
	{
		showPopUp("../../pmactions/generic-definitions/ProductGroupCopy.do?groupCode="+id+"&versionNo="+ver,328,720,'no','no');
	}else if(nextAction==4)
	{	
		showPopUp("../../pmactions/generic-definitions/UsergroupCopy.do?usergroupId="+id+"&versionNo="+ver,384,990,'no','no');
	} else if(nextAction==26)
	{
		showPopUp("../../pmactions/generic-definitions/ProductCopy.do?productCode="+id+"&versionNo="+ver,368,720,'no','no');

	}else if(nextAction==30)
	{	
		showPopUp("../../pmactions/generic-definitions/WorkflowReasonsCopy.do?reasonId="+id+"&versionNo="+ver,270,720,'no','no');
	}else if(nextAction==33)
	{	
		showPopUp("../../pmactions/generic-definitions/ProdChargeCodeMapCopy.do?prodChargeCodeMapId="+id+"&versionNo="+ver,322,990,'no','no');
	}else if(nextAction==32)
	{	
		showPopUp("../../pmactions/generic-definitions/ChannelCopy.do?channelId="+id+"&versionNo="+ver,476,1008,'no','no');
	}else if(nextAction==31)
	{	
		showPopUp("../../pmactions/generic-definitions/ProductInterestRateMapCopy.do?prodIntRateCodeMapId="+id+"&versionNo="+ver,322,990,'no','no');
	}else if(nextAction==25)
	{	
		showPopUp("../../pmactions/generic-definitions/ChargeCodeCopy.do?chargeCode="+id+"&versionNo="+ver,360,720,'no','no');
	}else if(nextAction==42)
	{	
		showPopUp("../../pmactions/generic-definitions/OpportunityMaintenanceCopy.do?opportunityId="+id+"&versionNo="+ver,538,720,'no','no');
	}else if(nextAction==43)
	{	
		showPopUp("../../pmactions/generic-definitions/ChannelAssociateCopy.do?channelAsscId="+id+"&versionNo="+ver,313,730,'no','no');
	}
	else if(nextAction==45)
	{	
		showPopUp("../../pmactions/generic-definitions/ChannelTypeMaintenanceCopy.do?channelTypeId="+id+"&versionNo="+ver,420,850,'no','no');
	}
	else{
		alert("Next Action Not Specified"); 	
	}

}

/* Function is used to make buttons visible and invisible */

 function makeVisible()
{
	if(document.getElementById('statuscode').value=='3')
	{
		document.getElementById('delete').style.display="none";
	}
	else
	{		
		document.getElementById('reopen').style.display="none";
	}
}


/* Function is used to make buttons visible and invisible */

function validateUserGroupAction(nav,editorHold,status)
{	
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[2].submit();
		}
	}
	else if(status=="EDIT")
	{
		
		var flag=false;		
		
		if(checkMandatory())
			flag=true;
		else
			return false;
		
		if(validateFormForList(go)){		
			flag=true;
		}else{ 
			return false;
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
		
	}
	else
	{
		var flag=false;

		if(status=="ROLE-HOLD" || status=="ROLE-NEW" || status=="ROLE-EDIT")
		{
			var roleRet=getNumCheckedRoles(editorHold);
			if(roleRet==false)
				return false
			else if(roleRet==true)
				flag=true;
			else
				disp_text+=mesg[roleRet]+"\n";
		}

		if(checkMandatory())
			flag=true;
		else
			return false;

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
	}	
}

/* Function to check if the value is changed in List or not */

var currSelVal; //store the current selected roles
var prevSelVal; //store the previous roles before edit
var selObject; //store the object value of the selected items

	function validateFormForList(form){

		var selResult = compareSelectedItems(selObject,prevSelVal)
	
		populateListArray(form, prevVals);
		var result = compareArray(currVals, prevVals);
		
		if(result == true && selResult == true){						
			if(confirm("None of the fields are modified. Do you wish to save?")==true){			
				return true;
			}else{
				return false;
			}
			
		}else{
			if(confirm("Do you wish to save the changes?")==true){			
				return true;
			}else{
				return false;
			}			
		}
	}//validateFormForList Ends

	/*
	fill values of control in form to arr
	form - whose cotrol values req. to store
	arr - cotrol values req. to store in this array
	*/

	function populateListArray(form, arr){

		cInd=0;
		for(i=0; i<form.elements.length; i++){
	
			var cType = form.elements[i].type;

			switch(cType){
				case "checkbox":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "text":
					var cValue = form.elements[i].value;
					arr[cInd++]=cValue;
					break;

				case "radio":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "select-one":
					var cValue = form.elements[i].value;
					arr[cInd++]=cValue;
					break;
					
				case "textarea":
					var cValue = form.elements[i].value;			
					arr[cInd++]=cValue;
					break;
			}	
		}

	}


function compareSelectedItems(obj,preVal)
{
currSelVal=obj.options.length;
if(currSelVal == preVal){
	return true;
}
else 
	return false;
}

/* 
	This is used to show popup window for ROLES
*/

function doSomeActionForRoles(nextAction,ver,id)
{
	if(nextAction==1)
	{
		showPopUp("../../pmactions/generic-definitions/GDinfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,770,'no','no');
	}
	else if(nextAction==2)
	{
		showPopUp("../../pmactions/generic-definitions/ServiceinfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==3)
	{
		showPopUp("../../pmactions/generic-definitions/CRMinfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	} 
	else if(nextAction==4)
	{
		showPopUp("../../pmactions/generic-definitions/ReportsinfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==5)
	{
		showPopUp("../../pmactions/generic-definitions/SalesinfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==6)
	{
		showPopUp("../../pmactions/generic-definitions/AdmininfoRoleDisplay.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else
	    alert("Next Action Not Specified"); 	
}

/* 
	This is used to copy ROLES
*/

function doCopyActionForRoles(nextAction,ver,id)
{
	if(nextAction==1)
	{
		showPopUp("../../pmactions/generic-definitions/GDinfoRoleCopy.do?roleId="+id+"&versionNo="+ver,501,770,'no','no');
	}
	else if(nextAction==2)
	{
		showPopUp("../../pmactions/generic-definitions/ServiceinfoRoleCopy.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==3)
	{
		showPopUp("../../pmactions/generic-definitions/CRMinfoRoleCopy.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	} 
	else if(nextAction==4)
	{
		showPopUp("../../pmactions/generic-definitions/ReportsinfoRoleCopy.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==5)
	{
		showPopUp("../../pmactions/generic-definitions/SalesinfoRoleCopy.do?roleId="+id+"&versionNo="+ver,501,720,'no','no');
	}
	else if(nextAction==6)
	{
		showPopUp("../../pmactions/generic-definitions/AdmininfoRoleCopy.do?roleId="+id+"&versionNo="+ver,514,720,'no','no');
	}
	else
	    alert("Next Action Not Specified"); 	
}



/* Function is used to make buttons visible and invisible */

function validateListAction(nav,editorHold,status)
{	
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[2].submit();
		}
	}
	else if(status=="EDIT")
	{
		
		var flag=false;		
		
		if(checkMandatory())
			flag=true;
		else
			return false;
		
		if(validateFormForList(go)){		
			flag=true;
		}else{ 
			return false;
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
		
	}
	else
	{
		var flag=false;

		if(status=="ROLE-HOLD" || status=="ROLE-NEW" || status=="ROLE-EDIT")
		{
			var roleRet=getNumCheckedRoles(editorHold);
			if(roleRet==false)
				return false
			else if(roleRet==true)
				flag=true;
			else
				disp_text+=mesg[roleRet]+"\n";
		}

		if(checkMandatory())
			flag=true;
		else
			return false;

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
	}	
}


// Function To validate OPPORTUNITY MAINTENACE
function validateOppAction(nav,editorHold,status)
{	
	
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" || status=="LOGUSER")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[2].submit();
		}
	}
	
	else
	{
		var flag=false;
		
		if(checkMandatory())
			flag=true;
		else
			return false;
			
		if(status=="EDIT"){
				if(validateOppForm(go)){		
				flag=true;
			}else{ 
			return false;
			}
		}

		if(checkNumber())
			flag=true;
		else
			return false;
	
		if(isInteger())
			flag=true;		
		else
			return false;
		
		if(checkPhone())
			flag=true;
		else
			return false;		
		go.submit();
	}	
}

 /******Function to check if the value is changed in edit page in OPPORTUNITY MIANTENANCE ***********/

  var prevProduct="";
  var prevChannelAsso="";

	function validateOppForm(form){

		// store prev values
		populateOppArray(form, prevVals);
		 var currProduct= form.product.value;
		 var currChannelAsso= form.channelAssociate.value;
		 var selectResult= false;	
		if(currProduct != prevProduct || currChannelAsso != prevChannelAsso){
			selectResult = false;
		}else{
			selectResult= true;
		}
		var result = compareArray(currVals, prevVals);
		
		if(result == true && selectResult == true){						
			if(confirm("None of the fields are modified. Do you wish to save?")==true){			
				return true;
			}else{
				return false;
			}
			
		}else{
			if(confirm("Do you wish to save the changes?")==true){			
				return true;
			}else{
				return false;
			}			
		}
	}//validateOppForm Ends

	/*
	fill values of control in form to arr
	form - whose cotrol values req. to store
	arr - cotrol values req. to store in this array
	*/
	function populateOppArray(form, arr){

		cInd=0;
		for(i=0; i<form.elements.length; i++){
	
			var cType = form.elements[i].type;

			switch(cType){
				case "checkbox":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "text":
					var cValue = form.elements[i].value;
					arr[cInd++]=cValue;
					break;

				case "radio":
					var cValue = form.elements[i].checked;
					arr[cInd++]=cValue;
					break;

				case "select-one":
					var cValue = form.elements[i].value;
					var cName = form.elements[i].name;
					if(cName == "product" || cName == "channelAssociate"){
						break;
					} 
					else
					arr[cInd++]=cValue;
					break;
					
				case "select-multiple":
					var control = form.elements[i];
					for(j=0; j<control.length; j++){
						var cValue = control.options[j].selected;
						arr[cInd++]=cValue;
					}
					break;

				case "textarea":
					var cValue = form.elements[i].value;			
					arr[cInd++]=cValue;
					break;
			}	
		}
}

function makePreviousDisabled(tdpre){
	document.getElementById(tdpre).disabled=true;
	document.getElementById(tdpre).className='';
}

function confirmations()
{
	//  Do you wish to save?
	if(confirm(agmtMesg[10])==true){			
		return true;
	}else{
		return false;
    }			
}

function confirmations1()
{
	//  Do you wish to save the changes?
	if(confirm(agmtMesg[11])==true){			
		return true;
	}else{
		return false;
    }			
}

function changePreviousIcon(){
	document.getElementById('tdpre').className='left-arrow-disabled';
}

/*
Pass the method name as a variable, and appending '()' to call that method, when user press the enter key
*/
function methodCallAfterEnterKey(name)
{	
	var methodName= eval(name+"()");
	var key;
	if (window.event)
	key = window.event.keyCode;
	if(key==13)
	{
		methodName;
	}
}