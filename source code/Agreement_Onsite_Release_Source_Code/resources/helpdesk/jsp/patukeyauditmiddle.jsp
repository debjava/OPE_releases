<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>


<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="sessionKey" value="PMDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>

<fmt:requestEncoding value="UTF-8"/>

<html xmlns="http://www.w3.org/1999/xhtml">

<fmt:setLocale value="${sessionObj.languageId}"/>
<link href= '<c:url value="/css/process_style.css"/>' rel="stylesheet" type="text/css"/>

<script language="javascript">

function getDBPath()
{
var pathname = window.location.pathname;
var iPos = window.location.pathname.toString().toLowerCase().lastIndexOf('.nsf');
if(iPos>0) return pathname.substring(0, iPos+4);

return pathname; 
} 
function call()
{
	var currentUser,currentBranch,result,fetchStatus,reportPath,userSystemDate,report,onlineStatus;
	
	currentUser=document.getElementById("currentUser").value;
	currentBranch=document.getElementById("currentBranch").value;
	userSystemDate=document.getElementById("userSystemDate").value;

	fetchStatus=document.getElementById("fetchStatus").value;
	
	result=document.getElementById("result").value;

	var dataMatriceImageUrl = window.location.protocol + "//" + window.location.host+'<c:url value="/resources/common/images/dmlogologin.jpg"/>';

	var clientLogoImageUrl = window.location.protocol + "//" + window.location.host+'<c:url value="/resources/common/images/rpt_logo_client.jpg"/>';

	var rpt=document.getElementById('reportPath').value;

	var randomnumber=Math.floor(Math.random()*10001);

	report = rpt +"patukeyauditdetails"+randomnumber+".rptdocument";
		
	document.PatuKeyAuditDetailsSearch.method="POST";

	document.PatuKeyAuditDetailsSearch.action="<c:out value="${pageContext.request.contextPath}"/>/frameset?__report=resources/helpdesk/reportdesigns/PatuKeyAuditDetails.rptdesign&__overwrite=true&__document="+report+"&CurrentUser="+currentUser+"&userSystemDate="+userSystemDate+"&dataMatriceImageUrl="+dataMatriceImageUrl+"&clientLogoImageUrl="+clientLogoImageUrl+"&Branch="+currentBranch+"&fetchStatus="+fetchStatus;		
	document.PatuKeyAuditDetailsSearch.submit();

	
}
</script>
<head>
<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
</head>
<body onload="call()">
<fmt:bundle basename="applicationconfig.installation">
<table width = "100%" >
<tr> <td></td></tr>
</table>
<form name="PatuKeyAuditDetailsSearch" action="">	
	
	<input type="hidden" name="currentUser" id="currentUser" value="${sessionObj.currentUser}"/>
	
	<input type="hidden" name="currentBranch" id="currentBranch" value="${sessionObj.branch}"/>
	
	<input type="hidden" name="userSystemDate" id="userSystemDate" value="${requestScope.userSystemDate}"/>
	
	<input type="hidden" name="fetchStatus" id="fetchStatus" value="${requestScope.fetchStatus}"/>

	<html:hidden property="patuUserId" value="${requestScope.patuUserId}" />
	<html:hidden property="patuId" value="${requestScope.patuId}"/>
	<html:hidden property="serviceBureauId" value="${requestScope.serviceBureauId}"/>
	<html:hidden property="serviceBureauName"  value="${requestScope.serviceBureauName}"/>
	<html:hidden property="dateFrom"  value="${requestScope.dateFrom}"/>
	<html:hidden property="dateTo" value="${requestScope.dateTo}"/>

	<c:set var="property"  value="applicationconfig.installation" />
	<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">
	<input type="hidden" name="reportPath" id="reportPath" value="<fmt:message key="rptPath"/>"/>
</fmt:bundle>

	<input type="hidden" name="result" id="result" value="${requestScope.result}"/>	

	</form>
	</fmt:bundle>
</body>
</html>