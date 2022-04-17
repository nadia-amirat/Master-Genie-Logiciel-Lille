<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<form action="./createUser" method="post">
    <table style="with: 50%">

        <tr>
            <td>UserName</td>
            <td><input type="text" name="email" required/></td>
        </tr>
        <tr>
            <td>PhoneNumber</td>
            <td>
                <input type="text" name="phoneNumber" pattern="\+[0-9]{11}" required/>
                <small>Format: +33100223344</small>
            </td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" required/></td>
        </tr>
    </table>
    <input class="btn btn-success" type="submit" value="Create account" /></form>
</body>
</html>
