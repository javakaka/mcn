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
<title>加班单审批</title>
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
			ID: "required",
			STATUS: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 加班单审批
	</div>
	<form id="inputForm" action="update.do" method="post">
	<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID}"/>
		<table class="input">
			<tr>
				<th>
					用户姓名:
				</th>
				<td>
					${row.USER_NAME}
				</td>
			</tr>
			<tr>
				<th>
					部门:
				</th>
				<td>
					${row.SITE_NAME}
				</td>
			</tr>
			<tr>
				<th>
					开始时间:
				</th>
				<td>
					<c:choose>
							<c:when test="${row.STATUS == 1}">
								<input type="text" name="START_TIME" class="text Wdate" onfocus="WdatePicker()" maxlength="200" value="${row.START_TIME}"/>
							</c:when>
							<c:otherwise>
								${row.START_TIME}
							</c:otherwise>
						</c:choose>
				</td>
			</tr>
			<tr>
				<th>
					结束时间:
				</th>
				<td>
					<c:choose>
							<c:when test="${row.STATUS == 1}">
								<input type="text" name="END_TIME" class="text Wdate" onfocus="WdatePicker()" maxlength="200" value="${row.END_TIME}"/>
							</c:when>
							<c:otherwise>
								${row.END_TIME}
							</c:otherwise>
						</c:choose>
				</td>
			</tr>
			<tr>
				<th>
					状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" style="width:190px;">
						<c:choose>
							<c:when test="${row.STATUS == 1}">
								<option value="1" selected>申请中</option>
								<option value="2" >审核通过</option>
								<option value="3" >审核不通过</option>
							</c:when>
							<c:when test="${row.STATUS == 2}">
								<option value="1" selected>申请中</option>
								<option value="2" >审核通过</option>
								<option value="3" >审核不通过</option>
							</c:when>
							<c:when test="${row.STATUS == 3}">
								<option value="1" selected>申请中</option>
								<option value="2" >审核通过</option>
								<option value="3" >审核不通过</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >申请中</option>
								<option value="2" >审核通过</option>
								<option value="3" >审核不通过</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					申请时间:
				</th>
				<td>
					${row.CREATE_TIME}
				</td>
			</tr>
			<tr>
				<th>
					备注:
				</th>
				<td>
					<input type="text" name="REMARK" class="text" maxlength="200" value="${row.REMARK}"/>
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