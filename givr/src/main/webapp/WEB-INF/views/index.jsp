<%@include file="_header.jsp" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100 bg-light-gray">
    <%@include file="_navigation.jsp" %>
    <main>
        <div class="container d-flex justify-content-center align-items-center">
            <div class="row w-75 my-3">
                <div class="col modern-border shadow-sm bg-white p-4">
                    <div class="d-flex align-items-center">
                        <h1 class="w-75">Dernières donations</h1>
                        <a class="btn btn-light action-button text-white background-univ my-auto" role="button" href="${context}/donations">Voir plus</a>
                    </div>
                    <div class="row donations-index">
                        <c:forEach items="${ten_last_donations}" var="donation">
                            <a class="donation_link bg-light-gray d-flex mb-4 text-reset text-decoration-none" href="/donations/${donation.id}" >
                                <img class="donation-img w-25"
                                    <c:choose>
                                        <c:when test="${not empty donation.images}">
                                            alt="image du don" src="${context}/api/images/${donation.id}/${donation.images[0].id}"
                                        </c:when>
                                        <c:otherwise>
                                            alt="image du don" src="${context}/img/no_image.png"
                                        </c:otherwise>
                                    </c:choose> >
                                <div class="w-75 my-auto ps-1">
                                    <p class="donation_title text-center text-truncate text-break fw-bold">
                                        <c:out value="${donation.title}"/>
                                    </p>
                                    <p class="donation_description text-center text-truncate text-break fst-italic">
                                        <c:out value="${donation.description}"/>
                                    </p>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <%@include file="_footer.jsp" %>
</body>
</html>