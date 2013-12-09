<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Podaci</title>

<!-- Bootstrap -->
<link href="resources/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="resources/bootstrap/js/bootstrap.js"></script>

</head>
<body>

<c:url value="podaci.html" var="podaciURL" />
	<div class="row">
		<div class="col-md-6 text-center">
				<h1>
					<a href="<c:url value='/' />"> Home</a>
				</h1>
		</div>

		<div class="col-md-6 text-center">
		</div>
		
		<div class="col-md-6 text-center">
				<h1>
					<a href="<c:url value='j_spring_security_logout' />"> Logout</a>
				</h1>
		</div>
	</div>

	<div class="row">
	<div class=" col-md-offset-3 col-md-6 col-md-offset-3 text-center">
		<form action="${podaciURL }" method="POST">
			<ul class="pagination">
				<c:forEach var="i" begin="1" end="${numberOfNews }">
					<li><input type="submit" class="btn btn-primary" value="${i}"
						name="page" /></li>
				</c:forEach>
			</ul>
		</form>
	</div>
	</div>
	<div class="panel panel-default">
		
		<c:forEach var="entry" items="${news}">

			<div class="panel-heading">
				<h1>
					${entry.getTitle()} <small>${entry.getAuthor()}</small>
				</h1>
			</div>
			<div class="panel-body">
				<p>${entry.getText()}</p>
			</div>

		</c:forEach>
	</div>
</body>
</html>
