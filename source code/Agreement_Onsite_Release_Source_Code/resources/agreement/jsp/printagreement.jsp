<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>
<%@ taglib uri="agreement.tld" prefix="ag" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:requestEncoding value="UTF-8"/>
<html xmlns="http://www.w3.org/1999/xhtml">

<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="language_id" value="${sessionObj.languageId}" />

<html>

<fmt:bundle basename="${language}">
<head>
<title class="hideonprint"></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href='<c:url value="/resources/agreement/css/reset.css"/>' rel="stylesheet" type="text/css" />
<link href='<c:url value="/resources/agreement/css/dm.css"/>' rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement1.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ifixpng2.js"></script>
<script type="text/javascript" src="../js/js.js"></script>
<script type="text/javascript" src="../js/dm.js"></script>

<link type="text/css" rel="stylesheet" href='<c:url value="/calender/theme.css"/>'>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-${language_id}.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/calendar-setup.js"/>' type="text/javascript" language="javascript"></script>
<script src='<c:url value="/resources/agreement/calender/date.js"/>' type="text/javascript" language="javascript"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/ope_ajax.js"/>'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/checkdate.js"/>'></script>
<html:xhtml/>
<html:base/>
</head>
<script>
function Printing(){
	   var agree=confirm('<fmt:message key="confirm_msg"/>');
	   if (agree)
	   {
			window.print();
	   }else
        return false;
	
}
</script>
</fmt:bundle>

<style type="text/css" media="screen">
	.hideonscreen {display: none;}
</style>

<style type="text/css" media="print">
	.hideonprint {display: none;}
	.popup_shadow {display: none;}
	#a5 {display: none;}
	#a4 {display: none;}
	#dd {display: none;}
	#dd1 {display: none;}
</style>

<body class="shadowbody">
<fmt:bundle basename="${language}">

<div class="popup"><span class="popup_shadow">
	<div class="popup_title" ><span class="popup_text"><fmt:message key="agreement"/></span></span></div></span>
</div>

<ul class="toolbar2">                     
 <li class="print">
	<a onClick="Printing()" onMouseOver="hover('a5','textOn');" onMouseOut="hover('a5','textOff');" tabindex="1"
	onkeypress="methodCallAfterEnterKey('Printing');"
	id="a5" title="<fmt:message key="print"/>"><fmt:message key="print"/></a>
 </li>
</ul>
		 
</fmt:bundle>
<fmt:bundle basename="${language}">
<div class="record-def">
<c:choose>
<c:when test="${not empty requestScope.agmtDetails}">
<!-- Begin Edit Deaprtment -->

<c:forEach items='${agmtDetails}' var='agmtList'>  
<table WIDTH="100%">
		<tr>
			<td width="40%"><p class="label_bold"><fmt:message key="bank_dnb_nord"/></P></td>
		</tr>
		<tr>
			<td><p class="label_normal"><fmt:message key="finland_branch"/></P></td>
		</tr>
		<tr>
			<td><p class="label_normal"><fmt:message key="agreement_no"/>: ${agmtList.internalAgreementId}</P></td>
			<td ><p class="label_normal"><fmt:message key="valid_from"/>: <fmt:formatDate pattern="dd.MM.yyyy" value="${agmtList.validFrom}"/></p></td>
			
			<c:if test="${agmtList.validTo ne null}"> 
				<td ><p class="label_normal"><fmt:message key="valid_to"/>: <fmt:formatDate pattern="dd.MM.yyyy" value="${agmtList.validTo}"/></p></td>
			</c:if>
		</tr>
</table>
<br><br><BR>
<table width="100%">
	<tr><td><p class="label_bold"><fmt:message key="agreement_print_header"/> - <a href="http://www.dnbnord.com" target="_blank" title="Click Here"><fmt:message key="dnb_nord"/></a></p></td></tr>
</table>
<br>
<table width="100%" border="0">
<tr>
   <td width="26%"><p class="label_bold"><fmt:message key="customer"/></p></td>
   <td><p class="label_normal"></p></td>
</tr>

<%-- Display of primary contact or customer name --%>
<tr>
   <td >&nbsp;</td>
   <!-- <td><p class="label_normal">${agmtList.agreementTitle}</p></td> -->
	<td><p class="label_normal">${agmtList.primaryContact}</p></td>
</tr>
<%-- Display of customer address --%>
<tr>
   <td >&nbsp;</td>
   <!-- <td><p class="label_normal">${agmtList.internalAgreementId}</p></td> -->
   <td><p class="label_normal">${agmtList.streetAddress}</p></td>
</tr>
<%-- Display of zip code and the city, both in one line --%>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal">${agmtList.zipCode} , ${agmtList.city}</p></td>
</tr>
<%-- Display of country name --%>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal">
   <ag:dropDown var="countries" scope="page" key="COUNTRY" />
    <c:forEach items="${countries}" var="country1">
	  <c:if test="${agmtList.country==country1.key}"><c:out value="${country1.value}"/></c:if>
	</c:forEach>
	</p></td>
</tr>
<%-- Display of agreement title --%>
<tr>
	<td >&nbsp;</td>
	<td><p class="label_normal">${agmtList.agreementTitle}</p></td>
</tr>

<%-- Display of email adress --%>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal">${agmtList.email}</p></td>
</tr>

<%-- Display of telephone number --%>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal">${agmtList.telephone}</p></td>
</tr>


</table>
<br>
<table width="100%" border="0">
<tr>
   <td width="26%"><p class="label_bold"><fmt:message key="bank"/></p></td>
   <td><p class="label_normal"><fmt:message key="bank_add1"/></p></td>
</tr>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal"><fmt:message key="bank_add2"/></p></td>
</tr>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal"><fmt:message key="bank_add3"/></p></td>
</tr>
<tr>
   <td >&nbsp;</td>
   <td><p class="label_normal"><fmt:message key="bank_add4"/></p></td>
</tr>
   
</table>

<!-- file trans service -->
<table width="100%">
<tr><td><p class="label_bold"><fmt:message key="file_trans_service"/></p></td></tr>
<tr>
   <td>
      <table width="99%" border="0">
		
		 <tr>
		      <td width="4%">&nbsp;</td>
			  <td width="22%"><p class="label_normal"><fmt:message key="accounts"/>:</p></td>
			  <td >&nbsp;</td>
         </tr>
		 <c:forEach items='${serviceAccounts}' var='accountList'> 
		  <tr>
		      <td>&nbsp;</td>
			  <td>&nbsp;</td>
			  <td><p class="label_normal">${accountList.accountNum} </p></td>             			
		 </tr>
		 </c:forEach>
		 
		 <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td><p class="label_normal"><fmt:message key="auto_services"/>:</p></td>
		 </tr>
		 <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td><p class="label_normal"><fmt:message key="balance_transactions_queries"/></p></td>
		 </tr>
		 <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td><p class="label_normal"><fmt:message key="real_time_transfers_between_accounts"/></p></td>
		 </tr>
         <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    <td><p class="label_normal"><fmt:message key="fx_rates"/></p></td>
		 </tr>
	  </table>
   </td>
</tr>
</table>
<table width="100%" >
<tr>
   <td>
       <table width="99%" border="0">
	   <tr>
		   <td>&nbsp;</td>
		   <td><p class="label_bold"><fmt:message key="user_ids"/></p></td>
		   <td>&nbsp;</td>
		   <td>&nbsp;</td>
	   </tr> 
	   <tr>
	      <td width="4%">&nbsp;</td>
	      <!-- <td width="22%"><p class="label_normal"><fmt:message key="customer_id"/></p></td> -->
		  <td width="23%"><p class="label_normal"><fmt:message key="bank_internal_customer_id"/></p></td>
	      <td width="22%"><p class="label_normal"><fmt:message key="user_id"/></p></td>
	      <td width="22%"><p class="label_normal"><fmt:message key="user"/></p></td>
	      <td width="22%"><p class="label_normal"><fmt:message key="service_id"/></p></td>
	   </tr>
       
	   <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
            <c:forEach items='${agmtMapDetails}' var='customerlist'> 
               <c:if test="${customerlist.internalRefId eq serviceSpList.internalRefId}">
					<c:forEach items='${serviceBureaus}' var='bureaulist'>
						<c:if test="${bureaulist.internalBureauId eq serviceSpList.bureauId}"> 
							<tr>
								  <td width="4%">&nbsp;</td>
								  <td><p class="label_normal">${customerlist.customerId}</p></td>
								  <td width="24%"><p class="label_normal">${bureaulist.patuId}</p></td>
								  <td><p class="label_normal">${customerlist.customerName}</p></td>
								  <td><p class="label_normal">${serviceSpList.serviceId}</p></td>
							</tr>
						</c:if>
                    </c:forEach>
				</c:if>
			</c:forEach>
	  </c:forEach>
	 
	   </table>
   </td>
</tr>

<tr>
   <td>
       <table width="99%" border="0">
        <tr>
		   <td>&nbsp;</td>
		   <td><p class="label_bold"><fmt:message key="patu_keys"/></p></td>
		   <td>&nbsp;</td>
		   <td>&nbsp;</td>
	   </tr> 
		
		<tr/><tr/>
	  	

	   <c:forEach items='${kekAuk}' var='keylist'> 
			
			<c:forEach items='${serviceBureaus}' var='bureaulist' varStatus='status'> 
				<c:if test="${bureaulist.internalBureauId eq keylist.internalBureauId}">
				
				  <tr>
					  <td width="4%">&nbsp;</td>
					  <td><p class="label_normal"><fmt:message key="service_bureau_name"/> : </p></td>
					  <td>${bureaulist.bureauName}</td>
				   </tr>

				   <tr>
						<td width="4%">&nbsp;</td>
						<td width="22%"><p class="label_bold"><fmt:message key="part_1"/></p></td>
				   </tr>

				   <%-- Display contact person for first person --%>
				   <tr>
					  <td width="4%">&nbsp;</td>
					  <td width="30%"><p class="label_normal">${bureaulist.contactPerson1}<fmt:message key="contact_person_1"/></p></td>
					  <td>&nbsp;</td>
				   </tr>

				   <%-- Display Telephone number for first person--%>
				   <tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal">${bureaulist.telephoneNo1}</p></td>
					   <td>&nbsp;</td>
				   </tr>

				   <%-- Display pincode1 and city for first person--%>
				   <tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal">${bureaulist.pinCode1} , ${bureaulist.city1}</p></td>
					   <td>&nbsp;</td>
				   </tr>

				   <tr>
						<td width="4%">&nbsp;</td>
						<td width="22%"><p class="label_bold"><fmt:message key="part_2"/></p></td>
				   </tr>
				  <%-- Display contact person for first person --%>
				   <tr>
					  <td width="4%">&nbsp;</td>
					  <td width="30%"><p class="label_normal">${bureaulist.contactPerson2}<fmt:message key="contact_person_2"/></p></td>
					  <td>&nbsp;</td>
				   </tr>

				   <%-- Display Telephone number for first person--%>
				   <tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal">${bureaulist.telephoneNo2}</p></td>
					   <td>&nbsp;</td>
				   </tr>

				   <%-- Display pincode1 and city for first person--%>
				   <tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal">${bureaulist.pinCode2} , ${bureaulist.city2}</p></td>
					   <td>&nbsp;</td>
				   </tr>
				
<%--
				  <c:choose>
						<c:when test="${status.count eq '1'}">
							<tr>
								<td width="4%">&nbsp;</td>
								<td width="22%"><p class="label_bold"><fmt:message key="part_1"/></p></td>
							</tr>
						</c:when>
						<c:otherwise>		
							<tr>
								<td width="4%">&nbsp;</td>
								<td width="22%"><p class="label_bold"><fmt:message key="part_2"/></p></td>
							</tr>					
						</c:otherwise>
				</c:choose>
--%>
					  
				   <tr>
					  <td width="4%">&nbsp;</td>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
					  <td>&nbsp;</td>
				   </tr>
			   </c:if>
			   
		    </c:forEach>
	   </c:forEach>
       </table>
   </td>
</tr>

</table>


<!-- services -->
<c:forEach items='${services}' var='serviceList'> 
<br>
<table width="100%">
<tr><td><p class="label_bold"><fmt:message key="${serviceList.serviceCode}"/></p></td></tr>
<tr>
   <td>
      <table width="99%" border="0">
		
		<!-- added by Anant -->

		 <c:if test="${serviceList.serviceCode ne 'S010'}">
		 <tr>
		      <td width="4%">&nbsp;</td>
			  <td width="22%"><p class="label_normal"><fmt:message key="accounts"/>:</p></td>
			  <td >&nbsp;</td>
         </tr>
		
		
		 <c:forEach items='${accountsList}' var='accountList1' varStatus='status'>
			<c:if test="${serviceList.serviceCode eq accountList1.serviceCode}">
			<tr>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td><p class="label_normal">${accountList1.accountNum}</p></td>             			
			</tr>
			</c:if>
		 </c:forEach>
        </c:if>
<%-- 
         <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
			 <c:if test="${serviceSpList.serviceCode eq serviceList.serviceCode}">
				 <c:forEach items='${serviceAccounts}' var='accountList' varStatus='status'>
					<c:if test="${serviceSpList.internalRefId eq accountList.internalRefId}">
					  <tr>
						  <td>&nbsp;</td>
						  <td>&nbsp;</td>
						  <td><p class="label_normal">${accountList.accountNum} </p></td>             			
					 </tr>
                    </c:if>
				 </c:forEach>
			 </c:if>
		</c:forEach>	
--%>
	  </table>
   </td>
</tr>
</table>

<!--
	 Display service ids only for the incoming services
	 like LMP300,TS,LUM2
-->

<table width="100%" >
<tr>
   <td>

       <table width="99%" border="0">

		<c:choose>
		<c:when test="${serviceList.serviceCode eq 'S001'}">
				<%-- For Domestic Bill Payment(LMP300) --%>
				<tr>
				   <td width="4%">&nbsp;</td>
				   <td width="22%"><p class="label_normal"><fmt:message key="service_ids"/></p></td>
				   <td>&nbsp;</td>
				   <td>&nbsp;</td>
			   </tr> 
			   <tr>
				  <td width="4%">&nbsp;</td>
				  <td><p class="label_normal"></p></td>
				  <td width="22%"><p class="label_normal"><fmt:message key="service_id"/></p></td>
				  <!-- <td><p class="label_normal"><fmt:message key="service_bureau_name"/></p></td> -->
				  <td><p class="label_normal"><fmt:message key="customer_name"/></p></td>
				  
			   </tr>
			   <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
					 <c:if test="${serviceSpList.serviceCode eq serviceList.serviceCode}">
					   <tr>
						  <td >&nbsp;</td>
						  <td><p class="label_normal"></p></td>
						  <td><p class="label_normal">${serviceSpList.serviceId}</p></td>
						  <td><p class="label_normal">${serviceSpList.bureauName}</p></td>     
					   </tr>
					 </c:if>
			   </c:forEach>
			   <tr></tr>
			   <tr>
				   <td width="4%">&nbsp;</td>
				   <td colspan="3"><p class="label_normal">
				   
				   <fmt:message key="balance_check_on_due_date"/>

				   <c:choose>
					  <c:when test="${serviceList.serviceCode eq 'S003'} ">
						<fmt:message key="balance_check_perform_one_day_before_due_date"/> 	
					  </c:when>

					  <c:when test="${serviceList.serviceCode eq 'S001'} ">
						<fmt:message key="balance_check_on_due_date"/>
					  </c:when>

					  <c:when test="${serviceList.serviceCode eq 'S005'} ">
						<fmt:message key="balance_check_on_due_date"/>
					  </c:when>

					  <c:otherwise>
						<!-- <fmt:message key="balance_check_on_due_date"/> -->				
					  </c:otherwise>
					 </c:choose>
					 
				   </p></td>
			   </tr>

		</c:when>
		<%-- End of printing Service Ids for Domestic Bill Payment (LMP300) --%>
		<%-- For Salary Transfer(TS) --%>
		<c:when test="${serviceList.serviceCode eq 'S003'}">

		<tr>
		   <td width="4%">&nbsp;</td>
		   <td width="22%"><p class="label_normal"><fmt:message key="service_ids"/></p></td>
		   <td>&nbsp;</td>
		   <td>&nbsp;</td>
	   </tr> 
	   <tr>
	      <td width="4%">&nbsp;</td>
		  <td><p class="label_normal"></p></td>
	      <td width="22%"><p class="label_normal"><fmt:message key="service_id"/></p></td>
		  <!-- <td><p class="label_normal"><fmt:message key="service_bureau_name"/></p></td> -->
		  <td><p class="label_normal"><fmt:message key="customer_name"/></p></td>
	      
	   </tr>
       <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
			 <c:if test="${serviceSpList.serviceCode eq serviceList.serviceCode}">
			   <tr>
				  <td >&nbsp;</td>
				  <td><p class="label_normal"></p></td>
				  <td><p class="label_normal">${serviceSpList.serviceId}</p></td>
				  <td><p class="label_normal">${serviceSpList.bureauName}</p></td>     
			   </tr>
			 </c:if>
	   </c:forEach>
	   <tr></tr>
	   <tr>
	       <td width="4%">&nbsp;</td>
		   <td colspan="3"><p class="label_normal">
		  
		   
		   <c:choose>
			  <c:when test="${serviceList.serviceCode eq 'S003'}">
				<fmt:message key="balance_check_perform_one_day_before_due_date"/> 	
			  </c:when>

			  <c:when test="${serviceList.serviceCode eq 'S001'} ">
					<fmt:message key="balance_check_on_due_date"/>
			  </c:when>

			  <c:when test="${serviceList.serviceCode eq 'S005'} ">
					<fmt:message key="balance_check_on_due_date"/>
			  </c:when>

			  <c:otherwise>
				<!-- <fmt:message key="balance_check_on_due_date"/> -->				
			  </c:otherwise>
			 </c:choose>
			
		   </p></td>
	   </tr>

		</c:when>
		<%-- End of printing Service Ids for Cross Border Payment(LUM2) --%>
		<%-- For Cross Border Payment(LUM2) --%>

		<c:when test="${serviceList.serviceCode eq 'S005'}">

		<tr>
		   <td width="4%">&nbsp;</td>
		   <td width="22%"><p class="label_normal"><fmt:message key="service_ids"/></p></td>
		   <td>&nbsp;</td>
		   <td>&nbsp;</td>
	   </tr> 
	   <tr>
	      <td width="4%">&nbsp;</td>
		  <td><p class="label_normal"></p></td>
	      <td width="22%"><p class="label_normal"><fmt:message key="service_id"/></p></td>
		 <!-- <td><p class="label_normal"><fmt:message key="service_bureau_name"/></p></td> -->
		<td><p class="label_normal"><fmt:message key="customer_name"/></p></td>
	      
	   </tr>
       <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
			 <c:if test="${serviceSpList.serviceCode eq serviceList.serviceCode}">
			   <tr>
				  <td >&nbsp;</td>
				  <td><p class="label_normal"></p></td>
				  <td><p class="label_normal">${serviceSpList.serviceId}</p></td>
				  <td><p class="label_normal">${serviceSpList.bureauName}</p></td>     
			   </tr>
			 </c:if>
	   </c:forEach>
	   <tr></tr>
	   <tr>
	       <td width="4%">&nbsp;</td>
		   <td colspan="3"><p class="label_normal">  
		   <c:choose>
			  <c:when test="${serviceList.serviceCode eq 'S003'}">
				<fmt:message key="balance_check_perform_one_day_before_due_date"/> 	
			  </c:when>

			  <c:when test="${serviceList.serviceCode eq 'S001'} ">
					<fmt:message key="balance_check_on_due_date"/>
			  </c:when>

			  <c:when test="${serviceList.serviceCode eq 'S005'}">
				<fmt:message key="balance_check_on_due_date"/> 	
			  </c:when>

			  <c:otherwise>
				<!-- <fmt:message key="balance_check_on_due_date"/> -->				
			  </c:otherwise>
			 </c:choose>
		   </p></td>
	   </tr>

		</c:when>
		<%-- End of printing Service Ids for Salary Transfer(TS) --%>
		<c:otherwise>		
							
		</c:otherwise>
	</c:choose>
	<!-- Modified as per the new change request -->
	   </table>


   </td>
</tr>


<tr>
	<td>
	   <table width="99%" border="0">
	   <tr>
		   <td width="4%">&nbsp;</td>
			
		  <c:choose>
				<c:when test="${serviceList.serviceCode eq 'S001'}">
					<td width="22%"><p class="label_normal"><fmt:message key="sender_of_payment"/></p></td> 	
				</c:when>
				<c:when test="${serviceList.serviceCode eq 'S003'}">
					<td width="22%"><p class="label_normal"><fmt:message key="sender_of_payment"/></p></td> 	
				</c:when>
				<c:when test="${serviceList.serviceCode eq 'S005'}">
					<td width="22%"><p class="label_normal"><fmt:message key="sender_of_payment"/></p></td> 	
				</c:when>

				<c:otherwise>
					<td width="25%"><p class="label_normal"><fmt:message key="retriever_of_file"/></p></td>				
				</c:otherwise>

		   </c:choose>
		   <!-- <td width="22%"><p class="label_normal"><fmt:message key="sender_of_payment"/></p></td> -->
		   <td>&nbsp;</td>
		   <td>&nbsp;</td>
	   </tr> 
	   <c:forEach items='${serviceSpecifications}' var='serviceSpList'> 
			 <c:if test="${serviceSpList.serviceCode eq serviceList.serviceCode}">
			   <c:forEach items='${serviceBureaus}' var='bureauList'>
				   <c:if test="${bureauList.internalBureauId eq serviceSpList.bureauId}">
				   <tr>
					  <td width="4%">&nbsp;</td>
					  <td></td>
					  <td><p class="label_normal">${bureauList.bureauId},${bureauList.bureauName}, ${bureauList.telephoneNo1}, ${bureauList.contactPerson1}</p></td>
					  <td><p class="label_normal"></p></td>
				   </tr>
				   </c:if>
			   </c:forEach>
			 </c:if>
		</c:forEach>
	   </table>
	</td>
</tr>

<tr>
	<td>
		<!-- Modified as per the new change request -->
		<!-- Display of back reporting for the services LMP300,TS and LUM2 -->
		<c:choose>

			<c:when test="${serviceList.serviceCode eq 'S001'}" >
					<table width="99%" border="0">
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td width="22%"><p class="label_normal"><fmt:message key="back_reporting"/></p></td>
					   <td>&nbsp;</td>
					   <td>&nbsp;</td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="executed_payments"/></p></td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="payments_pending_report"/></p></td>
					</tr>
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="erro_report"/></p></td>
					</tr>
				   </table>
			</c:when>

			<c:when test="${serviceList.serviceCode eq 'S003'}" >
					<table width="99%" border="0">
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td width="22%"><p class="label_normal"><fmt:message key="back_reporting"/></p></td>
					   <td>&nbsp;</td>
					   <td>&nbsp;</td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="executed_payments"/></p></td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="payments_pending_report"/></p></td>
					</tr>
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="erro_report"/></p></td>
					</tr>
				   </table>
			</c:when>

			<c:when test="${serviceList.serviceCode eq 'S005'}" >

			<!-- if (VLU2) service, is slected in this agreement, then only following message is displying in the (LUM2) -->
			 <c:set var="variable">NO</c:set>
			 <c:forEach items='${services}' var='serviceList'>
				<c:if test="${serviceList.serviceCode eq 'S002'}">
					<c:set var="variable">YES</c:set>
				</c:if>
			 </c:forEach>
			
			<c:if test="${variable eq 'YES'}">
					<table width="99%" border="0">
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td width="22%"><p class="label_normal"><fmt:message key="back_reporting"/></p></td>
					   <td>&nbsp;</td>
					   <td>&nbsp;</td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="executed_payments"/></p></td>
					</tr> 
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="payments_pending_report"/></p></td>
					</tr>
					<tr>
					   <td width="4%">&nbsp;</td>
					   <td><p class="label_normal"></p></td>
					   <td colspan="2"><p class="label_normal"><fmt:message key="erro_report"/></p></td>
					</tr>
				   </table>
			</c:if>

			</c:when>
			<c:otherwise>

			</c:otherwise>

		</c:choose>
		
	</td>
</tr>

</table>

</c:forEach>
<!-- services -->

<br>
<table width="99%" border="0">
<tr>
    <td width="4%">&nbsp;</td>
    <td width="22%" ><p class="label_bold"><fmt:message key="dnb_nord_ip"/></p></td>
	<td><p class="label_normal"><fmt:message key="ip_address"/></p></td>
</tr>
</table>

<br>

<table width="100%" border="0">
<tr>
	<td><p class="label_bold_small"><fmt:message key="acceptance_of_risks_relating_to_use_of_internet_connection"/><br>
	    <fmt:message key="bank_hereafter"/></p>
    </td>
</tr>
</table>

<table width="100%" border="0">
<tr>
    <td width="4%"></td>
	<td><p class="label_normal_small" >
		<fmt:message key="description"/>
        </p>
        <br>
		<p class="label_normal">
		<fmt:message key="parties_to_agreement_accept_the_terms_of_this_agreement"/></p>
		<p class="label_normal"><fmt:message key="undertake_to_follow"/></p>
        <br>
        
		<p></p>
		<c:set var="currentDate" value="<%=new java.util.Date()%>" />
        <p class="label_normal_italic"><fmt:message key="helsinki_date"/> : <fmt:formatDate value="${currentDate}"  pattern="yyyy-MM-dd" /></p>
        
		<p></p>
		<br>
		<br>
        <p class="label_normal"><fmt:message key="customer_signature"/> : ${agmtList.primaryContact}</p>
        <p></p>
		
		<br>
		<br>
		<!-- Do not display any line -->
		<!-- <HR class="hr1" align="left" size="1"> -->

		<p></p>
		
		<br>
        <p class="label_normal"><fmt:message key="bank_signature"/></p>
		<br>
        <p class="label_bold"><fmt:message key="bank_dnb_nord"/>, <fmt:message key="finland_branch"/></p>
		
		<br><br>
		<!-- Do not display any line -->
		<!-- <HR class="hr1" align="left" size="1"> -->
		
   </td>
</tr>
</table>
<br><br>
<table width="100%">
<tr>
	<td><p class="label_bold"><fmt:message key="enclosures"/></p></td>
	<td><p class="label_bold"></p></td>
</tr>

<!-- Display of enclosures -->
<tr>
	<td>&nbsp;</td>
	<td><p class="label_bold">1. <fmt:message key="general_terms_of_ope_agreement"/></p></td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><p class="label_bold">2. <fmt:message key="general_terms_and_condn_for_lum2"/></p></td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><p class="label_bold">3. <fmt:message key="general_terms_and_condn_within_finland"/></p></td>
</tr>

</table>
<br><br>
<table width="100%">
<tr>
	<td><p class="label_normal_very_small" >
	<fmt:message key="footer_address1"/><br>
    <fmt:message key="footer_address2"/><br>
    <fmt:message key="footer_address3"/></p></td>
</tr>
</table>
<br><br>

</c:forEach>
</c:when>
<c:otherwise>
<fmt:bundle basename="errors.AgreementErrorResources_${sessionObj.languageId}">
  <p class="login_userpass"><fmt:message key="ERRAG-003"/></p>
</fmt:bundle>
<script>
    document.getElementById('a5').disabled="true";
</script>
</c:otherwise>
</c:choose>

</fmt:bundle>

<!-- code added for floating print and close links-->
<fmt:bundle basename="${language}">
<script>
if (!document.layers)
document.write('<div id="divStayTopLeft" style="position:absolute">')
</script>

<layer id="divStayTopLeft" >

<!--EDIT BELOW CODE TO YOUR OWN MENU-->
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td >
      <p align="left" class="label_bold"> <a onclick="Printing();" id="dd" onMouseOver="hover('dd','textOn');"  onMouseOut="hover('dd','textOff');" title="<fmt:message key="print"/>"><u><fmt:message key="print"/></u></p></a><br>
    </td>
	<td >&nbsp;&nbsp;</td>
	<td >
      <p align="left" class="label_bold"> <a  onclick="javascript:window.close();" id="dd1" onMouseOver="hover('dd1','textOn');"  onMouseOut="hover('dd1','textOff');" title="<fmt:message key="close"/>"><u><fmt:message key="close"/></u></p></a><br>
    </td>
  </tr>
</table>
<!--END OF EDIT-->

</layer>
<script type="text/javascript">

/*
Floating Menu script-  Roy Whittle (http://www.javascript-fx.com/)
Script featured on/available at http://www.dynamicdrive.com/
This notice must stay intact for use
*/

var verticalpos="frombottom"

if (!document.layers)
document.write('</div>')

function JSFX_FloatTopDiv()
{
	var startX = 595,
	startY = 40;
	var ns = (navigator.appName.indexOf("Netscape") != -1);
	var d = document;
	function ml(id)
	{
		var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
		if(d.layers)el.style=el;
		el.sP=function(x,y){this.style.left=x;this.style.top=y;};
		el.x = startX;
		if (verticalpos=="fromtop")
		el.y = startY;
		else{
		el.y = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		el.y -= startY;
	}
		return el;
	}
	window.stayTopLeft=function()
	{
		if (verticalpos=="fromtop"){
		var pY = ns ? pageYOffset : document.body.scrollTop;
		ftlObj.y += (pY + startY - ftlObj.y)/8;
		}
		else{
		var pY = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		ftlObj.y += (pY - startY - ftlObj.y)/8;
		}
		ftlObj.sP(ftlObj.x, ftlObj.y);
		setTimeout("stayTopLeft()", 10);
	}
	ftlObj = ml("divStayTopLeft");
	stayTopLeft();
}
JSFX_FloatTopDiv();
</script>
</fmt:bundle>
<!-- ends here -->
</div>
</body>
</html>

<fmt:bundle basename="${language}">
<c:if test="${empty requestScope.kekAuk}">
<%@ include file="common/JavascriptErrorMsg.jsp" %>
<script>
alert(agmtMesg[7]); // Please generate the PATU Keys in service Bureau
</script>
</c:if>
</fmt:bundle>

