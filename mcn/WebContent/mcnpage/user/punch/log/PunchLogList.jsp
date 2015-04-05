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
<% if (exportToExcel == null) { %>
	<div class="path">
		管理中心 &raquo;打卡记录
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="PunchLogList.do" method="get">
		<div class="bar">
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span><cc:message key="admin.common.delete" />
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
				<div class="menuWrap">
					<a href="javascript:;" id="pageSizeSelect" class="button">
						<cc:message key="admin.page.pageSize" /><span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="pageSizeOption">
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 10}">class="current"</c:if> val="10" >10</a>
							</li>
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 20}">class="current"</c:if> val="20">20</a>
							</li>
							<li>
								<a href="javascript:;" <c:if test="${page.pageSize == 50}"> class="current"</c:if>val="50">50</a>
							</li>
							<li>
								<a href="javascript:;"  <c:if test="${page.pageSize == 100}">class="current"</c:if>val="100">100</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div>
				<a href="?exportToExcel=YES" class="iconButton">
				<span>导 出&nbsp;</span>
			</a>
			</div>
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'NAME'}"> class="current"</c:if> val="NAME">姓名</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'PUNCH_TIME'}">class="current"</c:if> val="PUNCH_TIME">打卡时间</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<% }  %> 
		<table id="listTable" class="list">
			<tr align='center'>
				<% if (exportToExcel == null) { %>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<% }  %> 
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
					<a href="javascript:;" class="sort" name="PUNCH_RESULT">打卡结果</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PLACE_NAME">打卡地址</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="IMG_PATH">头像</a>
				</th>
				<% if (exportToExcel == null) { %>
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
				<% }  %> 
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr align='center' style="height:110px;">
				<% if (exportToExcel == null) { %>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<% }  %> 
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
					<td>
						<c:choose>
							<c:when test="${row.PUNCH_RESULT == 1}">正常</c:when>
							<c:when test="${row.PUNCH_RESULT == 2}">早退</c:when>
							<c:when test="${row.PUNCH_RESULT == 3}">迟到</c:when>
							<c:otherwise> </c:otherwise>
						</c:choose>
					</td>
					<td>${row.PLACE_NAME}</td>
					<% if (exportToExcel == null) { %>
					<td><img src="<%=basePath %>${row.IMG_PATH}"  height="100px;"/></td>
					<% }  %> 
					<% if (exportToExcel == null) { %>
					<td>
						<select onchange="updatestatus(this.options[this.options.selectedIndex].value,${row.ID})"><option value="0">正常</option><option value="1">地址不对</option><option value="2">头像不对</option></select>
						<a href="edit.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>
						<a href="#" target="_blank"><cc:message key="admin.common.view" /></a>
					</td>
					<% }  %> 
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>