<%@include file="../_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100">
<%@include file="../_navigation.jsp" %>
<main class="d-md-flex justify-content-md-center">
    <div class="container" style="margin-top: 32px;">
        <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
        <div>
            <div class="row g-0 row-cols-1 row-cols-sm-1 row-cols-md-3 row-cols-lg-4 row-cols-xxl-3 d-flex justify-content-center"
                 style="margin-top: 10px;">

                <c:choose>
                    <c:when test="${empty listAnnouncement}">
                        <div class="col text-center w-100">
                            <h1 class="w-100" style="font-family: Montserrat, sans-serif;">Aucune donation
                                disponible</h1>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="annonce" items="${listAnnouncement}">
                            <div class="col donation modern-border shadow-sm">
                                <div>
                                    <div style="margin: 10px 0;width: 100%;height: 250px;">
                                        <img class="donation-img"
                                             style="border-top-left-radius: .25rem;border-top-right-radius: .25rem;"
                                        <c:choose>
                                        <c:when test="${not empty annonce.images}">
                                             alt="Image du produit" src="${context}/api/images/${annonce.id}/${annonce.images[0].id}"
                                        </c:when>
                                        <c:otherwise>
                                             alt="Image du produit" src="${context}/img/no_image.png"
                                        </c:otherwise>
                                        </c:choose>
                                        >
                                    </div>
                                    <p class="donation-title"
                                       style="font-family: Montserrat, sans-serif;font-weight: bold;"><c:out
                                            value="${annonce.title}"/></p>
                                    <p class="text-truncate donation-title" style="font-style: italic;"><c:out
                                            value="${annonce.description}"/></p>
                                    <div class="d-flex justify-content-center"><a class="btn btn-primary background-univ w-100 h-100"
                                                                                  role="button"
                                                                                  href="${context}/donations/edit/${annonce.id}"
                                                                                  style="border-bottom-right-radius: .25em;border-bottom-left-radius: .25em;border-top-left-radius: 0px;border-top-right-radius: 0px;">Edit
                                        </a>
                                        <button type="button" class="supprimer" data-ref="${annonce.id}"  style="color: red;">Delete</button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div

        </div>
    </div>
</main>
</body>
<%@include file="../_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/delete_donation.js" defer></script>
