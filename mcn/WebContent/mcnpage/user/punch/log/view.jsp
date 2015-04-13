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
<title>打卡记录详情</title>
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
			name: "required",
			num: "required"
		}
	});

});
</script>
</head>
<body>
	<div class="path">
		考勤管理 &raquo;打卡详情
	</div>
	<form id="inputForm" action="update.do" method="post">
		<input type="hidden" name="ID" value="${row.ID}" />
		<table class="input">
			<tr>
				<th>
					人员姓名:
				</th>
				<td>
					${row.NAME}
				</td>
			</tr>
			<tr>
				<th>
					人员头像:
				</th>
				<td>
					<img src="<%=basePath %>${row.IMG_PATH}"  height="100px;" name="IMG_PATH" />
				</td>
			</tr>
			<tr>
				<th>
					打卡类型:
				</th>
				<td>
					<c:choose>
							<c:when test="${row.PUNCH_TYPE == 1}">上午上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 2}">上午下班</c:when>
							<c:when test="${row.PUNCH_TYPE == 3}">下午上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 4}">下午下班</c:when>
							<c:when test="${row.PUNCH_TYPE == 5}">加班开始</c:when>
							<c:when test="${row.PUNCH_TYPE == 6}">加班结束</c:when>
							<c:otherwise>-- </c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>
					打卡时间:
				</th>
				<td>
					${row.PUNCH_TIME}
				</td>
			</tr>
			<tr>
				<th>
					打卡结果:
				</th>
				<td>
					<c:choose>
							<c:when test="${row.PUNCH_RESULT == 1}">正常</c:when>
							<c:when test="${row.PUNCH_RESULT == 2}">早退</c:when>
							<c:when test="${row.PUNCH_RESULT == 3}">迟到</c:when>
							<c:otherwise>--</c:otherwise>
						</c:choose>
				</td>
			</tr>
			<tr>
				<th>
					打卡地址:
				</th>
				<td>
					${row.PLACE_NAME}
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" class="button" id="printBtn" name="printBtn" value="打印" onclick="window.print();"/>
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>