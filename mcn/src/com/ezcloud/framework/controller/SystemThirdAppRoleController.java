package com.ezcloud.framework.controller;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.Bureau;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.service.system.SystemThirdAppFun;
import com.ezcloud.framework.service.system.SystemThirdAppRole;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
/**
 *APP 角色维护
 * @author JianBoTong
 *
 */
@Controller("frameworkSystemThirdAppRoleController")
@RequestMapping("/system/role/app")
public class SystemThirdAppRoleController  extends BaseController{
	
	@Resource(name = "frameworkSystemThirdAppRoleService")
	private SystemThirdAppRole systemThirdAppRoleService;
	
	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureauService;
	
//	@Resource(name = "frameworkSystemTirdFunService")
//	private SystemThirdFun systemThirdAppFunService;
	
	@Resource(name = "frameworkSystemTirdAppFunService")
	private SystemThirdAppFun systemThirdAppFunService;
	
	@Resource(name = "frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	
	/**
	 * 查询企业下的角色列表
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/RoleList")
	public String list(Pageable pageable, ModelMap model) {
		// 取得企业token
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/app/RoleList";
		}
		
		systemThirdAppRoleService.getRow().put("pageable", pageable);
		systemThirdAppRoleService.getRow().put("bureau_no", org_id);
		Page page = systemThirdAppRoleService.queryPage();
		model.addAttribute("page", page);
		return "/system/role/app/RoleList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/app/add";
		}
		
		DataSet ds=bureauService.queryBureau(org_id);
		model.addAttribute("bureau", ds);
		return "/system/role/app/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String BUREAU_NO, String ROLE_NAME, String ROLE_DESC, String ROLE_BEGINTIME,String ROLE_ENDTIME ,String STATE,RedirectAttributes redirectAttributes) {
		systemThirdAppRoleService.getRow().clear();
		if(BUREAU_NO == null){
			BUREAU_NO ="";
		}
		systemThirdAppRoleService.getRow().put("BUREAU_NO", BUREAU_NO);
		systemThirdAppRoleService.getRow().put("ROLE_NAME", ROLE_NAME);
		systemThirdAppRoleService.getRow().put("ROLE_DESC", ROLE_DESC);
		systemThirdAppRoleService.getRow().put("ROLE_BEGINTIME", ROLE_BEGINTIME);
		systemThirdAppRoleService.getRow().put("ROLE_ENDTIME", ROLE_ENDTIME);
		systemThirdAppRoleService.getRow().put("STATE", STATE);
		systemThirdAppRoleService.save();
		return "redirect:RoleList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/app/RoleList";
		}
		systemThirdAppRoleService.getRow().put("id", id);
		model.addAttribute("role", systemThirdAppRoleService.find());
		model.addAttribute("bureau", bureauService.queryBureau(org_id));
		return "/system/role/app/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ROLE_ID,String BUREAU_NO, String ROLE_NAME, String ROLE_DESC, String ROLE_BEGINTIME,String ROLE_ENDTIME ,String STATE,ModelMap model) {
		systemThirdAppRoleService.getRow().clear();
		Assert.notNull(ROLE_ID, "ROLE_ID 不能为空 ");
		Assert.notNull(ROLE_NAME, "ROLE_NAME 不能为空 ");
		systemThirdAppRoleService.getRow().put("ROLE_ID", ROLE_ID);
		systemThirdAppRoleService.getRow().put("BUREAU_NO", BUREAU_NO);
		systemThirdAppRoleService.getRow().put("ROLE_NAME", ROLE_NAME);
		systemThirdAppRoleService.getRow().put("ROLE_DESC", ROLE_DESC);
		systemThirdAppRoleService.getRow().put("ROLE_BEGINTIME", ROLE_BEGINTIME);
		systemThirdAppRoleService.getRow().put("ROLE_ENDTIME", ROLE_ENDTIME);
		systemThirdAppRoleService.getRow().put("STATE", STATE);
		systemThirdAppRoleService.update();
		return "redirect:RoleList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		systemThirdAppRoleService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	// =================================role fun
	
	@RequestMapping(value="/RoleFun")
	public String roleFun(ModelMap model)
	{
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/app/RoleFun";
		}
		model.addAttribute("roles", systemThirdAppRoleService.findRoleByBureauNo(org_id));
//		model.addAttribute("jsTreeData", systemThirdAppFunService.getFunJsTree("-1", "权限树", "checkbox", "js_node_permission", systemThirdAppFunService.findAll()));
		return "/system/role/app/RoleFun";
	}
	
	@RequestMapping(value = "/FunJsTree")
	public @ResponseBody
	ResponseVO funJsTree(String default_up_id,String tree_name,String show_type,String selector_id) {
		ResponseVO ovo =new ResponseVO();
		String jsTreeData= systemThirdAppFunService.getFunJsTree("-1", "权限树", "checkbox", "js_node_permission", systemThirdAppFunService.findAll());
		ovo.put("treeData", jsTreeData);
		return ovo;
	}
	
	@RequestMapping(value="/SelectRoleFun")
	public String selectRoleFun(String role_id,ModelMap model)
	{
		//部门列表
		// 取得企业token
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/app/SelectRoleFun";
		}
		
//		model.addAttribute("funs", systemThirdAppFunService.getSortedFuns(systemThirdAppFunService.findAllBySpecifiedFunId("04")));
		model.addAttribute("funs", systemThirdAppFunService.getSortedFuns(systemSiteService.queryOrgSiteAsFun(org_id)));
		if(role_id == null || role_id.replace(" ", "").length() == 0)
		{
//			角色拥有管理权限的部门列表
			model.addAttribute("role_funs", systemThirdAppFunService.getSortedFuns(null));
		}
		else
		{
			model.addAttribute("role_funs", systemThirdAppFunService.getSortedFuns(systemThirdAppFunService.getFunByRoleId(role_id)));
		}
		return "/system/role/app/SelectRoleFun";
	}
	
	@RequestMapping(value = "/FunsOfSelectedRole")
	public @ResponseBody
	DataSet  getFunByRoleId(String role_id) {
		Assert.notNull(role_id, "role id can not be null");
		DataSet ds=null;
		ds=systemThirdAppFunService.getFunByRoleId(role_id);
		return ds;
	}
	
	@RequestMapping(value="/SaveRoleFun")
	public @ResponseBody ResponseVO saveRoleFuns(String role_id,String fun_id)
	{
		ResponseVO ovo =new ResponseVO();
		Assert.notNull(role_id, "角色编号不能为空！");
		Assert.notNull(fun_id, "权限编号不能为空！");
		System.out.println("role_id=============="+role_id);
		System.out.println("fun_id=============="+fun_id);
		int num =systemThirdAppFunService.saveRoleFuns(role_id,fun_id);
		ovo.put("num", num);
		return ovo;
	}
}
