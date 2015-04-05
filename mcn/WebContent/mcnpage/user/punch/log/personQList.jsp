<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String time = session.getAttribute("datatime2").toString();
String year = time.substring(0,4);
String month = time.substring(5,7);
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
<script type="text/javascript">
$().ready(function() {
	 $('#year').val("<%=year%>");
	 $('#month').val("<%=month%>");
});
function findList(){
	 var year=$('#year').val(); 
	 var month=$('#month').val();
	 var time=year+"-"+month
	 window.location.href = "<%=basePath%>mcnpage/user/punch/log/personQList.do?time="+time;
}
function se (){
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
		<span></span>
	</div>
	<form id="listForm" action="personQList.do" method="get">
		<div class="bar">
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span><cc:message key="admin.common.refresh" />
				</a>
			</div>
			<div class="iconButton">
				<a id="refreshButton" class="iconButton">
							<span >&nbsp;</span><input id="year" name="year" type="text" value="2014" onfocus="if (value =='2014'){value =''}" onblur="if (value ==''){value='2014'}"/>年<input type="text" id="month" name="month" value="08" onfocus="if (value =='08'){value =''}" onblur="if (value ==''){value='08'}"/>月&nbsp; &nbsp;<input type="button" value="查 找" onclick="findList();"/>
				</a>
			</div>
			<div>
				<a href="?exportToExcel=YES" class="iconButton">
				<span>导 出&nbsp;</span>
			</a>
			</div>
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
			</div>
		</div>
		<% }  %> 
		<table id="listTable" class="list">
			<tr align='center'>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_TYPE">所属部门</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_TIME">姓名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_RESULT">签到时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_RESULT">签到地址</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_RESULT">签退时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="PUNCH_RESULT">签退地址</a>
				</th>
			
			</tr>
			<c:forEach items="${page}" var="row" varStatus="status">
				<tr align='center'>
					<td>
						<span title="${row.DEPART_NAME}">${row.DEPART_NAME}</span>
					</td>
					<td>
						${row.USER_NAME}
					</td>
					<td>
						${row.QDTIME}
					</td>
					<td>
					${row.QD_ADDRESS}
					</td>
					<td>
					${row.QT_TIME}
					</td>
					<td>
					${row.QT_ADDRESS}
					</td>
					
				</tr>
			</c:forEach>
		</table> 
	</form>
</body>
</html>