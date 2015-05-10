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
<title>修改用户</title>
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
						$.message("error","处理出错!");
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
			USERNAME: "required",
			PASSWORD: "required",
			DEFAULT_MANAGER: "required"
		}
	});
	
});
var user_id =${row.ID};
var old_username =${row.USERNAME};

function changeUsername()
{
	$("#USERNAME").removeAttr("readonly");
	$("#USERNAME").focus();
	$("#saveNameBtn").css('display',''); 
	$("#cancelNameBtn").css('display',''); 
	
}

function saveUsername()
{
	$("#USERNAME").attr("readonly","readonly")
	$("#saveNameBtn").css('display','none'); 
	$("#cancelNameBtn").css('display','none'); 
	var name =$("#USERNAME").val();
	var url ="<%=basePath%>mcnpage/user/member/changeUserName.do";
	var params ={id:user_id,old_name:old_username,new_name:name};
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
				$.message("success","修改成功!");
				cancelUsername();
			}
			else
			{
				$.message("error",ovo.msg);
				$("#USERNAME").val(old_username)
			}
		},
		complete: function (XMLHttpRequest, textStatus){
		},
		error: function (){
			$.message("error","处理出错!");
			$("#USERNAME").val(old_username)
		}
	});
}

function cancelUsername()
{
	$("#USERNAME").attr("readonly","readonly")
	$("#saveNameBtn").css('display','none'); 
	$("#cancelNameBtn").css('display','none'); 
	
}
</script>
</head>
<body>
	<div class="path">
		管理中心 &raquo; 修改用户
	</div>
	<form id="inputForm" action="update.do" method="post">
	<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID}"/>
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>部门:
				</th>
				<td>
					<select id="DEPART_ID" name="DEPART_ID" class="text" maxlength="200"  style="width:190px;" >
						<option value="" selected>请选择...</option>
						<c:forEach items="${sites}" var="opt" varStatus="status">
							<c:choose>
							<c:when test="${row.DEPART_ID ==opt.SITE_NO }">
								<option value="${opt.SITE_NO}" selected>${opt.SITE_NAME}</option>
							</c:when>
							<c:otherwise>
								<option value="${opt.SITE_NO}">${opt.SITE_NAME}</option>
							</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					姓名:
				</th>
				<td>
					<input type="text" name="NAME" class="text" maxlength="200" value="${row.NAME}" />
				</td>
			</tr>
			<tr>
				<th>
					用户名:
				</th>
				<td>
					<input type="text" id="USERNAME" name="USERNAME" class="text" maxlength="200" value="${row.USERNAME}" readonly/>
					<!-- 
					<input type="button" id="editNameBtn" name="editNameBtn" class="button"  value="修改" onclick="changeUsername()"/>
					<input type="button" id="saveNameBtn" name="saveNameBtn" class="button"  value="保存" onclick="saveUsername()" style="display:none;" />
					<input type="button" id="cancelNameBtn" name="cancelNameBtn" class="button"  value="取消" onclick="cancelUsername()" style="display:none;" />
					 -->
				</td>
			</tr>
			<tr>
				<th>
					密码:
				</th>
				<td>
					<input type="password" name="PASSWORD" class="text" maxlength="200" value="${row.PASSWORD}" />
				</td>
			</tr>
			<tr>
				<th>
					手机:
				</th>
				<td>
					<input type="text" name="TELEPHONE" class="text" maxlength="200" value="${row.TELEPHONE}"/>
				</td>
			</tr>
			<tr>
				<th>
					座机:
				</th>
				<td>
					<input type="text" name="PHONE" class="text" maxlength="200" value="${row.PHONE}"/>
				</td>
			</tr>
			<tr>
				<th>
					邮箱:
				</th>
				<td>
					<input type="text" name="EMAIL" class="text" maxlength="200" value="${row.EMAIL}"/>
				</td>
			</tr>
			<tr>
				<th>
					性别:
				</th>
				<td>
					<select id="SEX" name="SEX" class="text" maxlength="200" style="width:190px;">
					<c:if test="${row.SEX =='男'}">
						<option value="男" selected>男</option>
						<option value="女" >女</option>
					</c:if>
					<c:if test="${row.SEX == '女'}">
						<option value="男" >男</option>
						<option value="女" selected>女</option>
					</c:if>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					职位:
				</th>
				<td>
					<input type="text" name="POSITION" class="text" maxlength="200" value="${row.POSITION}"/>
				</td>
			</tr>
			<tr>
				<th>
					部门负责人:
				</th>
				<td>
					<select id="MANAGER_ID" name="MANAGER_ID" class="text" maxlength="200"  style="width:190px;" >
						<c:if test="${row.MANAGER_ID == '否'}">
						<option value="否" selected>否</option>
						<option value="是" >是</option>
					</c:if>
					<c:if test="${row.MANAGER_ID == '是'}">
						<option value="否" >否</option>
						<option value="是" selected>是</option>
					</c:if>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					默认审批人:
				</th>
				<td>
					<select id="DEFAULT_MANAGER" name="DEFAULT_MANAGER" class="text" maxlength="200"  style="width:190px;" >
					<c:choose>
						<c:when test="${row.DEFAULT_MANAGER == '0'}">
							<option value="0" selected>否</option>
							<option value="1" >是</option>
						</c:when>
						<c:when test="${row.DEFAULT_MANAGER == '1'}">
							<option value="0" >否</option>
							<option value="1" selected>是</option>
						</c:when>
						<c:otherwise>
							<option value="" selected>请选择...</option>
							<option value="0" >否</option>
							<option value="1" >是</option>
						</c:otherwise>
					</c:choose>
					</select>(每个部门只有一个默认审批人,如果新设置默认审批人，新的会自动覆盖原来的审批人)
				</td>
			</tr>
			<tr>
				<th>
					状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" maxlength="200"  style="width:190px;">
						<c:choose>
							<c:when test="${row.STATUS == 1}">
								<option value="" >请选择...</option>
								<option value="1" selected>正常</option>
								<option value="2" >停用</option>
								<option value="3" >删除</option>
							</c:when>
							<c:when test="${row.STATUS == 2}">
								<option value="" >请选择...</option>
								<option value="1" >正常</option>
								<option value="2" selected>停用</option>
								<option value="3" >删除</option>
							</c:when>
							<c:when test="${row.STATUS == 3}">
								<option value="" >请选择...</option>
								<option value="1" >正常</option>
								<option value="2" >停用</option>
								<option value="3" selected>删除</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >正常</option>
								<option value="2" >停用</option>
								<option value="3" >删除</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					备注:
				</th>
				<td>
					<input type="text" name="REMARK" class="text" maxlength="200" value="${row.REMARK}"/>
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