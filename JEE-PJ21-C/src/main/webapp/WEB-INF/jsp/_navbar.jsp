<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<script src="/assets/bootstrap/js/bootstrap.min.js"></script>
<nav class="navbar navbar-light navbar-expand-md navigation-clean">
    <div class="container"><a class="navbar-brand" href="/" data-bs-target="/">Drive - MonMagasin</a>
        <button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-1"><span
                class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navcol-1">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item dropdown"><a class="dropdown-toggle nav-link" aria-expanded="false"
                                                 data-bs-toggle="dropdown" href="#">Employé</a>
                    <div class="dropdown-menu"><a class="dropdown-item" href="/order">Préparation des commandes</a>
                    </div>
                </li>
                <li class="nav-item dropdown"><a class="dropdown-toggle nav-link" aria-expanded="false"
                                                 data-bs-toggle="dropdown" href="#">Client</a>
                    <div class="dropdown-menu"><a class="dropdown-item" href="/stock/articles">Commander un
                        article</a><a
                            class="dropdown-item" href="/stock/panier/articles">Panier</a></div>
                </li>
                <li class="nav-item dropdown"><a class="dropdown-toggle nav-link" aria-expanded="false"
                                                 data-bs-toggle="dropdown" href="#">Administrateur</a>
                    <div class="dropdown-menu"><a class="dropdown-item" href="/articles">Gestion des articles</a><a
                            class="dropdown-item" href="/stock">Gestion des stocks</a><a class="dropdown-item"
                                                                                         href="/stock/expired">Stocks
                        périmés</a><a class="dropdown-item"
                                      href="/stock/perishable">Création de stocks périssables</a></div>
                </li>
            </ul>
            <a class="btn btn-primary" role="button" href="/connect" style="margin-left: 60px;">Connexion</a>
        </div>
    </div>
</nav>
