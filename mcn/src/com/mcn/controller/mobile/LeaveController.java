package com.mcn.controller.mobile;

import java.text.ParseException;

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
import com.mcn.service.LeaveService;

/**
 * 手机端请假接口
 * @author JianBoTong
 *
 */
@Controller("mobileLeaveController")
@RequestMapping("/api/leave")
public class LeaveController extends BaseController{
	@Resource(name = "mcnLeaveService")
	private  LeaveService leaveService;
	
	@Resource(name = "companyUserService")
	private  CompanyUser companyUserService;
	
	@Resource(name = "companySiteService")
	private  CompanySite companySiteService;
	
//	@RequestMapping("/list")
//	public @ResponseBody String getMobileroomList(HttpServletRequest request) throws JException
//	{
//		String json ="";
//		parseRequest(request);
//		String token =ivo.getString("token",null);
//		String page =ivo.getString("page","1");
//		String pageSize =ivo.getString("pageSize","10");
//		DataSet ds =leaveService.getMobileRoomPage(token, page, pageSize);
//		ovo.set("room_list", ds);
//		json =VOConvert.ovoToJson(ovo);
//		return json;
//	} 
//	
	
	
	//新增请假记录
	// id
	@RequestMapping("/add")
	public @ResponseBody String addLeave(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		String sum_day =ivo.getString("sum_day",null);
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(token == null)
		{
			ovo =new OVO(-20006,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		//找出部门审批人
		Row userRow =companyUserService.findById(id);
		String depart_id =userRow.getString("depart_id","");
		String audit_user_id =companyUserService.queryDefaultAuditUserId(depart_id);
		if(StringUtils.isEmptyOrNull(audit_user_id))
		{
			DataSet ds =companyUserService.queryManagersByDepartID(depart_id);
			if(ds == null || ds.size() == 0)
			{
				//找出上级部门的审批人，如果当前部门有上级部门，则继续查找，否则，提示无审批人，不能请假
				Row departRow =companySiteService.findById(depart_id);
				String up_site_id =departRow.getString("up_site_no","");
				if(StringUtils.isEmptyOrNull(up_site_id))
				{
					ovo =new OVO(-1,"无审批人，不能请假","");
					json =VOConvert.ovoToJson(ovo);
					return json;
				}
				else
				{
					audit_user_id =findAuditUserIdByDepartId(up_site_id);
				}
			}
			else
			{
				Row uRow =(Row)ds.get(0);
				Row updateRow =new Row();
				uRow.put("default_manager", "1");
				updateRow.put("id",uRow.getString("id","") );
				companyUserService.update(updateRow);
				audit_user_id =uRow.getString("id","");
			}
		}
		if(StringUtils.isEmptyOrNull(audit_user_id))
		{
			ovo =new OVO(-1,"无审批人，不能请假","");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String leave_type=ivo.getString("leave_type","1");
		String start_date=ivo.getString("start_date",null);
		String end_date=ivo.getString("end_date",null);
		String reason=ivo.getString("reason",null);
		Row leaveRow =new Row();
		leaveRow.put("leave_type", leave_type.trim());
		leaveRow.put("start_date", start_date.trim());
		leaveRow.put("end_date",end_date.trim() );
		leaveRow.put("reason",reason.trim() );
		leaveRow.put("org_id",token.trim() );
		leaveRow.put("user_id",id.trim() );
		leaveRow.put("sum_day", sum_day.trim());
		int leave_id = leaveService.mobileAdd(leaveRow);
		Row checkRow = new Row();
		checkRow.put("leave_id",leave_id);
		checkRow.put("check_type",1);
		checkRow.put("up_check_id",audit_user_id);
//		leaveService.check_up_log(checkRow,token,id);
		leaveService.insert_check_up_log(checkRow);
		ovo =new OVO(0,"success","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	public String findAuditUserIdByDepartId(String depart_id)
	{
		String id="";
		id =companyUserService.queryDefaultAuditUserId(depart_id);
		if(! StringUtils.isEmptyOrNull(id))
		{
			return id;
		}
		else
		{
			DataSet ds =companyUserService.queryManagersByDepartID(depart_id);
			if(ds == null || ds.size() == 0)
			{
				//找出上级部门的审批人，如果当前部门有上级部门，则继续查找，否则，提示无审批人，不能请假
				Row departRow =companySiteService.findById(depart_id);
				String up_site_id =departRow.getString("up_site_no","");
				id =findAuditUserIdByDepartId(up_site_id);
			}
			else
			{
				Row uRow =(Row)ds.get(0);
				Row updateRow =new Row();
				uRow.put("default_manager", "1");
				updateRow.put("id",uRow.getString("id","") );
				companyUserService.update(updateRow);
				id =uRow.getString("id","");
				return id;
			}
		}
		return id;
	}
	//查询自己的请假记录
		// id
		@RequestMapping("/selfList")
		public @ResponseBody String querySelfLeaveList(HttpServletRequest request) throws JException
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
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			String status =ivo.getString("status",null);
			DataSet ds =leaveService.querySelfLeaveList(id,page,page_size,status);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds);
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
	
		//根据假期类别查询自己的请假记录
		@RequestMapping("/leaveTypeList")
		public @ResponseBody String queryLeaveTypeList(HttpServletRequest request) throws JException
		{
			parseRequest(request);
			System.out.println("通过token="+ivo.getString("token",null));
			String token = ivo.getString("token",null);
			String user_id = ivo.getString("user_id",null);
			String leave_type = ivo.getString("leave_type",null);
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			DataSet dataSet = leaveService.queryLeaveTypeList(token, user_id, page, page_size, leave_type);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",dataSet);
			String json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		
		
		//分页查询属下的请假记录
		// id
		@RequestMapping("/followerList")
		public @ResponseBody String queryFollowerLeaveList(HttpServletRequest request) throws JException, ParseException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			String id =ivo.getString("id",null);
			if(id == null)
			{
				ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			DataSet ds =leaveService.queryFollowerLeaveList(id,page,page_size);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
	
		//分页查询属下的请假记录
		// id
		@RequestMapping("/statusList")
		public @ResponseBody String queryFollowerLeaveList2(HttpServletRequest request) throws JException, ParseException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			String id =ivo.getString("id",null);
			if(id == null)
			{
				ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			DataSet ds =leaveService.queryFollowerLeaveList(id,page,page_size);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		
		//审批请假
		@RequestMapping("/audit")
		public @ResponseBody String auditFollowerLeave(HttpServletRequest request) throws JException
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
			String log_id =ivo.getString("log_id",null);
			String audit_objection =ivo.getString("audit_objection","");
			if(log_id == null)
			{
				ovo =new OVO(-20015,"log_id请假记录不能为空","log_id请假记录不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			
			String status =ivo.getString("status",null);
			if(status == null)
			{
				ovo =new OVO(-20016,"status审核状态不能为空","status审核状态不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String check_id =ivo.getString("check_id",null);
			if(!status.equals("0")){
			Row leaveRow =new Row();
			leaveRow.put("status", status);
			leaveRow.put("id", log_id);
			leaveRow.put("audit_objection", audit_objection);
			leaveRow.put("audit_id", id);
			leaveRow.put("modify_time", DateUtil.getCurrentDateTime());
			leaveService.auditLeave(leaveRow);
			
			String modify_time =DateUtil.getCurrentDateTime();
			leaveService.auditLeave2(check_id,status,audit_objection,modify_time);
			}else{
			Row checkRow =new Row();
			checkRow.put("leave_id", log_id);
			checkRow.put("check_id", id);
			String up_check_id =ivo.getString("up_id",null);
			checkRow.put("up_check_id", up_check_id);
			checkRow.put("check_type", 1);
			leaveService.auditCheck(checkRow);
			}
			ovo =new OVO(0,"success","");
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		//根据企业ID查询所有管理用户
		@RequestMapping("/allUsersList")
		public @ResponseBody String allUsersList(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			
			DataSet ds =leaveService.allUsersList(token);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		//根据请假记录levae_ID查询审核人
		@RequestMapping("/leaveId")
		public @ResponseBody String queryLeaveId(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			String leave_id =ivo.getString("id",null);
			DataSet ds =leaveService.queryLeaveId(leave_id);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		//请假规则说明
		@RequestMapping("/leavegz")
		public @ResponseBody String queryLeaveGZ(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			Row row =leaveService.queryLeaveGZ(token);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("row",row);
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		//审批规则说明
		@RequestMapping("/leavespgz")
		public @ResponseBody String queryLeaveSPGZ(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			Row row =leaveService.queryLeaveSPGZ(token);
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("row",row);
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
		
		//分页查询自己的外出申请单
		@RequestMapping("leaveLogList")
		public @ResponseBody String queryLeaveListByUserId(HttpServletRequest request) throws Exception
		{
			String json ="";
			parseRequest(request);
			String token =ivo.getString("token",null);
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			String user_id =ivo.getString("user_id",null);
			if(StringUtils.isEmptyOrNull(user_id))
			{
				ovo =new OVO(-10022,"用户编号不能为空","用户编号不能为空");
				return VOConvert.ovoToJson(ovo);
			}
			DataSet ds=leaveService.queryLeaveListByType(user_id, "7", Integer.parseInt(page),  Integer.parseInt(page_size));
			ovo =new OVO(0, "请求成功", "请求成功");
			ovo.set("list",ds); 
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		
}
