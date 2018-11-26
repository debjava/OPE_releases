<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="language_id" value="${sessionObj.languageId}" />
<fmt:bundle basename="${language}">

<head>
<title><fmt:message key="patu_key_audit"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<html:xhtml/>
<html:base/>

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/resources/agreement/calender/theme.css"/>'>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-${language_id}.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/checkdate.js"/>'></script>

<script type="text/javascript" src='<c:url value="/resources/agreement/js/jquery.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/agreement/js/ifixpng2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/agreement/js/js.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/agreement/js/dm.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/agreement/js/search.js"/>'></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script>
function validateReport()
{

	var rpt=document.getElementById('reportPath').value;

	var disp_text='';

	if(disp_text!=''){
		alert(disp_text);
		return false;
	}else if(DoDatesCompare()==false){
		return false;
	}else{
		document.FileTransferAuditDetailsSearch.submit();	
		return true;
	}	

	//submitPageAfterEnterKey('ChannelDetailsSearch');
}

function DoDatesCompare(){

		if(compareDates(document.getElementById('<fmt:message key="date_from"/>').value,'${sessionObj.dateFormat}',
						 document.getElementById('<fmt:message key="date_to"/>').value,
						 '${sessionObj.dateFormat}')==true)
			{
				alert (fileTransferMesg[0]); // Date To should be greater than or equal to Date From
				document.getElementById('<fmt:message key="date_to"/>').select();
				return false;
			}else if(checkDate()==true){
					return false;
			}
		 else
			{
				 return true;
		    }
}

function checkDate(){

	if(document.getElementById('<fmt:message key="date_from"/>').value!='' && document.getElementById('<fmt:message key="date_to"/>').value!=''){
	var parsedFirstDate= parseDate(document.getElementById('<fmt:message key="date_from"/>').value);
	var parsedSecondDate= parseDate(document.getElementById('<fmt:message key="date_to"/>').value);

	var date1 = Date.UTC( parsedFirstDate.getFullYear(), parsedFirstDate.getMonth(), parsedFirstDate.getDate(),0,0,0);
	var date2 = Date.UTC( parsedSecondDate.getFullYear(), parsedSecondDate.getMonth(), parsedSecondDate.getDate(),0,0,0);

	if(date1==date2){
		if(parseInt(document.getElementById('hoursFrom').value) > parseInt(document.getElementById('hoursTo').value)){
			alert (fileTransferMesg[1]); // Hours in date to should be greater than or equal to hours in date from
			return true;
		}else{
			return false;
		}
	}
	}
}

function checkDateFrom(obj){
	if(document.getElementById('<fmt:message key="date_from"/>').value==''){
	   obj.selectedIndex=0;
	   alert('Please enter the Date From.');
	}
}

function disableHoursFrom(obj){

	if(obj.value==''){
		document.getElementById('hoursFrom').selectedIndex=0;			
	}

}

function checkDateTo(obj){
	if(document.getElementById('<fmt:message key="date_to"/>').value==''){
	   obj.selectedIndex=0;
	   alert('Please enter the Date To.');
	}
}

function disableHoursTo(obj){

	if(obj.value==''){
		document.getElementById('hoursTo').selectedIndex=0;			
	}

}

</script>

</head>

<body >
<!-- Tool Bar -->

<%@ include file="/resources/agreement/jsp/common/JavascriptErrorMsg.jsp" %>

<html:form action="/resources/helpdesk/jsp/FileTransferAuditDetailsSearch?subtitle=patu_key_audit&module=agreement"  target="_blank">

<div class="search-content">
		<table cellpadding="2" cellspacing="2" >

				<tr>
					<c:set var="patu_id"><fmt:message key="patu_id"/></c:set>
					<td height = "22"><p class="login_userpass"><fmt:message key="patu_id"/>:</p></td>
					<td><html:text property="patuId" styleClass="select" styleId="${patu_id}" maxlength="17"				
					onblur="javascript: this.value=Trim(this.value);"
					onkeyup="javascript: this.value=this.value.toUpperCase();" 
					onkeypress="return alphanumericWithSpace(this);"/>
					</td>
				</tr>

				<tr>
					<c:set var="agreement_id"><fmt:message key="agreement_id"/></c:set>
					<td height = "22"><p class="login_userpass"><fmt:message key="agreement_id"/>:</p></td>
					<td><html:text property="agreementId" styleClass="select" styleId="${agreement_id}" maxlength="9"				
					onblur="javascript: this.value=Trim(this.value);"
					onkeyup="javascript: this.value=this.value.toUpperCase();" 
					onkeypress="return alphanumericWithSpace(this);"/>
					</td>
				</tr>

				<tr>
					<c:set var="agreement_id"><fmt:message key="service_type"/></c:set>
					<td height = "22"><p class="login_userpass"><fmt:message key="service_type"/>:</p></td>
					<td><html:text property="serviceType" styleClass="select" styleId="${service_type}" maxlength="17"				
					onblur="javascript: this.value=Trim(this.value);"
					onkeyup="javascript: this.value=this.value.toUpperCase();" 
					onkeypress="return alphanumericWithSpace(this);"/>
					</td>
				</tr>
				
				<tr>				
				<td ><p class="login_userpass"><fmt:message key="date_from"/>:</p></td>				
				<td>		
					<c:set var="date_from"><fmt:message key="date_from"/></c:set>
					<html:text property="dateFrom" size="10" styleId="${date_from}"  
					onkeypress="disableHoursFrom(this);numcheck(this.value,'${sessionObj.dateFormat}');" 
					onkeyup="disableHoursFrom(this);return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="disableHoursFrom(this);DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}');
					return mask(this.value,this,'${sessionObj.dateFormat}');" />
					
					<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar">
					<img id="stdate_trigger"  src='<c:url value="/resources/agreement/images/cal_img.gif"/>' 
					style="{vertical-align:top;}" border="0"  >
					</A>
				</td>
				<td><p class="login_userpass">
					<html:select property="hoursFrom" styleId="hoursFrom" onchange="javascript: checkDateFrom(this);">
						<option value="0">00</option>						
						<option value ="1">01</option>
						<option value ="2">02</option>
						<option value ="3">03</option>
						<option value ="4">04</option>
						<option value ="5">05</option>
						<option value ="6">06</option>
						<option value ="7">07</option>
						<option value ="8">08</option>
						<option value ="9">09</option>
						<option value ="10">10</option>
						<option value ="11">11</option>
						<option value ="12">12</option>
						<option value ="13">13</option>
						<option value ="14">14</option>
						<option value ="15">15</option>
						<option value ="16">16</option>
						<option value ="17">17</option>
						<option value ="18">18</option>
						<option value ="19">19</option>
						<option value ="20">20</option>
						<option value ="21">21</option>
						<option value ="22">22</option>
						<option value ="23">23</option>						
					</html:select> <fmt:message key="hours"/></p>

					<html:hidden property="ampmFrom" value="0" />
				</td>
				
				</tr>
				<tr>
				
				<tr>
				<td><p class="login_userpass"><fmt:message key="date_to"/>:</p></td>				
				<td>
					<c:set var="date_to"><fmt:message key="date_to"/></c:set>
					<html:text property="dateTo" size="10" styleId="${date_to}"  
					onkeypress="disableHoursTo(this);numcheck(this.value,'${sessionObj.dateFormat}');" 
					onkeyup="disableHoursTo(this);return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="disableHoursTo(this);DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}'); return mask(this.value,this,'${sessionObj.dateFormat}');" />

					<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar1">
					<img id="stdate_trigger1" src='<c:url value="/resources/agreement/images/cal_img.gif"/>' 
					style="{vertical-align:top;}" border="0">
					</A>  
				</td>
				
				<td><p class="login_userpass">
					<html:select property="hoursTo" styleId="hoursTo" onchange="javascript: checkDateTo(this);">
						<option value="0">00</option>						
						<option value ="1">01</option>
						<option value ="2">02</option>
						<option value ="3">03</option>
						<option value ="4">04</option>
						<option value ="5">05</option>
						<option value ="6">06</option>
						<option value ="7">07</option>
						<option value ="8">08</option>
						<option value ="9">09</option>
						<option value ="10">10</option>
						<option value ="11">11</option>
						<option value ="12">12</option>
						<option value ="13">13</option>
						<option value ="14">14</option>
						<option value ="15">15</option>
						<option value ="16">16</option>
						<option value ="17">17</option>
						<option value ="18">18</option>
						<option value ="19">19</option>
						<option value ="20">20</option>
						<option value ="21">21</option>
						<option value ="22">22</option>
						<option value ="23">23</option>						
					</html:select> <fmt:message key="hours"/></p>

					<html:hidden property="ampmTo" value="0" />
				</td>
				</tr>			
				<tr>
				<td >&nbsp;</td>
				<td colspan="2"><input type="button" class="button" value="<fmt:message key="generate_report"/>" onclick="return validateReport();"></td>
			</tr>
		</table>
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

<script language="javascript" type="text/javascript">
	Calendar.setup({
        inputField     :    "${date_from}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",      // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
	
		Calendar.setup({
        inputField     :    "${date_to}",      // id of the input field
        ifFormat       :    "<c:out value='${date_format}'/>",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "stdate_trigger1",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
	</script>
<c:set var="property"  value="applicationconfig.installation" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">
	<input type="hidden" name="reportPath" id="reportPath" value="<fmt:message key="report_path"/>"/>
</fmt:bundle>
</html:form>
</fmt:bundle>
</body>
</html>