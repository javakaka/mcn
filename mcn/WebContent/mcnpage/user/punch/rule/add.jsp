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
<title>添加打卡规则</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	//[@flash_message /]
	// 表单验证
	$inputForm.validate({
		rules: {
			DEPART_ID: "required"
		}
	});
});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 添加打卡规则
	</div>
	<form id="inputForm" action="save.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>请选择部门:
				</th>
				<td>
					<select id="DEPART_ID" name="DEPART_ID" class="text" maxlength="200" >
						<option value="" selected>请选择...</option>
						<c:forEach items="${sites}" var="row" varStatus="status">
							<option value="${row.SITE_NO}">${row.SITE_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					上午上班:
				</th>
				<td>
					<input type="text" name="AM_START" class="text Wdate" value="" onfocus="WdatePicker({dateFmt:'HH:mm'});"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					上午下班:
				</th>
				<td>
					<input type="text" name="AM_END" class="text Wdate" value="" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					下午上班:
				</th>
				<td>
					<input type="text" name="PM_START" class="text Wdate" value="" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					下午上班:
				</th>
				<td>
					<input type="text" name="PM_END" class="text Wdate" value="" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
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