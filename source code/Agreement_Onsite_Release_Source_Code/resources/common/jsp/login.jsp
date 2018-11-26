<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.Common_MessageResources" />
<c:set var="language" value="${property}" />
<c:set var="property1"  value="applicationconfig.installation" />
<c:set var="language1" value="${property1}_${sessionObj.languageId}" />
<head>
<fmt:bundle basename="${language}">
<title>DataMatrice - <fmt:message key="login"/></title>
</fmt:bundle>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href='<c:url value="/resources/common/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/common/css/login.css"/>' rel="stylesheet" type="text/css" />

<script type="text/javascript" src='<c:url value="/resources/common/js/jquery.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/common/js/ifixpng2.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/common/js/js.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/common/js/login.js"/>'></script>

<script>
function loginToForm(){
 document.getElementById('loaderImage').className='showLoader';
}
</script>

</head>
<fmt:bundle basename="${language}">
<body>
<div class="lg-header" title="DataMatrice">
  <h1 class="png">DataMatrice</h1>
  <h5><fmt:message key="version"/> <fmt:message key="release_version_data"/></h5>
</div>
 
<center>
<%@ include file="ErrorHandler.jsp" %>
</center>

<html:form action="/resources/common/jsp/Login" styleId="lg-form" styleClass="png" onsubmit="javascript:loginToForm();">

  <p class="hideLoader" id="loaderImage"></p>

  <label for="user_id"><fmt:message key="user_id"/>:</label>

  <html:text property="userId" styleId="userId" maxlength="10" size="10" onkeyup="javascript: this.value=this.value.toUpperCase();" 
					onblur="javascript: this.value=this.value.toUpperCase();"/>

  <label for="password"><fmt:message key="password"/>:</label>
  
  <html:password property="password" styleId="password" maxlength="10" size="10" />

  <button type="submit"><fmt:message key="login"/></button>

</html:form>

</body>
</fmt:bundle>
</html>
<script>
document.getElementById('userId').focus();
</script>
<script language="JavaScript"><!--
//javascript:window.history.forward(1);
window.history.go(+1);
//--></script>