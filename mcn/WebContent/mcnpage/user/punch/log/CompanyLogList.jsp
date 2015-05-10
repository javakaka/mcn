<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String time = session.getAttribute("datatime").toString();
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
<title>企业考勤汇总</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
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
	 $('#year').val("<%=year%>");
	 $('#month').val("<%=month%>");
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
						+'<th>111:<\/th>'
						+'<td>'
						+'<select name="productCategoryId">'
						+'<option value="">请选择...<\/option>'
						+'<option value="1" selected="selected">11<\/option>'
						+'<option value="2" >22<\/option><option value="3" >33<\/option>'
						+'<\/select>'
						+'<\/td>'
						+'<\/tr>'
						+'<\/table>',
				width: 470,
				modal: true,
				ok: "ok",
				cancel: "cancel",
				onOk: function() {
					$("#moreTable :input").each(function() {
						var $this = $(this);
						$("#" + $this.attr("name")).val($this.val());
					});
					$listForm.submit();
				}
			});
		});
		
		// 商品筛选
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
function findList(){
	 var year=$('#year').val(); 
	 var month=$('#month').val();
	 var time="";
	 if(typeof year =="undefined" || year.trim() =="")
	 {
		 $.message("error","请选择年份");
		 /* $('#year').focus(); */ 
		 return false;
	 }
	 if(typeof month !="undefined" && month.trim() !="")
	 {
		 time=year+"-"+month;
	 }
	 else
	 {
		 time=year
	 }
	 window.location.href = "<%=basePath%>mcnpage/user/punch/log/companyLogList.do?time="+time;
}
function selectListPerson(id){
window.location.href = "<%=basePath%>mcnpage/user/punch/log/PunchLogList2.do?user_id="+id;
}


function exportExcel()
{
	var year=$('#year').val(); 
	var month=$('#month').val();
	if(typeof year =="undefined" || year.trim() =="")
	 {
		year ="";
	 }
	 if(typeof month =="undefined" || month.trim() =="")
	 {
		 month ="";
	 }
	var url ="<%=basePath%>mcnpage/user/punch/log/exportCompanyLog.do?";
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
		管理中心 &raquo;企业考勤汇总
		<span></span>
	</div>
	<form id="listForm" action="companyLogList.do" method="get">
		<div class="bar">
			<a href="add.do" class="iconButton">
				<span class="addIcon">&nbsp;</span><cc:message key="admin.common.add" />
			</a>
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
		</div>
		<% }  %> 
		<table id="listTable" class="list">
			<tr align='center'>
				<th>
					<a href="javascript:;" >所属部门</a>
				</th>
				<th>
					<a href="javascript:;" >姓名</a>
				</th>
				<th>
					<a href="javascript:;" >打卡</a>
				</th>
				<th>
					<a href="javascript:;" >外出</a>
				</th>
				<th>
					<a href="javascript:;" >年假</a>
				</th>
				<th>
					<a href="javascript:;" >事假</a>
				</th>
				<th>
					<a href="javascript:;" >病假</a>
				</th>
				<th>
					<a href="javascript:;" >调休</a>
				</th>
				<th>
					<a href="javascript:;" >加班</a>
				</th>
				<th>
					<a href="javascript:;" >早退</a>
				</th>
				<th>
					<a href="javascript:;" >漏打卡</a>
				</th>
				<th>
					<a href="javascript:;" >迟到10分钟以内</a>
				</th>
				<th>
					<a href="javascript:;" >大于10小于30分钟</a>
				</th>
				<th>
					<a href="javascript:;" >超过30分钟</a>
				</th>
			</tr>
			<c:forEach items="${page}" var="row" varStatus="status">
				<tr align='center' >
					<td>
						<span title="${row.DEPART_NAME}">${row.DEPART_NAME}</span>
					</td>
					<td>
						${row.MANAGER_NAME}
					</td>
					<td>
						<a href='javascript:selectListPerson(${row.USER_ID})'>${row.ALL_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=6'>${row.WAI_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=1'>${row.YEAR_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=5'>${row.SHI_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=2'>${row.SICK_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=3'>${row.TIAO_DAY}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/PunchLogList3.do?user_id=${row.USER_ID}&leave_type=4'>${row.ADD_DAY}</a>
					</td>
					<td>
					${row.LEAVE_EARLY}
					</td>
					<td>
					${row.LOST_PUNCH}
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/chidaoLogList.do?user_id=${row.USER_ID}&type=0'>${row.XSHI}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/chidaoLogList.do?user_id=${row.USER_ID}&type=1'>${row.DSHI}</a>
					</td>
					<td>
					<a href='<%=basePath%>mcnpage/user/punch/log/chidaoLogList.do?user_id=${row.USER_ID}&type=2'>${row.CSHI}</a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</body>
</html>