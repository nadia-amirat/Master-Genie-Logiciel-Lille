<%@ page contentType="text/html; charset=UTF-8" %>
<footer class="footer-basic mt-auto" style="background: var(--bs-gray-100);">
    <ul class="list-inline">
        <li class="list-inline-item"><a href="${context}/">Accueil</a></li>
        <li class="list-inline-item"><a href="${context}/donations">Donations</a></li>
        <c:if test="${isAuth}">
            <li class="list-inline-item"><a href="${context}/donations/create">Donner</a></li>
        </c:if>
    </ul>
    <p class="copyright">Notre objectif est de venir en aide à un maximum d'étudiant dans le besoin.<br>Vos dons peuvent aider de nombreuses personnes au sein de la France métropolitaine.</p>
    <p class="copyright">GivR © 2021</p>
</footer>
<%@include file="_scripts.jsp" %>