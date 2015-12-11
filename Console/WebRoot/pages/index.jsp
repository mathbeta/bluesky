<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<title>SmartStorage云存储</title>
		<%@include file="../common/include.jsp"%>
	</head>
	<body>
		<script type="text/javascript">
			
		</script>
		<h1>用户登录</h1>
		<form action="<%=basePath%>ws/user/login" method="post">
			<p>User Name: <input type="text" name="userName" placeholder="User Name"/></p>
			<p>Password: <input type="password" name="password" placeholder="Password"/></p>
			<p><input type="submit" value="Submit"/></p>
		</form>
	</body>
</html>