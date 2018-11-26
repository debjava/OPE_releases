<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>
<script type="text/javascript" src="../js/search.js"></script>

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

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
					<div id="content"><jsp:include page="agreementdisplay.jsp"/>
						<!--<div id="content-loader"></div>-->
					</div>
		</td>
	</tr>
</table>
</div>
</body>
</html>
<script>
addTab('Agreement','<c:url value="/resources/agreement/jsp/main.jsp"/>');
</script>