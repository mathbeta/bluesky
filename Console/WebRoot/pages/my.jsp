<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<title>SmartStorage云存储</title>
		<%@include file="../common/include.jsp"%>
		<style type="text/css">
			#level div,#breadcrumb,.toolbar{height:30px;line-height:30px;padding:0 40px;font-size:24px;font-family:Yahei Microsoft;}
			#breadcrumb{margin:20px 0;}
			#level div input{margin-right:10px;}
			#level div a{text-decoration:none;}
			.toolbar input{margin:0 5px;}
			.add-panel,.upload-panel{display:none;width:300px;height:180px;margin:0 auto;border:1px solid #ccc;}
			.upload-panel{width:700px;height:400px;}
		</style>
	</head>
	<body>
		<script type="text/javascript">
			String.prototype.endsWith=function(c){
				if(this==null||this==undefined)
					return true;
				if(c==null||c==undefined)
					return false;
				if(this.length>=c.length){
					for(var i=0;i<c.length;i++){
						if(this.charAt(this.length-i-1)!=c.charAt(c.length-i-1))
							return false;
					}
					return true;
				}
				return false;
			}
			var curPath='/',curParentId=null;
			$(document).ready(function(){
				$.ajax({
					url: '<%=basePath%>ws/storage/getUserRootId',
					dataType: 'json',
					type: 'get',
					async: true,
					success: function(data,textStatus){
						curParentId=data;
						ls(curPath,curParentId);
					}
				});
				
				// add button
				$('#add-tool').click(function(){
					$('.add-panel').css('display','block');
					$('.add-panel #path').val('');
				});
				// upload button
				$('#upload-tool').click(function(){
					$('.upload-panel').css('display','block');
					$('.upload-panel #path').val(curPath);
				});
				// delete button
				$('#dlt-tool').click(function(){
					var checks=$('#level div input:checked'),ids=[];
					if(checks.length==0){
						alert('请选择要删除的文件或文件夹！');
						return;
					}
					for(var i=0;i<checks.length;i++){
						ids.push($(checks[i]).attr('i'));
					}
					$.ajax({
						url: '<%=basePath%>ws/storage/delete',
						dataType: 'json',
						data: {'ids': ids.join(',')},
						type: 'post',
						async: true,
						success: function(data,textStatus){
							if(!data.result){
								alert(data.message);
							}
							ls(curPath,curParentId);
						}
					});
				});
				
				$('.add-panel #addBtn').click(function(){
					var path=$('.add-panel #path').val();
					if(path=='') {
						alert('目录名称不能为空，且不能包含 /\?":*<>! 中任一个！');
						return;
					}
					$.ajax({
						url: '<%=basePath%>ws/storage/add-dir',
						data: {path: path,curPath: curPath, parentId: curParentId},
						dataType: 'json',
						type: 'post',
						async: true,
						success: function(data,textStatus){
							if(data.result){
								alert('添加成功！');
								ls(curPath,parentId);
								$('.add-panel').css('display','none');
							}else{
								alert('添加失败！'+data.message);
							}
						}
					});
				});
			});
			
			function ls(path, parentId){
				var rp=path.substr(1);
				if(rp==''){
					var span=$('<span>/</span>');
					span.attr('p',path);
					span.attr('i',parentId);
					$('#b').html(span);
				}else{				
					var span=$('#b span:last'),a=$('<a href="javascript:void(0)"></a>');
					$('#b').append(a);
					a.html(span.html());
					a.attr('p',span.attr('p'));
					a.attr('i',span.attr('i'));
					a.click(function(){
						cb(a);
					});
					span.remove();
					var s=rp.split('/'),span=$('<span></span>');
					span.html(s[s.length-1]+'/');
					span.attr('p',path);
					span.attr('i',parentId);
					$('#b').append(span);
				//	$('#b').append('<span>/</span>');
				}
				$.ajax({
					url: '<%=basePath%>ws/storage/list/'+parentId,
					dataType: 'json',
					type: 'get',
					async: true,
					success: function(data,textStatus){						
						$('#level').html('');
						if(data!=null && data.length>0){
							for(var i=0;i<data.length;i++){
								(function(i){
									var wrap=$('<div><input type="checkbox"/></div>'),a=$('<a href="javascript:void(0)"></a>');
									a.html(data[i].name);
									wrap.find('input').attr('i',data[i].id);
									a.attr('pid',curParentId);
									a.click(function(){
										if(data[i].dir){
											if(curPath.endsWith('/')){
												curPath+=data[i].name;
											}else{
												curPath+='/'+data[i].name;
											}
											curParentId=data[i].id;
											ls(curPath,curParentId);
										}else{
											a.attr('href','<%=basePath%>ws/storage/download?path='+curPath+'/'+data[i].name);
										}
									});
									wrap.append(a);
									$('#level').append(wrap);
								})(i);
							}
						}
					}
				});
			}
			
			function cb(a){
				var c=$('#b').children(),k;
				for(var i=0;i<c.length;i++){
					if(c[i]==a[0]){
						k=i
						break;
					}
				}
				for(var i=k;i<c.length;i++){
					c[i].remove();
				}
			//	var span=$('<span></span>');
			//	span.html(a.html());
			//	span.attr('p',a.attr('p'));
			//	span.attr('i',a.attr('i'));
			//	$('#b').append(span);
			//	if(a.html()!='/'){
				//	$('#b').append('<span>/</span>');
				//	span.append('/');
			//	}
			//	c[k].remove();
				curPath=a.attr('p');
				curParentId=a.attr('i');
				ls(curPath, curParentId);
			}
		</script>
		<h1>我的文件</h1>
		<div class="toolbar">
			<input type="button" id="add-tool" value="创建目录"/>
			<input type="button" id="dlt-tool" value="删除"/>
			<input type="button" id="upload-tool" value="上传文件"/>
		</div>
		<div id="breadcrumb"><span>当前路径：</span><span id="b">/</span></div>
		<div id="level">
		</div>
		<div class="add-panel">
			<table>
				<tr><td with="150">目录名称：</td><td width="200"><input type="text" name="path" id="path"/></td></tr>
				<tr><td colspan="2"><input type="button" id="addBtn" value="创建"/></td></tr>
			</table>
		</div>
		<div class="upload-panel">
			<h1>上传文件</h1>
			<form action="<%=basePath%>ws/storage/upload" enctype="multipart/form-data" method="post" id="uploadForm">
				<p>File: <input type="file" name="file"/><input type="hidden" name="path" id="path"/></p>
				<p>Description: <textarea name="description" style="width: 400px;height: 200px;"></textarea></p>
				<p><input type="submit" value="提交"/></p>
			</form>
		</div>
	</body>
</html>