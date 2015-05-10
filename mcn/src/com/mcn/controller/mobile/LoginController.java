package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.system.Bureau;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.mcn.service.CompanyModule;
import com.mcn.service.CompanySite;
import com.mcn.service.CompanyUser;

@Controller("mobileLoginController")
@RequestMapping("/api/action")
public class LoginController extends BaseController {
	
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureauService;
	
	@Resource(name = "companySiteService")
	private CompanySite companySiteService;
	
	@Resource(name = "adminCompanyModuleService")
	private CompanyModule companyModuleService;
	
	/**
	 * 登陆
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/login")
	public @ResponseBody
	String  login(HttpServletRequest request) throws JException
	{
		parseRequest(request);
		String token =ivo.getString("token",null);
		String username =ivo.getString("username",null);
		String password =ivo.getString("password",null);
		//检查企业是否已停用、在有效期、部门是否在有效期
		boolean bool =bureauService.isBureauStoped(token);
		if(! bool)
		{
			ovo =new OVO(-10003,"企业已停用，不能登陆","企业已停用，不能登陆");
			return VOConvert.ovoToJson(ovo);
		}
		bool =bureauService.isBureauInServiceTime(token);
		if(! bool)
		{
			ovo =new OVO(-10003,"企业使用时间已过期，不能登陆","企业使用时间已过期，不能登陆");
			return VOConvert.ovoToJson(ovo);
		}
		Row staff =companyUserService.login(token, username);
		if(staff == null)
		{
			ovo =new OVO();
			ovo.iCode =-10003;
			ovo.sMsg ="不存在此帐户";
			ovo.sExp ="不存在此帐户";
		}
		else
		{
			//检查部门是否已停用
			boolean boolDepart =false;
			String depart_id =staff.getString("depart_id","");
			if(! StringUtils.isEmptyOrNull(depart_id))
			{
				boolDepart =companySiteService.isDepartStoped(depart_id);
			}
			if(boolDepart)
			{
				ovo =new OVO(-10003,"此用户所在部门已停用，不能登陆","此用户所在部门已停用，不能登陆");
				return VOConvert.ovoToJson(ovo);
			}
			String pwd =staff.getString("password",null);
			System.out.println("pwd="+pwd);
			if(pwd != null && ! pwd.equals(password))
			{
				ovo =new OVO(-10004, "密码错误", "密码错误");
			}
			else if(pwd !=null && pwd.equals(password)){
				//检查用户是否已停用
				String user_status =staff.getString("status","");
				if(user_status.equals("2") || user_status.equals("3") )
				{
					ovo =new OVO(-10003,"此用户已停用，不能登陆","此用户已停用，不能登陆");
					return VOConvert.ovoToJson(ovo);
				}
				ovo =new OVO(0, "登陆成功", "登陆成功");
				// user profile info
				ovo.set("user_id",staff.getString("id","") );
				ovo.set("username",username );
				ovo.set("realname",staff.getString("name", ""));
				ovo.set("telephone",staff.getString("telephone", ""));
				ovo.set("sex",staff.getString("sex", ""));
				ovo.set("depart_id",staff.getString("depart_id", ""));
				ovo.set("position",staff.getString("position", ""));
				ovo.set("manager_id",staff.getString("manager_id", ""));
				ovo.set("remark",staff.getString("remark", ""));
				//获取企业模块
				Row module = companyModuleService.edit(token);
//				checkin 签到，0没有此功能1有此功能
//				ask_leave 请假，0没有此功能1有此功能
//				query_leave 基本查询，0没有此功能1有此功能
//				query_base 基本查询，0没有此功能1有此功能
//				change_pwd 修改密码，0没有此功能1有此功能
//				message 通知，0没有此功能1有此功能
				String punch=module.getString("punch","0");
				String checkin=module.getString("checkin","0");
				String ask_leave=module.getString("ask_leave","0");
				String query_leave=module.getString("query_leave","0");
				String query_base=module.getString("query_base","0");
				String change_pwd=module.getString("change_pwd","0");
				String message=module.getString("message","0");
				String  base_statistic=module.getString("base_statistic","0");
				String punch_statistic=module.getString("punch_statistic","0");
				String audit=module.getString("audit","0");
				ovo.set("punch", punch);
				ovo.set("checkin", checkin);
				ovo.set("ask_leave", ask_leave);
				ovo.set("query_leave", query_leave);
				ovo.set("query_base", query_base);
				ovo.set("change_pwd", change_pwd);
				ovo.set("message", message);
				ovo.set("base_statistic", base_statistic);
				ovo.set("punch_statistic", punch_statistic);
				ovo.set("audit", audit);
			}
		}
		return VOConvert.ovoToJson(ovo);
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/changePassword")
	public @ResponseBody
	String changePassword(HttpServletRequest request) throws JException
	{
		parseRequest(request);
//		String token =ivo.getString("token",null);
		String user_id =ivo.getString("user_id",null);
		String oldPwd =ivo.getString("oldPwd",null);
		String newPwd =ivo.getString("newPwd",null);
		
		if(user_id == null || user_id.replace(" ", "").length() ==0){
			ovo =new OVO(-10005,"用户编号不能为空","用户编号不能为空");
		}
		else
		{
			int status =companyUserService.changePassword(user_id, oldPwd, newPwd);
			if(status ==1)
			{
				ovo =new OVO(-10006,"用户不存在","用户不存在");
			}
			else if(status ==2)
			{
				ovo =new OVO(-10007,"用户旧密码不正确","用户旧密码不正确");
			}
			else if(status ==3)
			{
				ovo =new OVO(-10008,"修改密码失败","修改密码失败");
			}
			else if(status ==0)
			{
				ovo =new OVO(0,"修改密码成功","");
			}
		}
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	/**
	 * 更新信息
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/updateProfile")
	public @ResponseBody
	OVO updateProfile(HttpServletRequest request) throws JException
	{
		parseRequest(request);
		System.out.print("login controller ======>>"+ivo);
		ovo.set("name", "admin");
		return ovo;
	}
}
