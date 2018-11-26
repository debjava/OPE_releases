<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt_error" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="sessionKey" value="DNBDATA"/>
<c:set var="sessionObj" value="${sessionScope[sessionKey]}"/>
<fmt:setLocale value="${sessionObj.languageId}"/>
<c:set var="property"  value="messageresource.Common_MessageResources" />
<c:set var="language" value="${property}_${sessionObj.languageId}" />
<c:set var="property1"  value="applicationconfig.installation" />
<c:set var="language1" value="${property1}_${sessionObj.languageId}" />

<c:set var="SVRBVIEW" value="0" />
<c:set var="AGMTVIEW" value="0" />

<c:choose>
<c:when test="${not empty sessionObj.roleList}">
   <c:forEach var="lst" items="${sessionObj.roleList}">

   <c:if test="${lst.entityCode == 'STMTREQ' }">
   <c:set var="STMTREQ" value="1"/>
   </c:if>
   <c:if test="${lst.entityCode == 'STMTREQVW' }">
   <c:set var="STMTREQVW" value="1"/>
   </c:if> <c:if test="${lst.entityCode == 'STMTHISTVW' }">
   <c:set var="STMTHISTVW" value="1"/>
   </c:if>

     <c:if test="${lst.entityCode == 'SVRB' }">
		<c:set var="SVRB" value="1"/>
		<c:set var="rights" value="${lst.rightNo}"/>
		<c:set var="SVRBVIEW" value="${fn:substring(rights, 3, 4)}" />
   </c:if>

    <c:if test="${lst.entityCode == 'AGMT' }">
		<c:set var="AGMT" value="1"/>
	    <c:set var="rights" value="${lst.rightNo}"/>
		<c:set var="AGMTVIEW" value="${fn:substring(rights, 3, 4)}" />
   </c:if>

    <c:if test="${lst.entityCode == 'PKAD' }">
		<c:set var="PKAD" value="1"/>
	    <c:set var="rights" value="${lst.rightNo}"/>
		<c:set var="PKADVIEW" value="${fn:substring(rights, 3, 4)}" />
   </c:if>

    <c:if test="${lst.entityCode == 'FTAD' }">
		<c:set var="FTAD" value="1"/>
	    <c:set var="rights" value="${lst.rightNo}"/>
		<c:set var="FTADVIEW" value="${fn:substring(rights, 3, 4)}" />
   </c:if>


   </c:forEach>
</c:when>
</c:choose>


<fmt:bundle basename="${language}">

 <div class="left-block">
          <h3><span class="shadow"><fmt:message key="user_info"/></span><span class="text"><fmt:message key="user_info"/></span> <a href="#" class="min-max">&nbsp;</a></h3>
          <div class="block-content">
            <dl class="list">
              <dt><fmt:message key="user"/>:</dt>
              <dd><c:out value="${sessionObj.userName}"/></dd>
              <dt class="even"><fmt:message key="last_login"/>:</dt>
              		  
				<c:choose>
					<c:when test="${sessionObj.lastLoginDate ne null}">
					  	<dd class="even"><c:out value="${sessionObj.lastLoginDate}"/></dd>
					</c:when>
					<c:otherwise>
						<dd class="even">&nbsp;</dd>
					</c:otherwise>
				</c:choose>

              <dt><fmt:message key="business_date"/>:</dt>
              <dd><c:out value="${sessionObj.currentBusinessDate}"/></dd>
              <dt class="even"><fmt:message key="home_branch"/>:</dt>
              <dd class="even"><c:out value="${sessionObj.divisionId}"/></dd>
              <dt><fmt:message key="current_branch"/>:</dt>
              <dd><c:out value="${sessionObj.enterpriseId}"/></dd>
            </dl>
          </div>
        </div>
        <div class="left-block">
          <h3><span class="shadow"><fmt:message key="navigation"/></span><span class="text"><fmt:message key="navigation"/></span> <a href="#" class="min-max">&nbsp;</a></h3>
          <div class="block-content">
            <ul class="tree">
              <li class="folder"><a href="#" title="<fmt:message key="one_point_of_entry"/>"><fmt:message key="one_point_of_entry"/></a>
                <ul>
					<c:choose>
						<c:when test="${SVRB=='1' && SVRBVIEW=='1'}">
						<c:choose>
							<c:when test="${param.module eq 'agreement'}" >
							 <li class="pagelink"><a href='<c:url value="/resources/agreement/jsp/servicebureaumain.jsp"/>?subtitle=service_bureau&module=agreement'
							 title="<fmt:message key="service_bureau"/>"><fmt:message key="service_bureau"/></a></li>
							</c:when>
							<c:otherwise>
							 <li class="pagelink"><a href='<c:url value="/resources/agreement/jsp/servicebureaumain.jsp"/>?subtitle=service_bureau&module=agreement'
							 title="<fmt:message key="service_bureau"/>" ><fmt:message key="service_bureau"/></a></li>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>

					</c:otherwise>
					</c:choose> 

				  <c:choose>
						<c:when test="${AGMT=='1' && AGMTVIEW=='1'}">
						<c:choose>
							<c:when test="${param.module eq 'agreement'}" >
							 <li class="pagelink"><a href='<c:url value="/resources/agreement/jsp/agreementmain.jsp"/>?subtitle=agreement&module=agreement'
							 title="<fmt:message key="agreement"/>"><fmt:message key="agreement"/></a></li>
							</c:when>
							<c:otherwise>
							 <li class="pagelink"><a href='<c:url value="/resources/agreement/jsp/agreementmain.jsp"/>?subtitle=agreement&module=agreement'
							 title="<fmt:message key="agreement"/>" ><fmt:message key="agreement"/></a></li>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>

				<c:choose>
				<c:when test="${FTADVIEW=='1' || PKADVIEW=='1'}">					
					<li class="folder"><a href="#" title="<fmt:message key="help_desk"/>"><fmt:message key="help_desk"/></a>
					<ul>
					<c:choose>
						<c:when test="${SVRB=='1' && SVRBVIEW=='1'}">
						<c:choose>
							<c:when test="${param.module eq 'agreement'}" >
							 <li class="pagelink"><a href="<c:url value="/resources/helpdesk/jsp/patukeyauditmain.jsp"/>?subtitle=patu_key_audit&module=agreement" title="<fmt:message key="patu_key_audit"/>"><fmt:message key="patu_key_audit"/></a></li>
							</c:when>
							<c:otherwise>
							 <li class="pagelink"><a href="<c:url value="/resources/helpdesk/jsp/patukeyauditmain.jsp"/>?subtitle=patu_key_audit&module=agreement" title="<fmt:message key="patu_key_audit"/>"><fmt:message key="patu_key_audit"/></a></li>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>

					</c:otherwise>
					</c:choose> 

				    <c:choose>
						<c:when test="${AGMT=='1' && AGMTVIEW=='1'}">
						<c:choose>
							<c:when test="${param.module eq 'agreement'}" >
								 <li class="pagelink"><a href="<c:url value="/resources/helpdesk/jsp/filetransferauditmain.jsp"/>?subtitle=file_audit&module=agreement" title="<fmt:message key="file_audit"/>"><fmt:message key="file_audit"/></a></li>
							</c:when>
							<c:otherwise>
								<li class="pagelink"><a href="<c:url value="/resources/helpdesk/jsp/filetransferauditmain.jsp"/>?subtitle=file_audit&module=agreement" title="<fmt:message key="file_audit"/>"><fmt:message key="file_audit"/></a></li>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				    </c:choose>

					</ul>
					</li>
				</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
                </ul>
              </li> 
			 </ul>
          </div>
        </div>

  </fmt:bundle>