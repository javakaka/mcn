package com.mcn.controller.mobile;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mcn.service.VersionService;

@Controller("mobileVersionController")
@RequestMapping("/api/version")
public class VersionController extends BaseController {
	
	
	@Resource(name = "mcnVersionService")
	private VersionService versionService;
	
	//版本升级
	@RequestMapping(value = "/current")
	public @ResponseBody String BanSj(){
		return versionService.queryCurrentVersion();
	}
	//版本升级修改
	@RequestMapping(value = "/update")
	public @ResponseBody String BanSjUpdate(String bb){
		versionService.updateVersion(bb);
		return null;
	}
}
