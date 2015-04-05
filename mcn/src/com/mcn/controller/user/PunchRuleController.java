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
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
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

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
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
		model.addAttribute("row", punchRuleService.find());
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID,String AM_START,String AM_END, String PM_START,String PM_END, ModelMap model) {
		punchRuleService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchRuleService.getRow().put("ID", ID);
		punchRuleService.getRow().put("DEPART_ID", DEPART_ID);
		punchRuleService.getRow().put("AM_START", AM_START);
		punchRuleService.getRow().put("AM_END", AM_END);
		punchRuleService.getRow().put("PM_START", PM_START);
		punchRuleService.getRow().put("PM_END", PM_END);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchRuleList.do";
		punchRuleService.update();
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
	@RequestMapping(value = "/message")
	public String messageList(ModelMap model) {
		System.out.println("通过");
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		DataSet data = punchRuleService.messageList(org_id);
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
		model.addAttribute("row", row);
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
