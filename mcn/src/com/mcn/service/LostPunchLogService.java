package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**
 * 漏打卡业务处理
 * @author Administrator
 */
@Component("mcnLostPunchLogService")
public class LostPunchLogService extends Service{

	public int insert(Row row)
	{
		int rowNum =0;
		int id=getTableSequence("mcn_lost_punch_log", "id", 1);
		String create_time =DateUtil.getCurrentDateTime();
		row.put("id", id);
		row.put("create_time", create_time);
		insert("mcn_lost_punch_log", row);
		return rowNum;
	}
}