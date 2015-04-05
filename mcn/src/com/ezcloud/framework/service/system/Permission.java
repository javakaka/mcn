/**
 * @Title: Permission.java
 * @Package com.ezcloud.framework.service.system
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午04:00:55
 * @version V1.0
 */
package com.ezcloud.framework.service.system;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;

/**
 * 权限服务
 * 
 * @ClassName: Permission
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午04:00:55
 */
@Component("frameworkPermissionService")
public class Permission extends Service {

	/**
	 * 根据用户编号获取用户的权限
	 * 
	 * @return void
	 */
	public void getStaffPermission() {
		String staff_no = row.getString("staff_no");
		sql = "select DISTINCT c.FUN_ID,d.FUN_NAME,d.FUN_DESC,d.WIN_ID,d.WIN_MODE,d.ICO_NAME,d.LEVEL_INDEX,d.UP_FUN_ID,e.WIN_TARGET,e.WIN_DESC "
				+ "from sm_staff a,sm_staff_role b ,sm_role_fun c,sm_fun d LEFT JOIN sm_window e on d.WIN_ID=e.WIN_ID " + "where a.STAFF_NO=b.STAFF_NO " + "and a.STAFF_NO='" + staff_no + "' "
				+ "and b.ROLE_ID=c.ROLE_ID " + "and c.FUN_ID=d.FUN_ID ";
		dataSet = queryDataSet(sql);
	}

	public static void main(String args[]) {

	}
}