
/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : ProcessMateDateCheck.js                                     *
* Author                      : Ravi Y                                                      *
* Creation Date               : 21-Jul-2006                                                 *
* Description                 : This file contains date validation javascript functions.    *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/


<!-- Begin
// Check browser version
		var isNav4 = false, isNav5 = false, isIE4 = false
		var strSeperator = "/"; 
		var vDateType = 0; 
		var err = 0; // Set the error code to a default of zero

	if(navigator.appName == "Netscape") 
	{
		if (navigator.appVersion < "5") 
		{
		isNav4 = true;
		isNav5 = false;
		}
		else
		if (navigator.appVersion > "4") 
		{
		isNav4 = false;
		isNav5 = true;
	   	}
	}
	else
	{
	isIE4 = true;
	}

function DateFormat(vDateName, vDateValue, e, dateCheck, dateformat) 
{
	vDateType = datetype(dateformat);
// vDateName = object name
// vDateValue = value in the field being checked
// e = event
// dateCheck 
// True  = Verify that the vDateValue is a valid date
// False = Format values being entered into vDateValue only
// vDateType
// 1 = mm/dd/yyyy
// 2 = yyyy/mm/dd
// 3 = dd/mm/yyyy
	if(vDateValue.length>=1 && vDateValue.length<=6)
	{
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	}

// Reformat date to format that can be validated. mm/dd/yyyy
	if (vDateValue.length >= 8 && dateCheck) 
	{
// Additional date formats can be entered here and parsed out to
// a valid date format that the validation routine will recognize.
		if (vDateType == 1) // mm/dd/yyyy
		{
		var mMonth = vDateName.value.substr(0,2);
		var mDay = vDateName.value.substr(3,2);
		var mYear = vDateName.value.substr(6,4)
		}
		if (vDateType == 2) // yyyy/mm/dd
		{
		var mYear = vDateName.value.substr(0,4)
		var mMonth = vDateName.value.substr(5,2);
		var mDay = vDateName.value.substr(8,2);
		}
		if (vDateType == 3) // dd/mm/yyyy
		{
		var mDay = vDateName.value.substr(0,2);
		var mMonth = vDateName.value.substr(3,2);
		var mYear = vDateName.value.substr(6,4)
		}

// Store reformatted date to new variable for validation.
		var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;

		if (!dateValid(vDateValueCheck)) 
		{
		alert("Invalid Date\nPlease Re-Enter");
		vDateName.value = "";
		vDateName.focus();
		return true;
		}
		return true;
	}
	return false;
}

function dateValid(objName) 
{
	var strDate;
	var strDateArray;
	var strDay;
	var strMonth;
	var strYear;
	var intday;
	var intMonth;
	var intYear;
	var booFound = false;
	var datefield = objName;
	var strSeparatorArray = new Array("-"," ","/",".");
	var intElementNr;
// var err = 0;
	strDate = objName;
	if (strDate.length < 1) 
	{
	return true;
	}
	for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) 
	{
		if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) 
		{
		strDateArray = strDate.split(strSeparatorArray[intElementNr]);
			if (strDateArray.length != 3) 
			{
			err = 1;
			return false;
			}
			else
			{
			strDay = strDateArray[0];
			strMonth = strDateArray[1];
			strYear = strDateArray[2];
			}
		booFound = true;
		}
	}

	strTemp = strDay;
	strDay = strMonth;
	strMonth = strTemp;
	intday = parseInt(strDay, 10);
	intMonth = parseInt(strMonth, 10);
	intYear = parseInt(strYear, 10);
	/* Added by anantaraj, to restrict 0000 year as a 2000 */
	if (intYear<1900) 
	{
	err = 5;
	return false;
	}
	// Ends here

	if (intMonth>12 || intMonth<1) 
	{
	err = 5;
	return false;
	}
	if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) 
	{
	err = 6;
	return false;
	}
	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) 
	{
	err = 7;
	return false;
	}
	if (intMonth == 2) 
	{
		if (intday < 1) 
		{
		err = 8;
		return false;
		}
		if (LeapYear(intYear) == true) 
		{
			if (intday > 29) 
			{
			err = 9;
			return false;
			}
		}
		else
		{
			if (intday > 28) 
			{
			err = 10;
			return false;
		    }
	    }
	}
	return true;
}

function LeapYear(intYear) 
{
	if (intYear % 100 == 0) 
	{
		if (intYear % 400 == 0)
		{
		return true; 
		}
	}
	else
	{
		if ((intYear % 4) == 0)
		{
		 return true; 
		}
	}
	return false;
}

function numcheck(str,format)
{
	if(str.length>=format.length)
	event.keyCode=0;
	if (event.keyCode<48 || event.keyCode>57)
	{
	event.keyCode=0;
	}
}


function mask(str,textbox,format)
{
	var a1 = format.indexOf('-');
	var a2 = format.indexOf('/');
	var a3 = format.indexOf(' ');
	var b1 = format.indexOf('-',a1+1);
	var b2 = format.indexOf('/',a2+1);
	var b3 = format.indexOf(' ',a3+1);
	if(a1!=-1)
	{
	var loc = a1 + ',' + b1;
	var delim = '-';
	}
	if(a2!=-1)
	{
	var loc = a2 + ',' + b2;
	var delim = '/';
	}
	if(a3!=-1)
	{
	var loc = a3 + ',' + b3;
	var delim = ' ';
	}

	var locs = loc.split(','); 
	for (var i = 0; i <= locs.length; i++)
	{
		for (var k = 0; k <= str.length; k++)
		{
			if (k == locs[i])
			{
				if (str.substring(k, k+1) != delim)
				{
				str = str.substring(0,k) + delim + str.substring(k,str.length)
				}
			}
		}
	}
	if(event.keyCode==8 || event.keyCode==37 || event.keyCode==39)
	return true;
	if(str.length>=format.length)
	{
	var str1 = str.substring(0,format.length);
	textbox.value = str1;
	}
	else
	textbox.value = str;
}


function datetype(dateformat)
{
	if(dateformat=='mm-dd-yyyy')
	return 1;
	else if(dateformat=='yyyy-MM-dd')
	return 2;
	else if(dateformat=='dd-mm-yyyy')
	return 3;
}

/**
	This Function is used for comparing two date objects.
	The function takes the four parameter as 1)firstDate2)checkCriteria 3)secondDate 4)disp_text
	The FIRST and THIRD parameter are the dates to be compare.
	The SECOND parameter is the check criteria which should be one  from the list{"<","<=",">",">=","=="}
	The FOURTH parameter is the alert message that will be displayed if the check is true.

*/

function compareDate(firstDate,checkCriteria,secondDate,disp_text)
{
	var parsedFirstDate= parseDate(firstDate);
	var parsedSecondDate= parseDate(secondDate);

	var date1 = Date.UTC( parsedFirstDate.getFullYear(), parsedFirstDate.getMonth(), parsedFirstDate.getDate(),0,0,0);
	var date2 = Date.UTC( parsedSecondDate.getFullYear(), parsedSecondDate.getMonth(), parsedSecondDate.getDate(),0,0,0);
	
	if(checkCriteria == "<"){
		
		if(date1 < date2)
		{	
			if(disp_text.length != 0)
			{
				alert(disp_text);
				return true;
			}
			else 
				return true;
		}
		else {
			return false;
		}
	}
	if(checkCriteria == "<="){
	
		if(date1 <= date2)
		{
			if(disp_text.length != 0)
			{
				alert(disp_text);
				return true;
			}
			else 
				return true;
		}
		else {
			return false;
		}
	}
	if(checkCriteria == ">"){
				
		if(date1 > date2)
		{
			if(disp_text.length != 0)
			{
				alert(disp_text);
				return true;
			}
			else 
				return true;
		}else {
			return false;
		}
	}
	if(checkCriteria == ">="){
				
		if(date1 >= date2)
		{
			if(disp_text.length != 0)
			{
				alert(disp_text);
				return true;
			}
			else 
				return true;
		} else {
			return false;
		}
	}
	if(checkCriteria == "=="){
				
		if(date1 == date2)
		{
			if(disp_text.length != 0)
			{
				alert(disp_text);
				return true;
			}
			else 
				return true;
		}else {
			return false;
		}
	}
}
