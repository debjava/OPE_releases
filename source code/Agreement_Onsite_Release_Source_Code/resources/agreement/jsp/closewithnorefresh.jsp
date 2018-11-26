<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri="c.tld" prefix="c" %>
<%@ taglib uri="fmt.tld" prefix="fmt" %>


<html>
<head>
<script>
//window.opener.location.reload();
//window.opener.document.forms(2).submit(); 
//opener.close();
// window.close();

</script>
</head>
<body >

<script>

<c:if test="${requestScope.versionNo ne null}">

var vhName = opener.document.getElementById('vh').value;

opener.document.getElementById(vhName).value='<c:out value="${requestScope.versionNo}"/>';
</c:if>
window.close();
</script>
</body>
</html>
