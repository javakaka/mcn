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
<title>修改打卡检测区域</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	${flash_message}
	// 表单验证
	$inputForm.validate({
		rules: {
			ID: "required",
			PLACE_NAME: "required",
			LONGITUDE: "required",
			LATITUDE: "required",
			STATE: "required",
			RADIUS: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 修改打卡检测区域
	</div>
	<form id="inputForm" action="update.do" method="post">
	<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID}"/>
		<table class="input">
			<tr>
				<th>
					区域检测规则:
				</th>
				<td>
					如果没有设置打卡检测区域，则系统自动标记打卡记录为不需要检测位置的类型；
					如果存在多个检测区域，只要用户打卡的位置在某一个或多个检测区域范围内，则认为打卡位置有效，否则为无效
				</td>
			</tr>
			<tr>
				<th>
					坐标拾取工具:
				</th>
				<td>
					<a href="http://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank" >点击进入</a>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>位置名称:
				</th>
				<td>
					<input type="text" name="PLACE_NAME" class="text" maxlength="200" value="${row.PLACE_NAME }" />
					<input type="hidden" name="MAP_TYPE" class="text" maxlength="200" value="1" />
					<input type="hidden" name="ID" class="text" maxlength="200" value="1" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>经度:
				</th>
				<td>
					<input type="text" name="LONGITUDE" class="text" maxlength="200" value="${row.LONGITUDE }" />(保留六位小数)
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>纬度:
				</th>
				<td>
					<input type="text" name="LATITUDE" class="text" maxlength="200" value="${row.LATITUDE }" />(保留六位小数)
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>半径:
				</th>
				<td>
					<input type="text" name="RADIUS" class="text" maxlength="200" value="${row.RADIUS }" />(整数/米)
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>状态:
				</th>
				<td>
					<select id="STATE" name="STATE" class="text" style="width:190px;" >
						<c:choose>
							<c:when test="${row.STATE ==1 }">
								<option value="" >请选择...</option>
								<option value="1" selected>启用</option>
								<option value="2" >停用</option>
							</c:when>
							<c:when test="${row.STATE ==2 }">
								<option value="" >请选择...</option>
								<option value="1" >启用</option>
								<option value="2" selected>停用</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >启用</option>
								<option value="2" >停用</option>
							</c:otherwise>
						</c:choose>
					</select>
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