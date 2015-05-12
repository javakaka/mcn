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
<title>打卡时间维护</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
var am_end="${row2.AM_END}";
var pm_start="${row2.PM_START}";
var punch_num="${punch_num}";

$().ready(function() {

	var $inputForm = $("#inputForm");
	
	//[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			DEPART_ID: "required",
			AM_START: "required",
			AM_END: "required",
			PM_START: "required",
			PM_END: "required"
		}
	});
	
	$('input:radio[name="PUNCH_NUM"]').change( function(){
			var val =this.value;
			if(val == 4)
			{
				$("#AM_END_TR").show();
				$("#PM_START_TR").show();
				$("#AM_END").val(am_end);
				$("#PM_START").val(pm_start);
			}
			else if(val == 2)
			{
				$("#AM_END_TR").hide();
				$("#PM_START_TR").hide();
				$("#AM_END").val("-1");
				$("#PM_START").val("-1");
				
			}
	});
});

function initPunchNum()
{
	var val =punch_num;
	if(val == 4)
	{
		$("#AM_END_TR").show();
		$("#PM_START_TR").show();
		$("#AM_END").val(am_end);
		$("#PM_START").val(pm_start);
	}
	else if(val == 2)
	{
		$("#AM_END_TR").hide();
		$("#PM_START_TR").hide();
		$("#AM_END").val("-1");
		$("#PM_START").val("-1");
	}
}
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 打卡时间维护
	</div>
	<form id="inputForm" action="update.do" method="post">
	<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID}"/>
		<table class="input">
			<tr >
				<th>
					当前打卡时间规则设置:
				</th>
				<td>
					如果部门需要切换打卡时间，请在下面的表格中填写打卡时间，并点击“确定”按钮，系统将在下个月采用新的打卡时间设置
				</td>
			</tr>
			<tr>
				<th>
					部门:
				</th>
				<td>
				<input type="hidden" id="DEPART_ID" name="DEPART_ID" class="text" value="${row.DEPART_ID}"  maxlength="200" />
				<c:forEach items="${sites}" var="opt" varStatus="status">
					<c:if test="${row.DEPART_ID ==opt.SITE_NO }">
						${opt.SITE_NAME}
					</c:if>
				</c:forEach>
				</td>
			</tr>
			<tr >
				<th>
					上午上班:
				</th>
				<td>
					${row.AM_START}
				</td>
			</tr>
			<tr >
				<th>
					上午下班:
				</th>
				<td>
					${row.AM_END}
				</td>
			</tr>
			<tr >
				<th>
					下午上班:
				</th>
				<td>
					${row.PM_START}
				</td>
			</tr>
			<tr >
				<th>
					下午下班:
				</th>
				<td>
					${row.PM_END}
				</td>
			</tr>
			<tr >
				<th>
					下次打卡时间规则设置:
				</th>
				<td>
					请注意:如果部门不需要切换打卡时间，请不要点击“确定按钮”，系统将按现有的打卡时间设置处理打卡流程<br/>
					如果需要设置下次打卡时间，设置后将在下个月1号的0点30分生效
				</td>
			</tr>
			<tr id="AM_START_TR">
				<th>
					<span class="requiredField">*</span>上午上班:
				</th>
				<td>
					<input type="text" id="AM_START" name="AM_START" class="text Wdate" value="${row2.AM_START}" onfocus="WdatePicker({dateFmt:'HH:mm'});"  maxlength="200" />
				</td>
			</tr>
			<tr id="AM_END_TR">
				<th>
					<span class="requiredField">*</span>上午下班:
				</th>
				<td>
					<input type="text" id="AM_END"  name="AM_END" class="text Wdate" value="${row2.AM_END}" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
				</td>
			</tr>
			<tr id="PM_START_TR">
				<th>
					<span class="requiredField">*</span>下午上班:
				</th>
				<td>
					<input type="text" id="PM_START"  name="PM_START" class="text Wdate" value="${row2.PM_START}" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
				</td>
			</tr>
			<tr id="PM_END_TR">
				<th>
					<span class="requiredField">*</span>下午下班:
				</th>
				<td>
					<input type="text" id="PM_END"  name="PM_END" class="text Wdate" value="${row2.PM_END}" onfocus="WdatePicker({dateFmt:'HH:mm'});"   maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>打卡次数:
				</th>
				<td>
					<input type="radio" name="PUNCH_NUM"  value="4" checked/>4次
					<input type="radio" name="PUNCH_NUM"  value="2" />2次(请注意：打卡次数切换，需要等到下个月才能生效，本月继续使用当前的打卡规则)
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
	<script type="text/javascript">
	initPunchNum();
	</script>
</body>
</html>