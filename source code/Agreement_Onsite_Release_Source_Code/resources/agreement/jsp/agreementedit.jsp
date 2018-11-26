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
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="language_id" value="${sessionObj.languageId}" />
<fmt:bundle basename="${language}">

<html>

<head>
<title><fmt:message key="agreement"/>-<fmt:message key="edit"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/resources/agreement/calender/theme.css"/>'>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-${language_id}.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>

<script language="javascript" type="text/javascript" src="../js/checkdate.js"></script>

<html:xhtml/>
<html:base/>

<!-- this function is for getting  list of check boxes has checked -->

 <SCRIPT LANGUAGE="JavaScript">

var eventFlag='Y';

         function getSelected(opt) {
            var selected = new Array();
            var index = 0;
            for (var intLoop = 0; intLoop < opt.length; intLoop++) {
               if ( (opt[intLoop].checked))
			   {
				  
                  index = selected.length;
				 
                  selected[index] = new Object;
				  
                  selected[index].value = opt[intLoop].value;
				 
                  selected[index].index = intLoop;
                  
               }
            }
            return selected;
         }

         function outputSelected() {
		    var dd=document.getElementsByName('services');
			var sel = getSelected(dd);
            var strSel = "";
            for (var item in sel)       
            strSel += sel[item].value + ",";
			
			if(strSel=="" || strSel==null)
			 {
				alert(agmtMesg[2]); //select atleast one service
				return false;
			 }
			 else
			 {
				 document.getElementById('selectedServices').value=strSel;
				 return true;
			 }
			
			
 			
        
         }

 function disableDates(){
document.getElementById('stdate_trigger').disabled=true;
document.getElementById('stdate_trigger1').disabled=true;

}

function enableDates(){
//document.getElementById('stdate_trigger').disabled=false;
document.getElementById('stdate_trigger1').disabled=false;
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

		function checkSpecialChar(obj) 
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

		function saveAfterEnterKey()
		{
			var key;
			if (window.event)
			key = window.event.keyCode;
			if(key==13)
			{
				validateAgreement('EDIT');
			}
		}

		function editAfterEnterKey()
		{
			var key;
			if (window.event)
			key = window.event.keyCode;
			if(key==13)
			{
				hover('tdpre','left-arrow');
				hover('a3','nohand');
				document.getElementById('openService').value='Y';
				enableDates();
				changePreviousIcon();
				enableButtons('all','','a3,a5,tdnext,tdpre,AUT_RSA,PKI,VPN,SSL_SSH');
			}
			
		}

		function printAfterEnterKey()
		{
			var key;
			if (window.event)
			key = window.event.keyCode;
			if(key==13)
			{
				var int_agreement_id=document.getElementById('int_agreement_id').value;
                var version_no1=document.getElementById('version_no1').value;
				doPrint(int_agreement_id,version_no1);
			}
		}

 </SCRIPT> 
 
<script>
	function doPrint(id,ver1)
	{					
        showPopUp("../jsp/agreementprint.do?internalAgreementId="+id+"&versionNo="+ver1,500,720,'yes','no');
      		
	}
</script>

</head>
</fmt:bundle>

<!-- closeWindows() method closes all the child windows of this screen,
      method body is defined in agreement1.js file -->

<body class="shadowbody" onUnLOad="closeWindows();" onbeforeunload="callDeleteAction();">
<html:form action="/resources/agreement/jsp/agreementedit.do" >

<%@ include file="common/JavascriptErrorMsg.jsp" %>
<c:choose>
<c:when test="${not empty requestScope.agreementDetails}">
<!-- Begin Edit Deaprtment -->
<c:forEach items='${agreementDetails}' var='agmtList'>  
<input type="hidden" name="findNewOrEditHold" value="EH">

<!-- Tool Bar -->
<fmt:bundle basename="${language}">
<input type="hidden" name="int_agreement_id" id="int_agreement_id" value="${agmtList.internalAgreementId}"/>
<input type="hidden" name="version_no1" id="version_no1" value="${agmtList.versionNo}"/>

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="agreement"/>-<fmt:message key="edit"/></span></span></div></span>
</div>

 <ul class="toolbar2">
	  <!-- Subnavigation -->	
	  
	  <c:forEach var="lstRights" items="${sessionObj.roleList}">
		<c:if test="${lstRights.entityCode eq 'AGMT'}">		
			<c:set var="rights" value="${lstRights.rightNo}"/>
		</c:if>
	 </c:forEach>

	 <c:set var="EDIT" value="${fn:substring(rights, 1, 2)}" />

	 <li class="save"><a onClick="javascript:validateAgreement('EDIT');"
		 disabled="true" onMouseOver="hover('a1','ahand');" tabindex="1" onkeypress="saveAfterEnterKey();"
		 onMouseOut="hover('a1','nohand');" id="a1" title="<fmt:message key="save_and_close"/>">
		 <fmt:message key="save_and_close"/></a>
	 </li>
	
	 <li class="edit" >
	   <c:choose>
			<c:when test="${EDIT=='1'}">
			   <c:choose>
				 <c:when test="${agmtList.versionPos=='N'}">
					<a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
				 </c:when>
				 
				 <c:otherwise>				 
					  <c:choose>
							<c:when test="${agmtList.status=='D'}">				 
								<a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
						   </c:when>
							<c:otherwise>				 				
							<a onClick="hover('tdpre','left-arrow');hover('a3','nohand');document.getElementById('openService').value='Y'; enableDates();
							changePreviousIcon();
							return enableButtons('all','','a3,a5,tdnext,tdpre,AUT_RSA,PKI,VPN,SSL_SSH');"
								onMouseOver="hover('a3','ahand');" tabindex="2" onkeypress="editAfterEnterKey();"
								onMouseOut="hover('a3','nohand');" id="a3" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
							</c:otherwise>
					  </c:choose>
				 </c:otherwise>
			   </c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					 <c:when test="${agmtList.versionPos=='N'}">
						<a disabled="true" title="<fmt:message key="edit"/>"><fmt:message key="edit"/></a>
					 </c:when>
					 
					 <c:otherwise>
							<a onClick="changePreviousIcon();return enableButtons('all','','a3,a5,tdnext,tdpre,AUT_RSA,PKI,VPN,SSL_SSH');"
						  onMouseOver="hover('a3','ahand');" tabindex="2"  onkeypress="editAfterEnterKey();"
						  onMouseOut="hover('a3','nohand');" id="a3" title="<fmt:message key="edit"/>" disabled><fmt:message key="edit"/></a>
					 </c:otherwise>
				 </c:choose>			 
			</c:otherwise>  
	   </c:choose>
	 </li>  
	 <li class="print">
		<c:choose>
		  <c:when test="${agmtList.versionPos=='N'}">
					<a disabled="true" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
		  </c:when>
		  <c:otherwise>				 
			   <c:choose>
					<c:when test="${agmtList.status=='D'}">				 
					  <a disabled="true" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
					</c:when>
					<c:otherwise>
					   <a onClick="doPrint('${agmtList.internalAgreementId}',${agmtList.versionNo})" onMouseOver="hover('a5','ahand');" 
					   onMouseOut="hover('a5','nohand');" id="a5" tabindex="3" onkeypress="printAfterEnterKey();"title="<fmt:message key="print"/>">
					   <fmt:message key="print"/></a>
					</c:otherwise>
				</c:choose>
		   </c:otherwise>
		 </c:choose>
	  </li>

 </ul>
		  
</fmt:bundle>

<div id="agreement_edit_scrollbar">
<%@ include file="common/ErrorHandler.jsp" %>

<div class="record-def">
<fmt:bundle basename="${language}">
<input type="hidden" name="editorHold" >
<!-- hidden field for holding selected services -->
<input type="hidden" name="selectedServices" id="selectedServices">
<html:hidden property= "versionNo" name="agmtList"/>

<input type="hidden" id="openService" value="N">

<table align="center" cellpadding="1" cellspacing="1" border="0" width="97%">
	<tr>
	  <input type="hidden" name="internalAgreementId" value="${agmtList.internalAgreementId}">
	  <td><p class="label_normal"><fmt:message key="agreement_id"/>:</p></td> 
	  <td><p class="label_bold"><span class="asterisk">&nbsp;&nbsp;</span>${agmtList.internalAgreementId}</p></td>  
	   
	  <td >&nbsp;</td>
	  <td >&nbsp;</td>
	</tr>
	
	<tr>
		<td><p class="label_normal"><fmt:message key="agreement_title"/>:</p></td> 
		<c:set var="agreement_title"><fmt:message key="agreement_title"/></c:set>
		<td colspan="3" ><span class="asterisk">*</span><html:text property="agreementTitle" maxlength="50" styleId="${agreement_title}" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialChar(this);" size="40" name="agmtList" disabled="true"/></td>
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="primary_contact"/>:</p></td>
		<c:set var="primary_contact"><fmt:message key="primary_contact"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="primaryContact" maxlength="40" styleId="${primary_contact}" size="30" onblur="javascript: this.value=Trim(this.value);" name="agmtList" disabled="true"/></td>
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="street_address"/>:</p></td> 
        <c:set var="street_address"><fmt:message key="street_address"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="streetAddress" maxlength="255" styleId="${street_address}" size="50" onblur="javascript: this.value=Trim(this.value);" name="agmtList" disabled="true"/></td>
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="city"/>:</p></td>
        <c:set var="city"><fmt:message key="city"/></c:set>
	    <td colspan="3"><span class="asterisk">*</span><html:text property="city" maxlength="32" styleId="${city}" onblur="javascript: this.value=Trim(this.value);" name="agmtList" disabled="true"/></td>
	</tr>

	<tr>
		<td><p class="label_normal"><fmt:message key="zip_code"/>:</p></td>
		<c:set var="zip_code"><fmt:message key="zip_code"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="zipCode" size="14" maxlength="8" styleId="${zip_code}" onkeypress="return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForZipCode(this);" name="agmtList" disabled="true"/></td>
	</tr>
	<tr>
        <td><p class="label_normal"><fmt:message key="country"/>:</p></td>       

		  <c:set var="country"><fmt:message key="country"/></c:set>
		<ag:dropDown var="countries" scope="page" key="COUNTRY" />
        <td colspan="3"><span class="asterisk">*</span><html:select property="country" styleId="${country}" styleClass="select" disabled="true">
                          <option value=""><fmt:message key="select"/></option>
				          <c:forEach items="${countries}" var="country1">
						  <option 
							value ="<c:out value="${country1.key}"/>" 
							<c:if test="${agmtList.country==country1.key}">selected</c:if>>
							<c:out value="${country1.value}"/>
						  </option>
							</c:forEach>

                        </html:select>
	</tr>
	<tr>
		<td><p class="label_normal"><fmt:message key="telephone"/>:</p></td>
        <c:set var="telephone"><fmt:message key="telephone"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="telephone" styleId="${telephone}" maxlength="13" onblur="javascript: this.value=Trim(this.value);checkSpecialCharForTelephone(this);" onkeypress="return chkTelFax(this);" name="agmtList" disabled="true" /></td>
	</tr>
	<tr>
		<td><p class="label_normal"><fmt:message key="email"/>:</p></td>
        <c:set var="email"><fmt:message key="email"/></c:set>
		<td colspan="3"><span class="asterisk">*</span><html:text property="email" size="30" maxlength="50"  styleId="${email}" onblur="javascript: chkEmail(this);" name="agmtList" disabled="true" /></td>
	</tr>

	<tr>
		<td ><p class="login_userpass"><fmt:message key="valid_from"/>:</p></td>		
		<td>		
	        <c:set var="valid_from"><fmt:message key="valid_from"/></c:set>
			<span class="asterisk">*</span><input type="text" readonly="true" name="validFrom" size="10" Id="${valid_from}"  disabled="true"
			onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
			onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}');
			return mask(this.value,this,'${sessionObj.dateFormat}');"
			value="${fn:substring(agmtList.validFrom,0,10)}"/>
			
			<A class="nohand" id="calendar" >
			<img id="stdate_trigger" src="../images/cal_img.gif"  
		    style="{vertical-align:bottom;}" border="0" >
			</A>
		</td>
		<td ><p class="login_userpass"><fmt:message key="valid_to"/>:</p></td>
		<td>
		    <c:set var="valid_to"><fmt:message key="valid_to"/></c:set>
			<input type="text" name="validTo" size="10" Id="${valid_to}"  disabled="true"
			onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
			onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}'); return mask(this.value,this,'${sessionObj.dateFormat}');" 
			value="${fn:substring(agmtList.validTo,0,10)}" readonly="true"/>

			<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar1" >
			<img id="stdate_trigger1" src="../images/cal_img.gif" 
			style="{vertical-align:bottom;}" border="0" >
			</A>  
		</td>
	</tr>
</table>

<!--- here list of services is adding--->
<table width="97%" border="0">
<tr>
  <td><p class="label_bold"><fmt:message key="services"/><span class="asterisk">*</span>:</p></td>
</tr>
</table>


<table width="97%" border="0">
<tr>
  <td>
    <table width="97%">
		<tr>
			<td width="40%"><p class="label_bold"><fmt:message key="customer_sends_to_bank"/></p></td>
			<td><p class="label_bold"><fmt:message key="bank_sends_to_customer"/></p></td>
		</tr>
	</table>
  </td>
</tr>


<tr>
  <td><%@include file="servicesedit.jsp" %></td> 
</tr>

</table>

<table border="0" width="97%">
    <tr><td >&nbsp;</td></tr>

	<tr>
		<td width="130px"><p class="label_bold"><fmt:message key="security_method"/><span class="asterisk">*</span>:</p></td>
		<td><p class="label_normal">

		<c:choose>
			<c:when test="${agmtList.securityMethod == 'PATU'}">
				<input type="radio" disabled="true" name="securityMethod" checked class="radio" value="PATU" maxlength="10" id="PATU">
			</c:when>
			<c:otherwise>
				<input type="radio" disabled="true" name="securityMethod" class="radio" value="PATU" maxlength="10" id="PATU">
			</c:otherwise>
		</c:choose>

		<fmt:message key="PATU"/></p></td>
		<td>&nbsp;</td>
		<td width="30px">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

	<tr>
	    <td>&nbsp;</td>
	    <td><p class="label_normal">

        <c:choose>
			<c:when test="${agmtList.securityMethod == 'AUT_RSA'}">
		        <input type="radio" disabled="true" checked name="securityMethod" class="radio" value="AUT_RSA" maxlength="10" id="AUT_RSA">
		    </c:when>
		    <c:otherwise>
				<input type="radio" disabled="true" name="securityMethod" class="radio" value="AUT_RSA" maxlength="10" id="AUT_RSA">
			</c:otherwise>
		</c:choose>

		<fmt:message key="AUTACK_RSA"/></p></td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <td><p class="label_normal">

		<c:choose>
			<c:when test="${agmtList.securityMethod == 'PKI'}">
		        <input type="radio" disabled="true" checked name="securityMethod" class="radio" value="PKI" maxlength="10" id="PKI">
            </c:when>
		    <c:otherwise>
				<input type="radio" disabled="true" name="securityMethod" class="radio" value="PKI" maxlength="10" id="PKI">
			</c:otherwise>
		</c:choose>

		<fmt:message key="PKI"/></p></td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><p class="label_normal">
        <c:choose>
			<c:when test="${agmtList.securityMethod == 'VPN'}">
		       <input type="radio" disabled="true" checked name="securityMethod" class="radio" value="VPN" maxlength="10" id="VPN">
            </c:when>
		    <c:otherwise>
				<input type="radio" disabled="true" name="securityMethod" class="radio" value="VPN" maxlength="10" id="VPN">
			</c:otherwise>
		</c:choose>
		<fmt:message key="VPN"/></p></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><p class="label_normal">
		<c:choose>
			<c:when test="${agmtList.securityMethod == 'SSL_SSH'}">
		       <input type="radio" disabled="true" checked name="securityMethod" class="radio" value="SSL_SSH" maxlength="10" id="SSL_SSH">
            </c:when>
		    <c:otherwise>
				<input type="radio" disabled="true" name="securityMethod" class="radio" value="SSL_SSH" maxlength="10" id="SSL_SSH">
			</c:otherwise>
		</c:choose>

		<fmt:message key="SSL_SSH"/></p></td>
		<td>&nbsp;</td>
		<td width="30px">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

   <tr>
		<td></td>
		<td></td>
  </tr>

</table>

</div>
</div>

<!-- insert audit trail bar-->

<c:set var="versionNo" value="${agmtList.versionNo}" scope="page"/> 
 <c:set var="lastVersionNo" value="${requestScope.maxVersionNo}" />
<input type="hidden" name="lastVersionNo" value="${requestScope.maxVersionNo}" />

<html:hidden property="createdBy" name="agmtList"/>

<table class="def-info">
    <tr>
      <th><fmt:message key="created_by"/></th>
      <th><fmt:message key="created_on"/></th>
      <th><fmt:message key="last_updated_by"/></th>
      <th><fmt:message key="last_updated_on"/></th>
	  <th><fmt:message key="authorized_by"/></th>
      <th><fmt:message key="authorized_on"/></th>
      <th><fmt:message key="version_no"/></th>
      <th><fmt:message key="status"/></th>
      <th class="last"></th>      
    </tr>

	<tr>
      <td><c:out value='${agmtList.createdBy}' /></td>
      <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agmtList.createdOn}"/></td>
      <td><c:out value='${agmtList.lastUpdatedBy}' /></td>
      <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agmtList.lastUpdatedOn}"/></td>
	  <td><c:out value='${agmtList.authorisedBy}' /></td>
	  <td><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agmtList.authorisedOn}"/></td>
      <td><c:out value='${agmtList.versionNo}'/>&nbsp;<fmt:message key="of"/>&nbsp;
			<c:out value='${requestScope.maxVersionNo}'/></td>
      <td><c:out value='${agmtList.status}' /></td>
      <td class="last">
	  <c:choose>
           <c:when test="${agmtList.versionNo>1}">	   
			   <a id="tdpre" onClick="return versionAgreement('PREVIOUS');" class="left-arrow ahand" title="<fmt:message key="Previous"/>">&larr;</a> 
			   <script>hover('apre','prev_next')</script>
          </c:when>
          <c:otherwise>         
	    	  <a id="tdpre" onClick="return versionAgreement('PREVIOUS');" class="left-arrow-disabled" disabled title="<fmt:message key="Previous"/>" >&larr;</a>	 
          </c:otherwise>
          </c:choose>

          <c:choose>
          <c:when test="${agmtList.versionPos=='N'}"> 
			  <a id="tdnext" class="right-arrow ahand" onClick="return versionAgreement('NEXT');"title="<fmt:message key="Next"/>">&rarr;</a>	 
          </c:when>

          <c:otherwise>   
			 <a id="tdnext" class="right-arrow-disabled" disabled onClick="return versionAgreement('NEXT');" title="<fmt:message key="Next"/>">
			 &rarr;</a>       
          </c:otherwise>
          </c:choose>
      </td>
    </tr>
  </table>

</fmt:bundle>
</c:forEach>
</c:when>

<c:otherwise>
	<fmt:bundle basename="errors.AgreementErrorResources">
		<p class="login_userpass"><fmt:message key="ERRAG-003"/></p>
	</fmt:bundle>
</c:otherwise>

</c:choose>

<c:choose>
<c:when test="${sessionObj.dateFormat=='yyyy-MM-dd'}">
<c:set var="date_format" value="${'%Y-%m-%d'}"/>
</c:when>
<c:when test="${sessionObj.dateFormat=='MM-dd-yyyy'}">
<c:set var="date_format" value="${'%m-%d-%Y'}"/>
</c:when>
<c:otherwise>
<c:set var="date_format" value="${'%d-%m-%Y'}"/>
</c:otherwise>
</c:choose>

<fmt:bundle basename="${language}">
<script language="javascript" type="text/javascript">
	Calendar.setup({
        inputField     :    "${valid_from}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",      // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
	
		Calendar.setup({
        inputField     :    "${valid_to}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger1",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
  
	</script>
</fmt:bundle>

</html:form>
</body>
</html>
<script>
disableDates();
populateArray(document.agreemeneditform, currVals);
</script>

<fmt:bundle basename="${language}">
<script>

function DoDatesCompare2(){

		if(compareDates(document.getElementById('<c:out value="${valid_from}"/>').value,'${sessionObj.dateFormat}',
						 document.getElementById('<c:out value="${valid_to}"/>').value,
						 '${sessionObj.dateFormat}')==true)
			{
					alert (agmtMesg[1]); // Valid To should be greater than or equal to Valid From
					document.getElementById('<c:out value="${valid_to}"/>').value='';
					document.getElementById('<c:out value="${valid_to}"/>').focus();
					return false;
			}
		 else
			{
				 return true;
		    }
	 }



/* this function  validates all mandatory fields  */
function validateAgreement(status1)
{
	var list_input = document.getElementsByTagName ('input');
	var list_select = document.getElementsByTagName ('select');
	document.getElementById('editorHold').value=status1;
	
	/* IF  part will execute when ONLY EDIT has clicked */
	if(status1=='EDIT')
	{
		if(document.getElementById('<c:out value="${agreement_title}"/>').value=="" || document.getElementById('<c:out value="${agreement_title}"/>').value=="Select"){
		disp_text='<c:out value="${agreement_title}"/>\n';	
	}
    
	if(document.getElementById('<c:out value="${primary_contact}"/>').value=="" || document.getElementById('<c:out value="${primary_contact}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${primary_contact}"/>\n';	
	}

	if(document.getElementById('<c:out value="${street_address}"/>').value=="" || document.getElementById('<c:out value="${street_address}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${street_address}"/>\n';
	}

	if(document.getElementById('<c:out value="${city}"/>').value=="" || document.getElementById('<c:out value="${city}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${city}"/>\n';
	}

	if(document.getElementById('<c:out value="${zip_code}"/>').value=="" || document.getElementById('<c:out value="${zip_code}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${zip_code}"/>\n';
	}

	if(document.getElementById('<c:out value="${country}"/>').value=="" || document.getElementById('<c:out value="${country}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${country}"/>\n';
	}

	if(document.getElementById('<c:out value="${telephone}"/>').value=="" || document.getElementById('<c:out value="${telephone}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${telephone}"/>\n';
	}

	if(document.getElementById('<c:out value="${email}"/>').value=="" || document.getElementById('<c:out value="${email}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${email}"/>\n';
	}

	if(document.getElementById('<c:out value="${valid_from}"/>').value=="" || document.getElementById('<c:out value="${valid_from}"/>').value=="Select"){
		disp_text=disp_text+'<c:out value="${valid_from}"/>\n';
	}

	if(displayMesg(2))
	{
		
		if(displayMesg(3))
		{
			
			if(displayMesg(130))
			{
				if(outputSelected())
				{
				if(checkFlags())
				{
				  if(document.getElementById('<c:out value="${valid_to}"/>').value.length!=0)
					{
					   if(DoDatesCompare2()==true)
					   {
						   if(confirmations()){
							eventFlag='N';
							document.agreemeneditform.submit();
						   }
					   }else
					    return false;
					}else{
						if(confirmations()){
							eventFlag='N';
							document.agreemeneditform.submit();
						}
					}
			     }
			    }
			}
		}
	}

	}
	/* else part will execute when PREVIOUS and NEXT has clicked */
	else
	{
		document.agreemeneditform.submit();			 		
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

function versionAgreement(type){
	eventFlag='N';
	validateAgreement(type);
	}

</script>
</fmt:bundle>

<fmt:bundle basename="errors.AgreementErrorResources">

<script>

	function callDeleteAction() {
		if(eventFlag=='Y'){
		//	var servicecode = document.getElementById('serviceCode').value;
			var internalAgreementId = document.getElementById('int_agreement_id').value;

			// alert(servicecode + " "  + internalAgreementId);
			event.returnValue ='<fmt:message key="WAR-CPW-002"/>';

			//	alert(internalAgreementId);

			clearTempTableData("", internalAgreementId);

			//	event.returnValue ='testing';
			//	window.opener.location.reload();
			//	window.close();
		}
	}

</script>

</fmt:bundle>