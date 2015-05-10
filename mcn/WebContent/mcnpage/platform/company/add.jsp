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
<title><cc:message key="framework.moudle.add"/></title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	//[@flash_message /]
	// 表单验证
	$inputForm.validate({
		rules: {
			BUREAU_NAME: "required",
			LINKS: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<cc:message key="framework.nav.index" /> &raquo; <cc:message key="framework.moudle.add"/>
	</div>
	<form id="inputForm" action="save.do" method="post" enctype="multipart/form-data">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>企业名称:
				</th>
				<td>
					<input type="text" name="BUREAU_NAME" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					企业Logo:
				</th>
				<td>
					<!--<input type="text" name="AREA_CODE" class="text" maxlength="200" />-->
					<input type="file" id="fileElem" multiple accept="image/*" value='LOGO上传' name="fileElem" onchange="handleFiles(this)" />
				</td>
			</tr>
			<tr>
				<th>
					企业域名:
				</th>
				<td>
					<input type="text" name="LINKS" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					企业登录网址:
				</th>
				<td>
					<input type="text" name="NOTES" class="text" maxlength="600" />
				</td>
			</tr>
			<tr>
				<th>
					开始日期:
				</th>
				<td>
					<input type="text" name="BEGIN_DATE" maxlength="200" class="text Wdate" value="" onfocus="WdatePicker();" />
				</td>
			</tr>
			<tr>
				<th>
					结束日期:
				</th>
				<td>
					<input type="text" name="END_DATE"  maxlength="200" class="text Wdate" value="" onfocus="WdatePicker();" />
				</td>
			</tr>
			<tr>
				<th>
					用户数:
				</th>
				<td>
					<input type="text" name="USER_SUM" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					企业状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" style="width:190px;" >
						<option value="" selected>请选择...</option>
						<option value="1" >有效</option>
						<option value="2" >停用</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					功能选项:
				</th>
				<td>
					<input type="checkbox" id="check_punch"        name="modules"  maxlength="100" value="punch" />打卡
					<input type="checkbox" id="check_reimburse"    name="modules"  maxlength="100" value="reimburse" />报销
					<input type="checkbox" id="check_task"         name="modules"  maxlength="100" value="task" />任务
					<input type="checkbox" id="check_meeting_room" name="modules"  maxlength="100" value="meeting_room" />会议室
					<input type="checkbox" id="check_checkin" name="modules"  maxlength="100" value="checkin" />签到
					<input type="checkbox" id="check_ask_leave" name="modules"  maxlength="100" value="ask_leave" />请假
					<input type="checkbox" id="check_query_leave" name="modules"  maxlength="100" value="query_leave" />签到查询
					<input type="checkbox" id="check_query_base" name="modules"  maxlength="100" value="query_base" />基本查询
					<input type="checkbox" id="check_change_pwd" name="modules"  maxlength="100" value="change_pwd" />修改密码
					<input type="checkbox" id="check_message" name="modules"  maxlength="100" value="message" />通知
					<input type="checkbox" id="check_base_statistic" name="modules"  maxlength="100" value="base_statistic" />基本查询
					<input type="checkbox" id="check_punch_statistic" name="modules"  maxlength="100" value="punch_statistic" />签到查询
					<input type="checkbox" id="check_audit" name="modules"  maxlength="100" value="audit" />审批
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
	<script>
	window.URL = window.URL || window.webkitURL;
	var fileElem = document.getElementById("fileElem"),
	    fileList = document.getElementById("fileList");
	function handleFiles(obj) {
		var files = obj.files,
			img = new Image();
		if(window.URL){
			//File API
			 // alert(files[0].name + "," + files[0].size + " bytes");
		      img.src = window.URL.createObjectURL(files[0]); //创建一个object URL，并不是你的本地路径
		      img.height = 60;
		      img.onload = function(e) {
		         window.URL.revokeObjectURL(this.src); //图片加载后，释放object URL
		      } 
			  fileList.innerHTML = "";
		      fileList.appendChild(img); 
		}else if(window.FileReader){
			//opera不支持createObjectURL/revokeObjectURL方法。我们用FileReader对象来处理
			var reader = new FileReader();
			reader.readAsDataURL(files[0]);
			reader.onload = function(e){
				//alert(files[0].name + "," +e.total + " bytes");
				img.src = this.result;
				img.height = 60;
				fileList.innerHTML = "";
				fileList.appendChild(img);
			}
		}else{
			//ie
			obj.select();
			obj.blur();
			var nfile = document.selection.createRange().text;
			document.selection.empty();
			img.src = nfile;
			img.width = 60;
			img.onload=function(){
		     // alert(nfile+",==="+img.fileSize + " bytes");
		    }
			fileList.innerHTML = "";
			fileList.appendChild(img);
			//fileList.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src='"+nfile+"')";
		}
	}
</script>
</body>
</html>