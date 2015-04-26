package com.ezcloud.framework.service.system;

import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.StringUtil;

/**
 * 区域
 * @author JianBoTong
 *
 */
@Component("frameworkSystemBureauTokenService")
public class SystemBureauTokenService  extends Service{
	
	public String getToken(int length,String bureau_no)
	{
		String token ="";
		token =StringUtil.getRandKeys(length).toUpperCase();
		String sql="";
		boolean isTokenExisted=true;
		do
		{
			sql ="select count(*) from sm_bureau_token where bureau_no='"+bureau_no+"' and token='"+token+"' ";	
			int num =Integer.parseInt(queryField(sql));
			if(num > 0)
			{
				isTokenExisted =true;
			}
			else
			{
				isTokenExisted =false;
			}
		}
		while(isTokenExisted);
		return token;
	}
	
	
	public String getBureauNoByToken(String token)
	{
		String bureau_no ="";
		String sql ="select bureau_no from sm_bureau_token where token='"+token+"'";
		bureau_no =queryField(sql);
		return bureau_no;
	}
	public int save(Row row)
	{
		int rowNum =0;
		rowNum =insert("sm_bureau_token",row);
		return rowNum;
	}
	
}
