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
<script type="text/javascript" src="../js/search.js"></script>
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

<c:set var="DELETE" value="${fn:substring(rights, 2, 3)}" />

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

<li class="delete" title="<fmt:message key="delete"/>">
 <c:choose>
	<c:when test="${DELETE=='1'}">
	<a href="javascript: deleteRecords('ServiceBureauSearch','deleteMe','DELETE','1');" tabindex="3" title="<fmt:message key="delete"/>">
	<span><fmt:message key="delete"/></span></a>
	</c:when>
	<c:otherwise>
    <a disabled title="<fmt:message key="delete"/>"><span><fmt:message key="delete"/></span></a>
	</c:otherwise>
</c:choose>
</li> 

</ul>

<div class="search-content">
  <html:form action="/resources/agreement/jsp/ServiceBureauSearch?subtitle=service_bureau&module=agreement" >    
	<div class="form-item">
      <label for="service_bureau_id"><fmt:message key="service_bureau_id"/>:</label>
      <div class="input">
        <html:text property="bureauId" styleId="${service_bureau_id}" maxlength="17" size="22"
		         onblur="javascript: this.value=this.value.toUpperCase();"
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
		onblur="javascript: this.value=Trim(this.value.toUpperCase());"
		onkeyup="javascript: this.value=this.value.toUpperCase();"/>
      </div>
    </div>
    
	<BR>

	<div class="form-item">
		<label ><%@include file="common/ErrorHandler.jsp"%></label>
	</div>

	<BR>
    
  <c:if test="${not empty requestScope.bureauList}">   

 <!--  <div class="tab-buttons">	
	<c:choose>
		<c:when test="${DELETE=='1'}">
	  			<a href="javascript:deleteRecords('ServiceBureauSearch','deleteMe','DELETE','1');" 
				title="<fmt:message key="delete"/>"><span><fmt:message key="delete"/></span></a>
	   </c:when>
	   <c:otherwise>
	  		<a disabled href="#" title="<fmt:message key="delete"/>"><span><fmt:message key="delete"/></span></a>
	   </c:otherwise>  
	</c:choose>
  </div>
 -->
 <div id="service_Bureau_scrollbar">
  <table class="search-result">
	<thead>
      <tr>        
		<%-- <th width="20" class="checkboxes"><input type="checkbox" onClick="checkAllCheckBox(this)" name="n1" value="all" /></th> --%>
		<th></th>
        <th><fmt:message key="service_bureau_id"/></th>
        <th><fmt:message key="service_bureau_name"/></th>
        <th><fmt:message key="patu_user_id"/></th>
        <th><fmt:message key="patu_id"/></th>
        <th><fmt:message key="status"/></th>       
		
      </tr>
    </thead>

    <tbody>
     
	<c:forEach items="${bureauList}" var="lst"  varStatus="status">

	 <c:choose>
        <c:when test="${(status.count)%2 == 1}">

	  <tr>   

	 <td class="checkboxes">
		 <c:choose>
	      <c:when test="${lst.status=='D'}">
					<input type="checkbox" disabled="true" name=<c:out value='${lst.internalBureauId}'/>>      	
		  </c:when>
		  <c:otherwise>
					<input type="checkbox" class="checkbox" name=<c:out value='${lst.internalBureauId}'/>  >				
		  </c:otherwise>
		</c:choose>
		</td>

        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value="${lst.bureauId}"/></td>
        
		<td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')">
			<c:forEach items="${lst.bureauName}" var="bureauName" > 
				<label value ="<c:out value='${bureauName}'/>"><c:out value="${fn:substring(bureauName,0,30)}"/></label>
			</c:forEach>
		</td>

        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')">
			<c:forEach items="${lst.companyName}" var="companyName" > 
				<label value ="<c:out value='${companyName}'/>"><c:out value="${fn:substring(companyName,0,10)}"/></label>
			</c:forEach>
		</td>

        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value='${lst.patuId}'/></td>
        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value='${lst.status}'/></td>

		
        
      </tr>

	  </c:when>
  <c:otherwise>

	   <tr class="even">

	   <td class="checkboxes">
		 <c:choose>
	      <c:when test="${lst.status=='D'}">
					<input type="checkbox" disabled="true" name=<c:out value='${lst.internalBureauId}'/>>      	
		  </c:when>
		  <c:otherwise>
					<input type="checkbox" class="checkbox" name=<c:out value='${lst.internalBureauId}'/>  >				
		  </c:otherwise>
		</c:choose></td>
        
        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value="${lst.bureauId}"/></td>

        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')">
			<c:forEach items="${lst.bureauName}" var="bureauName" > 
				<label value ="<c:out value='${bureauName}'/>"><c:out value="${fn:substring(bureauName,0,30)}"/></label>
			</c:forEach>
		</td>

        <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')">
			<c:forEach items="${lst.companyName}" var="companyName" > 
				<label value ="<c:out value='${companyName}'/>"><c:out value="${fn:substring(companyName,0,10)}"/></label>
			</c:forEach>
		</td>
        
	   <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value='${lst.patuId}'/></td>
       <td title="<fmt:message key="click_to_edit"/>" onclick="doSomeAction('1','<c:out value='${lst.versionNo}' />','<c:out value='${lst.internalBureauId}' />')"><c:out value='${lst.status}'/></td>

	   
		
      </tr>

	  </c:otherwise>
</c:choose>

</c:forEach>     
    </tbody>
  </table>
  </div>
  </c:if>
</div>
<input type="hidden" name="deleteMe" id="deleteMe"/>
<input type="hidden" id="deleteMode" name="deleteMode">
</html:form>
</fmt:bundle>