<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<nav class="navbar navbar-light navbar-expand-md navigation-clean-button">
    <div class="container"><a class="navbar-brand" href="${context}/">GivR</a><button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-1"><span class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navcol-1">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link ${isDonationPage ? "active" : ""}" href="${context}/donations">Donations</a></li>
                <c:if test="${isAuth}">
                    <li class="nav-item"><a class="nav-link ${isDonationCreatePage ? "active" : ""}" href="${context}/donations/create">Donner</a></li>
                    <li class="nav-item"><a class="nav-link ${isMyDonationPage ? "active" : ""}" href="${context}/donations/mydonations">Mes Donations</a></li>
                </c:if>
            </ul>

            <c:choose>
                <c:when test="${isAuth}">
                    <span class="navbar-text actions"> <a class="btn btn-light action-button background-univ" role="button" href="${context}/account">Mon compte</a></span>
                </c:when>
                <c:when test="${isLoginPage}">
                    <span class="navbar-text actions"> <a class="btn btn-light action-button background-univ" role="button" href="${context}/register">S'enregistrer</a></span>
                </c:when>
                <c:otherwise>
                    <span class="navbar-text actions"> <a class="btn btn-light action-button background-univ" role="button" href="${context}/login">Se connecter</a></span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>