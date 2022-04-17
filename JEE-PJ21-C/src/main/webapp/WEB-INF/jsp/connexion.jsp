<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Connexion"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container">
    <h1 class="text-center" style="text-align: center;font-size: 40.52px;margin-bottom: 30px;">Connexion</h1>
    <form class="row justify-content-center" style="padding: 0px;width: 100%;" action="/connect" method="POST">
        <div class="col-3 text-center" style="margin-left: 8px;">
            <p class="text-center" style="text-align: center;font-size: 18px;">Choisissez votre rôle<br />Rôle actuel : <c:out value="${actualRole}"/><br /></p>
            <div class="form-check"><input class="form-check-input" type="radio" id="formCheck-1" name="role" value="ADMIN"><label class="form-check-label" for="formCheck-1">Administrateur</label></div>
            <div class="form-check"><input class="form-check-input" type="radio" id="formCheck-2" name="role" value="CLIENT"><label class="form-check-label" for="formCheck-2">Client</label></div>
            <div class="form-check"><input class="form-check-input" type="radio" id="formCheck-3" name="role" value="EMPLOYEE"><label class="form-check-label" for="formCheck-3">Employé</label></div>
        </div>
        <div class="col-2">
            <div class="row">
                <div class="col"><button class="btn btn-primary" type="submit" style="margin: 10px;margin-left: 24px;">Valider</button></div>
            </div>
            <div class="row">
                <div class="col"><a class="btn btn-warning" role="button" href="/disconnect" style="margin: 10px;">Se déconnecter</a></div>
            </div>
        </div>
    </form>
</div>
<%@include file="_footer.jsp" %>
