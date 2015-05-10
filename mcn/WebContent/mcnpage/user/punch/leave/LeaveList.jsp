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
<title>请假列表</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<style type="text/css">
.moreTable th {
	width: 80px;
	line-height: 25px;
	padding: 5px 10px 5px 0px;
	text-align: right;
	font-weight: normal;
	color: #333333;
	background-color: #f8fbff;
}

.moreTable td {
	line-height: 25px;
	padding: 5px;
	color: #666666;
}

.promotion {
	color: #cccccc;
}
</style>
<script type="text/javascript">
$().ready(function() {

	${flash_message}
	var $listForm = $("#listForm");
	var $moreButton = $("#moreButton");
	var $filterSelect = $("#filterSelect");
	var $filterOption = $("#filterOption a");
	
	// 更多选项
	$moreButton.click(function() {
		$.dialog({
			title: "更多选项",
			content:'<table id="moreTable" class="moreTable">'
					+'<tr>'
					+'<th>请假开始日期:<\/th>'
					+'<td>'
					+'<input id="start_date" name="start_date" class="text Wdate" onfocus="WdatePicker();" value="${start_date}" />'
					+'<\/td>'
					+'<\/tr>'
					+'<tr>'
					+'<th>请假结束日期:<\/th>'
					+'<td>'
					+'<input id="end_date" name="end_date" class="text Wdate" onfocus="WdatePicker();" value="${end_date}" />'
					+'<\/td>'
					+'<\/tr>'
					+'<\/table>',
			width: 470,
			modal: true,
			ok: "确定",
			cancel: "取消",
			onOk: function() {
				$("#moreTable :input").each(function() {
					var $this = $(this);
					$("#" + $this.attr("name")).val($this.val());
				});
				$listForm.submit();
			}
		});
	});
	
	// 筛选
	$filterSelect.mouseover(function() {
		var $this = $(this);
		var offset = $this.offset();
		var $menuWrap = $this.closest("div.menuWrap");
		var $popupMenu = $menuWrap.children("div.popupMenu");
		$popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
		$menuWrap.mouseleave(function() {
			$popupMenu.hide();
		});
	});
	
	// 筛选选项
	$filterOption.click(function() {
		var $this = $(this);
		var $dest = $("#" + $this.attr("name"));
		if ($this.hasClass("checked")) {
			$dest.val("");
		} else {
			$dest.val($this.attr("val"));
		}
		$listForm.submit();
		return false;
	});

});
</script>
<style type="text/css">
table { table-layout: fixed; } 
td {overflow:hidden; white-space:nowrap; text-overflow:ellipsis;}
</style>
</head>
<body>
<% String exportToExcel = request.getParameter("exportToExcel");
if (exportToExcel != null && exportToExcel.toString().equalsIgnoreCase("YES")) {
response.setContentType("application/vnd.ms-excel");             
//response.setHeader("Content-Disposition", "inline; filename=" + "excel.xls");//导出的方式
response.setHeader("Content-disposition","attachment; filename=excel.xls");//下载的方式： 
} %>
<% if (exportToExcel == null) { %>
	<div class="path">
		管理中心 &raquo;请假列表
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="LeaveList.do" method="get">
	<input type="hidden" id="status" name="status" value="<c:if test="${status !=''}">${status}</c:if>" />
	<input type="hidden" id="start_date" name="start_date" value="${start_date}" />
	<input type="hidden" id="end_date" name="end_date" value="${end_date}" />
	
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
					<a href="javascript:;" id="filterSelect" class="button">
						审批状态<span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="filterOption" class="check">
							<li>
								<a href="javascript:;" name="status" val="1" <c:if test="${status == '1'}"> class="checked" </c:if> >审批中</a>
							</li>
							<li>
								<a href="javascript:;" name="status" val="2" <c:if test="${status == '2'}"> class="checked" </c:if> >审批通过</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="status" val="3" <c:if test="${status == '3'}"> class="checked" </c:if> >审批不通过</a>
							</li>
						</ul>
					</div>
				</div>
				<a href="javascript:;" id="moreButton" class="button">更多选项</a>
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
					<a href="javascript:;" class="sort" name="START_DATE">请假天数</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="START_DATE">开始时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">结束时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">假期类型</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">请假原因</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">流程过程</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="END_DATE">审批状态</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="STATUS">请假提交时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="REASON">最后审批时间</a>
				</th>
				<!--
				<th>
					<a href="javascript:;" class="sort" name="AUDIT_OBJECTION">审批意见</a>
				</th>
				-->
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr  align='center'>
					<% if (exportToExcel == null) { %>
					<td>
						<input type="checkbox" name="ids" value="${row.ID}" />
					</td>
					<% }  %>
					<td>
						<span title="${row.REASON}">${row.NAME}</span>
					</td>
					<td>
						<span title="${row.SUM_DAY}">${row.SUM_DAY}</span>
					</td>
					<td>
						<span title="${row.SUM_DAY}">${row.START_DATE}</span>
					</td>
					<td>
						<span title="${row.SUM_DAY}">${row.END_DATE}</span>
					</td>
					<td>
					<c:if test="${row.LEAVE_TYPE == 1}">
						<span title="${row.SUM_DAY}">年假</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 2}">
						<span title="${row.SUM_DAY}">病假</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 3}">
						<span title="${row.SUM_DAY}">调休</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 4}">
						<span title="${row.SUM_DAY}">加班</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 5}">
						<span title="${row.SUM_DAY}">事假</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 6}">
						<span title="${row.SUM_DAY}">外出</span>
					</c:if>
					<c:if test="${row.LEAVE_TYPE == 7}">
						<span title="${row.SUM_DAY}">其它</span>
					</c:if>
					</td>
					<td width="20%">
						<span title="${row.STATU_NAME}">${row.REASON}</span>
					</td>
					<td>
						<span title="${row.STATU_NAME}">${row.STATU_NAME}</span>
					</td>
					<td>
					<c:if test="${row.STATUS == 1}">
						<span title="${row.STATU_NAME}">审批中</span>
					</c:if>
					<c:if test="${row.STATUS == 2}">
						<span title="${row.STATU_NAME}">审批通过</span>
					</c:if>
					<c:if test="${row.STATUS == 3}">
						<span title="${row.STATU_NAME}">审批不通过</span>
					</c:if>
					</td>
					<td>
						<span title="${row.CREATE_TIME}">${row.CREATE_TIME}</span>
					</td>
					<td>
						<span title="${row.REASON}">${row.MODIFY_TIME}</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>