<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Gestion des stocks"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container">
    <h1 class="text-center">Gestion de la commande</h1>
    <c:choose>
        <c:when test="${orderError}">
            <div class="alert alert-danger" role="alert" style="margin-top: 20px;margin-bottom: 20px;">
                <span><strong><c:out value="${errorMessage}"/></strong></span>
            </div>
        </c:when>
    </c:choose>
    <form class="text-start" action="/order/view" method="get">
        <p>Entrez le numéro de la commande pour continuer</p>
        <div class="row">
            <div class="col"><label class="form-label" for="orderIdInput">Numéro de la commande</label><input
                    class="form-control" type="text" id="orderIdInput" name="orderId" placeholder="1234.."></div>
        </div>
        <div class="row">
            <div class="col text-center" style="margin-top: 29px;">
                <button class="btn btn-primary" type="submit">Trouver la commande</button>
            </div>
        </div>
    </form>
</div>
<%@include file="_footer.jsp" %>
