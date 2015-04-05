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
<title>预定记录</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo;预定记录
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="BookList.do" method="get">
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
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<!--
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'ROOMNAME'}"> class="current"</c:if> val="ROOMNAME">会议室名称</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'USERNAME'}"> class="current"</c:if> val="USERNAME">预定人姓名</a>
						</li>
						-->
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'START_TIME'}"> class="current"</c:if> val="START_TIME">开始时间</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'END_TIME'}">class="current"</c:if> val="END_TIME">结束时间</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="ROOMNAME">会议室名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="USERNAME">预定人姓名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="START_TIME">开始时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_TIME">结束时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="USE_FOR">用途</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="MEETERS">参会人员</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="STATUS">状态</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="CREATE_TIME">预定时间</a>
				</th>
				
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<td>
						<span title="${row.ROOMNAME}">${row.ROOMNAME}</span>
					</td>
					<td>
						<span title="${row.USERNAME}">${row.USERNAME}</span>
					</td>
					<td>
						${row.START_TIME}
					</td>
					<td>
						${row.END_TIME}
					</td>
					<td>
						${row.USE_FOR}
					</td>
					<td>
						${row.MEETERS}
					</td>
					<td>
						<c:choose>
							<c:when test="${row.STATUS == 1}">预定成功</c:when>
							<c:when test="${row.STATUS == 0}">预定失败</c:when>
						</c:choose>
					</td>
					<td>
						${row.CREATE_TIME}
					</td>
					<td>
						<a href="edit.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>
						<a href="#" target="_blank"><cc:message key="admin.common.view" /></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>