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
<title>公司列表</title>
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
		平台管理 &raquo;公司列表
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="CompanyList.do" method="get">
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
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'BUREAU_NAME'}"> class="current"</c:if> val="BUREAU_NAME">企业名称</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'LINKS'}">class="current"</c:if> val="LINKS">企业域名</a>
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
					<a href="javascript:;" class="sort" name="BUREAU_NO">编号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="TOKEN">TOKEN</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="BUREAU_NAME">企业名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="AREA_CODE">企业LOGO</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="LINKS">企业域名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="BEGIN_DATE">开始日期</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">结束日期</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="USER_SUM">用户数</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="STATUS">状态</a>
				</th>
				<!-- 
				<th>
					<a href="javascript:;" class="sort" name="NOTES">企业登录网址</a>
				</th>
				-->
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${row.BUREAU_NO}" />
					</td>
					<td>
						${row.BUREAU_NO}
					</td>
					<td>
						${row.TOKEN}
					</td>
					<td>
						${row.BUREAU_NAME}
					</td>
					<td>
						${row.AREA_CODE}
					</td>
					<td>
						<span>${row.LINKS}</span>
					</td>
					<td>
						<span>${row.BEGIN_DATE}</span>
					</td>
					<td>
						<span>${row.END_DATE}</span>
					</td>
					<td>
						<span>${row.USER_SUM}</span>
					</td>
					<td>
						<span>${row.STATUS}
						<c:choose>
						<c:when test="${row.STATUS == 1}">正常</c:when>
						<c:when test="${row.STATUS == 2}">停用</c:when>
						<c:when test="${row.STATUS == 3}">删除</c:when>
						<c:otherwise>--</c:otherwise>
						</c:choose>
						</span>
					</td>
					<!-- 
					<td>
						<span>${row.NOTES}</span>
					</td>
					-->
					<td>
						<a href="edit.do?id=${row.BUREAU_NO}"><cc:message key="admin.common.edit" /></a>
						<a href="view.do" target="_blank"><cc:message key="admin.common.view" /></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 

	</form>
</body>
</html>