<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main>
    <section class="login-clean">
        <form class="border-top-univ">
            <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
            <h2>S'enregistrer</h2>
            <div class="illustration"><i class="icon ion-ios-contact"></i></div>
            <div class="mb-3"><input class="form-control" type="text" id="lastname" name="lastname" placeholder="Nom"></div>
            <div class="mb-3"><input class="form-control" type="text" id="firstname" name="firstname" placeholder="Prénom"></div>
            <div class="mb-3"><input class="form-control" type="text" id="address" name="address" placeholder="Adresse"></div>
            <div class="mb-3"><input class="form-control" type="email" id="email" name="email" placeholder="Adresse mail"></div>
            <div class="mb-3"><input class="form-control" type="password" id="password" name="password" placeholder="Mot de passe"></div>
            <div class="mb-3"><a id="submit-form" class="btn btn-primary w-100 background-univ" href="#">Créer un compte</a></div><a class="forgot" href="${context}/mail/new-confirm">Besoin d'un nouveau mail d'activation ?</a>
        </form>
    </section>
</main>
<%@include file="_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/register.js" defer></script>