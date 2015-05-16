package com.mcn.controller.user;

import java.text.DecimalFormat;
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
import com.ezcloud.framework.util.GeographyUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.PunchZoneService;

@Controller("mcnPunchZoneController")
@RequestMapping("/mcnpage/user/punch/zone")
public class PunchZoneController extends BaseController{
	
	@Resource(name ="mcnPunchZoneService")
	private PunchZoneService punchZoneService;
	
	/**
	 * 企业查询自己的打卡规则
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) 
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null)
		{
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null)
		{
			return "/mcnpage/user/punch/zone/list";
		}
		Page page = punchZoneService.queryPageForCompany(pageable,org_id);
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/zone/list";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/mcnpage/user/punch/zone/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String>map,RedirectAttributes redirectAttributes) {
		Row zoneRow =MapUtils.convertMaptoRowFillNullFieldWithEmptySpace(map);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no","");
		String depart_id=staff.getString("depart_id","");
		if(StringUtils.isEmptyOrNull(org_id))
		{
			addFlashMessage(redirectAttributes, Message.error("请先登陆"));
			return "redirect:list.do";
		}
		if(StringUtils.isEmptyOrNull(depart_id))
		{
			depart_id ="";
		}
		zoneRow.put("org_id", org_id);
		zoneRow.put("depart_id", depart_id);
		//经度
		String longitude =zoneRow.getString("longitude","");
		//纬度
		String latitude =zoneRow.getString("latitude","");
		//检验经纬度的长度，是否是数字
		if(StringUtils.isEmptyOrNull(longitude))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写坐标经度，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(StringUtils.isEmptyOrNull(latitude))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写坐标纬度，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(longitude))
		{
			addFlashMessage(redirectAttributes, Message.error("经度数值应该是数字，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(latitude))
		{
			addFlashMessage(redirectAttributes, Message.error("纬度数值应该是数字，精确到小数点后6位"));
			return "redirect:list.do";
		}
		int lon_len =NumberUtils.getDecimalLength(longitude);
		if( lon_len < 6 )
		{
			addFlashMessage(redirectAttributes, Message.error("经度数值需精确到小数点后6位"));
			return "redirect:list.do";
		}
		int lat_len =NumberUtils.getDecimalLength(latitude);
		if( lat_len < 6 )
		{
			addFlashMessage(redirectAttributes, Message.error("纬度数值需精确到小数点后6位"));
			return "redirect:list.do";
		}
		//半径
		String radius =zoneRow.getString("radius","");
		if(StringUtils.isEmptyOrNull(radius))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写半径值，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(radius))
		{
			addFlashMessage(redirectAttributes, Message.error("半径数值应该是数字，整数"));
			return "redirect:list.do";
		}
		int iRadius =Integer.parseInt(radius);
		if(iRadius <=0)
		{
			addFlashMessage(redirectAttributes, Message.error("半径的值只能是正整数"));
			return "redirect:list.do";
		}
		// 计算圆的经纬度范围、南侧经度，北侧经度，西侧纬度，东侧纬度
		double south_longitude =0;
		double north_longitude =0;
		double west_latitude =0;
		double east_latitude =0;
		double x1 = Double.parseDouble(longitude);
		double y1 = Double.parseDouble(latitude);
		double r[] = GeographyUtil.getRange(x1, y1, Integer.parseInt(radius));
		south_longitude =r[0];
		north_longitude =r[1];
		west_latitude =r[2];
		east_latitude =r[3];
		DecimalFormat df =new DecimalFormat("#.######");
		zoneRow.put("south_longitude", df.format(south_longitude));
		zoneRow.put("north_longitude", df.format(north_longitude));
		zoneRow.put("west_latitude", df.format(west_latitude));
		zoneRow.put("east_latitude", df.format(east_latitude));
		//区域类型1 圆通过半径确定范围2矩形 通过长宽高确定有效范围；目前只支持圆形计算
//		zoneRow.put("map_type", "1");
		punchZoneService.save(zoneRow);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id, "编号不能为空");
		Row row =punchZoneService.find(id);
		model.addAttribute("row", row);
		return "/mcnpage/user/punch/zone/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String>map,RedirectAttributes redirectAttributes) {
		Row zoneRow =MapUtils.convertMaptoRowFillNullFieldWithEmptySpace(map);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no","");
		String depart_id=staff.getString("depart_id","");
		if(StringUtils.isEmptyOrNull(org_id))
		{
			addFlashMessage(redirectAttributes, Message.error("请先登陆"));
			return "redirect:list.do";
		}
		if(StringUtils.isEmptyOrNull(depart_id))
		{
			depart_id ="";
		}
		zoneRow.put("org_id", org_id);
		zoneRow.put("depart_id", depart_id);
		//经度
		String longitude =zoneRow.getString("longitude","");
		//纬度
		String latitude =zoneRow.getString("latitude","");
		//检验经纬度的长度，是否是数字
		if(StringUtils.isEmptyOrNull(longitude))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写坐标经度，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(StringUtils.isEmptyOrNull(latitude))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写坐标纬度，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(longitude))
		{
			addFlashMessage(redirectAttributes, Message.error("经度数值应该是数字，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(latitude))
		{
			addFlashMessage(redirectAttributes, Message.error("纬度数值应该是数字，精确到小数点后6位"));
			return "redirect:list.do";
		}
		int lon_len =NumberUtils.getDecimalLength(longitude);
		if( lon_len < 6 )
		{
			addFlashMessage(redirectAttributes, Message.error("经度数值需精确到小数点后6位"));
			return "redirect:list.do";
		}
		int lat_len =NumberUtils.getDecimalLength(latitude);
		if( lat_len < 6 )
		{
			addFlashMessage(redirectAttributes, Message.error("纬度数值需精确到小数点后6位"));
			return "redirect:list.do";
		}
		//半径
		String radius =zoneRow.getString("radius","");
		if(StringUtils.isEmptyOrNull(radius))
		{
			addFlashMessage(redirectAttributes, Message.error("请填写半径值，精确到小数点后6位"));
			return "redirect:list.do";
		}
		if(! NumberUtils.isNumber(radius))
		{
			addFlashMessage(redirectAttributes, Message.error("半径数值应该是数字，整数"));
			return "redirect:list.do";
		}
		int iRadius =Integer.parseInt(radius);
		if(iRadius <=0)
		{
			addFlashMessage(redirectAttributes, Message.error("半径的值只能是正整数"));
			return "redirect:list.do";
		}
		// 计算圆的经纬度范围、南侧经度，北侧经度，西侧纬度，东侧纬度
		double south_longitude =0;
		double north_longitude =0;
		double west_latitude =0;
		double east_latitude =0;
		double x1 = Double.parseDouble(longitude);
		double y1 = Double.parseDouble(latitude);
		double r[] = GeographyUtil.getRange(x1, y1, Integer.parseInt(radius));
		south_longitude =r[0];
		north_longitude =r[1];
		west_latitude =r[2];
		east_latitude =r[3];
		DecimalFormat df =new DecimalFormat("#.######");
		zoneRow.put("south_longitude", df.format(south_longitude));
		zoneRow.put("north_longitude", df.format(north_longitude));
		zoneRow.put("west_latitude", df.format(west_latitude));
		zoneRow.put("east_latitude", df.format(east_latitude));
		punchZoneService.update(zoneRow);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		punchZoneService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
