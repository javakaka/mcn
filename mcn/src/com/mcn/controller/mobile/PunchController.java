package com.mcn.controller.mobile;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.FileUtil;
import com.ezcloud.utility.StringUtil;
import com.mcn.service.CompanyUser;
import com.mcn.service.MessageReadLogService;
import com.mcn.service.PunchLogService;
import com.mcn.service.PunchRuleService;

/**
 * 手机端打卡接口
 * @author JianBoTong
 *
 */
@Controller("mobilePunchController")
@RequestMapping("/api/punch")
public class PunchController extends BaseController{
	
	@Resource(name = "mcnPunchRuleService")
	private   PunchRuleService punchRuleService;
	
	@Resource(name = "mcnPunchLogService")
	private   PunchLogService punchLogService;
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	@Resource(name = "mcnMessageReadLogService")
	private MessageReadLogService messageReadLogService;
	
	//根据人员编号查询打卡时间
	@RequestMapping("/times")
	public @ResponseBody String queryDepartPunchTimes(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
//		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		Row timeRow =punchRuleService.queryDepartTimes(id);
		if(timeRow !=null){
		ovo =new OVO();
		String am_start =timeRow.getString("am_start","");
		String am_end =timeRow.getString("am_end","");
		String pm_start =timeRow.getString("pm_start","");
		String pm_end =timeRow.getString("pm_end","");
		ovo.set("am_start", am_start);
		ovo.set("am_end", am_end);
		ovo.set("pm_start", pm_start);
		ovo.set("pm_end", pm_end);
		json =VOConvert.ovoToJson(ovo);
		}else {
			ovo =new OVO(-1,"时间未设置","时间未设置");
			json =VOConvert.ovoToJson(ovo);
		}
		return json;
	} 
	
	
	//打卡
	@RequestMapping("/add")
	public @ResponseBody String punch(HttpServletRequest request) throws JException, UnsupportedEncodingException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		String punch_time =ivo.getString("punch_time",null);
		String punch_type =ivo.getString("punch_type",null);
		String leave_id =ivo.getString("leave_id","");//外出单id
		String overtime_id =ivo.getString("overtime_id","");//加班单id
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(punch_type == null)
		{
			ovo =new OVO(-20025,"punch_type不能为空","punch_type不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		
		if(punch_type.equals("7") || punch_type.equals("8"))
		{
			if(StringUtils.isEmptyOrNull(leave_id))
			{
				ovo =new OVO(-20000,"签到必须关联外出申请单,overtime_id参数不能为空","签到必须关联外出申请单,overtime_id参数不能为空");
				return VOConvert.ovoToJson(ovo);
			}
		}
		else if(punch_type.equals("5") || punch_type.equals("6"))
		{
			if(StringUtils.isEmptyOrNull(overtime_id))
			{
				ovo =new OVO(-20000,"加班打卡必须关联加班申请单,leave_id参数不能为空","签到必须关联外出申请单,leave_id参数不能为空");
				return VOConvert.ovoToJson(ovo);
			}
		}
		//判断是否已经打过卡
		else
		{
			if(!punchLogService.checkBooleanPuncn(token,id,punch_time,punch_type)){
				ovo =new OVO(-20000,"打卡失败","你已经打过卡了");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
		}
		ovo =new OVO();
		if(punch_time == null)
		{
			ovo =new OVO(-20026,"punch_time打卡时间不能为空","punch_time打卡时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String longitude =ivo.getString("longitude","");
		String latitude =ivo.getString("latitude","");
		String place_name =ivo.getString("place_name","");
		//base64 编码图片
		String userPic2 =(ivo.getString("picture",null));
		String userPic = new String(Base64Util.decode(userPic2));
		String imgPath ="";
		String imgName =ivo.getString("picture_name",null);
		if(imgName == null || imgName.trim().equals(""))
		{
			imgName =System.currentTimeMillis()+"_"+StringUtil.getRandKeys(16)+"";
		}
		if(userPic != null && !userPic.trim().equals(""))
		{
//			String basePath =request.getRealPath("/resources");
			String basePath =request.getSession().getServletContext().getRealPath("/resources");
			basePath +="/"+token;
			File file =new File(basePath);
			if(! file.isDirectory())
			{
				FileUtil.mkdir(basePath);
			}
			imgPath=basePath+"/"+imgName+".jpg";
			imgPath =imgPath.replace("\\\\","\\");
			imgPath =imgPath.replace("\\","/");
			Base64Util.GenerateImage(userPic, imgPath);
		}
		//计算punch_result minus_time
		String p_time =DateUtil.getCurrentDateTime();
		String punch_result="";
		String minus_time="";
		String errorMsg="";
		Row pRow =null;
		//签到不计算结果
		if("123456".indexOf(punch_type) != -1)
		{
			if("1234".indexOf(punch_type) != -1)
			{
				pRow =punchLogService.getPunchResult(id,punch_type,p_time,"");
			}
			else
			{
				pRow =punchLogService.getPunchResult(id,punch_type,p_time,overtime_id);
			}
			errorMsg =pRow.getString("errorMsg","");
			if(! StringUtils.isEmptyOrNull(errorMsg))
			{
				ovo =new OVO(-20026,errorMsg,errorMsg);
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			punch_result =pRow.getString("punch_result","");
			minus_time =pRow.getString("minus_time","");
		}
		Row punchRow =new Row();
		String imgpath = "resources/"+token+"/"+imgName+".jpg";
		System.out.println("imgpath============="+imgpath);
		punchRow.put("punch_type", punch_type);
		punchRow.put("punch_time", p_time);
		punchRow.put("longitude", longitude);
		punchRow.put("latitude", latitude);
		punchRow.put("place_name", place_name);
//		punchRow.set("punch_result", "1");
		punchRow.put("org_id", token);
		punchRow.put("user_id", id);
		punchRow.put("img_path", imgpath);
		punchRow.put("punch_status", "0");
		if(! StringUtils.isEmptyOrNull(punch_result))
		{
			punchRow.put("punch_result", punch_result);
		}
		if(! StringUtils.isEmptyOrNull(minus_time))
		{
			punchRow.put("minus_time", minus_time);
		}
		punchLogService.mobilePunch(punchRow);
		//保存签到关联表mcn_punch_leave
		if(! StringUtils.isEmptyOrNull(leave_id))
		{
			String p_id =punchLogService.getRow().getString("id","");
			Row punch_leave_row =new Row();
			punch_leave_row.put("p_id", p_id);
			punch_leave_row.put("l_id", leave_id);
			punchLogService.savePunchLeaveRecord(punch_leave_row);
		}
		//保存到加班关联表mcn_punch_overtime
		if(! StringUtils.isEmptyOrNull(overtime_id))
		{
			String p_id =punchLogService.getRow().getString("id","");
			Row punch_overtime_row =new Row();
			punch_overtime_row.put("p_id", p_id);
			punch_overtime_row.put("o_id", overtime_id);
			punchLogService.savePunchOvertimeRecord(punch_overtime_row);
		}
		//计算地理位置是否正确
		
		ovo =new OVO(0,"success","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
		
	//根据人员编号和月份查询打卡记录
	@RequestMapping("/user_month_log")
	public @ResponseBody String queryUserPunchLog(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
//			String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String date =ivo.getString("date",null);//2014-10
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds = punchLogService.queryUserMonthLog(id,date,page,page_size);
		ovo =new OVO();
		ovo.set("list", ds);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//打卡设置
	@RequestMapping("punchsz")
	public @ResponseBody String queryPunchSZ(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String punchsz= punchLogService.queryPunchSZ(token);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("punchsz",punchsz);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//通知公告
	@RequestMapping("message")
	public @ResponseBody String queryMessage(HttpServletRequest request) throws Exception
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		Row row2 = (Row) punchLogService.queryMessage(token);
		Row row =new Row();
		row = row2;
		String content = AesUtil.encode(row.getString("message_content"));
		row.put("message_content", content);
		System.out.println("data====="+row.toString());
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("row",row);
		json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	
	
	
	
}
