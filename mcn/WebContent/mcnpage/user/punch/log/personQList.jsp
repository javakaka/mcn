<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String time =(String)session.getAttribute("datatime2");
System.out.println("time ---->>>"+time);
String year = "";
String month ="";
if(time != null)
{
	if(time.length()  >=4)
	{
		year = time.substring(0,4);
	}
	if(time.length()  >=7)
	{
		month = time.substring(5,7);
	}
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>签到记录</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	 $('#year').val("<%=year%>");
	 $('#month').val("<%=month%>");
});
function findList(){
	 var year=$('#year').val(); 
	 var month=$('#month').val();
	 var time="";
	 if(typeof year =="undefined" || year =="")
	 {
		 $.message("error","请选择年份");
		 /* $('#year').focus(); */ 
		 return false;
	 }
	 if(typeof month !="undefined" && month !="")
	 {
		 time=year+"-"+month;
	 }
	 else
	 {
		 time=year;
	 }
	 window.location.href = "<%=basePath%>mcnpage/user/punch/log/personQList.do?time="+time;
}
function se (){
}


function exportExcel()
{
	var year=$('#year').val(); 
	var month=$('#month').val();
	if(typeof year =="undefined" || year =="")
	 {
		year ="";
	 }
	 if(typeof month =="undefined" || month =="")
	 {
		 month ="";
	 }
	var url ="<%=basePath%>mcnpage/user/punch/log/exportUserCheckinLog.do?";
	url +="year="+year;
	url +="&month="+month;
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
		管理中心 &raquo;签到记录
		<span><cc:message key="admin.page.total" args="${page.total}"/></span>
	</div>
	<form id="listForm" action="personQList.do" method="get">
		<div class="bar">
			<!-- 
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			-->
			<div class="buttonWrap">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
				<a id="refreshButton" class="iconButton">
							<span >&nbsp;</span>
							<input id="year" name="year" type="text" class="text Wdate" onfocus="WdatePicker({dateFmt:'yyyy'});" />年
							<input type="text" id="month" name="month" class="text Wdate" onfocus="WdatePicker({dateFmt:'MM'});" value="" />月&nbsp; &nbsp;
							<input type="button" value="查 找" class="button" onclick="findList();"/>
				</a>
			</div>
			<div>
				<a href="javascript:void(0);" target="_blank"  onclick="exportExcel()" class="iconButton">
				导 出</a>
				<!-- 
				<a href="?exportToExcel=YES" class="iconButton">
				<span>导 出&nbsp;</span>
				-->
			</a>
			</div>
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'USER_NAME'}"> class="current"</c:if> val="USER_NAME">用户姓名</a>
						</li>
						<li>
							<a href="javascript:;" <c:if test="${page.searchProperty == 'SITE_NAME'}">class="current"</c:if> val="SITE_NAME">部门名称</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<% }  %> 
		<table id="listTable" class="list">
			<tr align='center'>
				<th>
					<a href="javascript:;" class="sort" name="SITE_NAME">所属部门</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="USER_NAME">姓名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_TIME">签到时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PlACE_NAME">签到地址</a>
				</th>
			</tr>
			<c:forEach items="${page.content}" var="row" varStatus="status">
				<tr>
					<td>
						<span title="${row.SITE_NAME}">${row.SITE_NAME}</span>
					</td>
					<td>
						${row.USER_NAME}
					</td>
					<td>
						${row.PUNCH_TIME}
					</td>
					<td>
						${row.PLACE_NAME}
					</td>
				</tr>
			</c:forEach>
		</table> 
		<%@ include file="/include/pagination.jsp" %> 
	</form>
</body>
</html>