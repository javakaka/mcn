<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	session =request.getSession();
	String token =(String)session.getAttribute("token");
	String redirect_url =basePath+"login/Login.jsp";
	if(token != null && token.length() >0)
	{
		redirect_url +="?token="+token;
	}
	request.getSession().removeAttribute("token");
	request.getSession().invalidate();
	response.sendRedirect(redirect_url);
%>