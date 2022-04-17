<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main>
    <div class="text-center" id="box-title-message">
        <p id="title-message" class="font-montserrat fw-bold">
            <c:if test="${not empty status}">
                [${status}]
            </c:if>
            <c:choose>
                <c:when test="${status == 404}">
                    La page demandée est introuvable
                </c:when>
                <c:when test="${status == 401}">
                    Vous n'êtes pas autorisé à interagir avec cette ressource
                </c:when>
                <c:otherwise>
                    <c:out value="${error}"/>
                </c:otherwise>
            </c:choose>
        </p>
    </div>
</main>
<%@include file="_footer.jsp" %>