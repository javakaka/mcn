package com.ezcloud.framework.util;

import javax.annotation.Resource;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.system.SystemConfigService;


/**
 * 
 * 读取通用配置项数据
 * @author Administrator
 *
 */
public class ConfigUtil {
	
	@Resource(name = "frameworkSystemConfigService")
	private static SystemConfigService configService;
	/**
	 * 读取指定业务类型和业务编号的值，返回数组
	 * @param busiType
	 * @param busiCode
	 * @return
	 * @throws JException
	 */
	public static String[] getConfigData(String busiType,
			String busiCode)  {
		return configService.getConfigData(busiType, busiCode);
	}

	

	/**
	 * 读取项目参数
	 * @param paramKey
	 * @return
	 * @throws JException
	 */
	public static String getParam(String paramKey) {
		return configService.getParam(paramKey);
	}
	

	/**
	 * 设置通用配置项，如果busi_code_set为null，则删除该配置项；<br/>如果已经存在改配置项，则更新该配置项；否则新建配置项
	 * @param busi_type
	 * @param busi_code
	 * @param busi_code_set
	 * @param set_memo
	 * @throws JException
	 */
	public static void setConfigData(String busi_type, String busi_code,
			String busi_code_set, String set_memo)
	{
		configService.setConfigData(busi_type, busi_code, busi_code_set, set_memo);
	}
	
}
