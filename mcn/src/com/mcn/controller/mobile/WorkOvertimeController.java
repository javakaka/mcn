package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.CompanySite;
import com.mcn.service.CompanyUser;
import com.mcn.service.WorkOvertimeService;

/**
 * 手机端加班申请接口
 * @author JianBoTong
 *
 */
@Controller("mobileWorkOvertimeController")
@RequestMapping("/api/work_overtime")
public class WorkOvertimeController extends BaseController{
	@Resource(name ="mcnWorkOvertimeService")
	private WorkOvertimeService workOvertimeService;
	
	@Resource(name = "companyUserService")
	private  CompanyUser companyUserService;
	
	@Resource(name = "companySiteService")
	private  CompanySite companySiteService;
	
	
	//新增
	// id
	@RequestMapping("/add")
	public @ResponseBody String add(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		String start_time =ivo.getString("start_time",null);
		String end_time =ivo.getString("end_time",null);
		String remark =ivo.getString("remark","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(token))
		{
			ovo =new OVO(-20006,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(start_time))
		{
			ovo =new OVO(-20006,"开始时间不能为空","开始时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(end_time))
		{
			ovo =new OVO(-20006,"结束时间不能为空","结束时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		Row row =new Row();
		row.put("user_id", id);
		row.put("start_time", start_time);
		row.put("end_time", end_time);
		row.put("remark", remark);
		row.put("status", 1);
		row.put("org_id", token);
		//is existed 
		boolean conflict=workOvertimeService.isExistedWorkOverTime(id,start_time,end_time);
		if(conflict)
		{
			ovo =new OVO(-20005,"加班时间和系统已有的加班申请时间冲突","加班时间和系统已有的加班申请时间冲突");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		// depart_id 
		Row userRow =companyUserService.findById(id);
		String depart_id =userRow.getString("depart_id","");
		row.put("depart_id", depart_id);
		String cur_time =DateUtil.getCurrentDateTime();
		String year =cur_time.substring(0,4);
		String month =cur_time.substring(5,7);
		String day =cur_time.substring(8,10);
		row.put("year", year);
		row.put("month", month);
		row.put("day", day);
		workOvertimeService.insert(row);
		ovo =new OVO(0,"success","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	@RequestMapping("/existed")
	public @ResponseBody String queryCurrentTimeIsInWorkOvertime(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(token))
		{
			ovo =new OVO(-20006,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		boolean bool =workOvertimeService.isExistedWorkOverTime(id);
		ovo =new OVO(0,"success","");
		if(bool )
		{
			Row overRow =workOvertimeService.getCurrentWorkOverTimeRecord(id);
			String start_time =overRow.getString("start_time");
			String end_time =overRow.getString("end_time");
			ovo.set("can_punch", "1");
			ovo.set("start_time", start_time);
			ovo.set("end_time", end_time);
		}
		else
		{
			ovo.set("can_punch", "0");
		}
		json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	@RequestMapping("/list")
	public @ResponseBody String queryPage(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		String status =ivo.getString("status","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(token))
		{
			ovo =new OVO(-20006,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		DataSet ds =workOvertimeService.queryPageByUserId(id,page,page_size,status);
		ovo =new OVO(0,"success","success");
		ovo.set("list", ds);
		json =VOConvert.ovoToJson(ovo);
		return json;
	}
		
}
