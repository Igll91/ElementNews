<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<body onload='document.f.username.focus();' background="../resources/background.png">

	<div class="container">
	

		<!-- spring security default login form controller -->
		<c:url value="/j_spring_security_check?locale=${param.locale}"
			var="loginUrl" />

		<div class="row">
			<div class="col-md-6 col-md-offset-3 text-center">
				<h2>
					<c:out value="Logirajte se u aplikaciju."></c:out>
				</h2>
				<c:if test="${not empty error}">
					<div class="alert alert-warning alert-dismissable">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						<strong>${error }</strong>
					</div>
				</c:if>

				<form name='f' method='POST' action='${loginUrl}'>
					<div class="form-group">
						<input id="username" type='text' name='j_username' value=''
							class="form-control" placeholder="Korisničko ime" maxlength="30">
					</div>
					<div class="form-group">
						<input type='password' name='j_password' class="form-control"
							placeholder="Lozinka" maxlength="30" />
					</div>
					<div class="form-group">
						<input name="submit" type="submit" value="potvrdi"
							class="btn btn-info btn-lg btn-block" />
					</div>
					<div class="form-group">
						<input name="reset" type="reset" value="poništi"
							class="btn btn-info btn-lg btn-block" />
					</div>
					<div class="form-group">
						<input id="remember_me" name="_spring_security_remember_me"
							type="checkbox" /> <label for="remember_me" class="inline">Zapamti me</label>
					</div>
				</form>

			</div>
		</div>
	</div>
</body>
</html>