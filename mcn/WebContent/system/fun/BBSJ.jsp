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
<title><cc:message key="framework.nav.i18n" /></title>
<link href="<%=basePath%>/res/admin/css/common_pop.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	//[@flash_message /]
	var url = "<%=basePath%>system/fun/BBSJ.do";
	$.post(url,function cbk(data){
		$("#bname").val(data);
	});
});
function updateB(){
	if($("#bname").val()!=""){
	if(window.confirm("您确定提交更新吗？")){
	var url = "<%=basePath%>system/fun/BBSJUpdate.do?bb="+$("#bname").val();
	$.post(url,function cbk(data){
	});
	}
	}else{
	alert("版本序号不能为空");
	}
}
</script>
</head>
<body>
<div class="path">
		客户端版本更新 &raquo;
	</div>
客户端版本序号：<input type="text" id="bname" name="bname">&nbsp;&nbsp;&nbsp;<input type = "submit" onclick="updateB()" value="提 交">
</body>
</html>