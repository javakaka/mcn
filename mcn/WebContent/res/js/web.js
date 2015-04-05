var WEB_PATH ="http://localhost:8080/insurer/"

function dynIframeSize(iframe, overheight) 
{
	overheight =overheight?overheight:0;
	var pTar = null;
	if (typeof (iframe) == "string") 
	{
		if (document.getElementById){
			pTar = document.getElementById(iframe);
		}
		else{
			eval('pTar = ' + iframe + ';');
		}
	}
	else if(iframe)
		pTar =iframe;
	if (pTar && !window.opera)
	{
		//begin resizing iframe
		pTar.style.display="block"

		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight)
		{
		//ns6 syntax
			pTar.height = pTar.contentDocument.body.offsetHeight+FFextraHeight +overheight; 
		}
		else if (pTar.Document && pTar.Document.body.scrollHeight)
		{
		//ie5+ syntax
			pTar.height = pTar.Document.body.scrollHeight +overheight;
		}
	}
}
/**
 * ��֤����
 * @param String
 * @return
 */
function checknumber(String)
{ 
   var Letters = "1234567890";
    var i;
    var c;
    for( i = 0; i < String.length; i ++ )
    {
         c = String.charAt( i );
      if (Letters.indexOf( c ) ==-1)
      {
       return true;
      }
    }
    return false;
}

/*
 * ���ʱ���,ʱ���ʽΪ ��-��-�� Сʱ:����:�� ���� ��/��/�� Сʱ�����ӣ���
 * ���У�������Ϊȫ��ʽ������ �� 2010-10-12 01:00:00
 * ���ؾ���Ϊ���룬�֣�Сʱ����
 */
 function GetDateDiff(startTime, endTime, diffType) 
 {
     //��xxxx-xx-xx��ʱ���ʽ��ת��Ϊ xxxx/xx/xx�ĸ�ʽ
     startTime = startTime.replace(/\-/g, "/");
     endTime = endTime.replace(/\-/g, "/");
     //�������������ַ�ת��ΪСд
     diffType = diffType.toLowerCase();
     var sTime = new Date(startTime);      //��ʼʱ��
     var eTime = new Date(endTime);  //����ʱ��
     //��Ϊ����������
     var divNum = 1;
     switch (diffType) {
         case "second":
             divNum = 1000;
             break;
         case "minute":
             divNum = 1000 * 60;
             break;
         case "hour":
             divNum = 1000 * 3600;
             break;
         case "day":
             divNum = 1000 * 3600 * 24;
             break;
         case "year":
             divNum = 1000 * 3600 * 24*365;
             break;
         default:
             break;
     }
     return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
}
 
 function getDate(type){
    var gdate=new Date();
    var year=gdate.getFullYear();
    var month=gdate.getMonth();
    var weekNum=gdate.getDay();
    var day=gdate.getDate();
    var hour=gdate.getHours();
    var minute=gdate.getMinutes();
    var second=gdate.getSeconds();
    var week;
    switch(weekNum){
       case 0:
          week="������";
          break;
       case 1:
          week="����һ";
          break;
       case 2:
          week="���ڶ�";
          break;
       case 3:
          week="������";
          break;
       case 4:
          week="������";
          break;
       case 5:
          week="������";
          break;
       case 6:
          week="������";
          break;
    }
    var getTimes ="";
    if(type == "1")
    {
    	getTimes=year+"��"+getInfo((month+1))+"��"+getInfo(day)+"��"+" "+week+" "+getInfo(hour)+":"+getInfo(minute)+":"+getInfo(second);	
    }
    else if(type == "2")
    {
    	getTimes=year+"-"+getInfo((month+1))+"-"+getInfo(day)+" "+getInfo(hour)+":"+getInfo(minute)+":"+getInfo(second);
    }
    else if(type == "3")
    {
    	getTimes=year+"-"+getInfo((month+1))+"-"+getInfo(day)+" 00:"+"00:00";
    }
    return getTimes;
 }
 
 function getInfo(info)
 {
     var ret;
     if(parseInt(info)<10)
         ret="0"+info;
     else
         ret=""+info;
     return ret;
 }


 /**
*ȡ���
*/
function getTable(id)
{
	var TableHandle =document.getElementById(id);
	return TableHandle;
}

/*������
*/
function clearTable(TableHandle, deleteAll)
{
	if (TableHandle == null) return;
	if(typeof deleteAll !="undefined" && deleteAll){
		for (var i = TableHandle.rows.length; i > 0; i--)
			TableHandle.deleteRow(i-1);
	}
	else
		for (var i = TableHandle.rows.length; i > 1; i--)
			TableHandle.deleteRow(i-1);
}

/**
*������
*/
function clickTable(a)
{
 var arr =document.all.tags(a.tagName);
 var length=arr.length;
 for(i=0; i<length; i++)
 {
	 var temp=arr[i];
	 var onclick =temp.onclick;
	 if(a==temp)
	 {
		 //ѡ�еı�ǩ��ʽ
		 temp.style.background="#E4F1FA";
	  }
	  else
	  {
		 if(onclick == null || typeof onclick == "undefined" || onclick == "" )
		 {
		 }
		 else
		 {
			//�ָ�ԭ״
			temp.style.background="#FFFFFF";
		 }
	  }
 }
}


function clickTableEvent(a)
{
 var arr =document.all.tags(a.tagName);

 var length=arr.length;
 for(i=0; i<length; i++)
 {
	 var temp=arr[i];
	 var onclick =temp.onclick;
	 if(a==temp)
	 {
		 //ѡ�еı�ǩ��ʽ
		 temp.className="tr_bg_over";
	  }
	  else
	  {
		 if(onclick == null || typeof onclick == "undefined" || onclick == "" )
		 {
			 //alert("null");
		 }
		 else
		 {
			//�ָ�ԭ״
			temp.className="tr_bg_out";
		 }
	  }
 }
}

/**
*����
*/
function lockScreen(progressImgName) 
{ 
var msgw,msgh,bordercolor; 
msgw=400;//��ʾ���ڵĿ�� 
msgh=100;//��ʾ���ڵĸ߶� 
titleheight=25 //��ʾ���ڱ���߶� 
bordercolor="#336699";//��ʾ���ڵı߿���ɫ 
titlecolor="#99CCFF";//��ʾ���ڵı�����ɫ 
var sWidth,sHeight; 
sWidth=document.body.offsetWidth;//��ȡ���ڿ�� 
sHeight=screen.height;//��ȡ��Ļ�߶� 
var bgObj=document.createElement("div");//�ؼ������ԭ����body�д���һ��div������������߶�����Ϊ�����������壬���һ�����޷�����������ʱ�в��� 
bgObj.setAttribute('id','bgDiv'); 
bgObj.style.position="absolute"; 
bgObj.style.top="0"; 
bgObj.style.background="#777"; 
bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75"; 
bgObj.style.opacity="0.6"; 
bgObj.style.left="0"; 
bgObj.style.width=sWidth + "px"; 
bgObj.style.height=sHeight + "px"; 
bgObj.style.zIndex = "10000"; 
document.body.appendChild(bgObj);//�������div������ʾ���� 
var msgObj=document.createElement('div');//����һ����Ϣ���� 
msgObj.setAttribute("id","msgDiv"); 
msgObj.setAttribute("align","center"); 
msgObj.style.background="white"; 
msgObj.style.border="1px solid " + bordercolor; 
msgObj.style.position = "absolute"; 
msgObj.style.left = "50%"; 
msgObj.style.top = "50%"; 
msgObj.style.font="12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif"; 
msgObj.style.marginLeft = "-225px" ; 
msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px"; 
msgObj.style.width = msgw+"px"; 
msgObj.style.height = msgh+"px"; 
msgObj.style.textAlign = "center"; 
msgObj.style.lineHeight ="25px"; 
msgObj.style.zIndex = "10001";
document.body.appendChild(msgObj); 
var progress =document.createElement("img");
progress.setAttribute("id","msgImg");
progress.src="../res/js/images/"+progressImgName;
document.getElementById("msgDiv").appendChild(progress); 
} 

function unLockScreen()
{
	var bgObj =document.getElementById("bgDiv");
	var msgObj =document.getElementById("msgDiv");
	document.body.removeChild(bgObj);//�Ƴ������������ڵ�div�� 
	document.body.removeChild(msgObj);//�Ƴ���Ϣ�� 
}


/**
*��ȡ�������
*/
  function getRequestParameter(paras)
 { 
        var url = location.href; 
        var paraString = url.substring(url.indexOf("?")+1,url.length).split("&"); 
        var paraObj = {} 
        for (i=0; j=paraString[i]; i++){ 
        paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length); 
        } 
        var returnValue = paraObj[paras.toLowerCase()]; 
        if(typeof(returnValue)=="undefined"){ 
        return ""; 
        }else{ 
        return returnValue; 
        } 
}

/**
*��֤�绰����
*/
function checkPhone(val)
{
	var p1 = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/; 
	var me = false; 
	if (p1.test(val))me=true; 
	if (!me){ 
	return false; 
	}
	else
		return true;
}
/**
*��֤�ֻ�����
*/
function checkMobile(val)
{
	var reg0 = /^13\d{5,9}$/; 
	var reg1 = /^153\d{4,8}$/; 
	var reg2 = /^159\d{4,8}$/; 
	var reg3 = /^0\d{10,11}$/; 
	var my = false; 
	if (reg0.test(val))my=true; 
	if (reg1.test(val))my=true; 
	if (reg2.test(val))my=true; 
	if (reg3.test(val))my=true; 
	if (!my){ 
	return false;
	}
	else
		return true;
}
/**
*��֤����
*/
function checkEmail(val)
{
	//�Ե����ʼ�����֤
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(!myreg.test(val))
	{
	  return false;
	}
	else 
		return true;
}