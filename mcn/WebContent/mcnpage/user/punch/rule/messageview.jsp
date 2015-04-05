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
});
</script>
</head>
<body>
	<div class="path">
		<cc:message key="framework.nav.index" /> &raquo; <cc:message key="framework.moudle.add"/>
	</div>
	<form id="inputForm" action="save.do" method="post" enctype="multipart/form-data">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>标题:
				</th>
				<td>
					<input type="text" name="BUREAU_NAME" class="text" maxlength="200" value="${row.MESSAGE_NAME}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>内容:
				</th>
				<td>
					<textarea rows="10" id="content" name="content" cols="100">${row.MESSAGE_CONTENT}</textarea>
				</td>
			</tr>
			<tr>
				<th>
					附件:
				</th>
				<td>
					<input type="text" name="LINKS" class="text" maxlength="200" value="${row.MESSAGE_FJ}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>发送群组:
				</th>
				<td>
					<input type="text" name="NOTES" class="text" maxlength="200" value="${row.MESSAGE_QZ}"/>
				</td>
			</tr>
			
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>