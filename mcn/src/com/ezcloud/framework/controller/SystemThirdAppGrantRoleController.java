package com.ezcloud.framework.controller;

import java.net.URLDecoder;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.system.StaffAppRoleService;
import com.ezcloud.framework.service.system.SystemPositionAppRole;
import com.ezcloud.framework.service.system.SystemThirdAppGrantRole;
import com.ezcloud.framework.service.system.SystemThirdAppRole;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("frameworkSystemThirdAppGrantRoleController")
@RequestMapping("system/grantrole/app")
public class SystemThirdAppGrantRoleController extends BaseController{

	@Resource(name = "frameworkSystemThirdAppGrantRoleService")
	private SystemThirdAppGrantRole systemThirdAppGrantRoleService;
	
	@Resource(name = "frameworkSystemThirdAppRoleService")
	private SystemThirdAppRole systemThirdAppRoleService;
	
	@Resource(name = "frameworkPositionAppRoleService")
	private SystemPositionAppRole systemPositionAppRoleService;
	
	@Resource(name = "frameworkStaffAppRoleService")
	private StaffAppRoleService systemStaffAppRoleService;
	
	
	@RequestMapping("/GrantRole")
	public String grantRole( ModelMap model) throws JException, SQLException
	{
		// 取得企业token
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/grantrole/app/GrantRole";
		}
		model.addAttribute("treeData", systemThirdAppGrantRoleService.getDeptPostionStaffTreeByBureauNo(org_id));
		return "/system/grantrole/app/GrantRole";
	}
	
	/**
	 * 显示当前选中的部门或者人员的角色列表
	 * 1 全部的角色列表
	 * 2 角色或者人员拥有的角色
	 * @param model
	 * @return
	 * @throws JException
	 * @throws SQLException
	 */
	@RequestMapping("/RoleAuth")
	public String roleAuth(HttpServletRequest request, ModelMap model) throws JException, SQLException
	{
		String type ="0";//默认不查询岗位或者人员的角色
		// 取得企业token
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/grantrole/app/RoleAuth";
		}
		// bureau roles
		model.addAttribute("roles", systemThirdAppRoleService.findRoleByBureauNo(org_id));
		// position or staff 's roles 
		String posi_no =(String)request.getParameter("posi_no");
		// position
		DataSet posiRoleDataSet =null;
		if( posi_no != null  && posi_no.replace(" ", "").length() > 0)
		{
			String posi_name  = (String)request.getParameter("posi_name");
			if(posi_name == null ){
				posi_name = "";
			}
			type ="1";
			System.out.println("posi_no =====>>"+posi_no);
			systemPositionAppRoleService.getRow().put("posi_no", posi_no);
			posiRoleDataSet = systemPositionAppRoleService.queryPositionRole();
			model.addAttribute("role_dataset", posiRoleDataSet);
			model.addAttribute("posi_name", posi_name);
			model.addAttribute("posi_no", posi_no);
		}
		String staff_no =(String)request.getParameter("staff_no");
		// staff
		DataSet staffRoleDataSet =null;
		if( staff_no != null  && staff_no.replace(" ", "").length() > 0)
		{
			String staff_name  = (String)request.getParameter("staff_name");
			if(staff_name == null )
			{
				staff_name = "";
			}
			type ="4";
			systemStaffAppRoleService.getRow().put("staff_no", staff_no);
			staffRoleDataSet = systemStaffAppRoleService.queryStaffRole();
			model.addAttribute("role_dataset", staffRoleDataSet);
			System.out.println("type ===============>>"+type);
			model.addAttribute("staff_name", staff_name);
			model.addAttribute("staff_no", staff_no);
		}
		model.addAttribute("type", type);
		return "/system/grantrole/app/RoleAuth";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveRoleAuth")
	public @ResponseBody ResponseVO saveRoleAuth(String type,String id, String items)
	{
		ResponseVO  ovo =new ResponseVO();
		Assert.notNull(type,"type not null");
		Assert.notNull(id,"id not null");
		String arr [] =null;
		String item[] =null;
		DataSet roleDataSet =new DataSet();
		Row roleRow =null;
		//posi
		if(type.equals("1"))
		{
			if(items == null || items.replace(" ", "").length() == 0)
			{
				roleDataSet =null;
			}
			else
			{
				arr  =items.split(",");
				for(int i=0; i< arr.length; i++)
				{
					item =arr[i].split("@");
					roleRow =new Row();
					roleRow.put("posi_no", id);
					roleRow.put("role_id", item[0]);
					roleRow.put("use_state", item[1]);
					roleRow.put("assign_state", item[2]);
					roleDataSet.add(roleRow);
				}
			}
			systemPositionAppRoleService.savePositionRoleAuth(id,roleDataSet);
		}
		else if(type.equals("4"))
		{
			if(items == null || items.replace(" ", "").length() == 0)
			{
				roleDataSet =null;
			}
			else
			{
				arr  =items.split(",");
				for(int i=0; i< arr.length; i++)
				{
					item =arr[i].split("@");
					roleRow =new Row();
					roleRow.put("staff_no", id);
					roleRow.put("role_id", item[0]);
					roleRow.put("use_state", item[1]);
					roleRow.put("assign_state", item[2]);
					roleDataSet.add(roleRow);
				}
			}
			systemStaffAppRoleService.saveStaffRoleAuth(id, roleDataSet);
		}
		ovo.put("status", "1");
		return ovo;
	}
}