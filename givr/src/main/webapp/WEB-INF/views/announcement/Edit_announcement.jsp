<%@include file="../_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<body class="d-flex flex-column min-vh-100">
<%@include file="../_navigation.jsp" %>
<main class="d-md-flex justify-content-md-center bg-light-gray">
    <section class="login-clean">
        <form class="border-top-univ"   modelAttribute="announcement">
            <input id="donation-id" type="hidden" value ="${announcement.id}" name="Id" />
            <div class="alert alert-danger shadow-sm d-none" role="alert"><span id="message"></span></div>
            <h2>Edit don</h2>
            <div class="illustration"><i class="icon ion-ios-heart"></i>
                <p style="font-size: 12px;color: var(--bs-gray-dark);">C'est grâce à vos dons que beaucoup d'étudiants puissent correctement vivre. Merci beaucoup</p>
            </div>
            <div class="mb-3"><input class="form-control" type="text" id="title" name="title" value="${announcement.title}" placeholder="Titre"></div>
            <div class="mb-3"><input class="form-control" type="file" id="files" multiple="" name="images"></div>
            <div class="mb-3"><textarea class="form-control" id="description" placeholder="Description" name="description" style="height: 150px;">${announcement.description}</textarea></div>
            <div class="mb-3"><a id="submit-form" class="btn btn-primary w-100 background-univ" href="#">Save</a></div>
        </form>
    </section>
</main>
</body>
<%@include file="../_footer.jsp" %>
<script src="${context}/js/form-utils.js" defer></script>
<script src="${context}/js/edit_donation.js" defer></script>




