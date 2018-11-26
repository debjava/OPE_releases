<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>

<script type="text/javascript" src="../../common/js/search.js"></script>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.AgreementMessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />

<fmt:bundle basename="${language}">

<script language="javascript" type="text/javascript" src="../js/agreement1.js"></script>
<script>
function searchRecords()
{
	 document.agreementsearch.submit();
}
</script>
<%@ include file="common/JavascriptErrorMsg.jsp" %>

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
</ul>

<div class="search-content">
  <html:form action="/resources/agreement/jsp/agreementsearch?subtitle=agreement&module=agreement" >    
	<div class="form-item">
	<c:set var="agreement_id"><fmt:message key="agreement_id"/></c:set>
      <label for="agreement_id"><fmt:message key="agreement_id"/>:</label>
      <div class="input">
        <html:text property="internalAgreementId" styleClass="form-text" styleId="${agreement_id}" maxlength="9" size="15"  
				  onblur="javascript: this.value=Trim(this.value);this.value=this.value.toUpperCase();"
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
	
	<div class="form-item">

	<%@include file="common/ErrorHandler.jsp"%>
	</div>
</div>
	
</html:form>
</fmt:bundle>