<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">

<script language="javascript" type="text/javascript" src="../js/agreement1.js"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/resources/agreement/js/agreement3.js"/>'></script>
<script type="text/javascript" src="../js/search.js"></script>

<script>
	function doAction(ver1,id)
	{
        var lastVersionNo=ver1;
		var editorHold='PREVIOUS';
		var ver2=parseInt(ver1)+1;		
		var url = 	'<c:url value="/resources/agreement/jsp/agreementedit.do"/>';
		url = url + "?internalAgreementId="+id+"&versionNo="+ver2+"&lastVersionNo="+lastVersionNo+"&editorHold="+editorHold;

		showPopUp(url,500,720,'no','no');
		
	}
	function searchRecords()
	{
		 document.agreementsearch.submit();
	}
</script>


<%@ include file="common/JavascriptErrorMsg.jsp" %>
<fmt:bundle basename="${language}">

<c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'AGMT'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
</c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />
<c:set var="EDIT" value="${fn:substring(rights, 1, 2)}" />
<c:set var="DELETE" value="${fn:substring(rights, 2, 3)}" />

<ul class="toolbar2">
  <li class="new-definition">
  <c:choose>
	  <c:when test="${NEW=='1'}">
	  <a href="javascript: showPopUp('agreementnew.jsp',500,720,'no','no');" 
	   title="<fmt:message key="new"/>" ><fmt:message key="new"/></a>
	  </c:when>
	  <c:otherwise>
	  <a disabled="true" title="<fmt:message key="new"/>" ><fmt:message key="new"/></a>
	  </c:otherwise>  					  
  </c:choose>
  </li>

  <li class="search" title="<fmt:message key="search"/>"><a href="javascript: searchRecords();"><fmt:message key="search"/></a></li>

  <li class="delete" title="<fmt:message key="delete"/>">
 <c:choose>
	<c:when test="${DELETE=='1'}">
	<a href="javascript: deleteRecords('agreementsearch','deleteMe','DELETE','1');" tabindex="3" title="<fmt:message key="delete"/>">
	<span><fmt:message key="delete"/></span></a>
	</c:when>
	<c:otherwise>
    <a disabled title="<fmt:message key="delete"/>"><span><fmt:message key="delete"/></span></a>
	</c:otherwise>
</c:choose>
</li> 

</ul>

<div class="search-content">
  <html:form action="/resources/agreement/jsp/agreementsearch?subtitle=agreement&module=agreement" >    
	<div class="form-item">
	<c:set var="agreement_id"><fmt:message key="agreement_id"/></c:set>
      <label for="agreement_id"><fmt:message key="agreement_id"/>:</label>
      <div class="input">
        <html:text property="internalAgreementId" styleClass="form-text" styleId="${agreement_id}" maxlength="9" size="15"  
				  onblur="javascript: this.value=Trim(this.value);"
				  onkeyup="javascript: this.value=this.value.toUpperCase();"
				  onkeypress="submitPageAfterEnterKey('agreementsearch');return alphanumericWithoutSpace(this);"/>
      </div>
    </div>

    <div class="form-item">
      <label for="agreement_title"><fmt:message key="agreement_title"/>:</label>
      <div class="input">
        <html:text property="agreementTitle" styleClass="form-text" maxlength="50" size="23" onblur="javascript: this.value=Trim(this.value);" onkeypress="submitPageAfterEnterKey('agreementsearch');"/>
      </div>
    </div>
    
	<!-- <div class="but-bar">
      <button type="submit"><fmt:message key="search"/></button>
    </div> -->
	<BR>
	<div class="form-item">
		<label ><%@include file="common/ErrorHandler.jsp"%></label>
	</div>
	<BR>

<input type="hidden" id="deleteMode" name="deleteMode">

<c:if test="${not empty requestScope.agreementList}">
<!-- <div class="tab-buttons">
<c:choose>
	<c:when test="${DELETE=='1'}">
	<a href="javascript: deleteRecords('agreementsearch','deleteMe','DELETE','1');" title="<fmt:message key="delete"/>">
	<span><fmt:message key="delete"/></span></a>
	</c:when>
	<c:otherwise>
    <a disabled title="<fmt:message key="delete"/>"><span><fmt:message key="delete"/></span></a>
	</c:otherwise>
</c:choose>
</div> -->

<table class="search-result" >
	<thead>
      <tr>
		<th width="20" class="checkboxes"><input type="checkbox" onClick="checkAllCheckBox(this)" name="n1" value="all" /></th>
        <th><fmt:message key="agreement_id"/></th>
        <th><fmt:message key="agreement_title"/></th>
        <th><fmt:message key="primary_contact"/></th>
        <th><fmt:message key="valid_from"/></th>
        <th><fmt:message key="valid_to"/></th>
        <th><fmt:message key="status"/></th>
	  </tr>
    </thead>

	<c:forEach items="${agreementList}" var="lst"  varStatus="status">  

	 <c:choose>
      <c:when test="${(status.count)%2 == 1}">

	  <tr >

	  <c:forEach items="${agreementList}" var="agList"  >
		  <c:if test="${agList.status eq 'D' && lst.internalAgreementId eq agList.internalAgreementId}">
			  <c:set var="status" value="D" />		 
		  </c:if>
	   </c:forEach>

	  <c:forEach items="${agreementList}" var="agList"  >
		  <c:if test="${lst.internalAgreementId eq agList.internalAgreementId}">

		<td class="checkboxes">
			<c:choose>
				<c:when test="${status eq 'D'}">
					<input type="checkbox" class="checkbox" disabled="true"  name=<c:out value='${lst.internalAgreementId}'/>  > 	
				</c:when>
				<c:otherwise>
					<input type="checkbox" class="checkbox" name=<c:out value='${lst.internalAgreementId}'/>  >				
				</c:otherwise>
			</c:choose>
		</td>

			<td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="85px">
			   <p class="login_userpass"><c:out value='${agList.internalAgreementId}'/></p>
			</td>
		 </c:if>
	  </c:forEach>

	  <td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="165px">
		   <p class="login_userpass"><c:out value="${fn:substring(lst.agreementTitle,0,15)}"/></p>
	  </td>

	  <td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="165px">
	      <p class="login_userpass"><c:out value="${fn:substring(lst.primaryContact,0,15)}"/></p>
	  </td>				 

	  <c:forEach items="${agreementList}" var="agList"  >
		<c:if test="${lst.internalAgreementId eq agList.internalAgreementId}">
		  
		   <td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="70px">
			 <p class="login_userpass">
			 <fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agList.validFrom}"/>
			 </p>
		   </td> 

			<td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="70px">
			 <p class="login_userpass">
			   <fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agList.validTo}"/>
			 </p>
		   </td> 
		 
	   <td class="list_bg_plain_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="50px">
		  <p class="login_userpass"><c:out value='${agList.status}'/></p>
	   </td>

	

	  </c:if>
	 </c:forEach>
	   
	</tr>			
  </c:when>

  <c:otherwise>

	   <tr class="even">
       
	    <c:forEach items="${agreementList}" var="agList"  >
		  <c:if test="${agList.status eq 'D' && lst.internalAgreementId eq agList.internalAgreementId}">
			  <c:set var="status" value="D" />		 
		  </c:if>
	   </c:forEach>

	   <c:forEach items="${agreementList}" var="agList"  >
		  <c:if test="${lst.internalAgreementId eq agList.internalAgreementId}">

		  <td class="checkboxes">
			 <c:choose>
			  <c:when test="${status eq 'D'}">
				<input type="checkbox" class="checkbox" disabled="true"  name=<c:out value='${lst.internalAgreementId}'/>  > 	
			  </c:when>
			  <c:otherwise>
				 <input type="checkbox" class="checkbox" name=<c:out value='${lst.internalAgreementId}'/>  >				
			  </c:otherwise>
			 </c:choose>
		  </td>

		<td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');"  width="85px">
		   <p class="login_userpass"><c:out value='${agList.internalAgreementId}'/></p>
		</td>

		 </c:if>
	   </c:forEach>

	    <td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');"  width="165px">
		   <p class="login_userpass"><c:out value="${fn:substring(lst.agreementTitle,0,15)}"/></p>
		</td>

	    <td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');"  width="165px">
		   <p class="login_userpass"><c:out value="${fn:substring(lst.primaryContact,0,15)}"/></p>
		</td>		

		<c:forEach items="${agreementList}" var="agList"  >
		  <c:if test="${lst.internalAgreementId eq agList.internalAgreementId}">
           
		   <td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="70px">
		     <p class="login_userpass"><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agList.validFrom}"/></p>
		   </td>

		    <td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="70px" >
		     <p class="login_userpass"><fmt:formatDate pattern="${sessionObj.dateFormat}" value="${agList.validTo}"/></p>
		   </td>
	      
		   <td class="list_bg_color_sep" align="left" title="<fmt:message key="click_to_edit"/>" onclick="doAction('<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalAgreementId}'/>');" width="50px">
		      <p class="login_userpass"><c:out value='${agList.status}'/></p>
		   </td>

		  
		  </c:if>
	     </c:forEach>	
      </tr>			
 </c:otherwise>
</c:choose>
</c:forEach>
 </tbody>
 </table>
</c:if>

</div>
<input type="hidden" name="deleteMe" id="deleteMe"/>
<input type="hidden" id="deleteMode" name="deleteMode">
</html:form>
</fmt:bundle>
</fmt:bundle>

