/*****************************************************************************************/
/* Project          : ProcessMate                                                        */
/* File Name        : ProcessMate3.js                                                    */
/* Date of Creation : 17-Mar-2006                                                        */
/* Created By       : Biju K R                                                           */
/* Purpose          : Contains all Javascript Validation function used in ProcessMate    */
/*****************************************************************************************/


/* This function can be used to check if value is a number or decimal or a string 
   It checks for the fields whose id contains a '#' suffixed in it. 
   If the value is a not a number or decimal an alert is sent to the UI.
   Parameter : 

	Return :
   				True / False, Alert.
*/





/* function can be used to check or uncheck all checkboxes in a web page.
   If the checkboxes is already checked, the function unchecks it or
   if unchecked the function checks it.
   Note: Use form-name as form2 only in your html page. 
         [This is only a temporary solution- needs to be sorted out.] 
*/
function getNumChecked(page)
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
				if(e.checked==true && e.name!="n1" && e.name!='checkDelete')
				{
					disp+=e.name+',';
					count++;				
				}
				
		}		
		
		if(parseInt(count)==0 && parseInt(list_input.length-6)>0)
		{
			alert(mesg[9]);
			return false;
		}
		else 
			if(parseInt(count)==0 ){alert(mesg[10]);} 
		else if(confirm(mesg[11] + count + ' ' +mesg[12]))
			{	

				caught=document.getElementById("deleteMe");
				caught.value=disp;
				return true;
			}
			else
				return false;
    }	
	else
		return false;
}

/* This can be used to display and Alert message at the User Interface 
   Displays a Message from the mesg Array defined above 
   
   Parameter :  key : index of Message Array
   Return    : True or False, Alert	
*/  

function displayMesg(key)
{
	if(disp_text!="")
	{
		disp_text=disp_text.replace(/\*/g,'');
		disp_text=disp_text.replace(/\#/g,'');		
		disp_text=disp_text.replace(/\&/g,'');
		disp_text=disp_text.replace(/\+/g,'');
		disp_text=disp_text.replace(/\?/g,'');
		alert(mesg[key] +"\n"+ disp_text);
		disp_text="";
		return false;
	}
	
	disp_text="";
	return true;
	
}


/* Function to get the Unit ID's from checked checkboxes */

function getNumCheckedUnits()
{
	//page=eval("document."+page);
	var list_input = document.getElementsByTagName ('input');
	var count=0;
	var disp="";
	var caught="";
	var formName="";
	
	formName=document.EnterpriseUnits.test_var.value;
	
	if (list_input)
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			var e = list_input[i]; 
			if (e.type == "checkbox")
				if(e.checked==true && e.name!="n1")
				{
					disp+=e.name+',';
					count++;				
				}
		}		
		
		if(parseInt(count)==0)
		{			
			//if(confirm(mesg[11] + count + mesg[13]))
			if(confirm(mesg[43]))
			{
				
				caught=document.EnterpriseUnits.getUnitId;
				caught.value=disp;
								
				//opener.document.UserEdit.checkOk.value="Y";
				
				opener.document.UserEdit.getUnitId.value=caught.value;

				//saveSelections(document.getElementById('EnterpriseUnits'));
				
				window.close();		
				
				return true;
			}
			else
				return false;

		}

		else
			if(confirm(mesg[11] + count +' '+mesg[13]))
			{	
			
					caught=document.EnterpriseUnits.getUnitId;
					caught.value=disp;
				
					opener.document.UserEdit.checkOk.value="Y";

					opener.document.UserEdit.getUnitId.value=caught.value;

					//saveSelections(document.getElementById('EnterpriseUnits'));					
			
					window.close();
					
					return true;
			}
			else
		return false;
    }	
	else
	return false;	
}





/* This function is used to filter non alphanumeric characters
*/
function alphanumeric(src)
{
	var numaric = src.value;
	for(var j=0; j<numaric.length; j++)
		{
		  var alphaa = numaric.charAt(j);
		  var hh = alphaa.charCodeAt(0);
		  if((hh > 47 && hh<59) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
		  {
		  }
		else	{
			var longMsg = mesg[5]; 
			alert(longMsg);
    		src.focus();
			 return false;			 
		  }
		}
		
 return true;
}

/*Function is used to restict special Character and only accept space &alphanumeric characters*/
function alphanumericWithSpace(src)
{
	 var key;
	if (window.event)
	   key = window.event.keyCode;

	if((key > 31 && key <33) || (key > 47 && key < 58) || (key > 64 && key<91) || (key > 96 && key<123))
		return true;
	else
		
	return false;
}

/**
 * Validate an email address
 * This uses regular expression. The expression is defined above 
 */
/*function emailChk(src) 
{
	 if(src.value!="")
	     if(!src.value.match(emailReg)) 
		 {	 
			alert(mesg[4]);
			src.focus();
		 }
 }*/

	var testresults;
	function emailChk(src){

	var str=src.value;
	var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([\w-]{2,6}(?:\.[\w-]{2})?)$/i ;

	if(str!=""){
		if(filter.test(str))
			testresults=true
		else{
			alert(mesg[4]);		
			src.select();		
			testresults=false
		}
	}
		return (testresults)
	}

function urlChk(src)
{
	if(src.value!="")
	if(!src.value.match(urlVal))
	{
		alert(mesg[4]);
		src.focus();
	}
}

/* function can be used to close a popup window */

function closeWindow()
{
	if(confirm(mesg[16])==true)
	{
		window.close();
	}
	else
		return false;
}


/* Function to get the Unit ID's from checked checkboxes */

function getNumCheckedUserUnits()
{
	//page=eval("document."+page);
	var list_input = document.getElementsByTagName ('input');
	var count=0;
	var disp="";
	var caught="";
	var formName="UserNew";
	
	if (list_input)
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			var e = list_input[i]; 
			if (e.type == "checkbox")
				if(e.checked==true && e.name!="n1")
				{
					disp+=e.name+',';
					count++;				
				}
		}		
		
		if(parseInt(count)==0)
		{			
			//if(confirm(mesg[11] + count + mesg[13]))
			if(confirm(mesg[43]))
			{
				//saveSelections(document.getElementById('EnterpriseUnits1'));

				caught=document.EnterpriseUnits1.getUnitId;
				caught.value=disp;
				opener.document.UserNew.getUnitId.value=caught.value;

				window.close();	
				return true;
			}
			else
				return false;			
		}		
		else 
			if(confirm(mesg[11] + count +' '+mesg[13]))
			{				
			caught=document.EnterpriseUnits1.getUnitId;
			caught.value=disp;
			
			if(formName=='UserNew')
				{	
					
					opener.document.UserNew.getUnitId.value=caught.value;	
				}
			//saveSelections(document.getElementById('EnterpriseUnits1'));
			window.close();
		return true;
			}
			else
		return false;
    }	
	else
	return false;	
}




/* Function to get the CustomColumn ID's from textboxes */

function getNumUnits()
{
	//page=eval("document."+page);
	var list_input = document.getElementsByTagName ('input');
	var count=0;
	var disp="";
	var caught="";
	var formName="unit";
	
	if (list_input)
	{
		for (var i = 0; i < list_input.length; ++i)
		{
			var e = list_input[i]; 
			if (e.type == "text")
				if(e.value!=null)
				{
					disp+=e.name+',';
					count++;				
				}
		}		
		
		if(parseInt(count)==0)
		{			
			if(confirm("You have filled : "+count+" CustomColumn. Are you sure to continue?"))
			{
				saveSelections(document.getElementByTagName('cusgeneralnew'));
				window.close();	
				return true;
			}
			else
				return false;			
		}		
		else 
			if(confirm("You have filled : "+count+" CustomColumn. Are you sure to continue?"))
			{				
			caught=document.cusgeneralnew.getName1;
			caught.value=disp;
			
			if(formName=='cusgeneralnew')
				{	
					
					opener.document.cusgeneralnew.getName1.value=caught.value;	
				}
			saveSelections(document.getElementByTagName('cusgeneralnew'));
			window.close();
		return true;
			}
			else
		return false;
    }	
	else
	return false;	
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

/** This function is used to force selecting in selecting roles to store in the database for CHANNEL TYPE */

function selectAllChannelTypeRoles(selObj)
{
	var mselVal = "";
	
	selectedFlag = selObj.options.selectedIndex == -1;
	for (i = 0; i < selObj.options.length; i ++)
	{ 
		selObj.options[i].selected = true;
		if(i== (selObj.options.length-1) )
			mselVal = mselVal + selObj.options[i].text;
		else
			mselVal = mselVal + selObj.options[i].text + ", ";
	}

	//selObj.value = mselVal;

	document.getElementById("mappedVariableList").value = mselVal;
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
	}
}



function checkMandatoryWhenHoldmult(go)
{
var v= go.agefrom.value;
var v1 = go.ageto.value;
var v2= go.multiplicationfactor.value;
if(v=="" && v1=="" && v2=="")
	alert(mesg[17]);
    else
	if(v=="" && v1=="")
		alert(mesg[18]);
	    else
		if(v1=="" && v2=="")
		   alert(mesg[19]);
	       else
		   if(v=="" && v2=="")
		      alert(mesg[20]);
		       else
			   if(v=="")
	              alert(mesg[21]);
                  else
			      if(v2=="")
			          alert(mesg[22]);
		              else
				      if(v1=="")
				         alert(mesg[23]);
		                else
							 if(v2 <= 0)
								{
							      alert(mesg[31]);
								  return false;
								}
							 else
	                     go.submit();
}


/* Function to check validataions in multiplication factor entity under custom definitions while save & close*/
function validatemultsaveedit(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[1].submit();
		}
	}
	else
	{

		var flag=false;
		var v2 = go.agefrom.value;
		var v3 = go.ageto.value;
		var v4 = go.multiplicationfactor.value;
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
		
		if(v4 <= 0){
			alert(mesg[31]);
			return false;}
		else
			flag = true;

		go.submit();
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

/*Function to validate YEAR filed in Baseinsuranceamount entity under customedefinitions HOLD*/

function checkMandatoryWhenHoldbase(go)
{
var v= go.year.value;
if(v=="")
	{
	alert(mesg[25]);
	return false;
    }
else
	if(v < 1000)
	{
	alert (mesg[26]);
	return false;
	}
	
	go.submit();
	
}


function validatebasesaveedit(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT")
	{	
		go.submit();
	}
	else if(status=="DELETE")
	{
		if(getNumChecked(editorHold))
		{
			document.forms[1].submit();
		}
	}
	else
	{

		var flag=false;
		var v2 = go.year.value;
		var v3 = go.amount.value;		
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

		if(v2 < 1000){
			alert(mesg[26]);
		    return false;  }
		else
			flag=true;
		
		if(	v3 <= 0){
			alert(mesg[32]);
			return false;}
		else
			flag = true;

		go.submit();
	}
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
function checkMandatoryWhenHoldbase(go)
{
var v= go.year.value;
if(v=="")
	{
	alert(mesg[25]);
	return false;
    }
else
	if(v < 1000)
	{
	alert (mesg[33]);
	return false;
	}
	
	go.submit();
	
}


/*
  Check mandatory fields in  save and close(new) functionality and CurrencyCode field validation .
  Check the status of the Previous and Next,Delete functionalities
*/
function validateCurrency(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	 
	editorHold.value=status;

	var go= eval("document."+nav);
  
    
	 		/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT")
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

		if(status == "EDIT")
		{
			if(validateForm(go)){						
				flag=true;
			}
			else{
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
      var v=go.currencyCode.value;        

         if( v.length< 3){
			alert(mesg[27]);
			go.currencyCode.select();
		    return false; }
		else
			flag=true;

		go.submit();
	}
}


/* This function is used for stastus of the functionalities of save and close(new) and previous ,next ,delete.
 
  validation for ExchangeRate field */

  function validateExchangeRate(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	 
	editorHold.value=status;

	var go= eval("document."+nav);
  
    
	 /**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT")
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
		
		
		if(status == "EDIT")
		{
			if(validateForm(go)){						
				flag=true;
			}
			else{
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


/*Function to replace the string with given string  */

	str_replace = function(search, replace, subject) {

   if (typeof(search) == 'string') { search = Array(search); }
   if (typeof(replace) == 'string') { replace = Array(replace); }

   for (var i = 0; i <=3; i++) {

       subject = subject.replace(search[i], replace[i]); 

   }   
   document.getElementById('subject').value=mesg[30]+' '+subject;
  
   return subject;
}


/*Function to delete the messages */
function DiscardMessage(page)
{
			if(confirm( mesg[29]))
			{	
					document.getElementById("deleteMe").value=document.getElementById("MessageId").value;					
					return true;							
			}
	    	else
    				document.getElementById('Refresh').value="false" ;
		    		return false;			
}

function DeleteMessage(nav,editorHold,status)
{
	/**
	 *	Set status for Delete 
	 */
	editorHold.value=status;
    var go= eval("document."+nav);
	if(status=="DELETE")
	{   
		go.DeleteStatus.value='Y';
		if(DiscardMessage(editorHold))
		{	
			go.submit();							
			window.close();					
			return true;			
		}
		else
			return false;							
	}	
}

function getNumCheckedMessageId(page)
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
				if(e.checked==true && e.name!="n1")
				{
					disp+=e.name+',';
					count++;				
				}
				
		}		
		
		if(parseInt(count)==0 && parseInt(list_input.length-6)>0)
		{
			alert(mesg[9]);
			return false;
		}
		else 
			if(parseInt(count)==0 ){alert(mesg[10]);}

		else if(confirm(mesg[11] + count +' '+mesg[12]))
			{	

				caught=document.getElementById('deleteMe');
				caught.value=disp;
				
				return true;
			}
			else
				return false;
    }	
	else
		return false;
}

function validateMessage(nav,editorHold,status,WFstatus)
{
	editorHold.value=status;
	
	var go= eval("document."+nav);

	var body=go.messageBody.value;  
	var To = go.messageToInternal.value;
	var From = go.messageToExternal.value;
		
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

		if(document.getElementById('internal').checked==true){	
		var semiStatusTo = false;
		var semiStatusCc = false;
		var selectTo=new Array();
		var selectCc=new Array();

			lenTo = document.forms['MessageNew'].messageToInternal.value.length;
			lenCc = document.forms['MessageNew'].messageCC.value.length;
		if(document.forms['MessageNew'].messageToInternal.value.charAt(lenTo-1)!=';'){
			alert("User Id in To field should end with delimeter Semicolon");
			go.messageToInternal.focus();
			return false;
		}
		if(document.forms['MessageNew'].messageCC.value.charAt(lenCc-1)!=';' && document.forms['MessageNew'].messageCC.value != ''){
			alert("User Id in CC field should end with delimeter Semicolon");
			go.messageCC.focus();
			return false;
		}
		for(i = 0; i<lenTo; i++)
			selectTo[i] = document.forms['MessageNew'].messageToInternal.value.charAt(i);
		for(j = lenTo; j > 0; j--){
			if(selectTo[j]==';'){
				semiStatusTo=true;
				templen = lenTo-j;
				break;
			}
		}
		for(i = 0; i<lenCc; i++)
			selectCc[i] = document.forms['MessageNew'].messageCC.value.charAt(i);
		for(j = lenTo; j > 0; j--){
			if(selectCc[j]==';'){
				semiStatusCc=true;
				templen = lenCc-j;
				break;
			}
		}
		if(semiStatusTo == false){
			alert("User Id in To field should end with delimeter Semicolon");
			go.messageToInternal.focus();
		return false;
		}
		if(semiStatusCc == false && document.forms['MessageNew'].messageCC.value != ''){
			alert("User Id in CC field should end with delimeter Semicolon");
			go.messageToInternal.focus();
		return false;
		}
	}
		if(body=='')
	  {
		var where_to= confirm(mesg[87]+' '+mesg[58]);

		if (!where_to)
		{
			go.messageBody.focus();
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


/* Functions to get and set value for User Status Check box*/
function setValue()
	{
		if (document.getElementById('Check').checked)
		{	
			document.getElementById('UserStatus').value = "A" ;
		}
		else
		{
			document.getElementById('UserStatus').value = "D";
		}		
		getReport();
} 


function getValue()
	{
		if (document.getElementById('Active').checked)
		{	
			document.getElementById('UserStatus').value = "A" ;
		}
		else
		{
			document.getElementById('UserStatus').value = "D";
	}
	
  } 


function  validatePassword()
{
	    
	if(document.getElementById('password').value!="")
	{   
		if(Trim(document.getElementById('UserName').value)=="")
		{   
			alert(mesg[40]);
			document.getElementById('UserName').value=Trim(document.getElementById('UserName').value);
			document.getElementById('UserName').focus();
	      return false;

		}
		else
		{	
			document.getElementById('UserName').value=Trim(document.getElementById('UserName').value);
			return true;    
		}
		return false;
	}
	else
		document.getElementById('UserName').value=Trim(document.getElementById('UserName').value);
		return true;
	
}


function validateInterface(nav,editorHold,status)
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
		
		if(validatePassword())
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

function validateInterfaceInEdit(nav,editorHold,status)
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
		
		if(validatePassword())
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

/* Function is used to validate fields */

function validateProduct(nav,editorHold,status)
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

		if(status == "EDIT")
		{
			if(validateForm(go)){						
				flag=true;
			}
			else{
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


/*
    this method for validation for ChargeCode Entity
*/

  function validateChargeCode(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	 
	editorHold.value=status;

	var go= eval("document."+nav);
  
    
	 		/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT")
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
        

       /*  var charge=go.chargeCode.value;        

         if( charge.length< 4){
			alert(mesg[28]);
		    return false; }
		else
			flag=true; */
       
       if(DoDatesCompare())
			
			flag=true;
		else
			return false;
   		go.submit();
	}
}

/*
    this method for validation for InterestRateCode Entity
*/
 
function validateInterestRateCode(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD,EDIT,NEW,PREVIOUS,NEXT 
	 */
	editorHold.value=status;

	var go= eval("document."+nav);

	/**
	 *	For HOLD - No Client Validation
	 */
	if(status=="HOLD" || status=="PREVIOUS" || status=="NEXT" )
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
		
		if(status == "EDIT")
		{
			if(validateForm(go)){						
				flag=true;
			}
			else{
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

		
         var rate=go.rateCode.value;        

         if( rate.length< 4){
			alert(mesg[42]);
		    return false; }
		else
			flag=true;
		      
		go.submit();
	}
}

	/* Function is used to validate fields */

function validateEnterpriseDate(nav,editorHold,status)
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

function validateWorkflowReasons(nav,editorHold,status)
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
		
		if(validatePassword())
			flag=true;
		else
			return false;	
		go.submit();
	}	
}

/* This function is used to focus on given field */

function doFocusOnFiled(obj)
{	
	document.getElementById(obj).focus();
}

/* This function is used to check mandatory fields while HOLD in charge code definition */

function validateWhenChargecodeHold(nav,editorHold,status)
{
	/**
	 *	Set status for HOLD with mandatory field
	 */
	editorHold.value=status;

	var go= eval("document."+nav);
	if(	status=="HOLD" )
	{	
		var flag=false;
		var charge=go.chargeCode.value;   
		if(checkMandatoryWhenHold())
			flag=true;
		else
			return false;
		    
         if( charge.length< 4){
			alert(mesg[28]);			
		    return false; }
		else
			flag=true;	
		if(checkNumber())
			flag=true;
		else
			return false;
		go.submit();
	}
}

/* Function is used to validate fields in Promotion Maintenance */

function validatePromotion(nav,editorHold,status)
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
			if(validateFormForList(go)){		
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

/*Function is used to validate the Entity Code length */

function validateEntityCode(id,name)
{
	document.getElementById(id).value=Trim(document.getElementById(id).value);
	var txtBoxVal=document.getElementById(id).value.replace(" ","");
	var warMsg=name+" "+mesg[71];

	if(txtBoxVal!=""){
         if( txtBoxVal.length< 4){
			alert(warMsg);
			document.getElementById(id).select();
		    return false; }
		else
			flag=true;
	}
  
}


/* Function is used to show the sent messages */
function sentMessages(nav)
{
	var go= eval("document."+nav);
	go.ExtorInt.value='E';
	go.submit();
}

/* Function is used to show the recieved messages */
function receivedMessages(nav)
{	
	var go= eval("document."+nav);
	go.ExtorInt.value='I';
	go.submit();	
}

/* This function can be used for multiple email validation */

	function multiEmailCheck(email_field) 
	{
		if(document.getElementById('external').checked==true){
		if(email_field.value.length)
		{
			var src=email_field.value;
			if(src!="")
			{
				var comma=src.split(",");
			}
			for (var i = 0; i < comma.length; i++) 
			{
				var semicolon=comma[i].split(";");
				for (var j = 0; j < semicolon.length; j++) 
				{
					var space=semicolon[j].split(" ");
					for (var k = 0; k < space.length; k++) 
					{
						if (!chkEmail(space[k]))
						{
							alert(mesg[86]); /*Invalid Email Id*/
							email_field.select();	
							return false;
						}
					
					}
				}
			}
		}
		}
	return true;
} 


function chkEmail(src)
{
		var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([\w-]{2,6}(?:\.[\w-]{2})?)$/i ;
		if(src.value!="")
		{
			if(filter.test(src.value))
				testresults=true
			else
			{
				alert(mesg[86]);//Invalid Email Id
				src.value='';
				src.focus();
				testresults=false
			}
		}
		return (testresults)
}

/* Function is used to pick up the selected division in division drop down */
function getDivisionId()
{
	box = document.getElementById('divisionId');	
	document.getElementById('divisionName').value=box.options[box.selectedIndex].text;	
	if(document.getElementById('divisionName').value=="Select")
	{
		document.getElementById('divisionName').value = "All";
	}
}

function showSelectedRecords()
	{
		if(document.getElementById('operationOR').checked==true)
		{
			document.getElementById('searchOperation').value="OR";
		}
		else
		{
			document.getElementById('searchOperation').value="AND";
		}
		getMultiple(document.getElementById('statuscode'));	
 }

function getMultiple(ob)
	{	
	   var selected = new Array();
	   
	   for (var i = 0; i < ob.options.length; i++)
		if (ob.options[ i ].selected)
		  selected.push(ob.options[ i ].value);   
	
		if(selected.toString()==""){
			document.getElementById('recordStatus').value="1";
		}else{
			document.getElementById('recordStatus').value=selected.toString();
		}		
	}

function getMultipleSelection(ob)
	{	
	   var selected = new Array();
	   selected=document.getElementById('recordStatus').value;	  
	   var k=selected.split(',');
       try{
		   if(k!="")
		   {
		   for (var i = 0; i < ob.options.length; i++)		    
			   
				ob.options[parseInt(k[i])-1].selected = true;	
		   }
	   }catch(ex){
		}	   
 }

function getNumCheckedToReopen(page,status)
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
				if(e.checked==true && e.name!="n1" && e.name!='checkDelete')
				{
					disp+=e.name+',';
					count++;				
				}
				
		}		
		
		if(parseInt(count)==0 && parseInt(list_input.length-6)>0)
		{
			alert(mesg[9]);
			return false;
		}
		else 
			if(parseInt(count)==0 ){alert(mesg[10]);} 
		else if(status =="DELETE"){
		 if(confirm(mesg[11] + count + ' ' +mesg[12]))
			{	
				caught=document.getElementById("deleteMe");
				caught.value=disp;
				return true;
			}
			else
				return false;
		}
		else if(status =="REOPEN"){
		 if(confirm(mesg[11] + count + ' ' +"row(s).Do you really want to reopen ?"))
			{	
				caught=document.getElementById("deleteMe");
				caught.value=disp;
				return true;
			}
			else
				return false;
		}
    }	
	else
		return false;
}