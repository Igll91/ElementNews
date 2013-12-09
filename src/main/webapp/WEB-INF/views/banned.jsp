<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir='/WEB-INF/tags' prefix='sc'%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login Screen</title>

<!-- Bootstrap -->
<link href="../resources/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="../resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="../resources/bootstrap/js/bootstrap.js"></script>

</head>
<body background="../resources/background.png">

	<div class="jumbotron">
		<h1 class="text-center">Trenutno vam je zabranjen pristup!</h1>

		<c:if test="${not empty message}">
			<div class="alert alert-warning alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				<strong><c:out value="${ message }"></c:out></strong>
			</div>
		</c:if>

		<c:url value="/login/recaptcha.html" var="capURL" />

		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#collapseOne"> Reaktivacija korisničkog računa </a>
				</h4>
			</div>
			<div id="collapseOne" class="panel-collapse collapse in">
				<div class="panel-body">
					<p>Captcha:</p>
					<form action="${capURL }" method="post"> 
					    <sc:captcha/>
					    <input type="submit" value="submit"/> 
					</form>
				</div>
			</div>
		</div>

	</div>

</body>
</html>
