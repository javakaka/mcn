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
import com.mcn.service.PunchRuleBakService;
import com.mcn.service.PunchRuleHistoryService;
import com.mcn.service.PunchRuleService;

/**   
 * E-mail:510836102@qq.com
 * 类说明: 每月1号0点30分切换打卡时间设置
 * 流程：1 查询mcn_punch_rule_bak是否存在可切换的记录，
 * 如果存在，则将部门现在使用的打卡规则记录（存在于表mcn_punch_rule，移动到mcn_punch_rule_history）
 * 再将待切换的记录（存在于表mcn_punch_rule_bak，移动到表mcn_punch_rule）
 */
@Component("mcnSwitchPunchTimeRulePunchJob")
@Lazy(false)
public class SwitchPunchTimeRuleJob extends Service{

	@Resource(name = "mcnPunchRuleBakService")
	private PunchRuleBakService punchRuleBakService;
	
	@Resource(name = "mcnPunchRuleService")
	private PunchRuleService punchRuleService;
	
	@Resource(name = "mcnPunchRuleHistoryService")
	private PunchRuleHistoryService punchRuleHistoryService;
		
	@Scheduled(cron = "${job.mcn.punch.rule.switch.cron}")
	public void execute()
	{
		System.out.println("-----------切换打卡规则时间  开始..................."+DateUtil.getCurrentDateTime());
		DataSet ruleDs =punchRuleService.queryAllPunchTimeRules();
		DataSet bakRuleDs =punchRuleBakService.queryAllPunchTimeBakRules();
		System.out.println("-----------共有："+bakRuleDs.size()+"(条可切换的打卡规则)...................");
		if(bakRuleDs !=null )
		{
			for(int i=0;i<bakRuleDs.size();i++)
			{
				Row temp =(Row)bakRuleDs.get(i);
				String depart_id =temp.getString("depart_id","");
				for(int j=0;j<ruleDs.size();j++)
				{
					Row temp2 =(Row)ruleDs.get(j);
					String rule_id=temp2.getString("id");
					String c_depart_id =temp2.getString("depart_id","");
					if(!StringUtils.isEmptyOrNull(depart_id) 
							&& !StringUtils.isEmptyOrNull(c_depart_id)
							&& depart_id.equals(c_depart_id))
					{
						//switch
						// insert into mcn_punch_rule_history
						Row historyRow =temp2;
						punchRuleHistoryService.save(historyRow);
						// copy bak row into mcn_punch_rule
						Row newRuleRow =temp2;
						newRuleRow.put("am_start",temp.getString("am_start","") );
						newRuleRow.put("am_end",temp.getString("am_end","") );
						newRuleRow.put("pm_start",temp.getString("pm_start","") );
						newRuleRow.put("pm_end",temp.getString("pm_end","") );
						newRuleRow.put("punch_num",temp.getString("punch_num","0") );
						newRuleRow.put("id",rule_id);
						System.out.println("newRuleRow===>>"+newRuleRow);
						punchRuleService.updateWithEmptyFields(newRuleRow);
						//delete bak row
						long bak_id=Long.parseLong(temp.getString("id","0"));
						punchRuleBakService.delete(bak_id);
						break;
					}
				}
			}
		}
		System.out.println("-----------切换打卡规则时间  操作成功！！----------------"+DateUtil.getCurrentDateTime());
	}
}
