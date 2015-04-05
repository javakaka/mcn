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
<title>请假规则说明</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	
	//[@flash_message /]
	
	// 表单验证
	
});
function leavegz(){
	var content = $('#content').val();
	if(content == null || content == ""){
		alert("提交内容不能为空！");
	}else{
	var returnVal = window.confirm("确认提交规则声明！", "标题");
	if(returnVal){
	alert("提交成功");
	document.getElementById('inputForm').submit();
	}
	}
}
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 请假规则说明
	</div>
	<form id="inputForm" action="Leavegzadd.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>请假规则说明:
				</th>
				<td>
					<textarea rows="10" id="content" name="content" cols="100">${content_gz}</textarea>
				</td>
			</tr>
			
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" class="button" onclick="leavegz();" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>