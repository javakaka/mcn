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
<title>打卡规则切换记录</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	//[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo;打卡规则切换记录
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="PunchRuleHistoryList.do" method="get">
		<div class="bar">
			<!-- 
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			-->
			<div class="buttonWrap">
				<!-- 
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span><cc:message key="admin.common.delete" />
				</a>
				-->
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
			<!-- 
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'AM_START'}">class="current"</c:if> val="AM_START">上午上班</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'AM_END'}">class="current"</c:if> val="AM_END">上午下班</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'PM_START'}">class="current"</c:if> val="PM_START">下午上班</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'PM_END'}">class="current"</c:if> val="PM_END">下午下班</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'BAK_DATE'}">class="current"</c:if> val="BAK_DATE">备份日期</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'RUN_DATE'}">class="current"</c:if> val="RUN_DATE">生效日期</a>
						</li>
					</ul>
				</div>
			</div>
			-->
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="DEPART_NAME">部门名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="AM_START">上午上班时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="AM_END">上午下班时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PM_START">下午上班时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PM_END">下午下班时间</a>
				</th>
				<!-- 
				<th>
					<a href="javascript:;" class="sort" name="BAK_DATE">备份日期</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="RUN_DATE">生效日期</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="IS_DEAL">状态</a>
				</th>
				-->
				<!-- 
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
				-->
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<td>
						<span title="${row.DEPART_NAME}">${row.DEPART_NAME}</span>
					</td>
					<td>
						<span title="${row.AM_START}">${row.AM_START}</span>
					</td>
					<td>
						<span title="${row.AM_END}">${row.AM_END}</span>
					</td>
					<td>
						${row.PM_START}
					</td>
					<td>
						${row.PM_END}
					</td>
					<!--
					<td>
						${row.BAK_DATE}
					</td>
					<td>
						${row.RUN_DATE}
					</td>
					<td>
						<c:choose>
							<c:when test="${row.IS_DEAL ==1 }">已处理</c:when>
							<c:when test="${row.IS_DEAL ==0 }">待处理</c:when>
						</c:choose>
					</td>
					-->
					<!-- 
					<td>
						<a href="edit.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>
						<a href="PunchRuleHistoryList.do?d_id=${row.DEPART_ID }" target="_blank">切换记录</a>
					</td>
					-->
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>