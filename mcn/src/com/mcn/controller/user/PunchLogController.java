package com.mcn.controller.user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.ExcelUtil;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.LeaveService;
import com.mcn.service.MemberService;
import com.mcn.service.PunchLogService;

@Controller("mcnPunchLogController")
@RequestMapping("/mcnpage/user/punch/log")
public class PunchLogController extends BaseController{
	
	@Resource(name ="mcnPunchLogService")
	private PunchLogService punchLogService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name ="mcnMemberService")
	private MemberService memberService;
	
	@Resource(name ="mcnLeaveService")
	private LeaveService leaveService;
	/**
	 * 查询企业的打卡汇总记录
	 * @param pageable
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/companyLogList")
	public String companyLoglist(Pageable pageable, ModelMap model,String time) throws ParseException {
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
	public String list(String punch_type,String punch_result,String depart_id,String user_id,
			String start_date,String end_date,Pageable pageable, ModelMap model) {
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
		punchLogService.getRow().put("punch_type", punch_type);
		punchLogService.getRow().put("punch_result", punch_result);
		punchLogService.getRow().put("depart_id", depart_id);
		System.out.println("user_id---------->>"+user_id);
		punchLogService.getRow().put("user_id", user_id);
		punchLogService.getRow().put("start_date", start_date);
		punchLogService.getRow().put("end_date", end_date);
		Page page = punchLogService.queryPageForCompany();
		model.addAttribute("page", page);
		model.addAttribute("punch_type", punch_type);
		model.addAttribute("punch_result", punch_result);
		model.addAttribute("depart_id", depart_id);
		model.addAttribute("user_id", user_id);
		model.addAttribute("start_date", start_date);
		model.addAttribute("end_date", end_date);
		//site list 
		DataSet siteDs =systemSiteService.queryOrgSite(org_id);
		model.addAttribute("site_list", siteDs);
		//user list 
		DataSet userDs =null;
		if(!StringUtils.isEmptyOrNull(depart_id))
		{
			userDs =memberService.queryUsersBySiteNo(depart_id);
		}
		model.addAttribute("user_list", userDs);
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
	
	@RequestMapping(value = "/view")
	public String view(Long id, ModelMap model) {
		punchLogService.getRow().put("id", id);
		model.addAttribute("row", punchLogService.findDetailById());
		return "/mcnpage/user/punch/log/view";
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
		if(StringUtils.isEmptyOrNull(time))
		{
			time =DateUtil.getCurrentDateTime().substring(0,7);
		}
		session.setAttribute("datatime2", time);
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/personQList";
		}
		Page page =punchLogService.personQLoglist(pageable,org_id,time);
		model.addAttribute("page", page);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportCompanyLog")
	public  String exportCompanyLog(HttpServletRequest request,HttpServletResponse response,String year,String month) throws JException, IOException
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return null;
		}
		DataSet pageSet = null;
		String time =year+month;
		if(StringUtils.isEmptyOrNull(year) && StringUtils.isEmptyOrNull(month))
		{
			time =null;
		}
		if(!StringUtils.isEmptyOrNull(year))
		{
			time =year;
		}
		if(!StringUtils.isEmptyOrNull(month))
		{
			if(time.length() >0)
			{
				time +="-"+month;
			}
		}
		String curTime =DateUtil.getCurrentDateTime();
		if( time == null )
		{
			time=curTime.substring(0,7);
		}
		if( session.getAttribute("datatime") == null )
		{
			session.setAttribute("datatime", time);
		}
		if(!session.getAttribute("datatime").equals(time))
		{
			session.removeAttribute("dataset");
			session.removeAttribute("datatime");
			session.setAttribute("datatime", time);
		}
		if(session.getAttribute("dataset")==null)
		{
			try 
			{
				pageSet = punchLogService.queryPageForCompanyDepart(org_id,time);
			} 
			catch (ParseException e) 
			{
				pageSet =null;
				e.printStackTrace();
			}
			session.setAttribute("dataset", pageSet);
		}
		else
		{
			pageSet = (DataSet) session.getAttribute("dataset");
		}
		DataSet titleDs =new DataSet();
		titleDs.add("所属部门");
		titleDs.add("姓名");
		titleDs.add("打卡");
		titleDs.add("外出");
		titleDs.add("年假");
		titleDs.add("事假");
		titleDs.add("病假");
		titleDs.add("调休");
		titleDs.add("加班");
		titleDs.add("早退");
		titleDs.add("漏打卡");
		titleDs.add("迟到10分钟以内");
		titleDs.add("大于10小于30分钟");
		titleDs.add("超过30分钟");
		DataSet keyDs =new DataSet();
		keyDs.add("DEPART_NAME");
		keyDs.add("MANAGER_NAME");
		keyDs.add("ALL_DAY");
		keyDs.add("WAI_DAY");
		keyDs.add("YEAR_DAY");
		keyDs.add("SHI_DAY");
		keyDs.add("SICK_DAY");
		keyDs.add("TIAO_DAY");
		keyDs.add("ADD_DAY");
		keyDs.add("LEAVE_EARLY");
		keyDs.add("LOST_PUNCH");
		keyDs.add("XSHI");
		keyDs.add("DSHI");
		keyDs.add("CSHI");
		String basePath =request.getSession().getServletContext().getRealPath("/resources");
		basePath +="/export_excel"+"/"+org_id;
		String fileName =time+"企业考勤汇总表.xls";
		String out_path=basePath;
		String file_path=basePath+"/"+fileName;
		System.out.println("out_path========>>>"+out_path);
		System.out.println("file_path========>>>"+file_path);
    	String sheetName="企业考勤汇总表";
    	ExcelUtil.writeExcel(titleDs, keyDs, pageSet, out_path,fileName,sheetName,0);
		InputStream is = new FileInputStream(file_path);
		response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
		return null;
	} 
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportUserCheckinLog")
	public  String exportUserCheckinLog(HttpServletRequest request,HttpServletResponse response,String year,String month) throws JException, IOException
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return null;
		}
		DataSet pageSet = null;
		String time =year+month;
		if(StringUtils.isEmptyOrNull(year) && StringUtils.isEmptyOrNull(month))
		{
			time =null;
		}
		if(!StringUtils.isEmptyOrNull(year))
		{
			time =year;
		}
		if(!StringUtils.isEmptyOrNull(month))
		{
			if(time.length() >0)
			{
				time +="-"+month;
			}
		}
		String curTime =DateUtil.getCurrentDateTime();
		if( time == null )
		{
			time=curTime.substring(0,7);
		}
		pageSet = punchLogService.queryUserCheckinLog(org_id,time);
		DataSet titleDs =new DataSet();
		titleDs.add("所属部门");
		titleDs.add("用户姓名");
		titleDs.add("签到时间");
		titleDs.add("签到地址");
//		titleDs.add("用户图像");
		DataSet keyDs =new DataSet();
		keyDs.add("SITE_NAME");
		keyDs.add("USER_NAME");
		keyDs.add("PUNCH_TIME");
		keyDs.add("PlACE_NAME");
		String basePath =request.getSession().getServletContext().getRealPath("/resources");
		basePath +="/export_excel"+"/"+org_id;
		String fileName =time+"用户签到表.xls";
		String out_path=basePath;
		String file_path=basePath+"/"+fileName;
		System.out.println("out_path========>>>"+out_path);
		System.out.println("file_path========>>>"+file_path);
		String sheetName="用户签到表";
		ExcelUtil.writeExcel(titleDs, keyDs, pageSet, out_path,fileName,sheetName,0);
		InputStream is = new FileInputStream(file_path);
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return null;
	} 
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportUserPunchLog")
	public  String exportUserPunchLog(HttpServletRequest request,HttpServletResponse response,
			String start_date,String end_date,String depart_id,String user_id) throws JException, IOException
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return null;
		}
		DataSet pageSet = null;
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			start_date =start_date.replaceAll(" ", "");
			start_date +=" 00:00:00";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			end_date =end_date.replaceAll(" ", "");
			end_date +=" 00:00:00";
		}
		String real_path =request.getRealPath("resources");
		int iPos =real_path.indexOf("resources");
		String base_real_path =real_path;
		if(iPos != -1)
		{
			base_real_path =real_path.substring(0,iPos);
		}
		
		pageSet = punchLogService.queryUserPunchLog(org_id,depart_id,user_id,start_date,end_date,base_real_path);
		DataSet titleDs =new DataSet();
		titleDs.add("所属部门");
		titleDs.add("用户姓名");
		titleDs.add("打卡类型");
		titleDs.add("打卡时间");
		titleDs.add("打卡结果");
		titleDs.add("打卡地址");
		titleDs.add("位置有效性");
		titleDs.add("用户头像");
		DataSet keyDs =new DataSet();
		keyDs.add("SITE_NAME");
		keyDs.add("NAME");
		keyDs.add("PUNCH_TYPE_NAME");
		keyDs.add("PUNCH_TIME");
		keyDs.add("PUNCH_RESULT");
		keyDs.add("PlACE_NAME");
		keyDs.add("MAP_VALID");
		keyDs.add("IMG_PATH");
		String basePath =request.getSession().getServletContext().getRealPath("/resources");
		basePath +="/export_excel"+"/"+org_id;
		String fileName ="用户打卡表.xls";
		String out_path=basePath;
		String file_path=basePath+"/"+fileName;
		String sheetName="用户打卡表";
		ExcelUtil.writeExcel(titleDs, keyDs, pageSet, out_path,fileName,sheetName,1400);
		//替换图片
		ExcelUtil.replaceForColumnFromImgPathToImage(7, 1, file_path);
		InputStream is = new FileInputStream(file_path);
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		
		return null;
	} 
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportUserLeaveLog")
	public  String exportUserLeaveLog(HttpServletRequest request,HttpServletResponse response,
			String start_date,String end_date) throws JException, IOException
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return null;
		}
		DataSet pageSet = null;
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			start_date =start_date.replaceAll(" ", "");
			start_date +=" 00:00:00";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			end_date =end_date.replaceAll(" ", "");
			end_date +=" 00:00:00";
		}
		pageSet = leaveService.queryOrgLeaveList(org_id,start_date,end_date);
		DataSet titleDs =new DataSet();
//		titleDs.add("所属部门");
		titleDs.add("用户姓名");
		titleDs.add("请假天数");
		titleDs.add("开始时间");
		titleDs.add("结束时间");
		titleDs.add("假期类型");
		titleDs.add("请假原因");
		titleDs.add("流程过程");
		titleDs.add("审批状态");
		titleDs.add("请假提交时间");
		titleDs.add("最后审批时间");
		DataSet keyDs =new DataSet();
//		keyDs.add("SITE_NAME");
		keyDs.add("NAME");
		keyDs.add("SUM_DAY");
		keyDs.add("START_DATE");
		keyDs.add("END_DATE");
		keyDs.add("LEAVE_TYPE_NAME");
		keyDs.add("REASON");
		keyDs.add("AUDIT_NAME");
		keyDs.add("STATUS_NAME");
		keyDs.add("CREATE_TIME");
		keyDs.add("MODIFY_TIME");
		String basePath =request.getSession().getServletContext().getRealPath("/resources");
		basePath +="/export_excel"+"/"+org_id;
		String fileName ="用户请假表.xls";
		String out_path=basePath;
		String file_path=basePath+"/"+fileName;
		System.out.println("out_path========>>>"+out_path);
		System.out.println("file_path========>>>"+file_path);
		String sheetName="用户请假表";
		ExcelUtil.writeExcel(titleDs, keyDs, pageSet, out_path,fileName,sheetName,0);
		InputStream is = new FileInputStream(file_path);
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return null;
	} 
}