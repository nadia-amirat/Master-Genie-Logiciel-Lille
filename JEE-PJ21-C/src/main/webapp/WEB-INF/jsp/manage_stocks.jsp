<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Gestion des stocks"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fil.univ.drive.domain.article.Article" %>
<%@ page import="fil.univ.drive.domain.stock.PerishableStock" %>
<%@ page import="fil.univ.drive.domain.stock.Stock" %>
<jsp:useBean id="allStocks" scope="request" type="java.util.List"/>

<div class="container">
    <c:choose>
        <c:when test="${articleNotFound or invalidStockQuantity or invalidArticle}">
            <div role="alert" class="alert alert-danger text-start alert-dismissible">
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                <span><c:out value="${errorMessage}"/></span>
            </div>
        </c:when>
    </c:choose>
    <div id="liste_des_stocks" style="margin-bottom: 40px;">
        <h1>Liste des stocks</h1>
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
                <c:forEach items="${allStocks}" var="stockEntry">
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
                                <c:otherwise>
                                    Aucune
                                </c:otherwise>
                            </c:choose></td>
                        <td>
                            <form style="margin-bottom: 0px;" class="d-inline-flex" action="/stock/<c:out value="${stockEntry.stockId}"/>/quantity"
                                  method="POST"><input type="number" class="form-control" style="width: 93.6px;"
                                                       value="${stockEntry.quantity}" name="quantity" required/>
                                <button class="btn btn-primary" type="submit" style="margin-left: 18px;"><i
                                        class="fas fa-save"></i></button>
                            </form>
                        </td>
                        <td class="text-start"><c:out value="${stockEntry.stockId}"/></td>
                        <td class="text-center">
                            <form style="margin-bottom: 0px;" action="/stock/<c:out value="${stockEntry.stockId}"/>/delete" method="POST">
                                <button class="btn btn-danger" type="submit"><i class="fa fa-remove"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="border rounded shadow-sm" id="creation-stock" style="padding: 15px;">
        <h1>Ajout de stocks</h1>
        <form action="/stock" method="post" style="margin-bottom: 0px;">
            <div class="row">
                <div class="col">
                    <label class="form-label" for="article_ref" style="margin-right: 5px;">
                        Référence de l&#39;article
                        <input type="text" class="form-control" id="article_ref" name="articleId" required/>
                    </label>
                    <label class="form-label" for="article_quantity" style="margin-left: 5px;margin-right: 5px;">
                        Quantité
                        <input type="text" class="form-control" id="article_quantity" name="quantity" required/>
                    </label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <button class="btn btn-primary" type="submit">Créer l&#39;article</button>
                </div>
            </div>
        </form>
    </div>
</div>
<%@include file="_footer.jsp" %>
