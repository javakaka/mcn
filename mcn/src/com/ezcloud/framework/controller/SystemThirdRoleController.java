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
import com.ezcloud.framework.service.system.SystemThirdFun;
import com.ezcloud.framework.service.system.SystemThirdRole;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
/**
 *角色维护
 * @author JianBoTong
 *
 */
@Controller("frameworkSystemThirdRoleController")
@RequestMapping("/system/role/third")
public class SystemThirdRoleController  extends BaseController{
	
	@Resource(name = "frameworkSystemThirdRoleService")
	private SystemThirdRole systemThirdRoleService;
	
	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureauService;
	
	@Resource(name = "frameworkSystemTirdFunService")
	private SystemThirdFun systemThirdFunService;
	
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
			return "/system/role/third/RoleList";
		}
		
		systemThirdRoleService.getRow().put("pageable", pageable);
		systemThirdRoleService.getRow().put("bureau_no", org_id);
		Page page = systemThirdRoleService.queryPage();
		model.addAttribute("page", page);
		return "/system/role/third/RoleList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		HttpSession session =getSession();
		String token =session.getAttribute("token").toString();
		System.out.println("token------->>"+token);
		String org_id =AesUtil.decode(URLDecoder.decode(token));
		if(StringUtils.isEmptyOrNull(org_id))
		{
			return "/system/role/third/add";
		}
		
		DataSet ds=bureauService.queryBureau(org_id);
		model.addAttribute("bureau", ds);
		return "/system/role/third/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String BUREAU_NO, String ROLE_NAME, String ROLE_DESC, String ROLE_BEGINTIME,String ROLE_ENDTIME ,String STATE,RedirectAttributes redirectAttributes) {
		systemThirdRoleService.getRow().clear();
		if(BUREAU_NO == null){
			BUREAU_NO ="";
		}
		systemThirdRoleService.getRow().put("BUREAU_NO", BUREAU_NO);
		systemThirdRoleService.getRow().put("ROLE_NAME", ROLE_NAME);
		systemThirdRoleService.getRow().put("ROLE_DESC", ROLE_DESC);
		systemThirdRoleService.getRow().put("ROLE_BEGINTIME", ROLE_BEGINTIME);
		systemThirdRoleService.getRow().put("ROLE_ENDTIME", ROLE_ENDTIME);
		systemThirdRoleService.getRow().put("STATE", STATE);
		systemThirdRoleService.save();
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
			return "/system/role/third/RoleList";
		}
		systemThirdRoleService.getRow().put("id", id);
		model.addAttribute("role", systemThirdRoleService.find());
		model.addAttribute("bureau", bureauService.queryBureau(org_id));
		return "/system/role/third/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ROLE_ID,String BUREAU_NO, String ROLE_NAME, String ROLE_DESC, String ROLE_BEGINTIME,String ROLE_ENDTIME ,String STATE,ModelMap model) {
		systemThirdRoleService.getRow().clear();
		Assert.notNull(ROLE_ID, "ROLE_ID 不能为空 ");
		Assert.notNull(ROLE_NAME, "ROLE_NAME 不能为空 ");
		systemThirdRoleService.getRow().put("ROLE_ID", ROLE_ID);
		systemThirdRoleService.getRow().put("BUREAU_NO", BUREAU_NO);
		systemThirdRoleService.getRow().put("ROLE_NAME", ROLE_NAME);
		systemThirdRoleService.getRow().put("ROLE_DESC", ROLE_DESC);
		systemThirdRoleService.getRow().put("ROLE_BEGINTIME", ROLE_BEGINTIME);
		systemThirdRoleService.getRow().put("ROLE_ENDTIME", ROLE_ENDTIME);
		systemThirdRoleService.getRow().put("STATE", STATE);
		systemThirdRoleService.update();
		return "redirect:RoleList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		systemThirdRoleService.delete(ids);
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
			return "/system/role/third/RoleFun";
		}
		model.addAttribute("roles", systemThirdRoleService.findRoleByBureauNo(org_id));
//		model.addAttribute("jsTreeData", systemThirdFunService.getFunJsTree("-1", "权限树", "checkbox", "js_node_permission", systemThirdFunService.findAll()));
		return "/system/role/third/RoleFun";
	}
	
	@RequestMapping(value = "/FunJsTree")
	public @ResponseBody
	ResponseVO funJsTree(String default_up_id,String tree_name,String show_type,String selector_id) {
		ResponseVO ovo =new ResponseVO();
		String jsTreeData= systemThirdFunService.getFunJsTree("-1", "权限树", "checkbox", "js_node_permission", systemThirdFunService.findAll());
		ovo.put("treeData", jsTreeData);
		return ovo;
	}
	
	@RequestMapping(value="/SelectRoleFun")
	public String selectRoleFun(String role_id,ModelMap model)
	{
		model.addAttribute("funs", systemThirdFunService.getSortedFuns(systemThirdFunService.findAllBySpecifiedFunId("04")));
		if(role_id == null || role_id.replace(" ", "").length() == 0)
		{
			model.addAttribute("role_funs", systemThirdFunService.getSortedFuns(null));
		}
		else
		{
			model.addAttribute("role_funs", systemThirdFunService.getSortedFuns(systemThirdFunService.getFunByRoleId(role_id)));
		}
		return "/system/role/third/SelectRoleFun";
	}
	
	@RequestMapping(value = "/FunsOfSelectedRole")
	public @ResponseBody
	DataSet  getFunByRoleId(String role_id) {
		Assert.notNull(role_id, "role id can not be null");
		DataSet ds=null;
		ds=systemThirdFunService.getFunByRoleId(role_id);
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
		int num =systemThirdFunService.saveRoleFuns(role_id,fun_id);
		ovo.put("num", num);
		return ovo;
	}
}
