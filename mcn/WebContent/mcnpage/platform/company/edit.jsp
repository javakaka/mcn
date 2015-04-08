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
<title><cc:message key="framework.moudle.edit"/></title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			BUREAU_NO: "required",
			BUREAU_NAME: "required"
		}
	});

});
</script>
</head>
<body>
	<div class="path">
		平台管理&raquo; 企业信息修改
	</div>
	<form id="inputForm" action="update.do" method="post" enctype="multipart/form-data">
		<input type="hidden" id="BUREAU_NO" name="BUREAU_NO" value="${bureau.BUREAU_NO}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>企业名称:
				</th>
				<td>
					<input type="text" name="BUREAU_NAME" class="text" value="${bureau.BUREAU_NAME}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					企业Logo:
				</th>
				<td>
					<input type="text" name="AREA_CODE" class="text" value="${bureau.AREA_CODE}" maxlength="200" />
					<input type="file" id="fileElem" multiple accept="image/*" value='LOGO上传' name="fileElem" onchange="handleFiles(this)">
				</td>
			</tr>
			<tr>
				<th>
					企业域名:
				</th>
				<td>
					<input type="text" name="LINKS" class="text" value="${bureau.LINKS}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					企业登录网址:
				</th>
				<td>
					<input type="text" name="NOTES" class="text" value="${bureau.NOTES}" style="width:600px;" maxlength="800" />
				</td>
			</tr>
			<tr>
				<th>
					开始日期:
				</th>
				<td>
					<input type="text" name="BEGIN_DATE" maxlength="200" class="text Wdate" value="${bureau.BEGIN_DATE}" onfocus="WdatePicker();" />
				</td>
			</tr>
			<tr>
				<th>
					结束日期:
				</th>
				<td>
					<input type="text" name="END_DATE"  maxlength="200" class="text Wdate" value="${bureau.END_DATE}" onfocus="WdatePicker();" />
				</td>
			</tr>
			<tr>
				<th>
					用户数:
				</th>
				<td>
					<input type="text" name="USER_SUM" class="text" maxlength="200" value="${bureau.USER_SUM}"/>
				</td>
			</tr>
			<tr>
				<th>
					企业状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" style="width:190px;" >
						<c:choose>
							<c:when test="${bureau.STATUS == 1}">
								<option value="" >请选择...</option>
								<option value="1" selected>有效</option>
								<option value="2" >停用</option>
								<option value="3" >删除</option>
							</c:when>
							<c:when test="${bureau.STATUS == 2}">
								<option value="" >请选择...</option>
								<option value="1" >有效</option>
								<option value="2" selected>停用</option>
								<option value="3" >删除</option>
							</c:when>
							<c:when test="${bureau.STATUS == 3}">
								<option value="" >请选择...</option>
								<option value="1" >有效</option>
								<option value="2" >停用</option>
								<option value="3" selected>删除</option>
							</c:when>
							<c:otherwise>
								<option value="" selected>请选择...</option>
								<option value="1" >有效</option>
								<option value="2" >停用</option>
								<option value="3" >删除</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					功能选项:
				</th>
				<td>
					<input type="checkbox" id="check_punch"       name="modules"  maxlength="10" value="punch" <c:if test="${module.PUNCH == 1}">checked</c:if> />打卡
					<input type="checkbox" id="check_reimburse"   name="modules"  maxlength="10" value="reimburse" <c:if test="${module.REIMBURSE == 1}">checked</c:if> />报销
					<input type="checkbox" id="check_task"        name="modules"  maxlength="10" value="task" <c:if test="${module.TASK == 1}">checked</c:if> />任务
					<input type="checkbox" id="check_meeting_room" name="modules"  maxlength="10" value="meeting_room" <c:if test="${module.MEETING_ROOM == 1}">checked</c:if> />会议室
				</td>
			</tr>
			<tr>
				<th>
					企业通讯录:
				</th>
				<td>
					<input type="button" name="uploadBtn" id="uploadBtn" class="button" value="点击上传" onclick="upload()"/>
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
</body>
<script type="text/javascript">
function upload()
{
	var id =$("#BUREAU_NO").val();
	var url ="<%=basePath%>sysupload/Upload.jsp?deal_type=company_user";
	url+="&deal_code="+id;
	url+="&type=user_excel&sub_type=user_excel&cover=0";
	url+="&file_type=*.xls;";
	url+="&callback=parseExcel";
	//alert(url);
	window.open (url,'uploadwindow','height=400,width=680,top=200,left=200 ,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function parseExcel()
{
	var id =$("#BUREAU_NO").val();
	var params={id:id}
	$.ajax({
			url: "<%=basePath%>mcnpage/platform/company/parseExcel.do",
			type: "POST",
			data: params,
			dataType: "json",
			cache: false,
			beforeSend: function (XMLHttpRequest){
				//alert('.....');
			},
			success: function(message, textStatus) {
				$.message(message);
				//alert(data);
			},
			complete: function (XMLHttpRequest, textStatus){
				//alert("complete...");
			},
			error: function (){
				alert('error...');
			}
		});
}
</script>
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
</html>