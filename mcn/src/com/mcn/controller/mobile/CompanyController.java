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
import com.mcn.service.CompanyService;
import com.mcn.service.CompanyUserPermission;

/**
 * 手机端查询企业相关信息接口
 * @author JianBoTong
 *
 */
@Controller("mobileCompanyController")
@RequestMapping("/api/company")
public class CompanyController extends BaseController{
	
	@Resource(name = "companyService")
	private   CompanyService companyService;
	
	@Resource(name = "mcnCompanyUserPermissionService")
	private   CompanyUserPermission companyUserPermissionService;
	
	//查询企业的部门以及部门人员,如果当前登陆人员在后台配置了权限，则查询他具有管理权限的部门数据
	@RequestMapping("/all")
	public @ResponseBody String queryUserPunchLog(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		DataSet departDs =null;
		DataSet userDs =null;
		String token =ivo.getString("token",null);
		//当前登陆用户的编号
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			departDs =companyService.queryAllSites(token);
			userDs =companyService.queryAllUsers(token);
		}
		else
		{
			Row permissionRow =companyUserPermissionService.queryUserPeimissionFunIds(user_id);
			String is_need_permission =permissionRow.getString("is_need_permission","");
			String fun_ids =permissionRow.getString("fun_ids","");
//		String role_ids =permissionRow.getString("role_ids","");
			//不需要权限过滤，查询全部数据
			if(is_need_permission.equals("0"))
			{
				departDs =companyService.queryAllSites(token);
				userDs =companyService.queryAllUsers(token);
			}
			else if(is_need_permission.equals("1"))
			{
				departDs =companyService.queryAllSites(token,fun_ids);
				userDs =companyService.queryAllUsers(token,fun_ids);
			}
		}
		ovo =new OVO();
		ovo.set("depart_list", departDs);
		ovo.set("user_list", userDs);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	
}
