package com.mcn.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.Bureau;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.MemberService;

@Controller("mcnMemberController")
@RequestMapping("/mcnpage/user/member")
public class MemberController extends BaseController{
	
	@Resource(name ="mcnMemberService")
	private MemberService memberService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureauService;
	
	/**
	 * 企业查询自己的用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/MemberList")
	public String getMoudleList(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/member/MemberList";
		}
		memberService.getRow().put("org_id", org_id);
		memberService.getRow().put("pageable", pageable);
		Page page = memberService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/member/MemberList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/member/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String NAME,String PASSWORD, 
			String USERNAME,String TELEPHONE,String SEX,String POSITION,
			String MANAGER_ID,String REMARK,String STATUS, String DEFAULT_MANAGER,
			String PHONE,String EMAIL,
			HttpSession session,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		Assert.notNull(NAME, "NAME can not be null");
		Assert.notNull(PASSWORD, "PASSWORD can not be null");
//		Assert.notNull(TELEPHONE, "TELEPHONE can not be null");
		if(StringUtils.isEmptyOrNull(STATUS))
		{
			STATUS ="";
		}
		if(StringUtils.isEmptyOrNull(PHONE))
		{
			PHONE ="";
		}
		if(StringUtils.isEmptyOrNull(EMAIL))
		{
			EMAIL ="";
		}
		memberService.getRow().put("DEPART_ID", DEPART_ID);
		memberService.getRow().put("NAME", NAME);
		memberService.getRow().put("PASSWORD", PASSWORD);
		if(USERNAME == null || USERNAME.replace(" ", "").length()==0){
			USERNAME ="";
		}
		memberService.getRow().put("USERNAME", USERNAME);//如果USERNAME为空，则系统自动生成
		if(!StringUtils.isEmptyOrNull(TELEPHONE))
		{
			memberService.getRow().put("TELEPHONE", TELEPHONE);
		}
		memberService.getRow().put("SEX", SEX);
		memberService.getRow().put("POSITION", POSITION);
		memberService.getRow().put("MANAGER_ID", MANAGER_ID);
		memberService.getRow().put("REMARK", REMARK);
		memberService.getRow().put("STATUS", STATUS);
		memberService.getRow().put("PHONE", PHONE);
		memberService.getRow().put("EMAIL", EMAIL);
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:MemberList.do";
		memberService.getRow().put("org_id", org_id);
		memberService.getRow().put("DEFAULT_MANAGER", DEFAULT_MANAGER);
		if(DEFAULT_MANAGER.equals("1"))
		{
			memberService.resetDefaultManager(DEPART_ID);
		}
		memberService.save();
		return "redirect:MemberList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, HttpSession session, ModelMap model) {
		memberService.getRow().put("id", id);
		model.addAttribute("row", memberService.find());
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/member/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID, String NAME,String PASSWORD, String USERNAME,
			String TELEPHONE,String PHONE,String EMAIL,String SEX,
			String POSITION,String MANAGER_ID,String REMARK,String STATUS, String DEFAULT_MANAGER,
			HttpSession session, ModelMap model) {
		memberService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		Assert.notNull(NAME, "NAME can not be null");
		Assert.notNull(PASSWORD, "PASSWORD can not be null");
//		Assert.notNull(TELEPHONE, "TELEPHONE can not be null");
		if(StringUtils.isEmptyOrNull(STATUS))
		{
			STATUS ="";
		}
		if(StringUtils.isEmptyOrNull(PHONE))
		{
			PHONE ="";
		}
		if(StringUtils.isEmptyOrNull(EMAIL))
		{
			EMAIL ="";
		}
		memberService.getRow().put("ID", ID);
		memberService.getRow().put("DEPART_ID", DEPART_ID);
		memberService.getRow().put("NAME", NAME);
		memberService.getRow().put("PASSWORD", PASSWORD);
		if(USERNAME == null || USERNAME.replace(" ", "").length()==0){
			USERNAME ="";
		}
		memberService.getRow().put("USERNAME", USERNAME);//如果USERNAME为空，则系统自动生成
		if(StringUtils.isEmptyOrNull(TELEPHONE))
		{
			TELEPHONE ="";
		}
		memberService.getRow().put("TELEPHONE", TELEPHONE);
		memberService.getRow().put("SEX", SEX);
		memberService.getRow().put("POSITION", POSITION);
		memberService.getRow().put("MANAGER_ID", MANAGER_ID);
		memberService.getRow().put("REMARK", REMARK);
		memberService.getRow().put("STATUS", STATUS);
		memberService.getRow().put("PHONE", PHONE);
		memberService.getRow().put("EMAIL", EMAIL);
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:MemberList.do";
		memberService.getRow().put("org_id", org_id);
		memberService.getRow().put("DEFAULT_MANAGER", DEFAULT_MANAGER);
		if(DEFAULT_MANAGER.equals("1"))
		{
			memberService.resetDefaultManager(DEPART_ID);
		}
		memberService.update();
		return "redirect:MemberList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		memberService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/changeUserName")
	public @ResponseBody
	ResponseVO changeUserName(String id,String old_name, String new_name) {
		ResponseVO ovo =null;
		ovo =memberService.changeUserName(id,old_name,new_name);
		return ovo;
	}
	
	@RequestMapping(value = "/queryUserBySiteno")
	public @ResponseBody
	ResponseVO queryUserBySiteno(String site_no) {
		ResponseVO ovo =new ResponseVO(0,"");
		DataSet ds=memberService.queryUsersBySiteNo(site_no);
		ovo.put("list", ds);
		return ovo;
	}
	
	@RequestMapping(value = "/checkCompanyUserIsOverNum")
	public @ResponseBody
	ResponseVO checkCompanyUserIsOverNum() {
		ResponseVO ovo =new ResponseVO(0,"");
		HttpSession session =getSession();
		Row staff =(Row)session.getAttribute("staff");
		if(staff == null)
		{
			ovo =new ResponseVO(-1,"请先登录");
			return ovo;
		}
		String org_id =staff.getString("bureau_no","");
		Row bureauRow =bureauService.find(org_id);
		int totalUserNum =Integer.parseInt(bureauRow.getString("user_sum","0"));
		//当前已启用的人数
		int curUsingTotalNum =memberService.queryOrgUserNumByStatus("1",org_id);
		if(curUsingTotalNum >= totalUserNum)
		{
			ovo =new ResponseVO(-1,"已达到企业购买的用户数上限，不能新加启用状态的用户","已达到企业购买的用户数上限，不能新加启用状态的用户");
			return ovo;
		}
		ovo =new ResponseVO(0,"");
		ovo.put("num", (totalUserNum -curUsingTotalNum));
		return ovo;
	}
	
}
