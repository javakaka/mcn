package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.mcn.service.CompanyUser;
import com.mcn.service.MessageReadLogService;
import com.mcn.service.MessageService;

/**
 * 手机端请假接口
 * @author JianBoTong
 *
 */
@Controller("mobileMessageController")
@RequestMapping("/api/message")
public class MessageController extends BaseController{
	
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	@Resource(name = "mcnMessageReadLogService")
	private MessageReadLogService messageReadLogService;
	
	@Resource(name = "mcnMessageService")
	private MessageService messageService;
	
	
	/**
	 * 用户查询自己能够看到的所有通知公告列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("org_message_list")
	public @ResponseBody String queryOrgMessageListByUserId(HttpServletRequest request) throws Exception
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String user_id =ivo.getString("user_id",null);
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1, "用户编号不能为空", "用户编号不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		Row userRow =companyUserService.findById(user_id);
		String user_depar_id =userRow.getString("depart_id","");
		if(StringUtils.isEmptyOrNull(user_depar_id))
		{
			ovo =new OVO(0, "", "");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		DataSet list = messageService.queryOrgMessageListByUserId(user_depar_id, token,Integer.parseInt(page),Integer.parseInt(page_size));
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("list",list);
		json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	/**
	 * 消息详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("org_message_detail")
	public @ResponseBody String queryMessageDetailByUserId(HttpServletRequest request) throws Exception
	{
		String json ="";
		parseRequest(request);
		String id =ivo.getString("id",null);
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1, "用户编号不能为空", "用户编号不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1, "消息编号不能为空", "消息编号不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		Row msgRow =messageService.queryMessageById(id);
		String content =msgRow.getString("content","");
		String attach =msgRow.getString("message_fj","");
		//保存阅读记录
		Row read_log_row =messageReadLogService.findByUserId(user_id);
		if(read_log_row == null)
		{
			read_log_row =new Row();
			read_log_row.put("user_id", user_id);
			messageReadLogService.save(read_log_row);
		}
		else
		{
			messageReadLogService.updateReadNum(user_id);
		}
		
		//更新消息总读数
		messageService.updateMessageTotalNum(id);
		ovo =new OVO(0, "请求成功", "请求成功");
		ovo.set("content",content);
		ovo.set("attach",attach);
		json =VOConvert.ovoToJson(ovo);
		return json;
	}
		
}
