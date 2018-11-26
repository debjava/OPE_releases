<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ taglib uri="c.tld" prefix="c" %>
<enterprisedate_data>
   <date_rec>
		<c:forEach items='${customerList}' var="lst" varStatus="status">
		<customer_name><c:out value="${lst.customerName}"/></customer_name>
		<customer_address><c:out value="${lst.address}"/></customer_address>		
		</c:forEach>
		<rec_index><c:out value="${requestScope.index}"/></rec_index>
	</date_rec>		
</enterprisedate_data>