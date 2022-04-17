<%@ page import="fil.univ.drive.domain.article.Article" %>
<%@ page import="fil.univ.drive.domain.stock.PerishableStock" %>
<%@ page import="fil.univ.drive.domain.stock.Stock" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Produits périmés"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<% String ctxPath = request.getContextPath(); %>
<link href="<%= ctxPath %>/css/dlc.css" rel="stylesheet"/>
<script src="<%= ctxPath %>/js/jquery.min.js" type="text/javascript"></script>
<script src="<%= ctxPath %>/js/delete_expiry_article.js" type="text/javascript"></script>
<div class="container">
    <h2>Liste des produits expirés</h2>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead class="table-light">
            <tr>
                <th>Article</th>
                <th>Date de péremption</th>
                <th class="text-start">Quantité</th>
                <th class="text-start">Référence du stock</th>
                <th class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${perishables}" var="stockEntry">
                <tr>
                    <td><c:out value="${stockEntry.article.name}"/></td>
                    <td>
                        <%
                            Stock<? extends Article> stock = (Stock<? extends Article>) pageContext.getAttribute(
                                    "stockEntry");
                            Boolean isPerishable = stock instanceof PerishableStock;
                        %>
                        <c:choose>
                            <c:when test="<%=isPerishable%>">
                                <c:out value="${stockEntry.getExpiryDate()}"/>
                            </c:when>
                        </c:choose></td>
                    <td>${stockEntry.quantity}</td>
                    <td class="text-start"><c:out value="${stockEntry.stockId}"/></td>
                    <td class="text-center">
                        <button class="btn btn-danger supprimer" data-ref="${stockEntry.stockId}" type="button"><i class="fa fa-remove"></i></button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="_footer.jsp" %>
