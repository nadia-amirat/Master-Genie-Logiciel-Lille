<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Articles"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<% String ctxPath = request.getContextPath(); %>
<link href="<%= ctxPath %>/css/dlc.css" rel="stylesheet"/>
<script src="<%= ctxPath %>/js/jquery.min.js" type="text/javascript"></script>
<script src="<%= ctxPath %>/js/add_disponible_article.js" type="text/javascript"></script>
<script src="<%= ctxPath %>/js/validate.js" type="text/javascript"></script>
<div class="container">

    <h2>Liste des articles Disponibles</h2>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead class="table-light">
            <tr>
                <th>Article</th>
                <th>Référence de l'article</th>
                <th class="text-start">Quantité</th>
                <th class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${articles}" var="stockEntry">
                <tr>
                    <td><c:out value="${stockEntry.article.name}"/></td>
                    <td class="text-start"><c:out value="${stockEntry.article.id}"/></td>
                    <td>${stockEntry.quantity}</td>
                    <td class="text-center">
                        <button class="btn btn-primary ajouter" data-ref="${stockEntry.stockId}" type="button"><i class="far fa-plus-square"></i></button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <button class="valider btn btn-sucess" type="button">Voir le panier <i class="fas fa-shopping-cart"></i></button>

</div>
<%@include file="_footer.jsp" %>
