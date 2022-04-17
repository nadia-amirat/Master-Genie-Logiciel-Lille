<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main class = "container" id="blur">
    <section class="login-clean">
        <form class="border-top-univ">
            <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
            <h2 style="margin-bottom: 20px;">Votre compte</h2>
            <div class="mb-3"><input class="form-control" type="text" id="lastname" name="lastname" placeholder="Nom" value="${lastname}"></div>
            <div class="mb-3"><input class="form-control" type="text" id="firstname" name="firstname" placeholder="Prénom" value="${firstname}"></div>
            <div class="mb-3"><input class="form-control" type="text" id="address" name="address" placeholder="Adresse" value="${address}"></div>
            <div class="mb-3"><input class="form-control" type="email" id="email" name="email" placeholder="Adresse mail" value="${email}"></div>
            <div><a id="submit-form" class="btn btn-success w-100" href="#">Modifier son compte</a></div>
            <div><a id="delete" class="btn btn-danger w-100 background-danger" href="#">Supprimer son compte</a></div>
            <div><a id="disconnect" class="btn btn-primary w-100 background-univ" href="#">Se déconnecter</a></div>
            <%@include file="account_deletion.jsp" %>
        </form>
    </section>
</main>
<%@include file="_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/account.js" defer></script>