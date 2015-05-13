package com.mcn.controller.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.MessageReadLogService;
import com.mcn.service.PunchRuleBakService;
import com.mcn.service.PunchRuleHistoryService;
import com.mcn.service.PunchRuleService;
import com.mcn.service.PunchSettingService;

@Controller("mcnPunchRuleController")
@RequestMapping("/mcnpage/user/punch/rule")
public class PunchRuleController extends BaseController{
	
	@Resource(name ="mcnPunchRuleService")
	private PunchRuleService punchRuleService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name ="mcnPunchSettingService")
	private PunchSettingService punchSettingService;
	
	@Resource(name ="mcnMessageReadLogService")
	private MessageReadLogService messageReadLogService;
	
	@Resource(name ="mcnPunchRuleBakService")
	private PunchRuleBakService punchRuleBakService;
	
	@Resource(name ="mcnPunchRuleHistoryService")
	private PunchRuleHistoryService punchRuleHistoryService;
	
	
	/**
	 * 企业查询自己的打卡规则
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/PunchRuleList")
	public String list(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/rule/PunchRuleList";
		}
		punchRuleService.getRow().put("org_id", org_id);
		punchRuleService.getRow().put("pageable", pageable);
		Page page = punchRuleService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/rule/PunchRuleList";
	}
	/**
	 * 企业查询自己的打卡规则,历史记录
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/PunchRuleHistoryList")
	public String historyList(String d_id,Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/rule/PunchRuleHistoryList";
		}
		if(d_id == null){
			return "/mcnpage/user/punch/rule/PunchRuleHistoryList";
		}
		punchRuleHistoryService.getRow().put("org_id", org_id);
		punchRuleHistoryService.getRow().put("depart_id", d_id);
		punchRuleHistoryService.getRow().put("pageable", pageable);
		Page page = punchRuleHistoryService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/rule/PunchRuleHistoryList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSiteWhereNotRuled(org_id));
		return "/mcnpage/user/punch/rule/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String AM_START,String AM_END, String PM_START,String PM_END,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchRuleService.getRow().put("DEPART_ID", DEPART_ID);
		punchRuleService.getRow().put("AM_START", AM_START);
		punchRuleService.getRow().put("AM_END", AM_END);
		punchRuleService.getRow().put("PM_START", PM_START);
		punchRuleService.getRow().put("PM_END", PM_END);
		
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchRuleList.do";
		punchRuleService.getRow().put("org_id", org_id);
		punchRuleService.save();
		return "redirect:PunchRuleList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		
		punchRuleService.getRow().put("id", id);
		Row row =punchRuleService.find();
		//下次打卡时间设置
		String depart_id =row.getString("depart_id","");
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0,4);
		String month =cur_date.substring(5,7);
		String day =cur_date.substring(8,10);
		String bak_date =cur_date.substring(0,10);
		Row row2 =punchRuleBakService.findByDepartId(depart_id);
		model.addAttribute("row", row);
		String time1 =row.getString("am_start","");
		String time2 =row.getString("am_end","");
		String time3 =row.getString("pm_start","");
		String time4 =row.getString("pm_end","");
		if(row2 != null)
		{
			 time1 =row2.getString("am_start","");
			 time2 =row2.getString("am_end","");
			 time3 =row2.getString("pm_start","");
			 time4 =row2.getString("pm_end","");
			
		}
		int num =0;
		if(!StringUtils.isEmptyOrNull(time1)){
			num ++;
		}
		if(!StringUtils.isEmptyOrNull(time2)){
			num ++;
		}
		if(!StringUtils.isEmptyOrNull(time3)){
			num ++;
		}
		if(!StringUtils.isEmptyOrNull(time4)){
			num ++;
		}
		if(num >=2){
			num =4;
		}
		else{
			num=2;
		}
		model.addAttribute("punch_num", num);
		model.addAttribute("row2", row2);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID,String AM_START,String AM_END, 
			String PM_START,String PM_END,String PUNCH_NUM, ModelMap model,RedirectAttributes redirectAttributes) {
		punchRuleService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		int num =0;
		if(AM_END.equals("-1"))
		{
			AM_END ="";
		}
		else
		{
			num ++;
		}
		if(PM_START.equals("-1"))
		{
			PM_START ="";
		}
		else
		{
			num ++;
		}
		if(!StringUtils.isEmptyOrNull(AM_START))
		{
			num++;
		}
		if(!StringUtils.isEmptyOrNull(PM_END))
		{
			num++;
		}
		if(num>2)
		{
			num =4;
		}
		else
			num =2;
//		punchRuleService.getRow().put("ID", ID);
//		punchRuleService.getRow().put("DEPART_ID", DEPART_ID);
//		punchRuleService.getRow().put("AM_START", AM_START);
//		punchRuleService.getRow().put("AM_END", AM_END);
//		punchRuleService.getRow().put("PM_START", PM_START);
//		punchRuleService.getRow().put("PM_END", PM_END);
//		punchRuleService.getRow().put("PUNCH_NUM", PUNCH_NUM);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchRuleList.do";
//		punchRuleService.update();
		//是否需要切换打卡次数
		punchRuleService.getRow().put("ID", ID);
		Row row=punchRuleService.find();
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0,4);
		String month =cur_date.substring(5,7);
		String day =cur_date.substring(8,10);
		String bak_date =cur_date.substring(0,10);
		Row curBakRow =punchRuleBakService.findByDepartId(DEPART_ID, year, month);
		if(curBakRow == null)
		{
			//insert
			row.put("bak_year", year);
			row.put("bak_month", month);
			row.put("bak_day", day);
			row.put("bak_date", bak_date);
			row.put("am_start", AM_START);
			row.put("am_end", AM_END);
			row.put("pm_start", PM_START);
			row.put("pm_end", PM_END);
			row.put("is_deal", 0);
			row.put("punch_num", num);
			punchRuleBakService.save(row);
		}
		else
		{
			//update 
			curBakRow.put("bak_year", year);
			curBakRow.put("bak_month", month);
			curBakRow.put("bak_day", day);
			curBakRow.put("bak_date", bak_date);
			curBakRow.put("am_start", AM_START);
			curBakRow.put("am_end", AM_END);
			curBakRow.put("pm_start", PM_START);
			curBakRow.put("pm_end", PM_END);
			curBakRow.put("is_deal", 0);
			row.put("punch_num", num);
			punchRuleBakService.update(curBakRow);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:PunchRuleList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		punchRuleService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	//假期设置
	@RequestMapping(value = "/setting")
	public String setting(ModelMap model) {
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		punchSettingService.getRow().put("org_id", org_id);
		model.addAttribute("row", punchSettingService.findOrgSetting());
		return "/mcnpage/user/punch/rule/setting";
	}
	
	//保存假期设置
	@RequestMapping(value = "/saveSetting")
	public String saveSetting(String id,String year,String sick,String exchange,String outing,String personal,String work,String punchsz,ModelMap model) {
		Assert.notNull(id);
		Assert.notNull(year);
		Assert.notNull(sick);
		Assert.notNull(exchange);
		Assert.notNull(outing);
		punchSettingService.getRow().put("id",id);
		punchSettingService.getRow().put("year",year);
		punchSettingService.getRow().put("sick",sick);
		punchSettingService.getRow().put("exchange",exchange);
		punchSettingService.getRow().put("personal",outing);
		punchSettingService.getRow().put("outing",personal);
		punchSettingService.getRow().put("work",work);
		punchSettingService.getRow().put("punchsz",punchsz);
		punchSettingService.update();
		return "redirect:setting.do";
	}
	
	//通知公告列表
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/message")
	public String messageList(ModelMap model) {
		System.out.println("通过");
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		DataSet data = punchRuleService.messageList(org_id);
		String org_names ="";
		if(data != null)
		{
			for(int i=0;i<data.size();i++)
			{
				Row temp =(Row)data.get(i);
				String org_ids=temp.getString("message_qz","");
				if(! StringUtils.isEmptyOrNull(org_ids))
				{
					org_names =systemSiteService.queryOrgSiteNameByIds(org_ids);
					temp.put("message_qz", org_names);
					data.set(i, temp);
				}
			}
		}
		model.addAttribute("page", data);
		return "/mcnpage/user/punch/rule/messageList";
	}
	//通知公告修改
	@RequestMapping(value = "/messageedit")
	public String messageEdit(ModelMap model,String create_time,String id,String fj,String message_name,MultipartFile fileElem,String message_content,String message_qz,ServletRequest request,RedirectAttributes redirectAttributes,HttpSession session) {
		String message_fj = "";
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		System.out.println("fileElemFileName=="+fileElem.getOriginalFilename());
		if(!(fileElem.getOriginalFilename() ==null || "".equals(fileElem.getOriginalFilename()))) {   
			 try {
				 String PathDir = request.getServletContext().getRealPath("resources/message");
				 message_fj = SaveFileFromInputStream(fileElem.getInputStream(),PathDir,fileElem.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
		System.out.println("通过3=="+message_fj);
		Row row =new Row();
		row.put("id", id);
		row.put("org_id", org_id);
		row.put("message_name", message_name);
		row.put("message_content", message_content);
		if(message_fj == ""){
		row.put("message_fj", fj);
		}else{
		row.put("message_fj", message_fj);
		}
		row.put("message_qz", message_qz);
		row.put("create_time", create_time);
		row.put("update_time", DateUtil.getCurrentDate());
		System.out.println("row========"+row);
		punchRuleService.messageEdit(row);
		return "redirect:message.do";
	}
	
	//通知公告查看
	@RequestMapping(value = "/messageedit2")
	public String messageEdit2(ModelMap model,HttpSession session,String id) {
		System.out.println("通过");
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		Row row = punchRuleService.messageView(id);
		String message_qz =row.getString("message_qz","");
		String arr[]=message_qz.split(",");
		DataSet existedDs =new DataSet();
		for(int i=0;i<arr.length;i++)
		{
			Row temp =new Row();
			temp.put("id", arr[i]);
			existedDs.add(temp);
		}
		model.addAttribute("row", row);
		model.addAttribute("message_depart_id_list", existedDs);
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/messageedit";
	}
	
	//通知公告查看
	@RequestMapping(value = "/messageview")
	public String messageView(ModelMap model,String id) {
		System.out.println("通过");
		Row row = punchRuleService.messageView(id);
		model.addAttribute("row", row);
		return "/mcnpage/user/punch/rule/messageview";
	}
	//通知公告已读列表
	@RequestMapping(value = "/messageview_list")
	public String messageViewList(String id,Pageable pageable,ModelMap model,RedirectAttributes redirectAttributes,HttpSession session) {
		System.out.println("通过");
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		messageReadLogService.getRow().put("org_id", org_id);
		messageReadLogService.getRow().put("m_id", id);
		Page page =messageReadLogService.queryPageForCompany(pageable);
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/rule/messageviewList";
	}
	//通知公告添加
	@RequestMapping(value = "/messageadd")
	public String messageAdd(ModelMap model,String message_name,MultipartFile fileElem,String message_content,String message_qz,ServletRequest request,RedirectAttributes redirectAttributes,HttpSession session) {
		String message_fj = "";
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		System.out.println("fileElemFileName=="+fileElem.getOriginalFilename());
		if(!(fileElem.getOriginalFilename() ==null || "".equals(fileElem.getOriginalFilename()))) {   
			 try {
				 String PathDir = request.getServletContext().getRealPath("resources/message");
				 message_fj = SaveFileFromInputStream(fileElem.getInputStream(),PathDir,fileElem.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
		System.out.println("通过3=="+message_fj);
		Row row =new Row();
		row.put("org_id", org_id);
		row.put("message_name", message_name);
		row.put("message_content", message_content);
		row.put("message_fj", message_fj);
		row.put("message_qz", message_qz);
		punchRuleService.messageAdd(row);
		return "redirect:message.do";
	}
	
	//通知公告删除
	@RequestMapping(value = "/messagedelete")
	public @ResponseBody
	Message messageDelete(Long[] ids) {
		punchRuleService.messageDelete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/add2")
	public String add2(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/messageadd";
	}
	
	//图片上传
	public String SaveFileFromInputStream(InputStream stream,String path,String fileElemFileName) throws IOException{  
		String imgurl="resources/message/";
		String newFileName=null;
		long time =new Date().getTime(); 
		int index=fileElemFileName.lastIndexOf(".");
        if (index ==-1) {
                newFileName=fileElemFileName+"-2-"+ time;        //文件名
        } else {
                newFileName =fileElemFileName.substring(0,index)+"-3-"+time+fileElemFileName.substring(index);
        }
        File dir= new File(path);
		 if(!dir.exists()){
            dir.mkdir();
		 }
		 imgurl += newFileName;
        FileOutputStream fs=new FileOutputStream( path + "/"+ newFileName);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0; 
        while ((byteread=stream.read(buffer))!=-1)
        {
           bytesum+=byteread;
           fs.write(buffer,0,byteread);
           fs.flush();
        } 
        fs.close();
        stream.close();  
        return imgurl;
    }   
}
