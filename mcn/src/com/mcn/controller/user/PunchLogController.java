package com.mcn.controller.user;

import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.controller.Page;
import com.ezcloud.framework.controller.Pageable;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.PunchLogService;

@Controller("mcnPunchLogController")
@RequestMapping("/mcnpage/user/punch/log")
public class PunchLogController extends BaseController{
	
	@Resource(name ="mcnPunchLogService")
	private PunchLogService punchLogService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	/**
	 * 查询企业的打卡汇总记录
	 * @param pageable
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/companyLogList")
	public String companyLoglist(Pageable pageable, ModelMap model,String time) throws ParseException {
		System.out.println("timefffff================"+time);
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/CompanyLogList";
		}
		DataSet pageSet = null;
		String curTime =DateUtil.getCurrentDateTime();
		System.out.println("critime==="+curTime+"===="+curTime.substring(0,7));
		if(time == null){
			time=curTime.substring(0,7);
		}
		if(session.getAttribute("datatime")==null){
		session.setAttribute("datatime", time);
		}
		if(!session.getAttribute("datatime").equals(time)){
		session.removeAttribute("dataset");
		session.removeAttribute("datatime");
		session.setAttribute("datatime", time);
		}
		if(session.getAttribute("dataset")==null){
		pageSet = punchLogService.queryPageForCompanyDepart(org_id,time);
		session.setAttribute("dataset", pageSet);
		}else{
		pageSet = (DataSet) session.getAttribute("dataset");
		}
		model.addAttribute("page", pageSet);
		return "/mcnpage/user/punch/log/CompanyLogList";
	}
	
	/**
	 * 查询迟到记录
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chidaoLogList")
	public String selectMM(ModelMap model,String user_id,String type) throws ParseException{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/chidaoLogList";
		}
		String time = session.getAttribute("datatime").toString();
		DataSet dataSet = punchLogService.selectMM(org_id, user_id, type,time);
		model.addAttribute("page", dataSet);
		return "/mcnpage/user/punch/log/chidaoLogList";
	}
	/**
	 * 企业查询自己的打卡记录
	 * @param pageable
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/PunchLogList")
	public String list(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/PunchLogList";
		}
		punchLogService.getRow().put("org_id", org_id);
		punchLogService.getRow().put("pageable", pageable);
		Page page = punchLogService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/log/PunchLogList";
	}
	
	@RequestMapping(value = "/PunchLogList2")
	public String list2(Pageable pageable, ModelMap model,String user_id) {
		//System.out.println("通过"+user_id);
		HttpSession session = getSession();
		String time = session.getAttribute("datatime").toString();
		DataSet dataSet = punchLogService.queryPageForCompany2(user_id,time);
		model.addAttribute("page", dataSet);
		return "/mcnpage/user/punch/log/PunchLogList2";
	}
	
	@RequestMapping(value = "/PunchLogList3")
	public String list3(Pageable pageable, ModelMap model,String user_id,String leave_type) {
		System.out.println("通过"+user_id+"-----"+leave_type);
		/*
		DataSet dataSet = punchLogService.queryPageForCompany3(user_id,leave_type);
		model.addAttribute("page", dataSet);
	tttt	return "/mcnpage/user/punch/log/PunchLogList3";
	*/
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/PunchLogList3";
		}
		punchLogService.getRow().put("org_id", org_id);
		punchLogService.getRow().put("pageable", pageable);
		String time = session.getAttribute("datatime").toString();
		//System.out.println("time=========================="+time);
		DataSet page = punchLogService.queryPageForCompany3(user_id,leave_type,time);
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/log/PunchLogList3";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/log/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String AM_START,String AM_END, String PM_START,String PM_END,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchLogService.getRow().put("DEPART_ID", DEPART_ID);
		punchLogService.getRow().put("AM_START", AM_START);
		punchLogService.getRow().put("AM_END", AM_END);
		punchLogService.getRow().put("PM_START", PM_START);
		punchLogService.getRow().put("PM_END", PM_END);
		
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchLogList.do";
		punchLogService.getRow().put("org_id", org_id);
		punchLogService.save();
		return "redirect:PunchLogList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		punchLogService.getRow().put("id", id);
		model.addAttribute("row", punchLogService.find());
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/log/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID,String AM_START,String AM_END, String PM_START,String PM_END, ModelMap model) {
		punchLogService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchLogService.getRow().put("ID", ID);
		punchLogService.getRow().put("DEPART_ID", DEPART_ID);
		punchLogService.getRow().put("AM_START", AM_START);
		punchLogService.getRow().put("AM_END", AM_END);
		punchLogService.getRow().put("PM_START", PM_START);
		punchLogService.getRow().put("PM_END", PM_END);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchLogList.do";
		punchLogService.update();
		return "redirect:PunchLogList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		punchLogService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	

	/**
	 * 查询 签到记录
	 * @param pageable
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/personQList")
	public String personQLoglist(Pageable pageable, ModelMap model,String time,String searchValue) throws ParseException {
		System.out.println("timeff================"+time+"------------"+searchValue);
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/personQList";
		}
		DataSet pageSet = null;
		String curTime =DateUtil.getCurrentDateTime();
		System.out.println("critime==="+curTime+"===="+curTime.substring(0,7));
		if(time == null){
			time=curTime.substring(0,7);
		}
		if(session.getAttribute("datatime2")==null){
		session.setAttribute("datatime2", time);
		}
		if(!session.getAttribute("datatime2").equals(time)){
		session.removeAttribute("dataset2");
		session.removeAttribute("datatime2");
		session.setAttribute("datatime2", time);
		}
		if(session.getAttribute("dataset2")==null){
		pageSet = punchLogService.personQLoglist(org_id,time);
		session.setAttribute("dataset2", pageSet);
		}else{
		pageSet = (DataSet) session.getAttribute("dataset2");
		}
		if(searchValue == null){
		model.addAttribute("page", pageSet);
		}else{
		DataSet data = new DataSet();
		for(int i=0;i<pageSet.size();i++){
			Row row = new Row();
			row = (Row) pageSet.get(i);
			if(row.getString("user_name").equals(searchValue)){
				data.add(row);
			}
		}
		model.addAttribute("page", data);
		}
		return "/mcnpage/user/punch/log/personQList";
	}
	
	@RequestMapping("/updatestatus")
	public @ResponseBody String queryUserPunchLog(HttpServletRequest request,String punch_status,String id) throws JException
	{
		String json="1";
		System.out.println("punch_status==="+punch_status+"-------id===="+id);
		punchLogService.queryUserPunchLog(punch_status,id);
		return json;
	} 
}