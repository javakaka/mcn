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
</head>
<body>
	<div class="path">
		<cc:message key="framework.nav.index" /> &raquo; <cc:message key="framework.moudle.add"/>
	</div>
	<form id="inputForm" action="" method="post" >
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>天数设置:
				</th>
				<td>
					<input type="text" name="BUREAU_NAME" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					审批人选项:
				</th>
				<td>
					<input type="checkbox" id="check_punch"        name="modules"  maxlength="10" value="punch" />小王
					<input type="checkbox" id="check_reimburse"    name="modules"  maxlength="10" value="reimburse" />小李
					<input type="checkbox" id="check_task"         name="modules"  maxlength="10" value="task" />小明
					<input type="checkbox" id="check_meeting_room" name="modules"  maxlength="10" value="meeting_room" />小花
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>