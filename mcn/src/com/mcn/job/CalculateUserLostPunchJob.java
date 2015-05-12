package com.mcn.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.CompanySite;
import com.mcn.service.CompanyUser;
import com.mcn.service.LostPunchLogService;
import com.mcn.service.PunchLogService;

/**   
 * E-mail:510836102@qq.com
 * 类说明: 周一到周五每天晚上23点50分0秒统计漏打卡数据,并保存到表 mcn_lost_punch_log
 */
@Component("mcnCalculateUserLostPunchJob")
@Lazy(false)
public class CalculateUserLostPunchJob extends Service{

	@Resource(name = "mcnPunchLogService")
	private   PunchLogService punchLogService;
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	@Resource(name = "companySiteService")
	private CompanySite companySiteService;
	
	@Resource(name = "mcnLostPunchLogService")
	private LostPunchLogService lostPunchLogService;
	
	
	@Scheduled(cron = "${job.mcn.calculate.lost.punch.cron}")
	public void execute()
	{
		System.out.println("-----------统计用户漏打卡数据  开始..................."+DateUtil.getCurrentDateTime());
		String cur_date =DateUtil.getCurrentDate();
		String punch_year =cur_date.substring(0,4);
		String punch_month =cur_date.substring(5,7);
		String punch_day =cur_date.substring(8,10);
		DataSet runningUserDs =companyUserService.queryAllRunningUsers();
		if(runningUserDs != null && runningUserDs.size()>0 )
		{
			System.out.println("共需要统计："+runningUserDs.size()+"(个)用户的漏打卡数据");
			for(int i=0; i<runningUserDs.size(); i++)
			{
				Row temp =(Row)runningUserDs.get(i);
				String user_id=temp.getString("id","");
				String depart_id=temp.getString("depart_id","");
				String org_id=temp.getString("org_id","");
				// get site punch_rule
				Row timeRow =companySiteService.querySitePunchTime(depart_id);
				if(timeRow == null)
				{
					continue;
				}
				String am_start="";
				String am_end="";
				String pm_start="";
				String pm_end="";
				String punch_type_str =punchLogService.getUserPunchTypeStr(user_id,cur_date);
				Row lostPunchRow=new Row();
//				mcn_lost_punch_log
				if(punch_type_str.indexOf("1") == -1)
				{
					am_start =timeRow.getString("am_start","");
					if(! StringUtils.isEmptyOrNull(am_start))
					{
						//save lost punch log
						lostPunchRow=new Row();
						lostPunchRow.put("org_id", org_id);
						lostPunchRow.put("user_id", user_id);
						lostPunchRow.put("punch_year", punch_year);
						lostPunchRow.put("punch_month", punch_month);
						lostPunchRow.put("punch_day", punch_day);
						lostPunchRow.put("punch_date", cur_date);
						lostPunchRow.put("lost_type", "1");
						lostPunchLogService.insert(lostPunchRow);
					}
				}
				if(punch_type_str.indexOf("2") == -1)
				{
					am_end =timeRow.getString("am_end","");
					if(! StringUtils.isEmptyOrNull(am_end))
					{
						//save lost punch log
						lostPunchRow=new Row();
						lostPunchRow.put("org_id", org_id);
						lostPunchRow.put("user_id", user_id);
						lostPunchRow.put("punch_year", punch_year);
						lostPunchRow.put("punch_month", punch_month);
						lostPunchRow.put("punch_day", punch_day);
						lostPunchRow.put("punch_date", cur_date);
						lostPunchRow.put("lost_type", "2");
						lostPunchLogService.insert(lostPunchRow);
					}
				}
				if(punch_type_str.indexOf("3") == -1)
				{
					pm_start =timeRow.getString("pm_start","");
					if(! StringUtils.isEmptyOrNull(pm_start))
					{
						//save lost punch log
						lostPunchRow=new Row();
						lostPunchRow.put("org_id", org_id);
						lostPunchRow.put("user_id", user_id);
						lostPunchRow.put("punch_year", punch_year);
						lostPunchRow.put("punch_month", punch_month);
						lostPunchRow.put("punch_day", punch_day);
						lostPunchRow.put("punch_date", cur_date);
						lostPunchRow.put("lost_type", "3");
						lostPunchLogService.insert(lostPunchRow);
					}
				}
				if(punch_type_str.indexOf("4") == -1)
				{
					pm_end =timeRow.getString("pm_end","");
					if(! StringUtils.isEmptyOrNull(pm_end))
					{
						//save lost punch log
						lostPunchRow=new Row();
						lostPunchRow.put("org_id", org_id);
						lostPunchRow.put("user_id", user_id);
						lostPunchRow.put("punch_year", punch_year);
						lostPunchRow.put("punch_month", punch_month);
						lostPunchRow.put("punch_day", punch_day);
						lostPunchRow.put("punch_date", cur_date);
						lostPunchRow.put("lost_type", "4");
						lostPunchLogService.insert(lostPunchRow);
					}
				}
			}
		}
		System.out.println("-----------统计用户漏打卡数据  操作成功！！----------------"+DateUtil.getCurrentDateTime());
	}
}
