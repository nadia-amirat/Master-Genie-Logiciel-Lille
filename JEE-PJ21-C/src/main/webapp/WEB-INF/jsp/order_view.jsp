<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Gestion des stocks"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container">
    <h1 class="text-start">Gestion de la commande</h1>
    <h4 class="text-start text-muted">Commande n°<c:out value="${order.id}"/> (<c:out
            value="${order.dateOrdered}"/>)</h4>
    <c:choose>
        <c:when test="${orderError}">
            <div class="alert alert-danger" role="alert" style="margin-top: 20px;margin-bottom: 20px;">
                <span><strong><c:out value="${errorMessage}"/></strong></span>
            </div>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${isOld}">
            <div role="alert" class="alert alert-info" style="margin-top: 20px;margin-bottom: 20px;"><span><strong>Cette commande date d&#39;il y a plus 7 jours. Il n&#39;est pas possible de retirer des articles de celle-ci</strong></span>
            </div>
        </c:when>
    </c:choose>
    <div class="table-responsive text-center">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Référence</th>
                <th>Nom</th>
                <th>Quantité</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orderArticles}" var="article">
                <tr>
                    <td>${article.article.id}</td>
                    <td><c:out value="${article.article.name}"/></td>
                    <td>${article.quantity}</td>
                    <td>
                        <form action="/order/<c:out value="${order.id}"/>/return/<c:out value="${article.article.id}"/>"
                              method="GET">
                            <button class="btn btn-warning retirer-de-commande" type="submit">Retirer de la commande
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="_footer.jsp" %>
