<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title><cc:message key="framework.moudle.add"/></title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {
		// 全选
	var $selectAll = $("#selectall");
	$selectAll.click( function() {
		for(var i=0;i<document.all.length;i++)
		{
		if (document.all(i).type=="checkbox")
		{
		//对checkbox进行处理
			if(document.all(i).checked==true)
			{
			document.all(i).checked=false;
			$selectAll.val("全选");
			}
			else
			{
			document.all(i).checked=true;
			$selectAll.val("反选");
			}
		}
		}
	});
});
function messagesave(){
if($("#message_name").val() == ""){
alert("标题不能为空");
}else if($("#message_content").val() == ""){
alert("内容不能为空");
}else if($("input:checkbox:checked").length <= 0){
alert("发送群组不能为空");
}else{
document.getElementById("inputForm").submit();
}
}
</script>
</head>
<body>
	<div class="path">
		<cc:message key="framework.nav.index" /> &raquo; <cc:message key="framework.moudle.add"/>
	</div>
	<form id="inputForm" action="messageadd.do" method="post" enctype="multipart/form-data">
		<table class="input" id="listTable">
			<tr>
				<th>
					<span class="requiredField">*</span>标题:
				</th>
				<td>
					<input type="text" id="message_name" name="message_name" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>内容:
				</th>
				<td>
					<textarea rows="10" id="message_content" name="message_content" cols="100"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					附件:
				</th>
				<td>
					<input type="file" name="fileElem"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>发送群组:
				</th>
				<td>
						<c:forEach items="${sites}" var="row" varStatus="status">
							<input type="checkbox" id="message_qz" name="message_qz" value="${row.SITE_NAME}"/>${row.SITE_NAME}&nbsp;
						</c:forEach>&nbsp;<input type="button" id="selectall" value="全选">
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="button" onclick="messagesave()" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>