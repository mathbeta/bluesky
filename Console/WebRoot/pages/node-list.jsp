<%@ page language="java" import="com.dc.storage.console.bean.Node,java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<title>SmartStorage云存储</title>
		<%@include file="../common/include.jsp"%>
	</head>
	<body>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#checkAll').click(function(){
					if($('#checkAll').is(':checked')){
						$('#nodeTable').find('input[type=checkbox]').attr('checked',true);
					} else {
						$('#nodeTable').find('input[type=checkbox]').removeAttr('checked')
					}
				});
				
				$('#add').click(function(){
					$('#nodeForm')[0].reset();
					$('#win').css('display','block');
				});
				
				$('#delete').click(function(){
					var check=$('#nodeTable').find('input[type=checkbox]:checked'),ids=[];
					check.each(function(index,el){
						if($(el).next().is('input')){
							ids.push($(el).next('input').val());
						}
					});
					if(ids.length==0){
						alert('Please select records to delete...');
						return;
					}
					$.ajax({
						url: '<%=basePath%>ws/node/delete',
						data: {ids: ids.join(',')},
						dataType: 'json',
						type: 'get',
						success: function(data,status){
							if(data.result){
								alert(status)
							}
							location.reload();
						}
					});
				});
			});
		</script>
		<h1>节点列表</h1>
		<p><input type="button" id="add" value="Add"/><input type="button" id="delete" value="Delete"/></p>
		<table style="border:1px solid #ccc;border-collapse:collapse" id="nodeTable">
			<tr>
				<th style="border:1px solid #ccc;width:30px;text-align:center;"><input type="checkbox" id="checkAll"/></th>
				<th style="border:1px solid #ccc;width:200px">Name</th>
				<th style="border:1px solid #ccc;width:500px">IP</th>
				<th style="border:1px solid #ccc;width:100px">Isolated</th>
			</tr>
			<%
				List<Node> nodes = (List<Node>) request.getAttribute("nodes");
				if(nodes != null && !nodes.isEmpty()) {
					for(Node node : nodes) {
			%>
			<tr>
				<td style="border:1px solid #ccc;width:30px;text-align:center;"><input type="checkbox"/><input type="hidden" name="id" value="<%=node.getId()%>"/></td>
				<td style="border:1px solid #ccc;width:200px"><%=node.getName()%></td>
				<td style="border:1px solid #ccc;width:500px"><%=node.getIp()%></td>
				<td style="border:1px solid #ccc;width:100px"><%=node.isIsolated()%></td>
			</tr>
			<%
					}
				}
			%>
		</table>
		<div id="win" style="display:none;width:400px;height:300px;border:1px solid #ff7800;padding:50px 100px 50px;">
			<form action="<%=basePath%>ws/node/add" method="post" id="nodeForm">
				<p>Node Name: <input type="text" name="name"/></p>
				<p>Node Ip: <input type="text" name="ip"/></p>
				<p>Isolated: <input type="radio" name="isolated" value="true" id="isolated" checked/><label for="isolated">Y</label><input type="radio" name="isolated" value="false" id="nonIsolated"/><label for="nonIsolated">N</label></p>
				<p>Storage Root: <input type="text" name="storageRoot"/></p>
				<p><input type="submit" value="Submit"/></p>
			</form>
		</div>
	</body>
</html>