<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ taglib uri="c.tld" prefix="c" %>
<enterprisedate_data>
   <date_rec>
		<c:forEach items='${bureauNameList}' var="lst" varStatus="status">
		<bureau_name><c:out value="${lst.bureauName}"/></bureau_name>
		</c:forEach>
  </date_rec>		
</enterprisedate_data>