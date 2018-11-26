<%-- Service Specification Edit - Second Window --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>

<%@ taglib uri="agreement.tld" prefix="ag" %>

<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<c:set var="language_id" value="${sessionObj.languageId}" />


<html>

<head>
<fmt:bundle basename="${language}">
<title><fmt:message key="${param.serviceCode}"/>-<fmt:message key="edit"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>
<script type="text/javascript" src="../js/search.js"></script>

<script type="text/javascript">

var eventFlag='Y';
var windowCloseFlag='false';
var serviceWindowFlag='false';

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



/* javascript function to delete row when user clicks on Minus button */

function removeRowFromTable(e, obj){


	windowCloseFlag='false';


	var ret = closeServiceWindow(); //closes all the child windows

//alert(ret);
//alert(windowCloseFlag);

	if(ret=='1') {
		windowCloseFlag='true';
	} else 
		windowCloseFlag='false';
if(windowCloseFlag=='true') {

	var tbl = document.getElementById('servicetable');
	var objId;
    if (obj != null) {
		objId = obj.id;
    } else {
		objId = this.id;
    }
	var lastRow = tbl.rows.length;
	var okok=objId;
	var v1,v2,v3,v4,v5,v6;
	for(var s=objId; s<lastRow-1;s++){
		
		okok++;
		
		v1=document.getElementById('formItems['+s+'].internalRefId').value;
		v2=document.getElementById('formItems['+s+'].<fmt:message key="service_id"/>*').value;
		v3=document.getElementById('formItems['+s+'].bureauId').value;
		v4=document.getElementById('formItems['+s+'].bureauName').value;
		v5=document.getElementById('formItems['+s+'].versionNo').value;
		
						
		document.getElementById('formItems['+s+'].internalRefId').value=
			document.getElementById('formItems['+okok+'].internalRefId').value;

		document.getElementById('formItems['+s+'].<fmt:message key="service_id"/>*').value=document.getElementById('formItems['+okok+'].<fmt:message key="service_id"/>*').value;

		document.getElementById('formItems['+s+'].bureauId').value=document.getElementById('formItems['+okok+'].bureauId').value;

		document.getElementById('formItems['+s+'].bureauName').value=document.getElementById('formItems['+okok+'].bureauName').value;
   	    document.getElementById('formItems['+s+'].versionNo').value=document.getElementById('formItems['+okok+'].versionNo').value;

		document.getElementById('formItems['+okok+'].internalRefId').value=v1;
		document.getElementById('formItems['+okok+'].<fmt:message key="service_id"/>*').value=v2;
		document.getElementById('formItems['+okok+'].bureauId').value=v3;
		document.getElementById('formItems['+okok+'].bureauName').value=v4;
		document.getElementById('formItems['+okok+'].versionNo').value=v5;
		
		}

		refId=document.getElementById('formItems['+okok+'].internalRefId').value;
		
        if(refId!=''){			

			var versionNo = document.getElementById('formItems['+okok+'].versionNo').value;
			// alert('refId : '+refId +' Version No : '+versionNo);
			if(getAjaxForDeleteServiceSpecification(refId,versionNo)){
				tbl.deleteRow(lastRow-1);
			}
		}else{
            tbl.deleteRow(lastRow-1);
		}
} 
windowCloseFlag='false'
}


var closeWindow='false';

//close service window
function closeServiceWindow() {


	var editWindowFlag='false';

//	alert('window len '+openWindow1.length);
		var openCount = openWindow1.length;

		if(openCount==0) { 
//			alert('openCount is 0');
			if(confirm(svpMesg[11])==true) {
//			alert('correct');
				return '1';
			} 
		}

		for(r=0;r<openCount;r++) {
//			alert('2');
			if(openWindow1[r]!=null) {
//				alert('3');
				editWindowFlag='true';
			if(confirm(svpMesg[11])==true) {
				serviceWindowFlag='true';
				openWindow1[r].close();
					windowCloseFlag='true';
					return '1';
					if(closeWindow=='true')
						openWindow1[r]=null;
				} else {
					windowCloseFlag='false';
				}
			}
			  } 

			 // openWindow1 = new Array();

			  if(editWindowFlag=='false'){
				windowCloseFlag='true';
				//return '1';
			  }			
		
		// windowCloseFlag='false';
		return '0';
	}

function setServiceWindowFlag() {
	serviceWindowFlag='false';
}

function setNullOpenWindowArray() {
			  openWindow1 = new Array();
}

function setCloseWindowFlag() {
	closeWindow='true';
}
//edit the service rows

function editServiceRow(e, obj)
{
	
	var objId;
    if (obj != null) {
		objId = obj.id;
    } else {
		objId = this.id;
    }
	var internalRefId = document.getElementById('formItems['+objId+'].internalRefId').value;
	var versionNo = document.getElementById('formItems['+objId+'].versionNo').value;
	// var versionNo = document.getElementById('versionNo').value;
	if(internalRefId=='')
		alert(svpMesg[5]);
	else
	{			  
	  
	  
	   showPopUp1('../jsp/servicespecificationsearchtype.do?internalRefId='+internalRefId+'&versionNo='+versionNo+'&rowNo='+objId+'&versionPos=<c:out value="${param.versionPos}"/>&status=<c:out value="${agmtList.status}"/>',490,650,'no','no');

	   document.getElementById('vh').value= 'formItems['+objId+'].versionNo';
	   document.getElementById('rowNo').value= objId;

	}
    
}

function getFirstService()
{
	var tbl = document.getElementById('servicetable');
	var lastRow = tbl.rows.length;
	
	if(lastRow==1)
	{
	   checkrow();
       getServiceType();
	}

}

function getServiceType()
{
	  var servicecode = document.getElementById('serviceCode').value;
	  var internalAgreementId = document.getElementById('internalAgreementId').value;
	  var versionNo = document.getElementById('versionNo').value;
		showPopUp('servicespecificationnewinedittype.jsp?serviceCode='+servicecode+'&internalAgreementId='+internalAgreementId+'&versionNo='+versionNo+'&versionPos=Y',490,650,'no','no');

}

//Ajax response to know the delete status

function ajaxReturnToFunctionDeleteServiceServiceSpecification(xmlEnterpriseDates)
{
		var xmlDoc = xmlEnterpriseDates.documentElement;
		var status = xmlDoc.getElementsByTagName("delete_status")[0].childNodes[0].nodeValue;
		if( status == 0)
		{
			alert(agmtMesg[5]); //Cannot delete agmtMesg[5]
			return false;
		}else
			return true;
}

 
 function deleteServiceRow()
 {
    var tbl= document.getElementById('servicetable');
    var lastRow=tbl.rows.length;
       
	if (lastRow >1)
	{
		var servicelist=document.getElementById('listOfServiceId').value;
        var serviceArrays= servicelist.split('~');
		
		if(serviceArrays.length!=0)
		{
			var tempString=serviceArrays.join('~');
			var serviceString= tempString.substring(0,tempString.lastIndexOf('~'));

		  
		}else
		{
			serviceString='';
		}
				
        document.getElementById('listOfServiceId').value=serviceString;
        tbl.deleteRow(lastRow-1);
	}
	
 }

 function addRowsForService()
	 {
		 var tbl= document.getElementById('servicetable');
		 var lastRow=tbl.rows.length;
		 iteration= lastRow-1;
		 if(lastRow == 1)
		 {
			 checkrow();
			 getServiceType();
		 }
		 else if(lastRow>1)
		 {
			 var ids= document.getElementById('formItems['+iteration+'].internalRefId').value;
			 if(ids!='')
			 {
			 checkrow();
			 getServiceType();
			 }
			 else
			 {
				 alert(svpMesg[10]); // please create service specification 
				 return false;
			 }
		 }
	 }


 function checkrow()
	 {

		 var tbl= document.getElementById('servicetable');
		 var lastRow=tbl.rows.length;
		
		 iteration= lastRow-1;
         if(lastRow==1)
		 {
			 var row=tbl.insertRow(lastRow);
			 var cell1= row.insertCell(0);
			 var index= lastRow-1;

			 cell1.align="left";
			 var e0=document.createElement('input');
			 e0.type="hidden";
			 e0.name= 'formItems['+lastRow+'].internalRefId'; 
			 e0.id='formItems['+lastRow+'].internalRefId';
			 e0.readOnly=true;
			 cell1.appendChild(e0);
			 
			 var cell2= row.insertCell(1);
			 var e1=document.createElement('input');
			 e1.type="text";
			 e1.name= 'formItems['+lastRow+'].serviceId'; 
			 e1.id='formItems['+lastRow+'].<fmt:message key="service_id"/>*';
			 e1.readOnly=true;
			 cell2.appendChild(e1);

			 var cell3= row.insertCell(2);
			 var e2=document.createElement('input');
			 e2.type="text";
			 e2.name= 'formItems['+lastRow+'].bureauId';
			 e2.id= 'formItems['+lastRow+'].bureauId';
			 e2.readOnly=true;
			 cell3.appendChild(e2);

			 var cell4= row.insertCell(3);
			 var e3=document.createElement('input');
			 e3.type="text";
			 e3.name= 'formItems['+lastRow+'].bureauName';
			 e3.id= 'formItems['+lastRow+'].bureauName';
			 e3.readOnly=true;
			 cell4.appendChild(e3);

		  
			 var cell5 = row.insertCell(4);
			 var e4 = document.createElement('input');
			 e4.type = 'button';
			 e4.name = 'edit['+lastRow+']';
			 e4.id = lastRow;
			 e4.value='<fmt:message key="edit"/>';
			 e4.title='<fmt:message key="edit"/>';
			 e4.className='button';
			 e4.onclick=editServiceRow;
			 cell5.appendChild(e4);

			 var cell6 = row.insertCell(5);
			 var e5 = document.createElement('input');
			 e5.type = 'button';
			 e5.name = 'delete['+lastRow+']';
			 e5.id = lastRow;
			 e5.value=' - ';
			 e5.onclick=removeRowFromTable;
			 e5.className='button';
			 e5.title='<fmt:message key="delete"/>';
			 cell6.appendChild(e5);  

			 var cell7= row.insertCell(6);
			 var e6=document.createElement('input');
			 e6.type="hidden";
			 e6.name= 'formItems['+lastRow+'].versionNo';
			 e6.id= 'formItems['+lastRow+'].versionNo';
			 e6.readOnly=true;
			 cell7.appendChild(e6);

			 document.getElementById('vh').value= 'formItems['+lastRow+'].versionNo';
			
		 }
		 
		 else
		 {
			
			var temp=document.getElementById('formItems['+iteration+'].<fmt:message key="service_id"/>*').value;
			
			if(temp==''){	
				return false;
			}
			else
			{ 
				 var row=tbl.insertRow(lastRow);
				 var cell1= row.insertCell(0);
				
				 var index= lastRow-1;

		 
				 cell1.align="left";
				 var e0=document.createElement('input');
				 e0.type="hidden";
				 e0.name= 'formItems['+lastRow+'].internalRefId'; 
				 e0.id='formItems['+lastRow+'].internalRefId';

				 e0.maxLength=11;
				 e0.size=20;
				 e0.readOnly=true;
				 cell1.appendChild(e0);
				 
				 var cell2= row.insertCell(1);
				 var e1=document.createElement('input');
				 e1.type="text";
				 e1.name= 'formItems['+lastRow+'].serviceId'; 
				 e1.id='formItems['+lastRow+'].<fmt:message key="service_id"/>*';
				 e1.maxLength=11;
				 e1.size=20;
				 e1.readOnly=true;
				 cell2.appendChild(e1);

				 var cell3= row.insertCell(2);
				 var e2=document.createElement('input');
				 e2.type="text";
				 e2.name= 'formItems['+lastRow+'].bureauId';
				 e2.id= 'formItems['+lastRow+'].bureauId';
				 e2.maxLength=40;
				 e2.size=20;
				 e2.readOnly=true;
				 cell3.appendChild(e2);

				 var cell4= row.insertCell(3);
				 var e3=document.createElement('input');
				 e3.type="text";
				 e3.name= 'formItems['+lastRow+'].bureauName';
				 e3.id= 'formItems['+lastRow+'].bureauName';
				 e3.maxLength=40;
				 e3.size=20;
				 e3.readOnly=true;
				 cell4.appendChild(e3);

			  
				 var cell5 = row.insertCell(4);
				 var e4 = document.createElement('input');
				 e4.type = 'button';
				 e4.name = 'edit['+lastRow+']';
				 e4.id = lastRow;
				 e4.value='<fmt:message key="edit"/>';
				 e4.title='<fmt:message key="edit"/>';
				 e4.className='button';
				 e4.onclick=editServiceRow;
				 cell5.appendChild(e4);

				 var cell6 = row.insertCell(5);
				 var e5 = document.createElement('input');
				 e5.type = 'button';
				 e5.name = 'delete['+lastRow+']';
				 e5.id = lastRow;
				 e5.value=' - ';
				 e5.onclick=removeRowFromTable;
				 e5.className='button';
				 e5.title='<fmt:message key="delete"/>';
				 cell6.appendChild(e5);  

				 var cell7= row.insertCell(6);
				 var e6=document.createElement('input');
				 e6.type="hidden";
				 e6.name= 'formItems['+lastRow+'].versionNo';
				 e6.id= 'formItems['+lastRow+'].versionNo';
				 e6.readOnly=true;
				 cell7.appendChild(e6);

				 document.getElementById('vh').value= 'formItems['+lastRow+'].versionNo';
			}
		 } 
	 }
     
	 function validateServiceId()
	 {


		 var tbl= document.getElementById('servicetable');

		 var lastRow=tbl.rows.length;
		 
		 var serviceList='';
		 var versionList='';
		 var version;

		 if(lastRow==1)
		 {
			 alert(svpMesg[6]);
			 return false;
		 }
		 
		 else if(lastRow>1){
			  
			    for(i=1;i<lastRow;i++)
				{
					 str=document.getElementById('formItems['+i+'].internalRefId').value;

					 version=document.getElementById('formItems['+i+'].versionNo').value;

					 if(str!='')
					 {
						 serviceList=serviceList+str+',';
						 versionList=versionList+version+',';
						
			         }
			    }


				document.getElementById('listOfServiceId').value=serviceList;
				document.getElementById('listOfServiceVersionNo').value=versionList;
				return true;
			 }	 
	 }


	 function validateService()
		{
			var list_input = document.getElementsByTagName ('input');
			var list_select = document.getElementsByTagName ('select');
			document.getElementById('editorHold').value='EDIT_SERVICE';
									  
            doValidation(list_input); // for service id fields
			doValidation(list_select); // for service id fields
			
			

			if(displayMesg(2))
			{	
				if(displayMesg(3))
				{
	        		if(displayMesg(130))
					{
						if(validateServiceId())
						{
						if(confirmations()){
						setFlags();
						closeWindows(); //closing all child windows
						document.servicespecificeditform.submit();
						}
						}
						
					}
				}
			}
		}

	/* Javascript validation for service id  fields */
	

	function doValidation(list_array){
    
	for (var i = 0; i < list_array.length; ++i)
	{   var obj,row;
		if(list_array[i].id.indexOf('*')!=-1 && list_array[i].value==""|| list_array[i].value=="Select" )
		{
			if((list_array[i].id.charAt('11'))==']')
			{
			obj=list_array[i].id.replace('formItems['+list_array[i].id.charAt('10')+'].','');
			row = parseInt(list_array[i].id.charAt('10'));	
		    }
			else  // if line number is in 2 digits, ie greater than 9, we are appending digits
			{   var digits = list_array[i].id.charAt('10')+list_array[i].id.charAt('11');
				obj=list_array[i].id.replace('formItems['+digits+'].','');
			    row = parseInt(digits);
			}	
			disp_text+=obj.replace('*','') +' and <fmt:message key="service_bureau_id"/>'+ " in row no. "+row+"\n";

		
			
		}
	}
}




</script>
</fmt:bundle>
</HEAD>

<!-- closeWindows() method closes all the child windows of this screen,
      method body is defined in agreement1.js file -->

<body class="shadowbody"  onbeforeunload="callDeleteAction()">
<html:form action="/resources/agreement/jsp/servicespecificedit">

 <input type="hidden" name="editorHold" Id="editorHold"/>
 <html:hidden property= "versionNo"  styleId="versionNo" value="${param.versionNo}"/>

  <!-- flags for all service -->
 <input type="hidden" name="S001F" id="S001F" value="N">
 <input type="hidden" name="S002F" id="S002F" value="N">
 <input type="hidden" name="S003F" id="S003F" value="N">
 <input type="hidden" name="S004F" id="S004F" value="N">
 <input type="hidden" name="S005F" id="S005F" value="N">
 <input type="hidden" name="S006F" id="S006F" value="N">
 <input type="hidden" name="S007F" id="S007F" value="N">
 <input type="hidden" name="S008F" id="S008F" value="N">
 <input type="hidden" name="S009F" id="S009F" value="N">
 <input type="hidden" name="S010F" id="S010F" value="N">
 <input type="hidden" name="S011F" id="S011F" value="N">

<!--  Hidden field to store version no of the selected row-->
 <input type="hidden" name="versionHolder" id="vh" value="">
 <input type="hidden" name="rowNo" id="rowNo" value="">

 
<c:choose>
<c:when test="${not empty requestScope.serviceDetails}">

<%@ include file="common/JavascriptErrorMsg.jsp" %>

<fmt:bundle basename="${language}">
<!-- Tool Bar -->

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="${param.serviceCode}"/>-<fmt:message key="edit"/></span></span></div></span>
</div>

<ul class="toolbar2">    
	     <!-- Subnavigation -->		                   
	     <c:set var="EDIT" value="Y" />			 
		 <li class="save">
			 <a onClick="javascript:saveService();" tabindex="1" onkeyPress="methodCallAfterEnterKey('validateService');"
			  disabled="true" onMouseOver="hover('a1','ahand');" 
			  onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_return"/>">
			  <fmt:message key="save_return"/></a>  
		 </li>	     		
</ul>
</fmt:bundle>

  <div id="service_type_new_scrollbar">
  
  <%@ include file="common/ErrorHandler.jsp" %>
  <div class="record-def">
  <fmt:bundle basename="${language}">
  <table width="100%" border="0">
	   <tr>
		 <td width="5%"><input type="button" value=" + " title='<fmt:message key="add"/>' id="addbutton" disabled="true" onclick="addRowsForService();" class="button"/></td>	  
	   </tr>
  </table>


<!--
ALL_BUREAUNAMES to get all the active and inactive bureaus
ACTIVE_BUREAUNAMES to get only active bureau names
-->

<c:choose>
<c:when test="${param.versionPos eq 'N'}">
	<c:set var="BUREAUS" value="ALL_BUREAUNAMES"/>
</c:when>
<c:when test="${param.status eq 'D'}">
	<c:set var="BUREAUS" value="ALL_BUREAUNAMES"/>
</c:when>
<c:otherwise>
	<c:set var="BUREAUS" value="ACTIVE_BUREAUNAMES"/>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${param.editView eq 'Y'}">
	<c:set var="EDITVIEW" value="edit"/>
</c:when>
<c:otherwise>
	<c:set var="EDITVIEW" value="view"/>
</c:otherwise>
</c:choose>

  <table width="100%" id="servicetable" border="0">
	<tr>
		<td></td>
		<td><p class="label_normal"><fmt:message key="service_id"/></p></td>
		<td><p class="label_normal"><fmt:message key="service_bureau_id"/></p></td>
		<td><p class="label_normal"><fmt:message key="service_bureau_name"/></p></td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
	</tr> 

	<c:forEach items='${serviceDetails}' var='serviceList' varStatus="status">  

	  <input type="hidden" name="serviceCode" value="${serviceList.serviceCode}" id="service_code"> 
	  <input type="hidden" name="internalAgreementId"value="${serviceList.internalAgreementId}">
	  <input type="hidden" name="listOfServiceId" id="listOfServiceId"/>
      <input type="hidden" name="listOfServiceVersionNo" id="listOfServiceVersionNo" />
      <input type="hidden" id="openService" >
	  
	 <tr>
	    <td>
				<input type="hidden" readonly="true" name="formItems[<c:out value="${status.count}"/>].internalRefId" 
				id="formItems[<c:out value="${status.count}"/>].internalRefId" 
				value="${serviceList.internalRefId}"/>
		</td>

		<td>
				<input type="text" readonly="true" disabled="true" name="formItems[<c:out value="${status.count}"/>].serviceId" 
				id="formItems[<c:out value="${status.count}"/>].<fmt:message key="service_id"/>*" value="${serviceList.serviceId}"/>
		</td>
		
		<ag:dropDown var="bureauIds" scope="page" key="${BUREAUS}" />
		<c:forEach items="${bureauIds}" var="bureauIds">       
			   <c:if test="${serviceList.bureauId eq bureauIds.key}">
					<c:set var="bureau_id" value="${bureauIds.value}" />
			   </c:if>
	   </c:forEach>

	  
       <ag:dropDown var="bureauNames" scope="page" key="ALL_BUREAUS" />
	   <c:forEach items="${bureauNames}" var="bureauName1">       
			   <c:if test="${serviceList.bureauId eq bureauName1.key}">
			    <!-- For previous version and deleted records bureau name will display from list -->
			   <c:choose>
						<c:when test="${param.versionPos eq 'N'}">
							<c:set var="bureau_Name" value="${serviceList.bureauName}" />
						</c:when>
						<c:when test="${param.status eq 'D'}">
							<c:set var="bureau_Name" value="${serviceList.bureauName}" />
						</c:when>
						<c:otherwise>
							 <c:set var="bureau_Name" value="${bureauName1.value}" />
						</c:otherwise>
				</c:choose>
			   </c:if>
	   </c:forEach>



		<td>
				<input type="text" readonly="true" disabled="true" name="formItems[<c:out value="${status.count}"/>].bureauId" 
				id="formItems[<c:out value="${status.count}"/>].bureauId" value="<c:out value="${bureau_id}"/>"/>
		</td>

		<td>
				<input type="text" readonly="true" disabled="true" name="formItems[<c:out value="${status.count}"/>].bureauName" 
				id="formItems[<c:out value="${status.count}"/>].bureauName" value="${bureau_Name}"/>
		</td>

		<td>
				<input type="button" title='<fmt:message key="${EDITVIEW}"/>' disabled="true" name="edit[<c:out value="${status.count}"/>]" class="button" 
				id="<c:out value="${status.count}"/>" value='<fmt:message key="${EDITVIEW}"/>' onclick="javascript: editServiceRow(this,this);" />
		</td>

		<td>
				<input type="button" title='<fmt:message key="delete"/>' disabled="true" name="delete[<c:out value="${status.count}"/>]" class="button"
				id="<c:out value="${status.count}"/>" value=" - " onclick="javascript: removeRowFromTable(this,this);" />
				<input type="hidden" readonly="true" disabled="true" name="formItems[<c:out value="${status.count}"/>].versionNo" 
				id="formItems[<c:out value="${status.count}"/>].versionNo" value="${serviceList.versionNo}"/>

		</td>
     </tr>
	 </c:forEach>
	 </fmt:bundle>
	</table>
	</div> 
	</div> 
</c:when>

 <c:otherwise>
	<fmt:bundle basename="errors.AgreementErrorResources">
		<p class="login_userpass"><fmt:message key="ERRAG-003"/></p>
	</fmt:bundle>
 </c:otherwise>

</c:choose>
</html:form>
</body>
</html>

<script>
if(opener.document.getElementById('openService').value=='Y'){
	document.getElementById('openService').value='Y';
	document.getElementById('a1').disabled=false;
	enableButtons('all','','');
}else{
	document.getElementById('a1').disabled=true;
	var tbl= document.getElementById('servicetable');
	var lastRow=tbl.rows.length;
	for(i=1;i<lastRow;i++)
	{
		document.getElementById(i).disabled=false;
	}

}


//Ajax to check service id,bureau id combinations
function ajaxReturnToFunctionClearTempTableData(xmlEnterpriseDates)
{

	var xmlDoc = xmlEnterpriseDates.documentElement;
	var service_id = xmlDoc.getElementsByTagName("service_id")[0].childNodes[0].nodeValue;
   
	if( service_id ==1)
	{
		//	alert("Content Deleted Successfully..!!"); 
		//	alert(svpMesg[7]); // Service Id, Bureau Id combination already exists for this Service type
		//	document.getElementById('<fmt:message key="service_id"/>').focus();
		return true;
	}else
	{	
//		setFlags();
//		addNewService();
//		document.servicespecifictypenewineditform.submit();
		return false;
	}
}


</script>

<fmt:bundle basename="errors.AgreementErrorResources">

<script>

function callDeleteAction() {

if(eventFlag=='Y'){

	var servicecode = document.getElementById('serviceCode').value;
	var internalAgreementId = document.getElementById('internalAgreementId').value;
	// alert(servicecode + " "  + internalAgreementId);

		event.returnValue ='<fmt:message key="WAR-CPW-002"/>';
	//	alert('servicecode->'+ servicecode + ' internalAgreementId->'+internalAgreementId);
	clearTempTableData(servicecode, internalAgreementId);

	//	event.returnValue ='testing';
	//	window.opener.location.reload();
	//	window.close(); 
	
	}

}

function saveService(){

	eventFlag='N';
	validateService('EDIT');
	}

function closeService(){
	eventFlag='N';
	validateService('EDIT');
	}

</script>

</fmt:bundle>