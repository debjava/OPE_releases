<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>

<%@ taglib uri="fmt.tld" prefix="fmt" %>
<%@ taglib uri="agreement.tld" prefix="ag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:setLocale value="${sessionObj.languageId}"/>

<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">
<HTML>
<HEAD>
<link href="../css/agreement_style.css" rel="stylesheet" type="text/css">
<script language="javascript" type="text/javascript" src='<c:url value="/js/agreement1.js"/>'></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/calender/theme.css"/>'>

<script src='<c:url value="/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/calender/calendar-en.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src="../js/checkdate.js"></script>

<script language="javascript" type="text/javascript" src="../js/ope_ajax.js"></script>

<script>
//function added to display kek and auk values

function putAllKeys(){
	    opener.document.getElementById('keyKEKPart1').value='<c:out value="${param.keyKEKPart1}"/>';
	    opener.document.getElementById('keyKEKPart2').value='<c:out value="${param.keyKEKPart2}"/>';
	    opener.document.getElementById('keyKEK').value='<c:out value="${param.keyKEK}"/>';
	    opener.document.getElementById('keyAUK').value='<c:out value="${param.keyAUK}"/>';
	    opener.document.getElementById('keyKVV').value='<c:out value="${param.keyKVV}"/>';
}

</script>
<title>KEK Keys</title>

</HEAD>

<BODY>
<html>

<%@ include file="/jsp/common/JavascriptErrorMsg.jsp" %>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="20" class="title_bg"><table width="100%" border="0" cellspacing="0" cellpadding="0">
         <tr>
          <td width="100%"  ><p class="title_font">KEK Keys</p></td>
         </tr>
		 </table>
		 </td>
        
      </tr>
</table>

<table width="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr >
     <td >
	    <table width="100%" height="21" class="sub_menu_cont_bg" border="0" cellspacing="0" cellpadding="0">
	     <tr>
		   <td><ul id="sub_menu_cont">
			  <!-- Subnavigation -->			  

			   <li><a onClick="putAllKeys();window.close();"
			  onMouseOver="hover('a3','ahover2');" 
			  onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="ok"/>"><fmt:message key="ok"/></a></li>

			  <li><a onClick="window.close();"
			  onMouseOver="hover('a4','ahover2');" 
			  onMouseOut="hover('a4','ahover1');" id="a4" title="<fmt:message key="cancel"/>"><fmt:message key="cancel"/></a></li>

			</ul></td>
         </tr>
        </table>
     </td>
  </tr>
</table>

 <c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />

<div id="bureau_new_scrollbar">

	<%@ include file="/jsp/common/ErrorHandler.jsp" %>

<table cellpadding="0" cellspacing="4" width="100%">
	<tr>
		<td>
			<fieldset><legend class="clsLegendLabel">KEK Part 1</legend>
				<table cellpadding="4" cellspacing="0" width="100%">
				<tr>
					<td  valign="top"><p class="label_normal">Branch Code: <c:out value="${sessionObj.divisionId}"/></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal">Key Generation Number: <c:out value="${param.generationNumber}"/></p></td>
				</tr>				

				<tr>
					<td  valign="top"><p class="label_normal">Part 1 of KEK: <c:out value="${param.keyKEKPart1}"/></p></td>
				</tr>

			</table>
			</fieldset>
		</td>
		<td width="2px">&nbsp;</td>
		<td>
			<fieldset><legend class="clsLegendLabel">KEK Part 2</legend>
				<table cellpadding="4" cellspacing="0" width="100%">
				<tr>
					<td  valign="top"><p class="label_normal">Branch Code: <c:out value="${sessionObj.divisionId}"/></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal">Key Generation Number: <c:out value="${param.generationNumber}"/></p></td>
				</tr>				

				<tr>
					<td  valign="top"><p class="label_normal">Part 2 of KEK: <c:out value="${param.keyKEKPart2}"/></p></td>
				</tr>				
			</table>
			</fieldset>
		</td>

	</tr>
</table>

</table>
</div>
</html>
</BODY>
</fmt:bundle>
</HTML>