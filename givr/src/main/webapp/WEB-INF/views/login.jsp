<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main>
    <section class="login-clean">
        <form class="border-top-univ">
            <c:choose>
                <c:when test="${not empty error}">
                    <div class="alert alert-danger shadow-sm" role="alert">
                        <span id="message">
                            <ul>
                                <li><c:out value="${error}"/></li>
                            </ul>
                        </span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
                </c:otherwise>
            </c:choose>
            <h2>Se connecter</h2>
            <div class="illustration"><i class="icon ion-ios-contact-outline"></i></div>
            <div class="mb-3"><input class="form-control" type="email" id="email" name="email" placeholder="Adresse mail"></div>
            <div class="mb-3"><input class="form-control" type="password" id="password" name="password" placeholder="Mot de passe"></div>
            <div class="mb-3"><a id="submit-form" class="btn btn-primary d-block w-100 background-univ" href="#">Connexion</a></div><a class="forgot" href="${context}/mail/new-confirm">Besoin d'un nouveau mail d'activation ?</a>
        </form>
    </section>
</main>
<%@include file="_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/login.js" defer></script>