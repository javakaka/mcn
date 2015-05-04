package com.mcn.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.PunchRuleService;

@Controller("mcnCompanyDepartController")
@RequestMapping("/mcnpage/user/depart")
public class CompanyDepartController extends BaseController{

	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name ="mcnPunchRuleService")
	private PunchRuleService punchRuleService;
	
	/**
	 * 企业查询自己的用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/DepartList")
	public String getMoudleList(Pageable pageable, ModelMap model) {
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		systemSiteService.getRow().put("org_id", org_id);
		systemSiteService.getRow().put("pageable", pageable);
		Page page = systemSiteService.queryPage();
		model.addAttribute("page", page);
		return "/mcnpage/user/depart/DepartList";
	}
	
	@RequestMapping(value = "/add")
	public String add(  ModelMap model )
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		model.addAttribute("departs", systemSiteService.queryOrgSite(org_id));
		//默认打卡时间
		Row punch_time_row =null;
		if(org_id != null)
		{
			punch_time_row =punchRuleService.queryOneDepartPunchTime(org_id);
		}
		String am_start =punch_time_row.getString("am_start","");
		String am_end =punch_time_row.getString("am_end","");
		String pm_start =punch_time_row.getString("pm_start","");
		String pm_end =punch_time_row.getString("pm_end","");
		model.addAttribute("am_start", am_start);
		model.addAttribute("am_end", am_end);
		model.addAttribute("pm_start", pm_start);
		model.addAttribute("pm_end", pm_end);
		return  "/mcnpage/user/depart/add";
	}
	
	@RequestMapping(value = "/save")
	public String save(String SITE_NAME,String UP_SITE_NO,String STATE,
			String AM_START,String AM_END,
			String PM_START,String PM_END, ModelMap model )
	{
		Assert.notNull(AM_START,"AM_START can not be null" );
		Assert.notNull(AM_END,"AM_END can not be null");
		Assert.notNull(PM_START,"PM_START can not be null");
		Assert.notNull(PM_END, "PM_END can not be null");
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/add";
		}
		Assert.notNull(SITE_NAME, "SITE_NAME can not be null");
		Row siteRow =new Row();
		siteRow.put("SITE_NAME", SITE_NAME);
		siteRow.put("BUREAU_NO", org_id);
		siteRow.put("STATE", STATE);
		if(UP_SITE_NO!= null && UP_SITE_NO.replace(" ", "").length() >0)
		{
			siteRow.put("UP_SITE_NO", UP_SITE_NO);
		}
		systemSiteService.insertOrgSite(siteRow);
		String site_no =siteRow.getString("site_no","");
		Row punch_time_row =new Row();
		punch_time_row.put("org_id", org_id);
		punch_time_row.put("depart_id", site_no);
		punch_time_row.put("AM_START", AM_START);
		punch_time_row.put("AM_END", AM_END);
		punch_time_row.put("PM_START", PM_START);
		punch_time_row.put("PM_END", PM_END);
		punchRuleService.insert(punch_time_row);
		return "redirect:DepartList.do";
	}
	
	@RequestMapping(value = "/edit")
	public String find(String id,  ModelMap model )
	{
		Assert.notNull(id, "id can not be null");
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		systemSiteService.getRow().put("id", id);
		model.addAttribute("departs", systemSiteService.queryOrgSite(org_id));
		model.addAttribute("row", systemSiteService.find());
		return  "/mcnpage/user/depart/edit";
	}
	
	@RequestMapping(value = "/update")
	public String update(String SITE_NO,String SITE_NAME,String UP_SITE_NO,String STATE,  ModelMap model )
	{
		Assert.notNull(SITE_NO, "SITE_NO can not be null");
		Assert.notNull(SITE_NAME, "SITE_NAME can not be null");
		Row siteRow =new Row();
		siteRow.put("SITE_NO", SITE_NO);
		siteRow.put("SITE_NAME", SITE_NAME);
		siteRow.put("STATE", STATE);
		if(UP_SITE_NO!= null && UP_SITE_NO.replace(" ", "").length() >0)
		{
			siteRow.put("UP_SITE_NO", UP_SITE_NO);
		}
		
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
			siteRow.put("BUREAU_NO", org_id);
		}
		else{
			return "redirect:DepartList.do";
		}
		systemSiteService.saveOrgSite(siteRow);
		// 停用,停用子部门以及子部门的人员
		if(STATE.equals("0"))
		{
			systemSiteService.stopChildrenOrgSite(SITE_NO);
		}
		// 启用，判断上级部门是否启用，并启用本部门的人员
		else if(STATE.equals("1"))
		{
			
		}
		return "redirect:DepartList.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		systemSiteService.deleteOrgSite(ids);
		return SUCCESS_MESSAGE;
	}
	@RequestMapping(value = "/checkUpSiteState")
	public @ResponseBody
	ResponseVO checkUpSiteState(String site_no) {
		ResponseVO ovo =new ResponseVO(0,"");
		String up_id =systemSiteService.isHaveUpSite(site_no);
		if(StringUtils.isEmptyOrNull(up_id))
		{
			ovo.put("state", "1");
		}
		else
		{
			Row up_row =systemSiteService.find(up_id);
			String state =up_row.getString("state","");
			if(StringUtils.isEmptyOrNull(state))
			{
				state ="0";
			}
			ovo.put("state", state);
		}
		return ovo;
	}
}
