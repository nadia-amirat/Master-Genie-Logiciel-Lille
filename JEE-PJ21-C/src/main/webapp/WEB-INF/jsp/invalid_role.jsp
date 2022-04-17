<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Accès non autorisé"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container">
    <h2>Oups, vous ne pouvez pas accéder à cette page avec le rôle : <c:out value="${role}"/> </h2>
</div>
<%@include file="_footer.jsp" %>
