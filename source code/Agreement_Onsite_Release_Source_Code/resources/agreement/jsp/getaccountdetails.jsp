<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ taglib uri="c.tld" prefix="c" %>
<enterprisedate_data>
   <date_rec>
		<account_name><c:out value="${requestScope.customerAccountList}"/></account_name>
		<rec_index><c:out value="${requestScope.index}"/></rec_index>
	</date_rec>		
</enterprisedate_data>