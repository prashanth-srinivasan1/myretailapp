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
<title>Search for Products</title>
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
							<h1>Search for Products</h1>
							<form:form action="/myretailapp/app/search" method="post"
								modelAttribute="search">
								<p>Search for Products</P>
								<ul class="nav nav-tabs">
									
									<li class="active"><a href="#menu1">By Id(s)</a></li>
									<li><a href="#menu2">By Category</a></li>									
								</ul>

								<div class="tab-content">
									<div id="menu1" class="tab-pane fade in active">
										<h3>Search By Product Id(s)</h3>
										<p>Enter a list of product ids separated by commas</p>
										<table id="" class="display" cellspacing="10"	width="100%">
											<thead />
											<tbody>
												<tr>
													<td><form:label path="ids">Product ID(s) :</form:label></td>
													<td><form:input path="ids" /></td>
												</tr>
												<tr>
												<td colspan="2"><input type="submit" value="Search" onclick="category.value=null;"/></td>
												</tr>
											</tbody>
										</table>
									</div>
									<div id="menu2" class="tab-pane fade">
										<h3>Search By Product Category</h3>
										<p>Enter a product Category to search by</p>
										<table id="" class="display" cellspacing="10"	width="100%">
											<thead />
											<tbody>
												<tr>
													<td><form:label path="category">Product Category :</form:label></td>
													<td><form:input path="category" /></td>
												</tr>
												<tr>
												<td colspan="2"><input type="submit" value="Search" onclick="ids.value=null;"/></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</form:form>
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
			$(".nav-tabs a").click(function() {
				$(this).tab('show');
			});
			$('.nav-tabs a').on('shown.bs.tab', function(event) {
				var x = $(event.target).text(); // active tab
				var y = $(event.relatedTarget).text(); // previous tab
				$(".act span").text(x);
				$(".prev span").text(y);
			});
			
			$('table.display').DataTable();			
		});
	</script>
</body>
</html>


