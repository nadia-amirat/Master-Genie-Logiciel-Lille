
<%@include file="../_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100">
<%@include file="../_navigation.jsp" %>
<main class="d-md-flex justify-content-md-center">
    <c:choose>
        <c:when test="${empty details}">
            <div class="text-center" id="box-title-message">
                <p id="title-message" class="font-montserrat fw-bold">La donation n'est pas (plus) enregistrée<br></p><a href="${context}/donations">Voir les donations disponibles</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container" style="margin-top: 32px;">
                <div class="row row-cols-1 row-cols-md-2 text-center w-100" id="row_image_owner-1">
                    <div class="col d-md-flex align-items-md-center modern-border shadow-sm">
                        <div class="carousel slide w-100" data-bs-ride="carousel" id="carousel-2">
                            <div class="carousel-inner">
                                <c:forEach items="${details.images}" var="image" varStatus="loop">
                                    <div class="carousel-item ${loop.index == 0 ? "active" : ""}">
                                        <img src="/api/images/${details.id}/${image.id}" class="w-100 d-block" alt="Slide Image">
                                    </div>
                                </c:forEach>
                            </div>
                            <div>
                                <a style="background: #e1e1e1;width: 10%;" class="carousel-control-prev" href="#carousel-2" role="button" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon"></span>
                                    <span class="visually-hidden">Previous</span>
                                </a>
                                <a style="background: #e1e1e1;width: 10%;" class="carousel-control-next" href="#carousel-2" role="button" data-bs-slide="next">
                                    <span class="carousel-control-next-icon"></span>
                                    <span class="visually-hidden">Next</span>
                                </a>
                            </div>
                            <ol class="carousel-indicators">
                                <c:forEach items="${details.images}" var="image" varStatus="loop">
                                    <li data-bs-target="#carousel-2" data-bs-slide-to="${loop.index}" class="${loop.index == 0 ? "active" : ""}"></li>
                                </c:forEach>
                            </ol>
                        </div>
                    </div>
                    <div class="col modern-border shadow-sm" style="padding: 11px;">
                        <div style="padding-top: 5px;padding-bottom: 5px;border-bottom: 1px solid var(--bs-cyan);">
                            <img class="pp-image" src="${context}/img/no_pp.png" style="border-radius: 360px;margin-top: 10px;margin-bottom: 10px;">
                            <span class="text-primary fw-bold" style="margin-left: 10px;"><c:out value="${details.account.email}"/></span>
                        </div>
                        <div style="margin-top: 10px;">
                            <p class="lead">Statistiques du donneur</p>
                        </div>
                        <div>
                            <span>Réputation</span>
                            <i class="fa fa-star" style="color: rgb(249,152,82);margin-right: 5px;margin-left: 5px;"></i>
                            <i class="fa fa-star" style="color: rgb(249,152,82);margin-right: 5px;margin-left: 5px;"></i>
                            <i class="fa fa-star" style="color: rgb(249,152,82);margin-right: 5px;margin-left: 5px;"></i>
                            <i class="fa fa-star" style="color: rgb(249,152,82);margin-right: 5px;margin-left: 5px;"></i>
                        </div>
                        <div><span>Se situe dans la ville de Lille</span></div>
                        <div class="text-center" style="margin-top: 30px;">
                            <a class="btn btn-primary contact w-50" role="button" style="font-size: 16px;" href="mailto:${details.account.email}">
                                <i class="fa fa-envelope-o" style="margin-right: 10px;"></i>Contacter
                            </a>
                        </div>
                    </div>
                </div>
                <div class="w-75" style="margin-top: 16px;padding-top: 5px;padding-bottom: 5px;">
                    <h3 style="color: var(--bs-gray-900);"><c:out value="${details.title}"/></h3>
                </div>
                <div class="w-75" style="margin-top: 16px;padding-top: 5px;padding-bottom: 5px;border-top: 1px solid rgb(202, 209, 217);">
                    <h3 style="text-decoration: underline;color: var(--bs-gray-900);">Description</h3>
                    <p class="text-break"><c:out value="${details.description}"/></p>
                </div>
                <div class="w-75" style="margin-top: 16px;padding-top: 5px;padding-bottom: 5px;border-top: 1px solid rgb(202, 209, 217);"></div>
            </div>
        </c:otherwise>
    </c:choose>
</main>
<%@include file="../_footer.jsp" %>
</body>
