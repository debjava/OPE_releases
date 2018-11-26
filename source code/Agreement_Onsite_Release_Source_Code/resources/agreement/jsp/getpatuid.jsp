<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="c.tld" prefix="c"%>
<c:forEach var="patu_data" items="${requestScope.patu_id}"><c:out value="${patu_data}"/>,</c:forEach>