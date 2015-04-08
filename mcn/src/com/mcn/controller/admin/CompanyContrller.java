package com.mcn.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

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
import com.ezcloud.framework.service.system.Bureau;
import com.ezcloud.framework.service.system.ProjectParameter;
import com.ezcloud.framework.service.system.Staff;
import com.ezcloud.framework.service.system.StaffRole;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.CompanyModule;
import com.mcn.service.CompanySite;
import com.mcn.service.CompanyUser;

@Controller("adminCompanyController")
@RequestMapping("/mcnpage/platform/company")
public class CompanyContrller  extends BaseController{

	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureau;
	@Resource(name = "adminCompanyModuleService")
	private CompanyModule companyModuleService;
	
	@Resource(name = "companySiteService")
	private CompanySite companySiteService;
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;	

	@Resource(name = "frameworkStaffService")
	private Staff staffService;
	
	@Resource(name = "frameworkStaffRoleService")
	private StaffRole staffRoleService;
	
	@Resource(name = "frameworkProjectParameterService")
	private ProjectParameter projectParameterService;
	
	/**
	 * 企业查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/CompanyList")
	public String list(Pageable pageable, ModelMap model) {
		bureau.getRow().put("pageable", pageable);
		Page page = bureau.queryPage();
		model.addAttribute("page", page);
		return "/mcnpage/platform/company/CompanyList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/mcnpage/platform/company/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	//public String save(String BUREAU_NAME, String UP_BUREAU_NO, String AREA_CODE, String LINKS,String NOTES ,ServletRequest request,RedirectAttributes redirectAttributes) {
	public String save(String BUREAU_NAME, String UP_BUREAU_NO, MultipartFile fileElem, String LINKS,
			String NOTES ,String BEGIN_DATE,String END_DATE,String USER_SUM,String STATUS,ServletRequest request,RedirectAttributes redirectAttributes) {
		String AREA_CODE = "";
		System.out.println("fileElemFileName=="+fileElem.getOriginalFilename());
		if(!(fileElem.getOriginalFilename() ==null || "".equals(fileElem.getOriginalFilename()))) {   
			  
			 try {
				 String PathDir = request.getServletContext().getRealPath("resources/logo");
				 AREA_CODE = SaveFileFromInputStream(fileElem.getInputStream(),PathDir,fileElem.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
              
        }
		bureau.getRow().clear();
		bureau.getRow().put("BUREAU_NAME", BUREAU_NAME);
		bureau.getRow().put("UP_BUREAU_NO", UP_BUREAU_NO);
		bureau.getRow().put("AREA_CODE", AREA_CODE);
		bureau.getRow().put("LINKS", LINKS);
		if(BEGIN_DATE !=null && BEGIN_DATE.replace(" ", "").length() >0)
		{
			bureau.getRow().put("BEGIN_DATE", BEGIN_DATE);
		}
		if(END_DATE !=null && END_DATE.replace(" ", "").length() >0)
		{
			bureau.getRow().put("END_DATE", END_DATE);
		}
		if(USER_SUM !=null && USER_SUM.replace(" ", "").length() >0)
		{
			bureau.getRow().put("USER_SUM", USER_SUM);
		}
		else
		{
			bureau.getRow().put("USER_SUM", "0");
		}
		if(STATUS !=null && STATUS.replace(" ", "").length() >0)
		{
			bureau.getRow().put("STATUS", STATUS);
		}
		//NOTES 字段为企业登陆 url,项目id为1
		NOTES =projectParameterService.queryParamValue("1", "USER_LOGIN_URL");
		if(NOTES == null)
			NOTES ="";
		bureau.getRow().put("NOTES", NOTES);
		try {
			bureau.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] checkboxArr = request.getParameterValues("modules");
        if( checkboxArr!=null ){
        	Row mrow =new Row();
        	String checkedValue="1";
        	mrow.put("org_id", bureau.getRow().getString("BUREAU_NO"));
            for(int i=0;i<checkboxArr.length;i++){
                System.out.println("======"+ checkboxArr[i] );
                mrow.put(checkboxArr[i] , checkedValue);
            }
            companyModuleService.save(mrow);
        }
        //创建企业管理员帐户
        Row staff =new Row();
        staff.put("staff_name", "admin");
        staff.put("password", Md5Util.Md5("123456"));
        staff.put("bureau_no", bureau.getRow().getString("BUREAU_NO"));
        staffService.setRow(staff);
        staffService.save();
        String staff_no  =staffService.getRow().getString("staff_no",null);
        //给帐户赋权限(企业的超级管理员，无岗位人员)
        Row staffRole =new Row();
        staffRole.put("staff_no",staff_no );
        staffRole.put("role_id","10003" );//默认是企业管理员角色
        staffRole.put("use_state","1" );
        staffRole.put("assign_state", "1");
        staffRoleService.setRow(staffRole);
        staffRoleService.save();
		return "redirect:CompanyList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		bureau.getRow().put("id", id);
		model.addAttribute("bureau", bureau.find());
		//查找功能选项
		Row module = companyModuleService.edit(String.valueOf(id));
		model.addAttribute("module", module);
		return "/mcnpage/platform/company/edit";
	}

	@RequestMapping(value = "/update")
	//public String update(String BUREAU_NO,String BUREAU_NAME, String UP_BUREAU_NO, String AREA_CODE, String LINKS,String NOTES ,  ServletRequest request,ModelMap model) {
	public String update(String BUREAU_NO,String BUREAU_NAME, String UP_BUREAU_NO,String AREA_CODE,
			String BEGIN_DATE,String END_DATE,String USER_SUM,String STATUS,MultipartFile fileElem, String LINKS,String NOTES ,  ServletRequest request,ModelMap model) {
		System.out.println("fileElemFileName=="+fileElem.getOriginalFilename());
		if(!(fileElem.getOriginalFilename() ==null || "".equals(fileElem.getOriginalFilename()))) { 
			 try {
				 String PathDir = request.getServletContext().getRealPath("resources/logo");
				 AREA_CODE = SaveFileFromInputStream(fileElem.getInputStream(),PathDir,fileElem.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
             
       }
		bureau.getRow().clear();
		bureau.getRow().put("BUREAU_NO", BUREAU_NO);
		bureau.getRow().put("BUREAU_NAME", BUREAU_NAME);
		bureau.getRow().put("UP_BUREAU_NO", UP_BUREAU_NO);
		bureau.getRow().put("AREA_CODE", AREA_CODE);
		bureau.getRow().put("LINKS", LINKS);
		bureau.getRow().put("NOTES", NOTES);
		if(BEGIN_DATE !=null && BEGIN_DATE.replace(" ", "").length() >0)
		{
			bureau.getRow().put("BEGIN_DATE", BEGIN_DATE);
		}
		if(END_DATE !=null && END_DATE.replace(" ", "").length() >0)
		{
			bureau.getRow().put("END_DATE", END_DATE);
		}
		if(USER_SUM !=null && USER_SUM.replace(" ", "").length() >0)
		{
			bureau.getRow().put("USER_SUM", USER_SUM);
		}
		else
		{
			bureau.getRow().put("USER_SUM", "0");
		}
		if(STATUS !=null && STATUS.replace(" ", "").length() >0)
		{
			bureau.getRow().put("STATUS", STATUS);
		}
		bureau.update();
		
		String[] checkboxArr = request.getParameterValues("modules");
        if( checkboxArr!=null ){
        	Row mrow =new Row();
        	String checkedValue="1";
            for(int i=0;i<checkboxArr.length;i++){
                System.out.println("======"+ checkboxArr[i] );
                mrow.put(checkboxArr[i] , checkedValue);
            }
            companyModuleService.update(mrow,BUREAU_NO);
        }
		return "redirect:CompanyList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		bureau.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 增量导入，每次解析excel文件，不会删除之前导入的部门以及人员
	 * 用户需要保证不同的excel文件中，没有重复的部门以及人员
	 * 避免重复导入部门以及人员
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/parseExcel")
	public @ResponseBody
	Message parseExcel(String id) {
		System.out.println("通过Excel表"+id);
		Assert.notNull(id, "id can not be null");
//		companySiteService.deteleSite(id);
//		companyUserService.delete(id);
		companySiteService.parseExcel(id);
		return SUCCESS_MESSAGE;
	}
	
	//图片上传
	public String SaveFileFromInputStream(InputStream stream,String path,String fileElemFileName) throws IOException{  
		String imgurl="resources/logo/";
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
