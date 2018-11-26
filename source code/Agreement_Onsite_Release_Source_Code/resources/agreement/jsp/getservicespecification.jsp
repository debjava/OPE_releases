<%-- Service Specification New - Edit - Data Found Second Window --%>
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
<title><fmt:message key="${param.serviceCode}"/>-<fmt:message key="new"/></title>

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
function setFlags()
{	
	var servicecode=document.getElementById('serviceCode').value;
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
	closeWindows(); //closes all the child windows
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
	
	if(internalRefId=='')
		alert(svpMesg[5]);
	else
	{		
		var versionNo = document.getElementById('formItems['+objId+'].versionNo').value;
		// alert('VNo'+versionNo);
	   showPopUp1('../jsp/getservicespecificationtype.do?internalRefId='+internalRefId+'&versionNo='+versionNo,490,650,'no','no');

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
		showPopUp('servicespecificationtype.jsp?serviceCode='+servicecode+'&internalAgreementId='+internalAgreementId+'&versionNo=1&versionPos=Y',490,650,'no','no');

}

//Ajax response to know the delete status

function ajaxReturnToFunctionDeleteServiceServiceSpecification(xmlEnterpriseDates)
{
		var xmlDoc = xmlEnterpriseDates.documentElement;
		/* Variable to store the Delete Status. */
		var status = xmlDoc.getElementsByTagName("delete_status")[0].childNodes[0].nodeValue;
		if( status =='')
		{
			alert(svpMesg[2]); //Cannot delete 
			return false;
		}else
			return true;
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
			 e4.value='<fmt:message key="view"/>';
			 e4.className='button';
			 e4.title='<fmt:message key="view"/>';
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
				 e4.value='<fmt:message key="view"/>';
				 e4.className='button';
				 e4.title='<fmt:message key="view"/>';
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
				// alert(serviceList);
				// alert(versionList);

				return true;
			 }	 
	 }

	 function validateService()
		{
			var list_input = document.getElementsByTagName ('input');
			var list_select = document.getElementsByTagName ('select');
			document.getElementById('editorHold').value='NEW_SERVICE';
									  
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
						closeWindows(); //closes all the child windows
						document.servicespecificnewform.submit();
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

<body class="shadowbody" onload="getFirstService();" onUnLOad="closeWindows();">
<html:form action="/resources/agreement/jsp/servicespecificnew">
<c:choose>
  <c:when test="${not empty requestScope.serviceDetails}">

  <%@ include file="common/JavascriptErrorMsg.jsp" %>

 <input type="hidden" name="editorHold" Id="editorHold"/>
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
 

 <input type="hidden" name="versionHolder" id="vh" value="">
  <input type="hidden" name="rowNo" id="rowNo" value="">

<fmt:bundle basename="${language}">
<!-- Tool Bar -->

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="${param.serviceCode}"/>-<fmt:message key="new"/></span></span></div></span>
</div>

<ul class="toolbar2">    
	<!-- Subnavigation -->		
	 <c:forEach var="lstRights" items="${sessionObj.roleList}">
		<c:if test="${lstRights.entityCode eq 'AGMT'}">		
			<c:set var="rights" value="${lstRights.rightNo}"/>
		</c:if>
	 </c:forEach>

	<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />     
		 <li class="save">
		   <c:choose>	 
			<c:when test="${NEW=='1'}">
			 <a onClick="javascript:validateService();"
			  onMouseOver="hover('a1','ahand');" tabindex="1" onkeyPress="methodCallAfterEnterKey('validateService');"
			  onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_return"/>">
			  <fmt:message key="save_return"/></a> 
			</c:when>

		   <c:otherwise>
			  <a disabled > <fmt:message key="save_return" /></a>
		   </c:otherwise>    
		 </c:choose>
	  </li>         		 
 </ul>
		  
</fmt:bundle>

  <div id="service_type_new_scrollbar">
  <%@ include file="common/ErrorHandler.jsp" %>
  
  <div class="record-def">
  <fmt:bundle basename="${language}">

	<table width="100%" border="0">
	   <tr>
		 <td width="5%"><input type="button" value=" + " title='<fmt:message key="add"/>' id="addbutton" onclick="addRowsForService();" class="button"/></td>	  
	   </tr>
	</table>
  
    <input type="hidden" name="serviceCode" value="${param.serviceCode}" id="serviceCode"> 
	<input type="hidden" name="internalAgreementId"value="${param.internalAgreementId}" id="internalAgreementId">
    <input type="hidden" name="listOfServiceId" id="listOfServiceId" />
	    <input type="hidden" name="listOfServiceVersionNo" id="listOfServiceVersionNo" />

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
	<tr>
	    <td>
				<input type="hidden" readonly="true" name="formItems[<c:out value="${status.count}"/>].internalRefId" 
				id="formItems[<c:out value="${status.count}"/>].internalRefId" 
				value="${serviceList.internalRefId}"/>
		</td>

		<td>
				<input type="text" readonly="true" name="formItems[<c:out value="${status.count}"/>].serviceId" 
				id="formItems[<c:out value="${status.count}"/>].<fmt:message key="service_id"/>*" value="${serviceList.serviceId}"/>
		</td>

        <ag:dropDown var="bureauIds" scope="page" key="ACTIVE_BUREAUNAMES" />
		<c:forEach items="${bureauIds}" var="bureauIds">       
			   <c:if test="${serviceList.bureauId eq bureauIds.key}">
					<c:set var="bureau_id" value="${bureauIds.value}" />
			   </c:if>
	    </c:forEach>	

		<ag:dropDown var="bureauNames" scope="page" key="ALL_BUREAUS" />
	    <c:forEach items="${bureauNames}" var="bureauName1">       
			   <c:if test="${serviceList.bureauId eq bureauName1.key}">
					<c:set var="bureau_Name" value="${bureauName1.value}" />
			   </c:if>
	    </c:forEach>

		<td>
				<input type="text" readonly="true" name="formItems[<c:out value="${status.count}"/>].bureauId" 
				id="formItems[<c:out value="${status.count}"/>].bureauId" value="<c:out value="${bureau_id}"/>"/>
		</td>

		<td>
				<input type="text" readonly="true" name="formItems[<c:out value="${status.count}"/>].bureauName" 
				id="formItems[<c:out value="${status.count}"/>].bureauName" value="${bureau_Name}"/>
		</td>

		<td>
				<input type="button" title='<fmt:message key="view"/>' name="edit[<c:out value="${status.count}"/>]" class="button"
				id="<c:out value="${status.count}"/>" value='<fmt:message key="view"/>' onclick="javascript: editServiceRow(this,this);" />
		</td>

		<td>
				<input type="button" title='<fmt:message key="delete"/>' name="delete[<c:out value="${status.count}"/>]" class="button"
				id="<c:out value="${status.count}"/>" value=" - " onclick="javascript: removeRowFromTable(this,this);" />

	
				<input type="hidden" readonly="true" name="formItems[<c:out value="${status.count}"/>].versionNo" 
				id="formItems[<c:out value="${status.count}"/>].versionNo" value="<c:out value="${serviceList.versionNo }"/>"/>

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

