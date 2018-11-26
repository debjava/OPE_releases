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
<script language="javascript" type="text/javascript" src="../js/agreement1.js"></script>

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
	var disp_text='';

	/*
	if(document.getElementById('<fmt:message key="patu_user_id"/>').value==""){
				disp_text='<fmt:message key="patu_user_id"/>\n';					
	}

	if(document.getElementById('<fmt:message key="patu_id"/>').value==""){
				disp_text=disp_text+'<fmt:message key="patu_id"/>\n';	
	}

	if(document.getElementById('<fmt:message key="service_bureau_id"/>').value==""){
				disp_text=disp_text+'<fmt:message key="service_bureau_id"/>\n';	
	}
	*/

	var rpt=document.getElementById('reportPath').value;	

	if(disp_text!=''){
		alert(disp_text);
		return false;
	}else if(DoDatesCompare()==false){
		return false;
	}else{
		document.PatuKeyAuditDetailsSearch.submit();	
		return true;
	}	

	//submitPageAfterEnterKey('ChannelDetailsSearch');
}

function DoDatesCompare(){
		if(compareDates(document.getElementById('<fmt:message key="date_from"/>').value,'${sessionObj.dateFormat}',
					 document.getElementById('<fmt:message key="date_to"/>').value,
					 '${sessionObj.dateFormat}')==true)
		{
			alert (patuAuditMesg[0]); // Date To should be greater than or equal to Date From
			document.getElementById('<fmt:message key="date_to"/>').select();
			return false;
		}
		else
		{
			 return true;
		}
}

</script>

</head>

<body >
<!-- Tool Bar -->
<%@ include file="/resources/agreement/jsp/common/JavascriptErrorMsg.jsp" %>
<html:form action="/resources/helpdesk/jsp/PatuKeyAuditDetailsSearch?subtitle=patu_key_audit&module=agreement"  target="_blank">
<div class="search-content">

		<table cellpadding="2" cellspacing="2" border="0">
				<tr>
				<c:set var="patu_user_id"><fmt:message key="patu_user_id"/></c:set>
				<td ><p class="login_userpass"><fmt:message key="patu_user_id"/>:</p></td>
				<td><html:text property="patuUserId" styleClass="select" styleId="${patu_user_id}" maxlength="9" size="16"		 		
				onblur="javascript: this.value=Trim(this.value);"				
				onkeypress="return alphanumericWithSpace(this);"/></td></tr>

				<tr>
				<c:set var="patu_id"><fmt:message key="patu_id"/></c:set>
				<td ><p class="login_userpass"><fmt:message key="patu_id"/>:</p></td>
				<td><html:text property="patuId" styleClass="select" styleId="${patu_id}" maxlength="17"				
				onblur="javascript: this.value=Trim(this.value);"
				onkeyup="javascript: this.value=this.value.toUpperCase();" 
				onkeypress="return alphanumericWithSpace(this);"/></td></tr>

				<tr>
				<c:set var="service_bureau_id"><fmt:message key="service_bureau_id"/></c:set>
				<td ><p class="login_userpass"><fmt:message key="service_bureau_id"/>:</p></td>
				<td><html:text property="serviceBureauId" styleClass="select" styleId="${service_bureau_id}" maxlength="17"				
				onblur="javascript: this.value=Trim(this.value);"
				onkeyup="javascript: this.value=this.value.toUpperCase();" 
		        onkeypress="return alphanumericWithSpace(this);"/></td>
				
				</tr>
				
				<tr>
				<c:set var="service_bureau_name"><fmt:message key="service_bureau_name"/></c:set>
				<td ><p class="login_userpass"><fmt:message key="service_bureau_name"/>:</p></td>
				<td><html:text property="serviceBureauName" styleClass="select" styleId="${serviceBureauName}" maxlength="40"			
				onblur="javascript: this.value=Trim(this.value);"				
		        onkeypress="return alphanumericWithSpace(this);"/></td>
				
				</tr>
				
				<tr>
				
				<td ><p class="login_userpass"><fmt:message key="date_from"/>:</p></td>				
				<td>		
					<c:set var="date_from"><fmt:message key="date_from"/></c:set>
					<html:text property="dateFrom" size="10" styleId="${date_from}"  
					onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
					onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}');
					return mask(this.value,this,'${sessionObj.dateFormat}');" />
					
					<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar">
					<img id="stdate_trigger"  src='<c:url value="/resources/agreement/images/cal_img.gif"/>'  
					 border="0" style="{vertical-align:top;}" >
					</A>
				</td>
				
				</tr>
				<tr>
				
				<tr>
				<td ><p class="login_userpass"><fmt:message key="date_to"/>:</p></td>				
				<td>
					<c:set var="date_to"><fmt:message key="date_to"/></c:set>
					<html:text property="dateTo" size="10" styleId="${date_to}"  
					onkeypress="numcheck(this.value,'${sessionObj.dateFormat}');" 
					onkeyup="return mask(this.value,this,'${sessionObj.dateFormat}');" onblur="DateFormat(this,this.value,event,true,'${sessionObj.dateFormat}'); return mask(this.value,this,'${sessionObj.dateFormat}');" />

					<A title="<fmt:message key="click_for_calendar"/>" class="ahand" id="calendar1">
					<img id="stdate_trigger1"  src='<c:url value="/resources/agreement/images/cal_img.gif"/>'  
					border="0" style="{vertical-align:top;}">
					</A>  
				</td>				
				</tr>				
				<tr>
				<td >&nbsp;</td>
				<td colspan="2"><input type="button" class="button" value="<fmt:message key="generate_report"/>" onclick="return validateReport();"></td>
			</tr>
		</table>	
</div>
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