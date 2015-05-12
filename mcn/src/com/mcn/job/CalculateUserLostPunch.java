package com.mcn.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.CompanyUser;
import com.mcn.service.PunchLogService;

/**   
 * E-mail:510836102@qq.com
 * 类说明: 周一到周五每天晚上23点50分0秒统计漏打卡数据,并保存到表 mcn_lost_punch_log
 */
@Component("mcnCalculateUserLostPunchJob")
@Lazy(false)
public class CalculateUserLostPunch extends Service{

	@Resource(name = "mcnPunchLogService")
	private   PunchLogService punchLogService;
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	@Scheduled(cron = "${job.mcn.calculate.lost.punch.cron}")
	public void execute()
	{
		System.out.println("-----------统计用户漏打卡数据  开始..................."+DateUtil.getCurrentDateTime());
		String cur_date =DateUtil.getCurrentDate();
		DataSet runningUserDs =companyUserService.queryAllRunningUsers();
		if(runningUserDs != null && runningUserDs.size()>0 )
		{
			System.out.println("共需要统计："+runningUserDs.size()+"(个)用户的漏打卡数据");
			for(int i=0; i<runningUserDs.size(); i++)
			{
				Row temp =(Row)runningUserDs.get(i);
				String user_id=temp.getString("id","");
				String depart_id=temp.getString("depart_id","");
				// get site punch_rule
				String punch_type_str =punchLogService.getUserPunchTypeStr(user_id,cur_date);
				if(punch_type_str.indexOf("1") == -1)
				{
					
				}
				if(punch_type_str.indexOf("2") == -1)
				{
					
				}
				if(punch_type_str.indexOf("3") == -1)
				{
					
				}
				if(punch_type_str.indexOf("4") == -1)
				{
					
				}
			}
		}
		System.out.println("-----------统计用户漏打卡数据  操作成功！！----------------"+DateUtil.getCurrentDateTime());
	}
	
}
