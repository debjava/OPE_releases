<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>

<%@ taglib uri="fmt.tld" prefix="fmt" %>
<%@ taglib uri="agreement.tld" prefix="ag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">
<HTML>
<HEAD>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>
<script type="text/javascript" src="../js/search.js"></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/resources/agreement/calender/theme.css"/>'>

<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-en.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/checkdate.js"/>'></script>

<script language="javascript" type="text/javascript" src="../js/ope_ajax.js"></script>

<script>

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

function checkSpecialCharWithSpace(obj) 
{
	var name = document.getElementById(obj.id).value;
	if(name!=''){
	if(name.match(/^[a-zA-Z0-9 ]+$/)) {
	return true;
	} else {
	alert(agmtMesg[9]); // Special characters are not allowed
	document.getElementById(obj.id).value='';
	document.getElementById(obj.id).focus();
	return false;
	}
	}
}

function checkSpecialCharForTelephone(obj) 
{
	var name = document.getElementById(obj.id).value;
	if(name!=''){
	if(name.match(/^[0-9+ -]+$/)) {
	return true;
	} else {
	alert(agmtMesg[12]); // Invalid telephone number
	document.getElementById(obj.id).value='';
	document.getElementById(obj.id).focus();
	return false;
	}
	}
}

function checkSpecialCharForZipCode(obj) 
{
	var name = document.getElementById(obj.id).value;
	if(name!=''){
	if(name.match(/^[0-9A-Za-z ]+$/)) {
	return true;
	} else {
	alert(agmtMesg[13]); // Invalid zip code
	document.getElementById(obj.id).value='';
	document.getElementById(obj.id).focus();

	return false;
	}
	}
}

function createPatuId(path,obj){
	if(checkSpecialChar(obj)){
	var companyName=document.getElementById('<fmt:message key="patu_user_id"/>*').value;
 
	if(document.getElementById('<fmt:message key="patu_id"/>').value!=''){		
		//alert(bureauMesg[1]); // PATU ID is already generated
		//return false;
	}
	else{
	
	var trimmedCompanyName = companyName.replace(/\s+|\s+/g, '') ;

	if(trimmedCompanyName=='' || trimmedCompanyName.length==0){
		//alert(bureauMesg[0]); // Please enter company name
		//document.getElementById('<fmt:message key="patu_user_id"/>*').focus();
		//return false;
	}else if(trimmedCompanyName.length<1){
		alert(bureauMesg[2]); // Company Name should be minimum 5 characters
		document.getElementById('<fmt:message key="patu_user_id"/>*').focus();
		return false;
	}
	else{
		var companyCode=trimmedCompanyName.substring(0,9).toUpperCase();		
		getAjaxValueForPatuId(companyCode,'CREATE_PATUID',path);
		return true;
	}
	}
	}
	else
	{
		return false;
	}
}

function ajaxReturnToFunctionPatuId(xmlDoc)
{

	var patuARRAY = xmlDoc.split(",");	
	var length=patuARRAY.length;
	
	if(patuARRAY.length == 0)
	{
		alert(bureauMesg[3]); // Failed to generate the PATU ID. Please try later.
		return false;
	}else
	{
		document.getElementById('<fmt:message key="patu_id"/>').value=patuARRAY[0];		
	}	
}

function clearPatuId(){
	document.getElementById('<fmt:message key="patu_id"/>').value='';
}

function checkServiceBureauId() {
	var bureauId=document.getElementById('<fmt:message key="service_bureau_id"/>*').value;
	if(bureauId.length!=0){
	if(bureauId.length<17){		
		alert(bureauMesg[4]); // Service Bureau Id should be 17 characters.
		document.getElementById('<fmt:message key="service_bureau_id"/>*').select();
		return false;
	}else{
		return true;
	}
	}
}

var eventFlag='Y';

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
		
		if(flag==true){
			// alert(bureauMesg[13]) ; //alert('Warning: Keys have to be generated for the service bureau');
		}
		checkPATUID();

		go.submit();		
	}	
}

function checkPATUID() {

// alert('TEST PATU->'+bureauMesg[16]);
	if(document.getElementById('<fmt:message key="patu_id"/>').value==''){		
		alert(bureauMesg[16]);
	}

}

function clearFunction()
{
	document.getElementById('<fmt:message key="service_bureau_id"/>*').value='';
	document.getElementById('<fmt:message key="service_bureau_name"/>*').value='';
	document.getElementById('<fmt:message key="patu_user_id"/>*').value='';
	document.getElementById('<fmt:message key="patu_id"/>').value='';
	document.getElementById('bureauDescription').value='';
	document.getElementById('<fmt:message key="patu_contact_person_1"/>*').value='';
	document.getElementById('<fmt:message key="patu_contact_person_2"/>*').value='';
	document.getElementById('<fmt:message key="address_1"/>*').value='';
	document.getElementById('<fmt:message key="address_2"/>*').value='';
	document.getElementById('<fmt:message key="city_1"/>*').value='';
	document.getElementById('<fmt:message key="city_2"/>*').value='';
	document.getElementById('<fmt:message key="zip_code_1"/>*').value='';
	document.getElementById('<fmt:message key="zip_code_2"/>*').value='';
	document.getElementById('<fmt:message key="telephone_1"/>*').value='';
	document.getElementById('<fmt:message key="telephone_2"/>*').value='';
	//document.getElementById('<fmt:message key="country_1"/>*').selectedIndex=0;
	//document.getElementById('<fmt:message key="country_2"/>*').selectedIndex=0;

	 
}

function saveBureauAfterEnterKey()
{
	var key;
	if (window.event)
	key = window.event.keyCode;
	var editorHold = document.getElementById('editorHold');
	if(key==13)
	{
		validate('ServiceBureauNew',editorHold,'NEW');
	}

}

function clearBureauAfterEnterKey()
{
	var key;
	if (window.event)
	key = window.event.keyCode;
	if(key==13)
	{
		document.ServiceBureauNew.reset();
		clearFunction();
	}
}

</script>

<title><fmt:message key="service_bureau"/> - <fmt:message key="new"/></title>

</HEAD>

<BODY class="shadowbody">

<%@ include file="common/JavascriptErrorMsg.jsp" %>

<html:form action="/resources/agreement/jsp/ServiceBureauNew">

<c:url value="/resources/agreement/jsp/CreatePatuId.do" var="CreatePatuId"/>

<%@ include file="common/JavascriptErrorMsg.jsp" %>

 <c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="service_bureau"/> - <fmt:message key="new"/></span></span></div></span>
</div>	

<ul class="toolbar2">
			  <!-- Subnavigation -->			
			  <li class="save">
				 <c:choose>	 
					<c:when test="${NEW=='1'}">
					<a onClick="return validate('ServiceBureauNew',editorHold,'NEW');" tabindex="1"
					  onMouseOver="hover('a1','ahand');" onkeyPress="saveBureauAfterEnterKey();"
					  onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_and_close"/>">
					  <fmt:message key="save_and_close"/>	</a>  
					 </c:when>

				 <c:otherwise>
					<a disabled > <fmt:message key="save_and_close" /></a>
				 </c:otherwise>    
			 </c:choose>
			  </li>			
			  
			  <li class="clear"><a onClick="document.ServiceBureauNew.reset();clearFunction();" onMouseOver="hover('a3','ahand');" onMouseOut="hover('a3','nohand');" onkeyPress="clearBureauAfterEnterKey();" tabindex="2" id="a3" title="<fmt:message key="clear"/>"><fmt:message key="clear"/></a></li>
</ul>


<div id="bureau_new_scrollbar">

<%@ include file="common/ErrorHandler.jsp" %>

<div class="record-def">

<input type="hidden" name="editorHold" id="editorHold" >

<input type="hidden" name="findNewOrEditHold" id="findNewOrEditHold" value="NH">

<ag:ServiceBureauId var="internalBureauId" scope="page"/>

<html:hidden property="internalBureauId" value="${internalBureauId}"/>

<html:hidden property= "versionNo" value="1"/>
					
<table align="center" cellpadding="3" cellspacing="2" border="0" width="98%">
	 <tr>
	  <c:set var="service_bureau_id"><fmt:message key="service_bureau_id"/></c:set>
	  <td><p class="label_normal"><fmt:message key="service_bureau_id"/>:</p></td> 
	  <td><span class="asterisk">*</span><html:text  property="bureauId" styleId="${service_bureau_id}*" maxlength="17" size="22"
		  onblur="javascript: this.value=Trim(this.value);javascript: this.value=this.value.toUpperCase();checkSpecialChar(this);"
		  onkeypress="return alphanumericWithoutSpace(this);" />
	  </td>
  	  <td>&nbsp;</td>
  	  <td>&nbsp;</td>
	  </tr>

	  <tr>
	  <c:set var="service_bureau_name"><fmt:message key="service_bureau_name"/></c:set>
	  <td><p class="label_normal"><fmt:message key="service_bureau_name"/>:</p></td> 
	  <td><span class="asterisk">*</span><html:text  property="bureauName" styleId="${service_bureau_name}*" maxlength="40" 
	  onblur="javascript: this.value=Trim(this.value);checkSpecialCharWithSpace(this);"
	  onkeypress="return alphanumericWithSpace(this);"/>
	  </td>
  	  <td>&nbsp;</td>
  	  <td>&nbsp;</td>
	</tr>
    <!-- company name is same as patu user id -->
	<tr>
	  <c:set var="patu_user_id"><fmt:message key="patu_user_id"/></c:set>
	  <td><p class="label_normal"><fmt:message key="patu_user_id"/>:</p></td> 
	  <td><span class="asterisk">*</span><html:text  property="companyName" styleId="${patu_user_id}*" maxlength="9" 
	  onkeypress="return alphanumericWithSpace(this);"
	  onblur="javascript: this.value=Trim(this.value);return createPatuId('${CreatePatuId}',this);" onchange="clearPatuId();"/>
	  </td>
  	  <td>&nbsp;</td>
  	  <td>&nbsp;</td>
	</tr>

	<tr>
	  <c:set var="patu_id"><fmt:message key="patu_id"/></c:set>
	  <td><p class="label_normal"><fmt:message key="patu_id"/>:</p></td> 
	  <td colspan="3">&nbsp;&nbsp;&nbsp;<html:text  property="patuId" styleId="${patu_id}" maxlength="17"  readonly="true" styleClass="textbox_bold" size="24"/>
  	  </td>
	  <td>&nbsp;</td>
	  <td>&nbsp;</td>
	</tr>

	<tr>		
		<td><p class="label_normal"><fmt:message key="service_bureau_description"/>:</p></td> 

		<td colspan="3"><span class="asterisk">&nbsp;</span>
				 <html:textarea property="bureauDescription" cols="39" rows="6" styleClass="text_area"
					 onkeypress="return checkMaxLength(this,event,255);" styleId="bureauDescription"
					 onkeydown="textCounter(ServiceBureauNew.bureauDescription,255);" 
					 onkeyup="textCounter(ServiceBureauNew.bureauDescription,255);"
					 onblur="textCounter(ServiceBureauNew.bureauDescription,255);javascript: this.value=Trim(this.value);">
					 </html:textarea>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		
		<c:set var="patu_contact_person_1"><fmt:message key="patu_contact_person_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="patu_contact_person_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="contactPerson1" onblur="javascript: this.value=Trim(this.value);" styleId="${patu_contact_person_1}*" maxlength="32"/></td>

			<c:set var="address_1"><fmt:message key="address_1"/></c:set>
		<td width="22%"><p class="label_normal"><fmt:message key="address_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="addressLine1" onblur="javascript: this.value=Trim(this.value);" styleId="${address_1}*" maxlength="255"/></td>

	</tr>

	<tr>
	    <c:set var="city_1"><fmt:message key="city_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="city_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="city1" onblur="javascript: this.value=Trim(this.value);" styleId="${city_1}*" maxlength="32"/></td>


		<c:set var="zip_code_1"><fmt:message key="zip_code_1"/></c:set>
		<td><p class="label_normal"><fmt:message key="zip_code_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="pinCode1" styleId="${zip_code_1}*" size="10" maxlength="8" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);"/></td>

    </tr>

		<tr>
		<c:set var="country_1"><fmt:message key="country_1"/></c:set>
        <td><p class="label_normal"><fmt:message key="country_1"/></td>
		
		<ag:dropDown var="countries1" scope="page" key="COUNTRY" />

        <td><span class="asterisk">*</span><html:select property="country1" styleId="${country_1}*" styleClass="select">
			<html:option value="" ><fmt:message key="select"/></html:option>
			<c:forEach items="${countries1}" var="country1">
			<option value ="${country1.key}" <c:if test="${country1.key eq 'FI'}">selected</c:if>>
			<c:out value="${country1.value}"/></option>
			</c:forEach>
			</html:select>
        </td>
		<c:set var="telephone_1"><fmt:message key="telephone_1"/></c:set>
	    <td><p class="label_normal"><fmt:message key="telephone_1"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="telephoneNo1" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" styleId="${telephone_1}*" maxlength="13" onkeypress="return chkTelFax(this);"/></td>
	</tr>


	<tr>
	
		<c:set var="patu_contact_person_2"><fmt:message key="patu_contact_person_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="patu_contact_person_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="contactPerson2" onblur="javascript: this.value=Trim(this.value);" styleId="${patu_contact_person_2}*"  maxlength="32" /></td>		


		<c:set var="address_2"><fmt:message key="address_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="address_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="addressLine2" onblur="javascript: this.value=Trim(this.value);" styleId="${address_2}*" maxlength="255"/></td>
	</tr>

	

	<tr>
		
		<c:set var="city_2"><fmt:message key="city_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="city_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="city2" onblur="javascript: this.value=Trim(this.value);" styleId="${city_2}*" maxlength="32"/></td>
		

		<c:set var="zip_code_2"><fmt:message key="zip_code_2"/></c:set>
		<td><p class="label_normal"><fmt:message key="zip_code_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="pinCode2" styleId="${zip_code_2}*" size="10" maxlength="8" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);"/></td>		
	</tr>


   
	<tr>
		<c:set var="country_2"><fmt:message key="country_2"/></c:set>
        <td><p class="label_normal"><fmt:message key="country_2"/>:</p></td>
		
		<ag:dropDown var="countries2" scope="page" key="COUNTRY" />

        <td ><span class="asterisk">*</span><html:select property="country2" styleId="${country_2}*" styleClass="select">
			<html:option value="" ><fmt:message key="select"/></html:option>
			<c:forEach items="${countries2}" var="country2">
			<option value="${country2.key}" <c:if test="${country2.key eq 'FI'}">selected</c:if>><c:out value="${country2.value}"/></option>
			</c:forEach>
			</html:select>
        </td>

		<c:set var="telephone_2"><fmt:message key="telephone_2"/></c:set>
	    <td><p class="label_normal"><fmt:message key="telephone_2"/>:</p></td>
		<td><span class="asterisk">*</span><html:text property="telephoneNo2" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" styleId="${telephone_2}*" maxlength="13" onkeypress="return chkTelFax(this);"/></td>
	</tr>

</table>
</div>
</div>
</html:form>
</BODY>

</fmt:bundle>
</HTML>
<fmt:bundle basename="${language}">
<script>
var v=document.getElementById('errcode');
if(v!= null){
	if((v.value)== 'ERR-SVB-004'){
	doFocusOnFiled('<fmt:message key="service_bureau_name"/>*');
	}else if((v.value)== 'ERR-SVB-009'){
    doFocusOnFiled('<fmt:message key="patu_user_id"/>*');
	}else{
    doFocusOnFiled('<fmt:message key="service_bureau_id"/>*');
	}
}else{
	doFocusOnFiled('<fmt:message key="service_bureau_id"/>*');
}

</script>
</fmt:bundle>
<fmt:bundle basename="errors.AgreementErrorResources">
<script>
function callDeleteAction()	{

	if(eventFlag=='Y') {
		event.returnValue ='<fmt:message key="WAR-CPW-001"/>';
	}
}
</script>
</fmt:bundle>