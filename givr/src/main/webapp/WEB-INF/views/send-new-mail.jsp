<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main>
    <section class="login-clean">
        <form class="border-top-univ">
            <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
            <h2>Confirmation d'adresse mail</h2>
            <div class="illustration"><i class="icon ion-android-mail"></i></div>
            <div class="mb-3"><input class="form-control" type="email" id="email" name="email" placeholder="Adresse mail"></div>
            <div><a id="submit-form" class="btn btn-primary w-100 background-univ" href="#">Envoyer un mail</a></div>
        </form>
    </section>
</main>
<%@include file="_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/send-new-mail.js" defer></script>