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
<title>假期设置</title>
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
		管理中心 &raquo; 修改用户
	</div>
	<form id="inputForm" action="saveSetting.do" method="post">
	<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID}"/>
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>年假:
				</th>
				<td>
					<input type="text" name="year" class="text" value="${row.YEAR}"  maxlength="200" />
					<input type="hidden" name="id" class="text" value="${row.ID}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>病假:
				</th>
				<td>
					<input type="text" name="sick" class="text" value="${row.SICK}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>调休:
				</th>
				<td>
					<input type="text" name="exchange" class="text" value="${row.EXCHANGE}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>事假:
				</th>
				<td>
					<input type="text" name="personal" class="text" value="${row.PERSONAL}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>外出:
				</th>
				<td>
					<input type="text" name="outing" class="text" value="${row.OUTING}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>加班:
				</th>
				<td>
					<input type="text" name="work" class="text" value="${row.WORK}"  maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>打卡设置:
				</th>
				<td>
				<c:if test="${row.PUNCHSZ == 0}">
					<input type="radio" id="punchsz" name="punchsz" value="0" checked="checked"/>
				</c:if>
				<c:if test="${row.PUNCHSZ != 0}">
					<input type="radio" id="punchsz" name="punchsz" value="0"/>
				</c:if>
					&nbsp;两次打卡&nbsp;&nbsp;<span class="requiredField">(注：即早上上班和下午下班打卡)</span><br/>
				<c:if test="${row.PUNCHSZ == 1}">
					<input type="radio" id="punchsz" name="punchsz" value="1" checked="checked"/>
				</c:if>
				<c:if test="${row.PUNCHSZ != 1}">
					<input type="radio" id="punchsz" name="punchsz" value="1" />
				</c:if>
					&nbsp;四次打卡&nbsp;&nbsp;<span class="requiredField">(注：即上午上下班和下午上下班打卡)</span>
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