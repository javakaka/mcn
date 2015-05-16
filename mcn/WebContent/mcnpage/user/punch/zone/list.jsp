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
<title>打卡位置列表</title>
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


function exportExcel()
{
	var start_date=$('#start_date').val(); 
	var end_date=$('#end_date').val();
	if(typeof start_date =="undefined" || start_date =="")
	{
		start_date ="";
	}
	if(typeof end_date =="undefined" || end_date =="")
	{
		end_date ="";
	}
	var url ="<%=basePath%>mcnpage/user/punch/log/exportUserLeaveLog.do?";
	url +="start_date="+start_date;
	url +="&end_date="+end_date;
	window.open(url);
}
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
		管理中心 &raquo;打卡位置列表
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="list.do" method="get">
	<input type="hidden" id="status" name="status" value="<c:if test="${status !=''}">${status}</c:if>" />
	<input type="hidden" id="start_date" name="start_date" value="${start_date}" />
	<input type="hidden" id="end_date" name="end_date" value="${end_date}" />
	
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
				<!-- 
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
				-->
				<!-- <a href="javascript:;" id="moreButton" class="button">更多选项</a>-->
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
		</div>
		<% }  %> 
		<table id="listTable" class="list">
			<tr align='center'>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PLACE_NAME">位置名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="LONGITUDE">经度</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="LATITUDE">纬度</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="RADIUS">半径</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="STATE">状态</a>
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
						<span title="${row.PLACE_NAME}">${row.PLACE_NAME}</span>
					</td>
					<td>
						<span title="${row.LONGITUDE}">${row.LONGITUDE}</span>
					</td>
					<td>
						<span title="${row.LATITUDE}">${row.LATITUDE}</span>
					</td>
					<td>
						<span title="${row.RADIUS}">${row.RADIUS}</span>
					</td>
					<td>
					<c:if test="${row.STATE == 1}">
						启用
					</c:if>
					<c:if test="${row.STATE == 2}">
						停用
					</c:if>
					</td>
					<td>
						<a href="edit.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>