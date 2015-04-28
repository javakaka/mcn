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
<script type="text/javascript" src="<%=basePath%>/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $selectAll = $("#selectall");
	$selectAll.click( function() {
		for(var i=0;i<document.all.length;i++)
		{
		if (document.all(i).type=="checkbox")
		{
		//对checkbox进行处理
			if(document.all(i).checked==true)
			{
			document.all(i).checked=false;
			$selectAll.val("全选");
			}
			else
			{
			document.all(i).checked=true;
			$selectAll.val("反选");
			}
		}
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<cc:message key="framework.nav.index" /> &raquo; <cc:message key="framework.moudle.add"/>
	</div>
	<form id="inputForm" action="messageedit.do" method="post" enctype="multipart/form-data">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>标题:
				</th>
				<td>
					<input type="text" name="message_name" class="text" maxlength="200" value="${row.MESSAGE_NAME}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>内容:
				</th>
				<td>
					<textarea id="editor"  name="message_content" class="editor">${row.MESSAGE_CONTENT}</textarea>
				</td>
			</tr>
			<tr>
				<th>
					附件:
				</th>
				<td>
					<input type="file" name="fileElem"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>发送群组:
				</th>
				<td>
					<c:forEach items="${sites}" var="row" varStatus="status">
							<input type="checkbox" id="message_qz" name="message_qz" value="${row.SITE_NO}"/>${row.SITE_NAME}&nbsp;
					</c:forEach>&nbsp;<input type="button" id="selectall" value="全选">
				</td>
			</tr>
			
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="hidden" name="id" value="${row.ID}">
					<input type="hidden" name="fj" value="${row.MESSAGE_FJ}">
					<input type="hidden" name="create_time" value="${row.CREATE_TIME}">
					<input type="submit" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>