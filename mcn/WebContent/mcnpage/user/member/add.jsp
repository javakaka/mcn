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
<title>添加用户</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	${flash_message}
	var $status = $("#STATUS");
	
	$status.change(function(){
		var val=this.value;
		if(val == '1')
		{
			//
			var url ="<%=basePath%>/mcnpage/user/member/checkCompanyUserIsOverNum.do";
			var params ={};
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
						var num =ovo.oForm.NUM;
						$.message("success","还可以添加"+num+"个启用状态的用户");
						$("#submitBtn").removeAttr("disabled");
					}
					else
					{
						$.message("error",ovo.msg);
						$("#submitBtn").attr("disabled","disabled");
					}
				},
				complete: function (XMLHttpRequest, textStatus){
				},
				error: function (){
					$.message("error","处理出错!");
					$("#submitBtn").attr("disabled","disabled");
				}
			});
		}
		else
		{
			$("#submitBtn").removeAttr("disabled");
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			DEPART_ID: "required",
			NAME: "required",
			PASSWORD: "required",
			STATUS: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 添加用户
	</div>
	<form id="inputForm" action="save.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>部门:
				</th>
				<td>
					<select id="DEPART_ID" name="DEPART_ID" class="text" maxlength="200"  style="width:190px;">
						<option value="" selected>请选择...</option>
						<c:forEach items="${sites}" var="row" varStatus="status">
							<option value="${row.SITE_NO}">${row.SITE_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>姓名:
				</th>
				<td>
					<input type="text" name="NAME" class="text" maxlength="200" />
					<input type="hidden" name="USERNAME" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>密码:
				</th>
				<td>
					<input type="text" name="PASSWORD" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					手机:
				</th>
				<td>
					<input type="text" name="TELEPHONE" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					座机:
				</th>
				<td>
					<input type="text" name="PHONE" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					邮箱:
				</th>
				<td>
					<input type="text" name="EMAIL" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					性别:
				</th>
				<td>
					<select id="SEX" name="SEX" class="text" maxlength="200"  style="width:190px;">
						<option value="男" selected>男</option>
						<option value="女" >女</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					职位:
				</th>
				<td>
					<input type="text" name="POSITION" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					部门负责人:
				</th>
				<td>
					<select id="MANAGER_ID" name="MANAGER_ID" class="text" maxlength="200"  style="width:190px;">
						<option value="否" selected>否</option>
						<option value="是" >是</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					默认审批人:
				</th>
				<td>
					<select id="DEFAULT_MANAGER" name="DEFAULT_MANAGER" class="text" maxlength="200"  style="width:190px;">
						<option value="0" selected>否</option>
						<option value="1" >是</option>
					</select>(每个部门只有一个默认审批人,如果新设置默认审批人，新的会自动覆盖原来的审批人)
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" maxlength="200"  style="width:190px;">
						<option value="1" >正常</option>
						<option value="2" selected>停用</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					备注:
				</th>
				<td>
					<input type="text" name="REMARK" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" id="submitBtn" name="submitBtn" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>