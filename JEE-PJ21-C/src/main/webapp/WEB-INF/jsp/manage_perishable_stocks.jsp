<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Création de stocks périssables"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class= "col-md-10 text-center">
    <h2>Ajouter un nouveau stock périssable</h2>
</div>
<br/>
<br/>
<div class= "col-md-10 text-center">
    <form id="create-article-form" method="POST" action="/stock/perishable/">
        <div class="form-group row">
            <label class="col-sm-5 col-form-label col-form-label-lg">Article Reference :</label>
            <div class="col-sm-5">
                <select class="form-select" aria-label="Default select example" name = "articleId">
                 <option value="${ref}" selected > Choisissez un article... </option>
                <c:forEach items="${refs}" var="ref">
                         <option value="${ref}"><c:out value="${ref}"/></option>
                 </c:forEach>
                </select>
                <c:if test="${ !empty ReférenceError }"><small class="text-danger"><c:out value="${ ReférenceError }" /></small></c:if>
            </div>
            <br/>
        </div>
        <div class="form-group row">
            <label class="col-sm-5 col-form-label col-form-label-lg">Quantity :</label>
            <div class="col-sm-5">
                <input class="form-control" type="number" name="quantity" value=1 required>
                 <c:if test="${ !empty quantityError }"><small class="text-danger"><c:out value="${ quantityError }" /></small></c:if>
            </div>
            <br/>
        </div>
        <div class="form-group row">
            <label class="col-sm-5 col-form-label col-form-label-lg"> Lot :</label>
            <div class="col-sm-5">
                <input class="form-control" type="number" name="lot" value=1 required>
                <c:if test="${ !empty lotError }"><small class="text-danger"><c:out value="${ lotError }" /></small></c:if>
            </div>
            <br/>
        </div>
        <div class="form-group row">
            <label class="col-sm-5 col-form-label col-form-label-lg">Expiration Date :</label>
            <div class="col-sm-5">
                <input class="form-control" type= "date" name="expirationDate" required>
            </div>
            <br/>
        </div>
        <br/>
        <button class="btn btn-success text-center" type="submit" style="font-weight: bold;">Créer</button>
        <a class="btn btn-info" href="/stock">Gérer les stocks</a>
        <c:if test="${ !empty  success}"><small class="valid-feedback"><c:out value="${success}" /></small></c:if>
    </form>

</div>
<%@include file="_footer.jsp" %>
