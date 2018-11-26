<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="language_id" value="${sessionObj.languageId}" />
<fmt:setLocale value="${sessionObj.languageId}"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DataMatrice</title>

</head>
<body>

<div class="all">
<jsp:include page="../../common/jsp/top.jsp"/>

<table class="container">
	<tr>
		<td class="sidebar">
			<jsp:include page="../../common/jsp/leftpanel.jsp"/> 
		</td>

		<td class="tabs-container">
			<div class="tabber">
				<div class="tab-area"></div>             
			</div>       
			<div class="tab-arrows"><a href="#" class="left">&larr;</a> <a href="#" class="right">&rarr;</a></div>
				<div class="tab-left-frag"></div>        
					<div id="content"><jsp:include page="filetransferauditsearch.jsp"/>
						<!--<div id="content-loader"></div>-->
					</div>
		</td>
	</tr>
</table>
</div>
</body>
</html>
<fmt:bundle basename="${language}">
<script>
	addTab('<fmt:message key="file_audit"/>','File Transfer Audit');
</script>
</fmt:bundle>