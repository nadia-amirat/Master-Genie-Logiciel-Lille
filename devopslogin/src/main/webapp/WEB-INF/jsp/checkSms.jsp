<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Double Auth</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<c:if test="${popup}">
    <p style="color : red">${popup}</p>
</c:if>

<form action="/broker" method="post">
    <table style="with: 50%">
        <tr>
            <td>Final password : </td>
            <td><input type="password" name="userCode" required/>
            <small>Format: passwordCodeSMS</small></td>
        </tr>
    </table>

    <input type="hidden" name="email" value="${email}"/></td>
    <input class="btn btn-primary" type="submit" value="Login" />
</form>

<form action="/api/v1/sms/sendSms" method="post">
    <input type="hidden" name="email" value="${email}"/></td>
    <input class="btn btn-primary" type="submit" value="Resend Code" />
    <small>You could resend an SMS code after 5 minutes.</small>
</form>

</body>
</html>
