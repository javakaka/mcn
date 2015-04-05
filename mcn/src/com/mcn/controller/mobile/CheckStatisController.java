package com.mcn.controller.mobile;

import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.mcn.service.CheckStatisService;

/**
 * 手机端打卡接口
 * @author JianBoTong
 *
 */
@Controller("mobileCheckStatisController")
@RequestMapping("/api/checkstatis")
public class CheckStatisController extends BaseController {
	@Resource(name = "checkStatisService")
	private CheckStatisService checkStatisService;
	//查询公司所有统计信息
	@RequestMapping("company")
	public @ResponseBody String CheckList(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token2="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String date = ivo.getString("date",null);
		Row row = checkStatisService.checkStatisCompany(token , date);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("company_name",row.getString("company_name"));
		ovo.set("work_day",row.getString("work_day"));
		ovo.set("leave_day",row.getString("leave_day"));
		ovo.set("add_day",row.getString("add_day"));
		ovo.set("all_day",row.getString("all_day"));
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//查询所有部门信息统计信息
	@RequestMapping("depart")
	public @ResponseBody String checkStatisDepart(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String date = ivo.getString("date",null);
		DataSet dataSet= checkStatisService.checkStatisDepart(token , date);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("depart_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//根据部门ID查询该部门统计信息
	@RequestMapping("person")
	public @ResponseBody String checkStatisPerson(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String date = ivo.getString("date",null);
		String depart_id = ivo.getString("depart_id",null);
		DataSet dataSet= checkStatisService.checkStatisPerson(token,depart_id,date);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("person_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//根据用户ID查询该员工统计的打卡详细信息
	@RequestMapping("persondetail")
	public @ResponseBody String checkStatisPersonDetail(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String date = ivo.getString("date",null);
		String user_id = ivo.getString("user_id",null);
		DataSet dataSet= checkStatisService.checkStatisPersonDetail(token,user_id,date);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("person_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//根据用户ID查询该员工统计的打卡详细信息
	@RequestMapping("personQDdetail")
	public @ResponseBody String checkStatisPersonDetail2(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String date = ivo.getString("date",null);
		String user_id = ivo.getString("user_id",null);
		DataSet dataSet= checkStatisService.checkStatisPersonDetail2(token,user_id,date);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("person_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//请假入口查询接口
	@RequestMapping("leaveUserId")
	public @ResponseBody String userIdAllLeave(HttpServletRequest request) throws JException, ParseException {
		parseRequest(request);
		System.out.println("通过token222222222="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String user_id = ivo.getString("user_id",null);
		DataSet dataSet= checkStatisService.userIdAllLeave(token,user_id);
		System.out.println("daadda=========="+dataSet.toString());
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("leave_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//公司看板查询
	@RequestMapping("companymodel")
	public @ResponseBody String companyModel(HttpServletRequest request) throws JException, ParseException{
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		String time = ivo.getString("date",null);
		DataSet dataSet= checkStatisService.companyModel(token,time);
		DataSet dataSet2 = checkStatisService.companyModelName(token);
		System.out.println("datasize=="+dataSet.size());
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("sum",dataSet.size());
		for(int i=0;i<dataSet.size();i++){
			DataSet dataSet_list = new DataSet();
			dataSet_list = (DataSet) dataSet.get(i);
			System.out.println("datastring=="+dataSet.get(i).toString());
			ovo.set("depart_list"+i,dataSet_list);
		}
		ovo.set("depart_name_list",dataSet2);
		//ovo.set("model_list",dataSet);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	//查询企业LOGO入口
	@RequestMapping("companylogo")
	public @ResponseBody String queryCompany(HttpServletRequest request) throws JException, ParseException{
		parseRequest(request);
		System.out.println("通过token="+ivo.getString("token",null));
		String token = ivo.getString("token",null);
		Row row = checkStatisService.queryCompany(token);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("company_row", row);
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
}
