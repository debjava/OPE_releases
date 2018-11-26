<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="errors.ErrorResources" />
<c:set var="language" value="${property}" />
<fmt:bundle basename="${language}">
<c:choose>
<c:when test="${not empty requestScope.commonErrorList}">
<c:forEach var="lst" items="${requestScope.commonErrorList}">
<head>
<style>
.error13 {
	width:340px;
	background:#fbc4c4;
	border:1px solid #e84c4c;
	color:#e70202;
	font:bold 11px Tahoma, Arial;
	padding:10px;
	text-align:center;
	top:-20%;
	left:3px;
	white-space:nowrap;
}
/*
.error13 .corner {
	width:16px;
	height:8px;	
	left:50%;
	bottom:409px;
	margin-left:-8px;
	background:url(../images/error-corn1.gif) 0 0 no-repeat;
}
*/
</style>
<div style="{height:70px;}">&nbsp;
</div>
<div class="error13">
<span>
<c:set var="key" value="${lst}"/>
<c:set var="chkKey"><fmt:message key="${key}" /></c:set>
<c:out value="${chkKey}"/>
<c:set var="keyTest" value="${fn:substring(chkKey, 0, 3)}"/>

</span>
 <p class="corner"></p>
 </div>

</c:forEach>
</c:when>
</c:choose>
</fmt:bundle>