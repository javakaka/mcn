package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;

@Component("mcnVersionService")
public class VersionService extends Service{

	public String queryCurrentVersion(){
		String sql ="select * from sm_ban";
		return queryField(sql);
	}
	
	public void updateVersion(String bb){
		String sql ="update sm_ban set content = '"+bb+"'";
		update(sql);
	}
}