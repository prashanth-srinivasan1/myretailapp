<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>Product Search Results</title>
<link rel="stylesheet"
	href="<c:url value='/resources/css/bootstrap.min.css' />" />
<link href="<c:url value='/resources/css/simple-sidebar.css' />"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<c:url value="https://cdn.datatables.net/1.10.10/css/jquery.dataTables.min.css"/>" />
</head>

<body>
	<div id="wrapper">
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand"><a href="/myretailapp/app/">
						MyRetail Home </a></li>
				<li><a href="/myretailapp/app/add">Add Product</a></li>
				<li><a href="/myretailapp/app/edit">Edit Product</a></li>
				<li><a href="/myretailapp/app/search">Search Product(s)</a></li>
				<li><a href="/myretailapp/app/delete">Delete Product</a></li>
			</ul>
		</div>
		<div id="page-content-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<div class="body">
							<h1>Product Search Results</h1>

							<p>Search Results</P>
							<table id="resultstable" class="table table-striped table-bordered"
								class="display" cellspacing="10" width="100%">
								<thead>
									<tr>
										<th>Id</th>
										<th>Product Name</th>
										<th>Sku</th>
										<th>Category</th>
										<th>Price</th>
										<th>Last Updated</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="product" items="${result}">
										<tr>
											<td>${product.id}</td>
											<td>${product.prodName}</td>
											<td>${product.sku}</td>
											<td>${product.category}</td>
											<td>${product.price.price}</td>
											<td>${product.lastUpdated}</td>
										</tr>										
									</c:forEach>
								</tbody>
							</table>
							<div>
								<p style="color: red">${error}</p>
							</div>
						</div>

						<br /> <br /> <br /> <a href="#menu-toggle"
							class="btn btn-default" id="menu-toggle">Toggle Menu</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="<c:url value='/resources/js/jquery.js' />"></script>
	<script src="<c:url value='/resources/js/bootstrap.min.js' />"></script>
	<script
		src="<c:url value="https://cdn.datatables.net/1.10.10/js/jquery.dataTables.min.js"/>"></script>
	<script>
		$("#menu-toggle").click(function(e) {			
			e.preventDefault();
			$("#wrapper").toggleClass("toggled");
		});

		$(document).ready(function() {
			$('#resultstable').DataTable();
		});
	</script>
</body>
</html>


