<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
<%@include file="_navigation.jsp" %>
<main>
    <div class="text-center" id="box-title-message">
        <p id="title-message" class="font-montserrat fw-bold"><c:out value="${message}"/><br></p>
        <p id="cooldown" class="font-montserrat fw-bold"></p>
    </div>
</main>
<%@include file="_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/confirm-account.js" defer></script>