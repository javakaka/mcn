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
<script type="text/javascript" src="<%=basePath%>/res/js/list2.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		平台管理 &raquo;通知公告列表
	</div>
	<form id="listForm" action="CompanyList.do" method="get">
		<div class="bar">
			<a href="add2.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span><cc:message key="admin.common.delete" />
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" >编号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" >标题</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" >内容</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" >阅读数</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" >附件</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" >发送群组</a>
				</th>
				<th>
					<span><cc:message key="admin.common.handle" /></span>
				</th>
			</tr>
			<c:forEach items="${page}" var="row" varStatus="status">
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<td>
						${row.ID}
					</td>
					<td>
						${row.MESSAGE_NAME}
					</td>
					<td>
						${row.MESSAGE_CONTENT}
					</td>
					<td>
						${row.READ_TOTAL_NUM}
					</td>
					<td>
						<span>${row.MESSAGE_FJ}</span>
					</td>
					<td>
						<span>${row.MESSAGE_QZ}</span>
					</td>
					<td>
						<a href="messageedit2.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>
						<a href="messageview.do?id=${row.ID}"><cc:message key="admin.common.view" /></a>
					</td>
				</tr>
			</c:forEach>
		</table> 
	</form>
</body>
</html>