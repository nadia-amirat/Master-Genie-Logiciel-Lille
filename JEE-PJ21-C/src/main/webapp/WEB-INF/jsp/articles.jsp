<%@ page import="fil.univ.drive.domain.article.Article" %>
<%@ page import="fil.univ.drive.domain.article.Product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title"
       value="Articles"
       scope="request"/>
<%@include file="_header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% String ctxPath = request.getContextPath(); %>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css"/>
<link href="<%= ctxPath %>/css/articles.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<%--    pagination table--%>
<link href="https://unpkg.com/bootstrap-table@1.18.3/dist/bootstrap-table.min.css" rel="stylesheet">
<script src="https://unpkg.com/bootstrap-table@1.18.3/dist/bootstrap-table.min.js"></script>
<meta charset="UTF-8">
<section class="search-art">
    <div class="container">
        <form action="/articles/search" method="GET"  class="form-search">
            <%

                String msgError = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                if(msgError != null){
                    out.println("<div class='alert alert-danger form-group has-error' role='alert'> "+msgError+"  </div>");
                }else{
                    if(success != null)
                        out.println("<div class='alert alert-success form-group' role='alert'> "+success+"  </div>");
                }
            %>

            <div class="row">
                <div class="col-lg-12">
                    <div class="row">
                        <div class="col-lg-2 col-md-2 col-sm-12 p-0">
                            <select class="form-control search-slt" name="category">
                                <option>All</option>
                                <c:forEach var="categorie" items="${category}">
                                    <option>${categorie}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-12 p-0">
                            <input type="text search" class="form-control search-slt" name="article_name" placeholder="Enter Article Name">
                        </div>

                        <div class="col-lg-2 col-md-2 col-sm-12 p-0">
                            <input type="number" min="0" class="form-control search-slt" name="ref" placeholder="Enter Article Reference">
                        </div>

                        <div class="col-lg-2 col-md-2 col-sm-12 p-0">
                            <button type="submit" class="btn btn-danger wrn-btn">Search</button>
                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-12 p-0">
                            <!-- Button to Open the Modal -->
                            <button type="button" class="btn btn-primary wrn-btn" data-toggle="modal" data-target="#myModal">New</button>
                        </div>
                    </div>
                </div>
            </div>

            <div>
            <c:choose>
                <c:when test="${empty search}">
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${empty list}">
                        </c:when>
                        <c:otherwise>
                            <%--                define pagination size with drop down menu--%>
                            <div class="float-right pagination-detail">
                                <div class="dropdown">
                                    <button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        Page size
                                    </button>
                                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                        <a class="dropdown-item" href="?page=${param.page}&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=10">10</a>
                                        <a class="dropdown-item" href="?page=${param.page}&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=50">50</a>
                                        <a class="dropdown-item" href="?page=${param.page}&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=200">200</a>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <h2>Search Results</h2>
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>Reference</th>
                            <th>Name</th>
                            <th>Price</th>
                        </tr>
                        </thead>
                        <tbody >

                        <c:forEach var="article" items="${search}">
                            <%
                                Article a = (Article) pageContext.getAttribute("article");
                                Boolean isProduct = a instanceof Product;
                            %>
                            <tr id="${article.getId()}" type="button" data-toggle="modal" data-target="#updateModal"  onclick="myFunction('${article.getId()}','${article.getName()}', '${article.getCategories().getName()}','${article.getCategoryId()}',${article.getPrice()})">
                                <td>${article.getId()}</td>
                                <td>${article.getName()}</td>
                                <td>${article.getPrice()}</td>
<%--                                <c:choose>--%>
<%--                                    <c:when test="<%=isProduct%>">--%>
<%--                                        <td>${article.getPrice()}</td>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <td>No price</td>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>

                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${empty list}">
                </c:when>
                <c:otherwise>

                <nav aria-label="Page navigation">
                    <ul class="pagination pagination-circle" id="pagination">
                        <li class="page-item">
                            <a class="page-link" href="?page=0&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=${param.size}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <c:forEach var="i"
                                   begin="${list.number-10 > 0 ? list.number-10 : 0}"
                                   end="${list.number+10 < list.totalPages ? list.number+10 : list.totalPages -1}">
                            <li class="${ i == list.number+1 ? 'active' : ''}"><a class="page-link" href="?page=${i}&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=${param.size}">${i}</a></li>
                        </c:forEach>
                        <li class="page-item">
                            <a class="page-link" href="?page=${list.totalPages - 1}&category=${param.category}&article_name=${param.article_name}&ref=${param.ref}&size=${param.size}">
                                <span aria-hidden="true">&raquo;</span>
                                <span class="sr-only">Next</span>
                            </a>
                        </li>
                    </ul>
                </nav>
                </c:otherwise>
            </c:choose>
            </div>
        </form>
    </div>
</section>

<section class="display">
    <c:forEach var="cate" items="${cat}">
        ${cate.getName()}
    </c:forEach>
</section>


<!-- To create a product -->
<section>
<!-- The Modal -->
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Create Product</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <div class="container" id="create-product">
                    <form id="create-product-form" method="POST" action="/articles/add">
                        <div class="form-group">
                            <label>Product Name</label>
                            <input type="text" class="form-control" name="product-name" placeholder="Enter Product Name" required>
                        </div>
                        <div class="form-group">
                            <label>Product Category</label>
                            <input type="text" class="form-control" name="category-name" placeholder="Category" required>
                        </div>
                        <div class="form-group">
                            <label>Category Id</label>
                            <input type="number" min="0" class="form-control" name="category-id" placeholder="Category Id" required>
                        </div>
                        <div class="form-group">
                            <label>Price</label>
                            <input type="text" class="form-control" name="product-price" placeholder="Product Price" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Create</button>
                    </form>
                </div>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>
</section>


<!-- To Update a product -->
<section>
    <!-- The Modal -->
    <div class="modal fade" id="updateModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">Update Product</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <div class="container" id="update-product">
                        <form id="update-product-form" method="POST" action="/articles/update">
                            <div class="form-group">
                                <label>Product Reference</label>
                                <input type="number" id ="id" class="form-control" name="product-id" readonly>
                            </div>
                            <div class="form-group">
                                <label>Product Name</label>
                                <input type="text" id ="productName" class="form-control" name="product-name" placeholder="Enter Product Name" required>
                            </div>
                            <div class="form-group">
                                <label>Product Category</label>
                                <input type="text" class="form-control" id="categoryName" name="category-name" placeholder="Category" required>
                            </div>
                            <div class="form-group">
                                <label>Category Id</label>
                                <input type="number" min="0" id="categoryId" class="form-control" name="category-id" placeholder="Category Id" required>
                            </div>
                            <div class="form-group">
                                <label>Price</label>
                                <input type="text" class="form-control" id="productPrice" name="product-price" placeholder="Product Price" required>
                            </div>

                            <button type="submit" class="btn btn-primary">Update</button>
                        </form>
                    </div>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                </div>

            </div>
        </div>
    </div>
</section>

<script>
    //alert("Hello! I am an alert box!!");
    function myFunction(pId,pName,cName,cId,pPrice){
        const exampleModal = document.getElementById('updateModal')
        const productId = exampleModal.querySelector('#id');
        const name = exampleModal.querySelector('#productName')
        const categoryName = exampleModal.querySelector('#categoryName') ;
        const categoryId = exampleModal.querySelector('#categoryId');
        const productPrice =exampleModal.querySelector('#productPrice');

       // const modalTitle = exampleModal.querySelector('.modal-title')
        name.value = pName;
        categoryName.value = cName;
        categoryId.value = cId;
        productPrice.value = pPrice;
        productId.value = pId;
    }

    function updateSize(e){
        const exampleModal = document.getElementById('pagination')
    }


</script>
<%@include file="_footer.jsp" %>
