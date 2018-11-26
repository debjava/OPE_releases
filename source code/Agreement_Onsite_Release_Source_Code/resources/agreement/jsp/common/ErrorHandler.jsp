<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:url value="/getException.do" var="actionGetException"/>
<style>
.error13 {
	/*width:100%;
	/*background:#fbc4c4;
	border:1px solid #e84c4c;*/
	color:#e70202;
	font:bold 11px Tahoma, Arial;
	padding:19px 19px 19px 19px;
	text-align:left;	
	white-space:nowrap;	
	
}
</style>				

<c:if test="${not empty requestScope.opeErrorList}">

<fmt:bundle basename="errors.AgreementErrorResources">

<c:forEach items='${opeErrorList}' var='lstErr'>  

<c:set var="key" value="${lstErr}"/>
<c:set var="chkKey"><fmt:message key="${key}" /></c:set>

<table align="left" cellpadding="0" cellspacing="0" border="0" width="96%">
<tr><td>
	<span class="error13"><c:out value="${chkKey}"/></span>
</td><tr>
</table>
</c:forEach>
</fmt:bundle>
</c:if>