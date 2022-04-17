<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100">
<%@include file="_navigation.jsp" %>
<main class="d-md-flex justify-content-md-center">
    <div class="container" style="margin-top: 32px;">
        <div id="row-info" class="bg-white"
             style="padding: 15px;border-radius: 4px;box-shadow: 1px 1px 5px rgb(0 0 0 / 10%);border-top-left-radius: 0px;border-top-right-radius: 0px;border-top: 2px solid rgb(71,171,244);">
            <p style="text-align: center;font-family: Montserrat, sans-serif;">Trouvez votre produit idéal grâce au
                réseau d'entraide et à la seconde main !</p>
            <form action="${context}/donations" method="get">
                <div class="row g-0 row-cols-2 d-flex d-xxl-flex justify-content-center justify-content-xxl-center bg-white"
                     style="/*padding: 15px;*//*border-radius: 4px;*//*box-shadow: 1px 1px 5px rgb(0 0 0 / 10%);*//*border-top-left-radius: 0px;*//*border-top-right-radius: 0px;*//*border-top: 2px solid rgb(71,171,244);*/">
                    <div class="col-7 col-sm-9 col-md-6 col-lg-5 col-xl-4 d-flex justify-content-center align-items-center">
                        <input class="form-control form-control" type="search" id="search" name="search"
                               placeholder="Rechercher un produit" style="max-width: 400px;" value="${search}"></div>
                    <div class="col-auto col-sm-auto col-md-auto col-lg-auto col-xl-auto col-xxl-auto d-sm-flex align-items-sm-center">
                        <button class="btn btn-primary" type="submit"
                                style="margin-left: 6px;background: var(--bs-blue);width: 64px;"><i
                                class="fa fa-search"></i></button>
                    </div>
                </div>
            </form>
        </div>
        <div>
            <div class="row g-0 row-cols-1 row-cols-sm-1 row-cols-md-3 row-cols-lg-4 row-cols-xxl-3 d-flex justify-content-center"
                 style="margin-top: 10px;">
                <c:choose>
                    <c:when test="${empty listAnnouncement.getContent()}">
                        <div class="col text-center w-100">
                            <h1 class="w-100" style="font-family: Montserrat, sans-serif;">Aucune donation
                                disponible</h1>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="annonce" items="${listAnnouncement.getContent()}">
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
                                                                                  href="${context}/donations/${annonce.id}"
                                                                                  style="border-bottom-right-radius: .25em;border-bottom-left-radius: .25em;border-top-left-radius: 0px;border-top-right-radius: 0px;">Voir
                                        les détails</a></div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div
            <c:if test="${not empty listAnnouncement.getContent()}">
                <div>
                        <%--Pagination --%>
                    <nav aria-label="Page navigation">
                        <ul class="pagination pagination-circle" id="pagination">
                            <li class="page-item">
                                <a class="page-link"
                                   href="?search=${param.search}&page=${param.page-1>= 0 ? param.page-1 : 0}"
                                   aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                            <c:forEach var="i"
                                       begin="${listAnnouncement.number-10 > 0 ? listAnnouncement.number-10 : 0}"
                                       end="${listAnnouncement.number+10 < listAnnouncement.totalPages ? listAnnouncement.number+10 : listAnnouncement.totalPages -1}">
                                <li class="${ i == listAnnouncement.number ? 'active' : ''} page-item"><a
                                        class="page-link"
                                        href="?search=${param.search}&page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item">
                                <a class="page-link"
                                   href="?search=${param.search}&page=${listAnnouncement.totalPages > param.page+1 ? param.page+1 : param.page}"
                                   aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>
    </div>
</main>
</body>
<%@include file="_footer.jsp" %>