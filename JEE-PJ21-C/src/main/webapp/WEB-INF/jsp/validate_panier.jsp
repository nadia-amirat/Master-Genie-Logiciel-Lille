<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Panier"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container">
    <h1>Mon panier</h1>
    <ul class="list-group">
        <c:forEach items="${articles}" var="article">
            <li class="list-group-item"><span><c:out value="${article.name}"/></span> - <span><c:out value="${article.qty}"/></span></li>
        </c:forEach>
    </ul>
</div>
<%@include file="_footer.jsp" %>
