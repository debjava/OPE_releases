<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri= "struts-bean.tld" prefix= "bean" %>
<%@ taglib uri= "struts-html.tld" prefix= "html" %>
<%@ taglib uri= "struts-logic.tld" prefix= "logic" %>

<script type="text/javascript" src="../js/search.js"></script>

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
	 document.ServiceBureauSearch.submit();
}
</script>
<%@ include file="common/JavascriptErrorMsg.jsp" %>

<c:forEach var="lstRights" items="${sessionObj.roleList}">
	<c:if test="${lstRights.entityCode eq 'SVRB'}">		
		<c:set var="rights" value="${lstRights.rightNo}"/>
	</c:if>
</c:forEach>

<c:set var="NEW" value="${fn:substring(rights, 0, 1)}" />

<ul class="toolbar2">
  
   <li class="new-definition">
	  <c:choose>
		  <c:when test="${NEW=='1'}">		   
		   <a href="#" onclick="javascript: showPopUp('servicebureaunew.jsp',424,720,'no','no'); return false;" title="<fmt:message key="new"/>"><fmt:message key="new"/></a>
		  </c:when>
		 <c:otherwise>
		  <a disabled title="<fmt:message key="new"/>"><fmt:message key="new"/></a>   
		</c:otherwise>  
	  </c:choose>
  </li>

  <li class="search"><a href="javascript: searchRecords();" title="<fmt:message key="search"/>"><fmt:message key="search"/></a></li>
</ul>

<div class="search-content">
  <html:form action="/resources/agreement/jsp/ServiceBureauSearch?subtitle=service_bureau&module=agreement" >    
	<div class="form-item">
      <label for="service_bureau_id"><fmt:message key="service_bureau_id"/>:</label>
      <div class="input">
        <html:text property="bureauId" styleId="${service_bureau_id}" maxlength="17" size="22"
		         onblur="javascript: this.value=this.value.toUpperCase();this.value=Trim(this.value);"
				 onkeyup="javascript: this.value=this.value.toUpperCase();"
		         onkeypress="submitPageAfterEnterKey('ServiceBureauSearch');return alphanumericWithoutSpace(this);"/>
      </div>
    </div>

    <div class="form-item">
      <label for="serv_bureau_name"><fmt:message key="service_bureau_name"/>:</label>
      <div class="input">
        <html:text property="bureauName" maxlength="40" 
		onkeypress="submitPageAfterEnterKey('ServiceBureauSearch');return alphanumericWithSpace(this);" onblur="javascript: this.value=Trim(this.value);"/>
      </div>
    </div>
    
	<div class="form-item">
      <label for="patu_id"><fmt:message key="patu_id"/>:</label>
      <div class="input">
        <html:text property="patuId" styleClass="login_userpass" maxlength="17" 
		onkeypress="submitPageAfterEnterKey('ServiceBureauSearch');return alphanumericWithoutSpace(this);"
		onkeyup="javascript: this.value=this.value.toUpperCase();"
		onblur="javascript: this.value=Trim(this.value.toUpperCase());this.value=Trim(this.value);"/>
      </div>
    </div>
    
	
	<div class="form-item">
	<label ><%@include file="common/ErrorHandler.jsp"%></label>
	</div>
	
</html:form>
</fmt:bundle>