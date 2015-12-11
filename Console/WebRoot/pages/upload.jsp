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
		<h1>上传文件</h1>
		<form action="<%=basePath%>ws/storage/upload" enctype="multipart/form-data" method="post">
			<p>File: <input type="file" name="file"/></p>
			<p>Description: <textarea name="description" style="width: 500px;height: 400px;"></textarea></p>
			<p><input type="submit" value="Submit"/></p>
		</form>
	</body>
</html>