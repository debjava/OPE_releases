<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/resources/agreement/calender/theme.css"/>'>

<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-en.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/checkdate.js"/>'></script>

<style type="text/css" media="screen">
	.hideonscreen {display: none;}
</style>

<style type="text/css" media="print">
	.hideonprint {display: none;}
</style>

<SCRIPT TYPE="text/javascript"> 
<!-- 
//Disable select-text script (IE4+, NS6+)
//visit http://www.rainbow.arch.scriptmania.com/scripts/ 
/////////////////////////////////// 
function disableselect(e){
return false
} 
function reEnable(){
return true
} 
//if IE4+
document.onselectstart=new Function ("return false") 
//if NS6
if (window.sidebar){
document.onmousedown=disableselect
document.onclick=reEnable
}
// -->
</SCRIPT>

<SCRIPT TYPE="text/javascript"> 
<!-- 
//Disable right click script 
//visit http://www.rainbow.arch.scriptmania.com/scripts/ 
var message="Sorry, right-click has been disabled"; 
/////////////////////////////////// 
function clickIE() {if (document.all) {(message);return false;}} 
function clickNS(e) {if 
(document.layers||(document.getElementById&&!document.all)) { 
if (e.which==2||e.which==3) {(message);return false;}}} 
if (document.layers) 
{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;} 
else{document.onmouseup=clickNS;document.oncontextmenu=clickIE;} 
document.oncontextmenu=new Function("return false") 
// --> 
</SCRIPT>

<script language="JavaScript">

function disableCtrlKeyCombination(e)
{
        var forbiddenKeys = new Array('a', 'n', 'c', 'x', 'v', 'j','h','e','b','d','f','i','k','l','m','n','o','p','q','r','s','t','u','v','z','g');
        var key;
        var isCtrl;
       
		if(window.event && window.event.keyCode == 114)
        {
			event.keyCode=0;
			return false;
		}
		if(window.event && window.event.keyCode == 116)
        {
			event.keyCode=0;
			return false;
		}
		if(window.event && window.event.keyCode == 122)
        {
			event.keyCode=0;
			return false;
		}
		if(window.event && window.event.keyCode == 117)
        {
			event.cancelBubble=true;
			event.keyCode=0;
			return false;
		}

        if(window.event)
        {
                key = window.event.keyCode;     //IE
		        if(window.event.ctrlKey)
                        isCtrl = true;
                else
                        isCtrl = false;
        }
        else
        {
                key = e.which;     //firefox
                if(e.ctrlKey)
                        isCtrl = true;
                else
                        isCtrl = false;
        }

        if(isCtrl)
        {
			    for(var i=0; i<forbiddenKeys.length; i++)
                {
                        //case-insensitive comparation
						if(forbiddenKeys[i].toLowerCase() == String.fromCharCode(key).toLowerCase())
                        {
                           event.keyCode=0;
                           return false;
                        }
                }
        }
		
        return true;
}
</script>

<title class="hideonprint"><fmt:message key="kek_part_2"/></title>

</HEAD>

<BODY onKeyPress="return disableCtrlKeyCombination(event);" onKeyDown="return disableCtrlKeyCombination(event);" class="shadowbody">
<html>

<%@ include file="common/JavascriptErrorMsg.jsp" %>
<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="kek_part_2"/></span></span></div></span>
</div>

<ul class="toolbar2">
			  <!-- Subnavigation -->			
			  
			  <li class="print">
			 <a onClick="window.print();" href="#"
			  onMouseOver="hover('a3','ahover2');" 
			  onMouseOut="hover('a3','ahover1');" id="a3" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
			  </li>
</ul>


 <c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
 </c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />
<div class="record_def">
<div id="bureau_new_scrollbar">

	<%@ include file="common/ErrorHandler.jsp" %>

<!-- 
     Changed the parameter according new requirement
     customer name
     generation date and
     control code
 -->
 
<table cellpadding="4" cellspacing="4" width="100%" >
	<tr>
		<td>
			<fieldset class="fieldset">
				<table cellpadding="4" cellspacing="4" width="100%" >

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="customers_name"/>: <c:out value="${requestScope.customerName2}"/></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="date"/>: <c:out value="${fn:substring(requestScope.generationDateTime,0,19)}"/></p></td>
				</tr>

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="control_code"/>: <c:out value="${requestScope.controlCode}"/></p></td>
				</tr>

			</table>
			</fieldset>
				</td>
			</tr>

			<ag:dropDown var="banks" scope="page" key="ACTIVE_BANKNAMES" />

			 <c:forEach items="${banks}" var="bank">
				 <c:set var="bankId" value="${bank.key}" />
				 <c:set var="bankName" value="${bank.value}" />
			 </c:forEach>	

			<tr>
				<td>
			
				<table cellpadding="3" cellspacing="2" width="100%">

				<tr>
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_name_or_identification"/>: <c:out value="${bankName}"/></p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: <c:out value="${bankId}"/></p></td>
				</tr>

				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="bank_id_or_code"/>: XXXXXXXXXXXXXXXXX</p></td>
				</tr>

				<tr class="hideonscreen">
					<td  valign="top"><p class="label_normal"><fmt:message key="key_generation_number"/>: <c:out value="${requestScope.generationNumber}"/></p></td>
				</tr>
				
				<tr class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="key_generation_number"/>: X</p></td>
				</tr>
				
				<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen"><p class="label_normal"><fmt:message key="part_2_of_kek"/>: <strong><c:out value="${requestScope.keyKEKPart2}"/></strong></p></td>
				</tr>

				<tr id="noprint" class="hideonprint">
					<td  valign="top"><p class="label_normal"><fmt:message key="part_2_of_kek"/>: XXXXXXXXXXXXXXXX</p></td>
				</tr>
				
					<tr id="print" class="hideonscreen">
					<td  valign="top" class="hideonscreen"><p class="label_normal"><fmt:message key="KVV"/>: <strong><c:out value="${requestScope.keyKVV}"/></strong></p></td>
				</tr>

				<tr id="noprint" class="hideonprint">
					<td  valign="top" ><p class="label_normal"><fmt:message key="KVV"/>: XXXXXX</p></td>
				</tr>
				
				<tr id="noprint" class="hideonprint">
					<td  valign="top">&nbsp;</td>
				</tr>
			</table>			
		</td>
	</tr>
</table>
</div>
</table>
</div>
</div>
</html>
</BODY>
</fmt:bundle>
</HTML>