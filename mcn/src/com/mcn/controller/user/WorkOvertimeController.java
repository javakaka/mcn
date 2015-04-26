package com.mcn.controller.user;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.WorkOvertimeService;

@Controller("mcnWorkOvertimeController")
@RequestMapping("/mcnpage/user/punch/overtime")
public class WorkOvertimeController extends BaseController{
	
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name ="mcnWorkOvertimeService")
	private WorkOvertimeService workOvertimeService;
	
	
	/**
	 * 企业查询自己的请假加班申请单
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(String depart_id,String status,String start_time,String end_time ,Pageable pageable, ModelMap model) {
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		
		if(org_id == null){
			return "/mcnpage/user/punch/overtime/list";
		}
		workOvertimeService.getRow().put("org_id", org_id);
		workOvertimeService.getRow().put("depart_id", depart_id);
		workOvertimeService.getRow().put("status", status);
		workOvertimeService.getRow().put("start_time", start_time);
		workOvertimeService.getRow().put("end_time", end_time);
		workOvertimeService.getRow().put("pageable", pageable);
		Page page = workOvertimeService.queryPageForCompany();
		model.addAttribute("page", page);
		model.addAttribute("depart_id", depart_id);
		model.addAttribute("status", status);
		model.addAttribute("start_time", start_time);
		model.addAttribute("end_time", end_time);
		return "/mcnpage/user/punch/overtime/list";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/overtime/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String AM_START,String AM_END, String PM_START,String PM_END,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		workOvertimeService.getRow().put("DEPART_ID", DEPART_ID);
		workOvertimeService.getRow().put("AM_START", AM_START);
		workOvertimeService.getRow().put("AM_END", AM_END);
		workOvertimeService.getRow().put("PM_START", PM_START);
		workOvertimeService.getRow().put("PM_END", PM_END);
		
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:LeaveList.do";
		workOvertimeService.getRow().put("org_id", org_id);
		workOvertimeService.save();
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		workOvertimeService.getRow().put("id", id);
		model.addAttribute("row", workOvertimeService.find());
		return "/mcnpage/user/punch/overtime/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String>map, ModelMap model,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		workOvertimeService.getRow().clear();
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:list.do";
		workOvertimeService.update(row);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		workOvertimeService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
