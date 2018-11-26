<%-- Service Specification Edit - New - Third Window --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri="c.tld" prefix="c" %>
<%@ taglib uri="fmt.tld" prefix="fmt" %>

<%@ taglib uri="agreement.tld" prefix="ag" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<c:set var="language_id" value="${sessionObj.languageId}" />
<fmt:bundle basename="${language}">

<HTML>
<HEAD>

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>
<script type="text/javascript" src="../js/search.js"></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<TITLE><fmt:message key="service_specification"/>-<fmt:message key="new"/></TITLE>
<script type="text/javascript">

var eventFlag='Y';

function setFlags()
{	
	var servicecode=document.getElementById('service_code').value;
	if(servicecode=='S001')
	{
       opener.document.getElementById('S001F').value='Y';
	}
	else if(servicecode=='S002')
	{
		opener.document.getElementById('S002F').value='Y';
	}
	else if(servicecode=='S003')
	{
		opener.document.getElementById('S003F').value='Y';
	}
	else if(servicecode=='S004')
	{
		opener.document.getElementById('S004F').value='Y';
	}
	else if(servicecode=='S005')
	{
		opener.document.getElementById('S005F').value='Y';
	}
	else if(servicecode=='S006')
	{
		opener.document.getElementById('S006F').value='Y';
	}
	else if(servicecode=='S007')
	{
		opener.document.getElementById('S007F').value='Y';
	}
	else if(servicecode=='S008')
	{
		opener.document.getElementById('S008F').value='Y';
	}
	else if(servicecode=='S009')
	{
		opener.document.getElementById('S009F').value='Y';
	}
	else if(servicecode=='S010')
	{
		opener.document.getElementById('S010F').value='Y';
	}
	else if(servicecode=='S011')
	{
		opener.document.getElementById('S011F').value='Y';
	}
}

/* This function is used to add  rows for account information */

function addRows1()
{
  var tbl=document.getElementById('accountsTab');
  var lastRow=tbl.rows.length;
  var iteration=lastRow-1;
  
  var temp=document.getElementById('formItems1['+iteration+'].<fmt:message key="fcc_account_no"/>*').value;
 if(temp==''){
	  alert(svpMesg[9]); // Please enter the account number 
	  return false;
  }
  else{ 
 
  var row=tbl.insertRow(lastRow);
 
  // cell 1
 
	 var cell1= row.insertCell(0);
	 cell1.align="left";
	 var e11=document.createElement('input');
	 e11.type="text";
	 e11.name='formItems1['+lastRow+'].accountNum';
	 e11.id='formItems1['+lastRow+'].<fmt:message key="fcc_account_no"/>*';
	 e11.onblur=checkDuplicate1;
	 e11.onkeyup=upperCase;
	 e11.onkeypress=numbersOnly;
	 e11.maxLength=14;
	 e11.size=33;
	 cell1.appendChild(e11);


	 //cell2

	 var cell2= row.insertCell(1);
	 var e12=document.createElement('input');
	 e12.type="text";
	 e12.name='formItems1['+lastRow+'].accountName';
	 e12.id='formItems1['+lastRow+'].<fmt:message key="account_name"/>';
	 e12.maxLength=32;
	 e12.readOnly=true;
	 cell2.appendChild(e12);

	 //cell4

	  var cell4 = row.insertCell(2);
	  var e14 = document.createElement('input');
	  e14.type = 'button';
	  e14.name = 'delete['+lastRow+']';
	  e14.id = lastRow;
	  e14.value=' - ';
	  e14.size = 25; 
	  e14.className='button';
	  e14.onclick=removeRowFromTable1;
	  e14.title='<fmt:message key="delete"/>';
	  cell4.appendChild(e14);
	 }
}

/*javascript to upper case*/
function upperCase()
{
	this.value=this.value.toUpperCase();
}

/* javascript to trim the field */
function clearField()
{
  this.value=Trim(this.value);
}

// after clicking clear form, all the rows get deleted, except zeroth row
function afterClear()
{
	var tbl=document.getElementById('tab');
	var lastRow=tbl.rows.length;

	if(lastRow>1){
	for(var s=(lastRow-1); s>0;s--){
    tbl.deleteRow(s);
	}
	document.getElementById('formItems[0].<fmt:message key="fcc_customer_id"/>*').readOnly=false;
	}else{
		document.getElementById('formItems[0].<fmt:message key="fcc_customer_id"/>*').readOnly=false;
	}


    var acctbl=document.getElementById('accountsTab');
    var lastRow1=acctbl.rows.length;

    if(lastRow1>1){
	for(var s=(lastRow1-1); s>0;s--){
    acctbl.deleteRow(s);
	}
    document.getElementById('formItems1[0].<fmt:message key="fcc_account_no"/>*').readOnly=false;
	}else{
		document.getElementById('formItems1[0].<fmt:message key="fcc_account_no"/>*').readOnly=false;
	}
	
}

function checkSpecialChar(obj) 
{
	var name = document.getElementById(obj.id).value;
	if(name!=''){
	if(name.match(/^[a-zA-Z0-9]+$/)) {
	return true;
	} else {
	alert(agmtMesg[9]); // Special characters are not allowed
	document.getElementById(obj.id).value='';
	document.getElementById(obj.id).focus();
	return false;
	}
	}
}

/* javascript function to delete row when user clicks on Minus button */

function removeRowFromTable1(e, obj){
	var tbl = document.getElementById('accountsTab');
	var lastRow = tbl.rows.length;

	if(lastRow==1)
	{
		clearRow1();
		//alert(svpMesg[2]); // canot delete first row
	}
	else
	{
	var objId;
    if (obj != null) {
		objId = obj.id;
    } else {
		objId = this.id;
    }
	
	var okok=objId;
	var v1,v2,v3;
		
	for(var s=objId; s<lastRow-1;s++){
		okok++;
		v1=document.getElementById('formItems1['+s+'].<fmt:message key="fcc_account_no"/>*').value;
		v2=document.getElementById('formItems1['+s+'].<fmt:message key="account_name"/>').value;
				
		document.getElementById('formItems1['+s+'].<fmt:message key="fcc_account_no"/>*').value=document.getElementById('formItems1['+okok+'].<fmt:message key="fcc_account_no"/>*').value;

		document.getElementById('formItems1['+s+'].<fmt:message key="account_name"/>').value=document.getElementById('formItems1['+okok+'].<fmt:message key="account_name"/>').value;

		document.getElementById('formItems1['+okok+'].<fmt:message key="fcc_account_no"/>*').value=v1;
		document.getElementById('formItems1['+okok+'].<fmt:message key="account_name"/>').value=v2;
		
		}
	tbl.deleteRow(lastRow-1);
	makeLastRowEditable1(); 
	}
}

//If last row is only readable, this method makes it to editable

function makeLastRowEditable1()
{
	var tbl = document.getElementById('accountsTab');
	var lastRow = tbl.rows.length;
	var index=lastRow-1;
	document.getElementById('formItems1['+index+'].<fmt:message key="fcc_account_no"/>*').readOnly=false;

}

function clearRow1()
{
	document.getElementById('formItems1['+0+'].<fmt:message key="fcc_account_no"/>*').value='';
	document.getElementById('formItems1['+0+'].<fmt:message key="account_name"/>').value='';
	document.getElementById('formItems1['+0+'].<fmt:message key="fcc_account_no"/>*').readOnly=false;
}

function checkDuplicate1()
 {
	 var tbl = document.getElementById('accountsTab');
     var lastRow = tbl.rows.length;
	 var flag=0;
     var rowNum=this.id.charAt(11);
	 if(lastRow >1){
	 	var i;
		for(i=(lastRow-2);i>=0;i--)
		 {
            if(rowNum!=i)
			{
				var v1=document.getElementById('formItems1['+i+'].<fmt:message key="fcc_account_no"/>*').value;
				if(this.value==v1)
				 {
					flag=1;
					break;
					
				 }
			}
	 }
		 if(flag==1)
		 {
			 alert(svpMesg[1]); // acc no already exists
			 document.getElementById('formItems1['+(lastRow-1)+'].<fmt:message key="fcc_account_no"/>*').value='';
			 document.getElementById('formItems1['+(lastRow-1)+'].<fmt:message key="fcc_account_no"/>*').focus();
			 return false;
		 }
	     else
		 { 
			 getAjaxValueForAccount(this);
			 return true;
	     } 
	}
  
 }


 function checkDuplicateForFirstRow1(obj)
 {
	 var tbl = document.getElementById('accountsTab');
     var lastRow = tbl.rows.length;
	 var flag=0;
	 if(lastRow >1){
	 	var i;
		for(i=1;i<lastRow;i++)
		 {
			var v1=document.getElementById('formItems1['+i+'].<fmt:message key="fcc_account_no"/>*').value;
			if(obj.value==v1)
			 {
				flag=1;
				break;
				
			 }
		  }
		 if(flag==1)
		 {
			 alert(svpMesg[1]);  // acc no already exists
			 document.getElementById('formItems1[0].<fmt:message key="fcc_account_no"/>*').select();
			 return false;
		 }
	     else
		 { 			 
			 getAjaxValueForAccount(obj);
			 return true;
	     } 
	}	 		 
 }


 function ajaxReturnToFunctionAccountInformation(xmlEnterpriseDates)
	{
	  
		var accountName = xmlEnterpriseDates.getElementsByTagName('account_name');
		 
		var index = xmlEnterpriseDates.getElementsByTagName('rec_index');

		var element=(index[0].text).substring(0,13);
		
		if(accountName.length == 0 || accountName[0].text=='')
		{
			document.getElementById(element+'.<fmt:message key="account_name"/>').value='';		
			document.getElementById(element+'.<fmt:message key="fcc_account_no"/>*').value='';
		    document.getElementById(element+'.<fmt:message key="fcc_account_no"/>*').focus();
            alert(svpMesg[0]); // Account Number does not exists
			return false;
		}
		else if(accountName[0].text=='@NO-VALUE@')
		{
			document.getElementById(element+'.<fmt:message key="account_name"/>').value=' ';
			document.getElementById(element+'.<fmt:message key="fcc_account_no"/>*').readOnly=true;
		}
		else
		{		
			document.getElementById(element+'.<fmt:message key="account_name"/>').value=accountName[0].text;
			document.getElementById(element+'.<fmt:message key="fcc_account_no"/>*').readOnly=true;
		}
	}


function ajaxReturnToFunctionServiceBureauName(xmlEnterpriseDates)
	{

		var bureauName = xmlEnterpriseDates.getElementsByTagName('bureau_name');
			
		if(bureauName.length == 0 || bureauName[0].text=='')
		{
			alert(svpMesg[3]); // Bureau Name does not exists
			return false;
		}else
		{		
			document.getElementById('<fmt:message key="service_bureau_name"/>').value=bureauName[0].text;		
		}
	}


function clearBureauName(obj){
	if(obj.value==''){
		document.getElementById('<fmt:message key="service_bureau_name"/>').value='';
	}
}

function addNewService()  
 {
	
     var internalRefId= document.getElementById('internalRefId').value;
	 var serviceId= document.getElementById('<fmt:message key="service_id"/>').value; 
	 var bureauName= document.getElementById('<fmt:message key="service_bureau_name"/>').value;
 	
	 var displayBureauId=document.getElementById('<fmt:message key="service_bureau_id"/>');
	 var bureauId = displayBureauId.options[displayBureauId.selectedIndex].text;

	 var tbl= opener.document.getElementById('servicetable');
     var lastRow=tbl.rows.length;
	 lastRow=lastRow-1;

     opener.document.getElementById('formItems['+lastRow+'].internalRefId').value=internalRefId;
     opener.document.getElementById('formItems['+lastRow+'].<fmt:message key="service_id"/>*').value=serviceId;
     opener.document.getElementById('formItems['+lastRow+'].bureauId').value=bureauId;
     opener.document.getElementById('formItems['+lastRow+'].bureauName').value=bureauName;
 
 }

 function addServiceList()
 {
	  var serviceList = opener.document.getElementById('listOfServiceId').value;
      if(serviceList=='')
      {
		var str=document.getElementById('internalRefId').value;
		str1 = serviceList.concat(str);
	  } else
	  {	 
		var str=document.getElementById('internalRefId').value;
		str1 = serviceList.concat('~',str);
	  }
      opener.document.getElementById('listOfServiceId').value= str1;
	  
 }


 //this function add  rows to the table for Customer id,  customer name, and address
function addRows()
{
  var tbl=document.getElementById('tab');
  var lastRow=tbl.rows.length;
  var iteration=lastRow-1;
  var temp=document.getElementById('formItems['+iteration+'].<fmt:message key="fcc_customer_id"/>*').value;
  if( temp==''){	
	  alert(svpMesg[8]);  // Please enter the customer id 
	  return false;
  }
  else
 {  
  var row=tbl.insertRow(lastRow);

  // cell 1
 var cell1= row.insertCell(0);
 cell1.align="left";
 var e0=document.createElement('input');
 e0.type="text";
 e0.name= 'formItems['+lastRow+'].customerId'; 
 e0.maxLength=14;
 e0.size=33;
 e0.id= 'formItems['+lastRow+'].<fmt:message key="fcc_customer_id"/>*'; 
 e0.onblur=checkDuplicate;
 e0.onkeyup=upperCase;
 e0.onkeypress=alphanumericWithoutSpace;
 cell1.appendChild(e0);


 //cell2

 var cell2= row.insertCell(1);
 var e1=document.createElement('input');
 e1.type="text";
 e1.name= 'formItems['+lastRow+'].customerName';
 e1.id= 'formItems['+lastRow+'].<fmt:message key="customer_name"/>';
 e1.maxLength=40;
 e1.readOnly=true;
 cell2.appendChild(e1);

//cell5
  var cell4 = row.insertCell(2);
  var e3 = document.createElement('input');
  e3.type = 'button';
  e3.name = 'delete['+lastRow+']';
  e3.id = iteration+1;
  e3.value=' - ';
  e3.size = 2; 
  e3.onclick=removeRowFromTable;
  e3.className='button';
  e3.title='<fmt:message key="delete"/>';
  cell4.appendChild(e3);
 }

}

/* javascript function to delete row when user clicks on Minus button */

function removeRowFromTable(e, obj){
	var tbl = document.getElementById('tab');
	var lastRow = tbl.rows.length;
	if(lastRow==1)
	{
		clearRow();
        //alert(agmtMesg[5]); //canot delete first row
	}
	else
	{
	var objId;
    if (obj != null) {
		objId = obj.id;
    } else {
		objId = this.id;
    }
	
	var okok=objId;
	var v1,v2,v3;
	
	for(var s=objId; s<lastRow-1;s++){
		okok++;
		v1=document.getElementById('formItems['+s+'].<fmt:message key="fcc_customer_id"/>*').value;
		v2=document.getElementById('formItems['+s+'].<fmt:message key="customer_name"/>').value;
						
		document.getElementById('formItems['+s+'].<fmt:message key="fcc_customer_id"/>*').value=document.getElementById('formItems['+okok+'].<fmt:message key="fcc_customer_id"/>*').value;

		document.getElementById('formItems['+s+'].<fmt:message key="customer_name"/>').value=document.getElementById('formItems['+okok+'].<fmt:message key="customer_name"/>').value;

		document.getElementById('formItems['+okok+'].<fmt:message key="fcc_customer_id"/>*').value=v1;
		document.getElementById('formItems['+okok+'].<fmt:message key="customer_name"/>').value=v2;
	
		}
	tbl.deleteRow(lastRow-1);
	makeLastRowEditable();
	}
}

//If last row is only readable, this method makes it to editable

function makeLastRowEditable()
{
	var tbl = document.getElementById('tab');
	var lastRow = tbl.rows.length;
	var index=lastRow-1;
	document.getElementById('formItems['+index+'].<fmt:message key="fcc_customer_id"/>*').readOnly=false;

}

function ajaxReturnToFunctionCustomerInformation(xmlEnterpriseDates)
{
	var customerName = xmlEnterpriseDates.getElementsByTagName('customer_name');
	var index = xmlEnterpriseDates.getElementsByTagName('rec_index');

	var element=(index[0].text).substring(0,12);
		
	if(customerName.length == 0 || customerName[0].text=='')
	{
		document.getElementById(element+'.<fmt:message key="customer_name"/>').value='';
		document.getElementById(element+'.<fmt:message key="fcc_customer_id"/>*').value='';
		document.getElementById(element+'.<fmt:message key="fcc_customer_id"/>*').focus();
		alert(agmtMesg[3]); // Customer Id does not exists
		return false;
	}else
	{		
		document.getElementById(element+'.<fmt:message key="customer_name"/>').value=customerName[0].text;
		document.getElementById(element+'.<fmt:message key="fcc_customer_id"/>*').readOnly=true;  
	}
}

//Ajax to check service id,bureau id combinations
function ajaxReturnToFunctionServiceId(xmlEnterpriseDates)
{
	var xmlDoc = xmlEnterpriseDates.documentElement;
	var service_id = xmlDoc.getElementsByTagName("service_id")[0].childNodes[0].nodeValue;
   
	if( service_id ==1)
	{
		alert(svpMesg[7]); // Service Id, Bureau Id combination already exists for this Service type
		document.getElementById('<fmt:message key="service_id"/>').focus();
		return false;
	}else
	{	
		
		if(confirmations()){
		setFlags();
		addNewService();
		document.servicespecifictypenewineditform.submit();
		return true;
		}
	}
}

function clearRow()
 {
      document.getElementById('formItems['+0+'].<fmt:message key="fcc_customer_id"/>*').value='';
	  document.getElementById('formItems['+0+'].<fmt:message key="customer_name"/>').value='';
	  document.getElementById('formItems['+0+'].<fmt:message key="fcc_customer_id"/>*').readOnly=false;
	
 }

 function checkDuplicate()
 {
	 var tbl = document.getElementById('tab');
     var lastRow = tbl.rows.length;
	 var flag=0;
	 var rowNum=this.id.charAt(10);
	 if(lastRow >1){
	 	var i;
		for(i=(lastRow-2);i>=0;i--)
		 {
			if(i!=rowNum)
			 {
				var v1=document.getElementById('formItems['+i+'].<fmt:message key="fcc_customer_id"/>*').value;
				if(this.value==v1)
				 {
					flag=1;
					break;
					
				 }
			 }
		  }
		 if(flag==1)
		 {
			 alert(agmtMesg[4]); // Duplicate customer id 
			 document.getElementById('formItems['+(lastRow-1)+'].<fmt:message key="fcc_customer_id"/>*').value='';
			 document.getElementById('formItems['+(lastRow-1)+'].<fmt:message key="fcc_customer_id"/>*').focus();
			 return false;
		 }
	     else
		 { 
			 
			 getAjaxValueForCustomer(this);
			 return true;
	     } 
	}	 		 
 }

 function checkDuplicateForFirstRow(obj)
	 {
		 var tbl = document.getElementById('tab');
		 var lastRow = tbl.rows.length;
		 var flag=0;
		 if(lastRow >1){
			var i;
			for(i=1;i<lastRow;i++)
			 {
				var v1=document.getElementById('formItems['+i+'].<fmt:message key="fcc_customer_id"/>*').value;
				if(obj.value==v1)
				 {  
					flag=1;
					break;
					
				 }
			  }
			 if(flag==1)
			 {   alert(agmtMesg[4]);
				 document.getElementById('formItems[0].<fmt:message key="fcc_customer_id"/>*').select();
				 return false;
			 }
			 else
			 { 	
				 getAjaxValueForCustomer(obj);
				 return true;
			 } 
		}	 		 
	 }

	 /* This function deletes, last row in service Type when error occurs in service specification*/
	 function checkTable()
	 {
		 var tbl= opener.document.getElementById('servicetable');
		 var lastRow=tbl.rows.length;
		 index=lastRow-1;
		 tbl.deleteRow(index);
	 }

 /* This function deletes, last row in service Type when  service specification closes without saving */
 function checkLastRow()
 {
	 var tbl= opener.document.getElementById('servicetable');
	 var lastRow=tbl.rows.length;
	 index=lastRow-1;
	 var id=opener.document.getElementById('formItems['+index+'].<fmt:message key="service_id"/>*').value;
	 if(id==''){
	 tbl.deleteRow(index);
	 }
 }
 // appending zeros to service ids if service id length are less than specified length

 function appendZeros(obj)
 {
	 var serviceCode= document.getElementById('service_code').value;
	 var service = obj.value;
	 if(service!='')
	 {
		 var len=service.length;
		 if(serviceCode=='S001' || serviceCode=='S003')
		 {
			 for(var i=len;i<9;i++)
			 {
				 service='0'+service;
			 }
		 }else{
			 for(var i=len;i<11;i++)
			 {
				 service='0'+service;
			 }   
		 }
		 document.getElementById(obj.id).value=service;
		 return true;
	  }else{
		return false;
	  }
 }

function clearAfterEnterKey()
{
	var key;
	if (window.event)
	key = window.event.keyCode;
	if(key==13)
	{
		document.servicespecifictypenewineditform.reset();
		afterClear();
	}
}

</script>
</HEAD>

<body class="shadowbody" onUnLoad="checkLastRow();" onbeforeunload="callDeleteAlert();">
<html:form action="/resources/agreement/jsp/servicespecifictypenewinedit" > 
<%@ include file="common/JavascriptErrorMsg.jsp" %>

<input type="hidden" name="editorHold" Id="editorHold"/>
<html:hidden property= "versionNo"  value="${param.versionNo}"/>

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="service_specification"/>-<fmt:message key="new"/></span></span></div></span>
</div>

<ul class="toolbar2">    
	<!-- Subnavigation -->		
	   <li class="save">
		 <a onClick="javascript: return saveService()"
		  onMouseOver="hover('a1','ahand');" tabindex="1" onkeyPress="methodCallAfterEnterKey('validateService');"
		  onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_return"/>">
		  <fmt:message key="save_return"/></a> 
		  
	  </li>
	  
	  <li class="clear">
		  <a onClick="javascript:document.servicespecifictypenewineditform.reset();afterClear();"
			onMouseOver="hover('a2','ahand');" tabindex="2" onkeyPress="clearAfterEnterKey();"
			onMouseOut="hover('a2','nohand');" id="a2" title="<fmt:message key="clear"/>"><fmt:message key="clear"/></a>
	  </li>
</ul>

  <div id="service_new_scrollbar">
  
  <%@ include file="common/ErrorHandler.jsp" %>

  <c:set var="length">11</c:set>
   <c:if test="${param.serviceCode eq 'S001'}">		
		<c:set var="length" value="9"/>
   </c:if>
   <c:if test="${param.serviceCode eq 'S003'}">		
	   <c:set var="length" value="9"/>
  
  </c:if>

    <ag:InternalRefId var="internalRefId" scope="page"/>
	<input type="hidden" name="internalRefId" id="internalRefId" value="<c:out value="${internalRefId}"/>">
	<input type="hidden" name="serviceCode" value="${param.serviceCode}" id="service_code"> 
	<input type="hidden" name="internalAgreementId"value="${param.internalAgreementId}">

  <div class="record-def">
  <table width="96%" border="0" >
  <tr><td>
  
  <table width="96%" border="0">
  <tr>
	<td>&nbsp;</td>
	<td>
		<table width="85%" >
		<tr> 
			<td><p class="label_normal"><fmt:message key="service_id"/>:</p></td>  
			<c:set var="service_id"><fmt:message key="service_id"/></c:set>
			<td><span class="asterisk">*</span><input type="text" name="serviceId" id="${service_id}" 
			maxlength="${length}" size="20"
			onblur="javascript: this.value=Trim(this.value);this.value=this.value.toUpperCase();checkSpecialChar(this);appendZeros(this);"
			onkeypress="return alphanumericWithoutSpace(this);"></td>
		</tr>

		<c:url value="/resources/agreement/jsp/getServiceBureauName.do" var="getBureauNamePath"/>
		<tr>
			<td><p class="label_normal"><fmt:message key="service_bureau_id"/>:</p></td>
			<c:set var="service_bureau_id"><fmt:message key="service_bureau_id"/></c:set>
			 <ag:dropDown var="bureauIds" scope="page" key="ACTIVE_BUREAUNAMES" />
			<td><span class="asterisk">*</span><select name="bureauId" id="${service_bureau_id}" onchange="getAjaxValueForServiceBureauName(this.value,'GET_BUREAU_NAME','${getBureauNamePath}');clearBureauName(this);">
	               <option><fmt:message key="select"/></option>
				   <c:forEach items="${bureauIds}" var="bureauIds">
				           <option value ="<c:out value="${bureauIds.key}"/>"><c:out value="${bureauIds.value}"/></option>
			       </c:forEach>		   
				</select> 
			</td> 
            
		</tr>

		<tr>
			<td><p class="label_normal"><fmt:message key="service_bureau_name"/>:</p></td>
			<c:set var="service_bureau_name"><fmt:message key="service_bureau_name"/></c:set>
			<td><span class="asterisk">&nbsp;&nbsp;</span><input type="text" name="bureauName" id="${service_bureau_name}" maxlength="40" size="30" readonly="true" /></td>
		</tr>

		<tr>
			<td><p class="label_normal"><fmt:message key="suo_var"/>:</p></td>
			<c:set var="suo_var"><fmt:message key="suo_var"/></c:set>
			<td>
				<table>
				<tr>
					<td><p class="label_normal">
						<c:choose>	 
						<c:when test="${param.serviceCode eq 'S001' || param.serviceCode eq 'S003' || param.serviceCode eq 'S005'}">
							<span class="asterisk">&nbsp;</span><input type="radio" checked name="suo" class="radio" value="Y" id="${suo_var}" maxlength="1">
							<fmt:message key="yes"/>
						</c:when> 
						<c:otherwise>
							<span class="asterisk">&nbsp;</span><input type="radio" name="suo" class="radio" value="Y" id="${suo_var}" maxlength="1">
							<fmt:message key="yes"/>
						</c:otherwise>
						</c:choose>	
						</p>
					</td>
					<td><p class="label_normal">
						<c:choose>	 
						<c:when test="${param.serviceCode eq 'S001' || param.serviceCode eq 'S003' || param.serviceCode eq 'S005'}">
							<input type="radio" name="suo" class="radio" value="N" id="${suo_var}" maxlength="1">
							<fmt:message key="no"/>
						</c:when> 
						<c:otherwise>
							<input type="radio" name="suo" checked class="radio" value="N" id="${suo_var}" maxlength="1">
							<fmt:message key="no"/>
						</c:otherwise>
						</c:choose>	
						</p>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
	</td>
  </tr>
   <!-- this row add and delete rows -->
  <tr>
		<td>&nbsp;</td>
        <td>
		<table>
			<tr>
				<td><input type="button" value=" + " title='<fmt:message key="add"/>' id="addbutton" onclick="addRows()" class="button"/></td>
			</tr>
		</table>
		</td>
  </tr>
  <tr>
		<td>&nbsp;</td>
		<td>
		<fieldset class="fieldset">
			<table border="0" width="95%">
			<tr>
				<th align="left" width="37%"><p class="label_normal"><fmt:message key="fcc_customer_id"/><span class="asterisk">*</span></p></th> 
				<th align="left" width="26%"><p class="label_normal"><fmt:message key="customer_name"/></p></th> 
				<th>&nbsp;</th>
			</tr>
			</table>

			<c:url value="/resources/agreement/jsp/getCustomerInformation.do" var="getCustomerInformationPath"/>
			  <div id="agreement_table_scrollbar" >
			  <table border="0" width="70%" id="tab">
				  <tr>
					<td><input type="text" name="formItems[0].customerId"  id="formItems[0].<fmt:message key="fcc_customer_id"/>*" maxlength="14" size="33" onblur="javascript: this.value=Trim(this.value);
					getAjaxValueForCustomer(this);this.value=this.value.toUpperCase();" 
					onkeypress="return alphanumericWithoutSpace(this);"
					onkeyup="javascript: this.value=this.value.toUpperCase();"/></td>
					<td><input type="text" name="formItems[0].customerName" id="formItems[0].<fmt:message key="customer_name"/>" maxlength="40" readonly="true"/></td>
					<td><input type="button" name="delete[0]" id="0" class="button" title='<fmt:message key="delete"/>' value=" - " onclick="removeRowFromTable(this,this);" /></td>
					
				  </tr>
			  </table>
			  </div>
		</fieldset>
		</td>
   </tr>
   <!-- For currency exchange rate service there is no account numbers-->
  <c:if test="${param.serviceCode ne 'S010'}">
   <tr>
		<td>&nbsp;</td>
		<td>
			<table>
			<tr>
			    <td>
					<!-- For salary payment service only one account number required, ' + ' has been disabled-->
					<c:choose>	 
				    <c:when test="${param.serviceCode eq 'S003'}">
			        <input type="button"  value=" + " disabled class="button"/>
					</c:when> 
		            <c:otherwise>
                    <input type="button"  value=" + " title='<fmt:message key="add"/>' onclick="addRows1()" class="button"/>
		            </c:otherwise>
					</c:choose>
				</td>
			    				
				<td>&nbsp;</td>
			</tr>
			</table>
		</td>
   </tr>

   <tr>
		<td>&nbsp;</td>
		<td>
		<fieldset class="fieldset">
			<table width="77%" border="0">
			<tr align="left">
				<th width="46%"><p class="label_normal"><fmt:message key="fcc_account_no"/><span class="asterisk">*</span></p></th>
				<th  width="35%"><p class="label_normal"><fmt:message key="account_name"/></p></th>
				<th>&nbsp;</th>
			</tr>
			</table>
     
			<div id="service_table_scrollbar"> 
	   		<table width="62%" border="0" id="accountsTab">
			<tr>
				<td><input type="text" name="formItems1[0].accountNum" maxlength="14" size="33"
				onblur="javascript: this.value=Trim(this.value);getAjaxValueForAccount(this);
				this.value=this.value.toUpperCase();"
				onkeyup="javascript: this.value=this.value.toUpperCase();"
				onkeypress="return numbersOnly();"
				id="formItems1[0].<fmt:message key="fcc_account_no"/>*"/></td>
				<td><input type="text" name="formItems1[0].accountName" maxlength="32" readonly="true"
				id="formItems1[0].<fmt:message key="account_name"/>"/></td>
				<td><input type="button" class="button" name="delete[0]" id="0" title='<fmt:message key="delete"/>' value=" - " onclick="removeRowFromTable1(this,this);" /></td>
			</tr>
			</table>
			</div> 
		</fieldset>
		</td>	 
   </tr>
   </c:if>
   </table>
   
  </td></tr>
  </table>

  </div>
  </div>
  </html:form>
  </body>
  </fmt:bundle>

  <script>

		function validateService()
		{
			var list_input = document.getElementsByTagName ('input');
			var list_select = document.getElementsByTagName ('select');
			document.getElementById('editorHold').value='EDIT';
			
			if(document.getElementById('<c:out value="${service_id}"/>').value=="" || document.getElementById('<c:out value="${service_id}"/>').value=="Select"){
				disp_text='<c:out value="${service_id}"/>\n';	
			}

			if(document.getElementById('<c:out value="${service_bureau_id}"/>').value=="" || document.getElementById('<c:out value="${service_bureau_id}"/>').value=="Select"){
				disp_text=disp_text+'<c:out value="${service_bureau_id}"/>\n';
			}

			var internalRefId= document.getElementById('internalRefId').value;
			var serviceId= document.getElementById('<c:out value="${service_id}"/>').value; 
			var bureauId= document.getElementById('<c:out value="${service_bureau_id}"/>').value;				
			var servicecode=document.getElementById('service_code').value;
			var agreementId= document.getElementById('internalAgreementId').value;
  
            doValidation(list_input); // for customer id fields
			doValidation(list_select); // for customer id fields
			/* For currency exchange rate service there is no account numbers, so no validation */
			if(document.getElementById('service_code').value !='S010'){
			doValidation1(list_input); // for account number
			doValidation1(list_select); // for account number
			}

			if(displayMesg(2))
			{			
				if(displayMesg(3))
				{			
					if(displayMesg(130))
					{
						checkAjaxForServiceId(internalRefId,serviceId,bureauId,servicecode,agreementId);				
					}
				}
			}
		}

		/* Javascript validation for Customer id customer name fields */ 

		function doValidation(list_array){
			for (var i = 0; i < list_array.length; ++i)
			{
				if(list_array[i].id.indexOf('*')!=-1 && list_array[i].id.indexOf('[')== 9 && list_array[i].value==""|| list_array[i].value=="Select" )
				{
					var obj=list_array[i].id.replace('formItems['+list_array[i].id.charAt('10')+'].','');
					var row = parseInt(list_array[i].id.charAt('10'))+1;
					disp_text+=obj.replace('*','') + " in row no. "+row+"\n";
				}
			}
		}


	   /* Javascript function for Account number fields */  

		function doValidation1(list_array){
			for (var i = 0; i < list_array.length; ++i)
			{
				if(list_array[i].id.indexOf('*')!=-1 && list_array[i].id.indexOf('[')== 10 && list_array[i].value==""|| list_array[i].value=="Select" )
				{
					var obj=list_array[i].id.replace('formItems1['+list_array[i].id.charAt('11')+'].','');
					var row = parseInt(list_array[i].id.charAt('11'))+1;
					disp_text+=obj.replace('*','') + " in row no. "+row+"\n";
				}		
			}
		}

</script>
</html>

<fmt:bundle basename="${language}">
<script>
doFocusOnFiled('<fmt:message key="service_id"/>');
</script>
</fmt:bundle>

<fmt:bundle basename="${language}">
<script>
var v=document.getElementById('errcode');
if(v!= null){
	if((v.value)!= ''){
	checkTable();
	}
}else{
	
}


</script>

</fmt:bundle>

<fmt:bundle  basename="errors.AgreementErrorResources">

<script>
/*
	function callDeleteAlert() {
		if(eventFlag=='Y'){
			event.returnValue ='<fmt:message key="WAR-CPW-001"/>';
		}
	}
*/

function callDeleteAlert() {


			if(eventFlag=='Y') {
						if(opener.serviceWindowFlag=='false') {
				event.returnValue ='<fmt:message key="WAR-CPW-001"/>';
			}
		} 
		opener.setServiceWindowFlag();
		opener.setNullOpenWindowArray();
		//opener.setCloseWindowFlag();
	}

	function saveService() {
		eventFlag='N';
		validateService();
	}

</script>

</fmt:bundle>