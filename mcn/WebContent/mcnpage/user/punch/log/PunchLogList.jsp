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
					+'<th>部门:<\/th>'
					+'<td>'
					+'<select id="depart_id" name="depart_id" class="text" style="width:190px;" onchange="selectUser(this)">'
					+'<option value="">请选择...<\/option>'
					+'<c:forEach items="${site_list}" var="row" varStatus="status">'
					+'<c:choose><c:when test="${depart_id == row.SITE_NO}"><option value="${row.SITE_NO}" selected>${row.SITE_NAME}<\/option></c:when><c:otherwise><option value="${row.SITE_NO}" >${row.SITE_NAME}<\/option></c:otherwise></c:choose>'
					+'</c:forEach>'
					+'<\/select>'
					+'<\/td>'
					+'<\/tr>'
					+'<tr>'
					+'<th>人员:<\/th>'
					+'<td>'
					+'<select id="user_id" name="user_id" class="text" style="width:190px;">'
					+'<option value="">请选择...<\/option>'
					+'<c:forEach items="${user_list}" var="row" varStatus="status">'
					+'<c:choose><c:when test="${user_id == row.ID}"><option value="${row.ID}" selected>${row.NAME}<\/option></c:when><c:otherwise><option value="${row.ID}" >${row.NAME}<\/option></c:otherwise></c:choose>'
					+'</c:forEach>'
					+'<\/select>'
					+'<\/td>'
					+'<\/tr>'
					+'<tr>'
					+'<th>开始日期:<\/th>'
					+'<td>'
					+'<input id="start_date" name="start_date" class="text Wdate" onfocus="WdatePicker();" value="${start_date}" />'
					+'<\/td>'
					+'<\/tr>'
					+'<tr>'
					+'<th>结束日期:<\/th>'
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
					//alert($("#" + $this.attr("name")).val());
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
function updatestatus(value,id){
var url = "<%=basePath%>/mcnpage/user/punch/log/updatestatus.do";
var params = {punch_status:value,id:id}
$.post(url,params,function cbk(){
alert("修改成功");
});
}


function selectUser(obj)
{
	var site_no =obj.value;
	if(typeof site_no == "undefined" || site_no =="")
	{
		return false;
	}
	var url ="<%=basePath%>mcnpage/user/member/queryUserBySiteno.do";
	var params ={site_no: site_no};
	$.ajax({
		url: url,
		type: "POST",
		data: params,
		dataType: "json",
		cache: false,
		beforeSend: function (XMLHttpRequest){
		},
		success: function(ovo, textStatus) {
			var code =ovo.code;
			if(code >=0)
			{
				var list =ovo.oForm.LIST;
				var html ="<option value=''>请选择...</option>";
				$.each(list,function(i,item){
					html +="<option value='"+item.ID+"'>"+item.NAME+"</option>";
				});
				$("#moreTable").find("#user_id").html(html);
			}
			else
			{
				$.message("error",ovo.msg);
			}
		},
		complete: function (XMLHttpRequest, textStatus){
		},
		error: function (){
			$.message("error","处理出错!");
		}
	});
}

function exportExcel()
{
	var depart_id=$('#depart_id').val(); 
	var user_id=$('#user_id').val(); 
	var start_date=$('#start_date').val(); 
	var end_date=$('#end_date').val();
	if(typeof depart_id =="undefined" || depart_id =="")
	{
		 depart_id ="";
	}
	if(typeof user_id =="undefined" || user_id =="")
	{
		 user_id ="";
	}
	if(typeof start_date =="undefined" || start_date =="")
	{
		start_date ="";
	}
	if(typeof end_date =="undefined" || end_date =="")
	{
		end_date ="";
	}
	var url ="<%=basePath%>mcnpage/user/punch/log/exportUserPunchLog.do?";
	url +="start_date="+start_date;
	url +="&end_date="+end_date;
	url +="&depart_id="+depart_id;
	url +="&user_id="+user_id;
	window.open(url);
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
	<input type="hidden" id="punch_type" name="punch_type" value="<c:if test="${punch_type !=''}">${punch_type}</c:if>" />
	<input type="hidden" id="punch_result" name="punch_result" value="<c:if test="${punch_result !=''}">${punch_result}</c:if>" />
	<input type="hidden" id="depart_id" name="depart_id" value="${depart_id}" />
	<input type="hidden" id="user_id" name="user_id" value="${user_id}" />
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
						筛选<span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="filterOption" class="check">
							<li>
								<a href="javascript:;" name="punch_type" val="1" <c:if test="${punch_type == '1'}"> class="checked" </c:if> >上午上班</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="2" <c:if test="${punch_type == '2'}"> class="checked" </c:if> >上午下班</a>
							</li>
							<li >
								<a href="javascript:;" name="punch_type" val="3" <c:if test="${punch_type == '3'}"> class="checked" </c:if> >下午上班</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="4" <c:if test="${punch_type == '4'}"> class="checked" </c:if> >下午下班</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="5" <c:if test="${punch_type == '5'}"> class="checked" </c:if> >加班开始</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="6" <c:if test="${punch_type == '6'}"> class="checked" </c:if> >加班结束</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="7" <c:if test="${punch_type == '7'}"> class="checked" </c:if> >签到</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_type" val="8" <c:if test="${punch_type == '8'}"> class="checked" </c:if> >签退</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="punch_result" val="1" <c:if test="${punch_result == '1'}"> class="checked" </c:if> >正常</a>
							</li>
							<li>
								<a href="javascript:;" name="punch_result" val="2" <c:if test="${punch_result == '2'}"> class="checked" </c:if> >早退</a>
							</li>
							<li >
								<a href="javascript:;" name="punch_result" val="3" <c:if test="${punch_result == '3'}"> class="checked" </c:if> >迟到</a>
							</li>
						</ul>
					</div>
				</div>
				<!-- --><a href="javascript:;" id="moreButton" class="button">更多选项</a>
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
				<a href="javascript:void(0);" target="_blank"  onclick="exportExcel()" class="iconButton">
				导 出</a>
				<!-- <a href="?exportToExcel=YES" class="iconButton"><span>导 出&nbsp;</span></a>-->
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
					<a href="javascript:;" class="sort" name="MAP_VALID">位置有效性</a>
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
							<c:when test="${row.PUNCH_TYPE == 1}">上午上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 2}">上午下班</c:when>
							<c:when test="${row.PUNCH_TYPE == 3}">下午上班</c:when>
							<c:when test="${row.PUNCH_TYPE == 4}">下午下班</c:when>
							<c:when test="${row.PUNCH_TYPE == 5}">加班开始</c:when>
							<c:when test="${row.PUNCH_TYPE == 6}">加班结束</c:when>
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
					<td>
						<c:choose>
							<c:when test="${row.MAP_VALID == 0}">不用检测</c:when>
							<c:when test="${row.MAP_VALID == 1}">有效</c:when>
							<c:when test="${row.MAP_VALID == 2}">无效</c:when>
							<c:otherwise> 
								--
							</c:otherwise>
						</c:choose>
					</td>
					<% if (exportToExcel == null) { %>
					<td><img src="<%=basePath %>${row.IMG_PATH}"  height="100px;"/></td>
					<% }  %> 
					<% if (exportToExcel == null) { %>
					<td>
						<select onchange="updatestatus(this.options[this.options.selectedIndex].value,${row.ID})"><option value="0">正常</option><option value="1">地址不对</option><option value="2">头像不对</option></select>
						<!-- <a href="edit.do?id=${row.ID}"><cc:message key="admin.common.edit" /></a>-->
						<a href="view.do?id=${row.ID }" target="_self"><cc:message key="admin.common.view" /></a>
					</td>
					<% }  %> 
				</tr>
			</c:forEach>
		</table>
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>