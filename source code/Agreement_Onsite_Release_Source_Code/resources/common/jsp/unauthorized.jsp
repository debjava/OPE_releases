<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href='<c:url value="/css/agreement_style.css"/>' rel="stylesheet" type="text/css" />

<title>Unauthorized Page</title>

<script language="javascript">
	function callHome()
	{
		location.href='<c:url value="/main.jsp"/>';
	}
</script>
</head>

<BODY>
<br><br>
<p class="label_normal" align="center"><br>You are not authorized to view this page.<br>
<br><a href="javascript:callHome();">Please click here for home page</a></p>
</BODY>
</HTML>