<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String user_id = request.getParameter("user_id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>打卡记录</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	//[@flash_message /]

});
function updatestatus(value,id){
var url = "<%=basePath%>/mcnpage/user/punch/log/updatestatus.do";
var params = {punch_status:value,id:id}
$.post(url,params,function cbk(){
alert("修改成功");
});
}
</script>
</head>
<body>
<% String exportToExcel = request.getParameter("exportToExcel");
if (exportToExcel != null && exportToExcel.toString().equalsIgnoreCase("YES")) {
response.setContentType("application/vnd.ms-excel");             
response.setHeader("Content-Disposition", "inline; filename=" + "excel.xls");//导出的方式
//response.setHeader("Content-disposition","attachment; filename=test2.xls");//下载的方式： 
} %>
<% if(exportToExcel == null){ %>
	<div class="path">
		管理中心 &raquo;打卡记录
	</div>
	<form id="listForm" action="" method="get">
		<div class="bar">
			<div class="buttonWrap">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
				<a href="?exportToExcel=YES&user_id=<%=user_id%>" class="iconButton">
				<span>导 出&nbsp;</span>
				</a>
				<a href="javascript:;" onclick="javascript:history.go(-1);" class="iconButton">
				<span>&nbsp;返回&nbsp;</span>
			</a>
			</div>
		</div>
		<% } %> 
		<table id="listTable" class="list">
			<tr align='center'> 
				<th>
					<a href="javascript:;" class="sort" name="NAME">姓名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_TYPE">类型</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_TIME">打卡时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PLACE_NAME">打卡地址</a>
				</th>
				<% if(exportToExcel == null){ %>
				<th>
					<a href="javascript:;" class="sort" name="IMG_PATH">头像</a>
				</th>
				<th>
					<span>迟到分钟</span>
				</th>
				<% } %> 
			</tr>
			<c:forEach items="${page}" var="row" varStatus="status">
				<tr align='center' style="height:110px;">
					<td>
						<span title="${row.NAME}">${row.NAME}</span>
					</td>
					<td>
						<c:choose>
							<c:when test="${row.PUNCH_TYPE == 1}">上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 2}">下班</c:when>
							<c:when test="${row.PUNCH_TYPE == 3}">上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 4}">下班</c:when>
							<c:otherwise> </c:otherwise>
						</c:choose>
					</td>
					<td>
						<span title="${row.PUNCH_TIME}">${row.PUNCH_TIME}</span>
					</td>
					<!--
					<td>
						<c:choose>
							<c:when test="${row.PUNCH_RESULT == 1}">正常</c:when>
							<c:when test="${row.PUNCH_RESULT == 2}">早退</c:when>
							<c:when test="${row.PUNCH_RESULT == 3}">迟到</c:when>
							<c:otherwise> </c:otherwise>
						</c:choose>
					</td>
					-->
					<td>${row.PLACE_NAME}</td> 
					<% if (exportToExcel == null) { %>
					<td><img src="<%=basePath %>${row.IMG_PATH}"  height="100px;"/></td>
					<% }  %> 
					<td>${row.PUNCH_MM}</td> 
				</tr>
			</c:forEach>
		</table>
	</form>
</body>
</html>