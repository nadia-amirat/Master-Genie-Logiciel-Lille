<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<section class="text-center bg-light devs">
    <div class="container">
        <h2 class="mb-4 font-montserrat">Information</h2>
        <div class="d-flex justify-content-center">
            <div class="alert alert-danger shadow-sm d-none w-100" role="alert" id="message-info-container"><span id="message-info"></span></div>
        </div>
        <div class="row g-0 row-cols-2 d-flex d-xxl-flex justify-content-center justify-content-xxl-center bg-white admin-row" id="row-info" style="border-top: 2px solid rgb(71,171,244);">
            <div class="col-7 col-sm-9 col-md-6 col-lg-5 col-xl-4 d-flex justify-content-center align-items-center"><input type="email" id="email-field-info" class="form-control admin-field" name="email" placeholder="Rechercher avec une adresse mail"></div>
            <div class="col-5 col-sm-3 col-md-2 col-lg-2 col-xl-1 d-sm-flex justify-content-sm-center align-items-sm-center"><button class="btn btn-primary" id="btn-info" type="button">Rechercher</button></div>
            <div class="col-12 d-none" id="col-table-info" style="margin-top: 20px;">
                <div class="table-responsive" style="table-layout: fixed;">
                    <table class="table table-striped table-borderless">
                        <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Adresse mail</th>
                            <th>Adresse</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="text-truncate text-break" id="cell-lastname"></td>
                            <td class="text-truncate text-break" id="cell-firstname"><br></td>
                            <td class="text-truncate text-break" id="cell-email"><br></td>
                            <td class="text-truncate text-break" id="cell-address"><br></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>