<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home</title>

<!-- Bootstrap -->
<link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="resources/bootstrap/js/bootstrap.js"></script>

</head>
<body background="resources/background.png">

	<div class="jumbotron">
		<h1 class="text-center">Dobrodošli!</h1>

		<h2>
			<a href="<c:url value='j_spring_security_logout' />"> Logout</a>
		</h2>

		<c:if test="${not empty error}">
			<div class="alert alert-warning alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				<strong><c:out value="${ error }"></c:out></strong>
			</div>
		</c:if>

		<c:url value="dohvati.html" var="dohvatiURL" />
		<c:url value="podaci.html" var="podaciURL" />

		<div class="row">
			<div class=" col-md-offset-3 col-md-6 col-md-offset-3 text-center">
				<form action="${dohvatiURL }" method="POST">
					<input type="submit" class="btn btn-primary"
						value="Dohvati podatke Vijesti" name="data" /> <input
						type="submit" class="btn btn-primary"
						value="Dohvati podatke Sport" name="data" />
				</form>
			</div>
		</div>
		
		<div class="row">
			<div class=" col-md-offset-3 col-md-6 col-md-offset-3 text-center">
				<form action="${podaciURL }" method="POST">
					<input type="submit" class="btn btn-primary"
						value="Pregled podataka" name="data" />
				</form>
			</div>
		</div>
	</div>

</body>
</html>


