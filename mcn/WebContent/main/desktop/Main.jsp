<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/$.tld" prefix="$" %>
<%@ include file="/include/Head2.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<style>
html {
overflow-x: hidden;
overflow-y: auto;
} </style>
<head>
<title>��������</title>
<link href="res/Main.css" rel="stylesheet" type="text/css">
<script src="res/Main.js"></script>
</head>
<body background="res/bg_01.gif" oncontextmenu="onSetDesktop();return false;">
<$:Logon/>
<div id="idDivSave" style="display:none"><input type=button value="������������" onCliCk="onSaveDesktopConfig()"></div>
<div class="move" id="root_row"><div class="title"><div class="title_close"><img src='<%=GlobalUtil._WEB_PATH%>/images/ico_close.gif'></div><!-- <div class="title_edit">�༭</div> --><div class="title_lock">����</div><div class="title_reduce"><!-- <img src='<%=GlobalUtil._WEB_PATH%>/images/ico_min_max.gif'> --></div><div class="title_a"></div></div><div class="content"></div></div>
<div class="root">
	<div class="cell_left left" id="m_1">
		<div class="line">��</div>
	</div>
	
	<div class="left r_nbsp">��</div>
	
	<div class="cell_center left" id="m_2">
		<div class="line">��</div>
	</div>
	
	<div class="left r_nbsp">��</div>
	
	<div class="cell_right left" id="m_3">
		<div class="line">��</div>
	</div>
</div>
</body>
</html>
<script language="javascript">
<!--
/*��������*/
var sConfig ="";
function onSetDesktop()
{
	var src =WEB_PATH+"/staff/portlet/WebPart.jsp";

	var sConfigs =$E.runOpen(src, true);
	if(sConfigs)
	{
		//*
		var sa =sConfigs.split(",");
		var sLeft ="";
		var sCenter ="";
		var sRight ="";
		var ss ="";
		for(var i=0; i<sa.length; i++)
		{
			if(i %3==0)
			{
				if(sLeft.length >0)
					sLeft +=",";
				sLeft +=sa[i];
			}
			else if(i %3==1)
			{
				if(sCenter.length >0)
					sCenter +=",";
				sCenter +=sa[i];
			}
			else if(i %3==2)
			{
				if(sRight.length >0)
					sRight +=",";
				sRight +=sa[i];
			}
		}
		ss =sLeft +"|"+sCenter+"|" +sRight;
		//*/
		var ssConfig =ss;
		var oTc =$E.getElementsByClassName("title_close", "div");//�رղ�
		var oTa =$E.getElementsByClassName("title_a", "div");//�����

		if(oTc.length ==oTa.length)
		{
			for(var i=0; i<oTc.length; i++)
			{
				if(oTa[i].innerHTML !="")
					oTc[i].click();
			}
		}
		sConfig =ssConfig+","+sConfigs;
		viewDesktop();
		onViewSaveDiv(true);
	}
}

/*������������*/
function onSaveDesktopConfig()
{
	var s =new Server("setStaffPortlet");
	s.setParam("part_serial", sConfig);
	if(s.send(s, true))
	{
		onViewSaveDiv(false);
		$E.message("�������óɹ���");
	}
}

/*��ʾ���ñ����*/
function onViewSaveDiv(tag)
{
	idDivSave.style.display =tag?"":"none";
}
//-->
</script>