<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/common/js/Common1.js"/>'></script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>

function actions(status)
{
		if(status=="click")
		{	if(confirm('Do you wish to exit?')==true)
			{			
				document.successform.action="Logout.do";
				
				document.successform.submit();
			}
			else
				return false;
		} else if(event.clientY < 0) {	
			document.successform.action="Logout.do";
			closeWindows();
			document.successform.submit();
			event.returnValue ='You have logged out. Do you wish to close the window?';
		}
}

function openSupport(){
  	alert('Link will be available shortly.');
}

function openHelp(){
  	alert('Link will be available shortly.');
}

</script>
<script language="JavaScript"><!--
//javascript:window.history.forward(1);
window.history.go(+1);
//--></script>
</head>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.Common_MessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="property1"  value="applicationconfig.installation" />
<c:set var="language1" value="${property1}_${sessionObj.languageId}" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DataMatrice</title>
<fmt:bundle basename="${language}">
<form name="successform">
  <div class="header">
    <div class="logo">
      <h1 class="png" title="DataMatrice">DataMatrice</h1>
      <h5><fmt:message key="version"/> <fmt:bundle basename="${language1}"><fmt:message key="release_version_data"/></fmt:bundle></h5>
    </div>
    <div class="toolbar"> <a href="#" onclick="javascript: openSupport();" class="but-support" title="<fmt:message key="support"/>"><fmt:message key="support"/></a><a href="#" onclick="javascript: openHelp();" class="but-help" title="<fmt:message key="help"/>"><fmt:message key="help"/></a><a href="#" onClick="actions('click')" class="but-exit" title="<fmt:message key="exit"/>"><fmt:message key="exit"/></a> </div>
  </div>
</form>
</fmt:bundle>