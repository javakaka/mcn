package com.ezcloud.framework.service.system;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;

/**
 * 系统帐户服务类
 * @author JianBoTong
 *
 */
@Component("frameworkStaffService")
public class Staff extends Service{

	public void save()
	{
		Row staff =getRow();
		String staff_no =String.valueOf(getTableSequence("sm_staff", "staff_no", 10001));
		staff.put("staff_no", staff_no);
		insert("sm_staff", staff);
		getRow().put("staff_no", staff_no);
	}
	
}
