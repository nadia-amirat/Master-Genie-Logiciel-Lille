<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Oups"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="errorMessage" scope="request" type="java.lang.String"/>
<div class="container">
    <h2>An error occured : </h2>
    <c:out value="${errorMessage}"/>
</div>
<%@include file="_footer.jsp" %>
