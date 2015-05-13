package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

/**
 * 用户app权限处理业务类，查找用户拥有管理哪些部门的权限
 * @author Administrator
 *
 */
@Component("mcnCompanyUserPermissionService")
public class CompanyUserPermission extends Service{
	
	/**
	 * 根据用户编号查找用户是否需要权限过滤
	 * 返回值：
	 * is_need_permission 是否需要权限过滤0不需要1需要
	 * fun_ids  具有管理权限的部门编号，格式： '1','2','3'...
	 * role_ids 具有的app角色编号，格式： '1','2','3'...
	 * @param user_id
	 * @return
	 */
	public Row queryUserPeimissionFunIds(String user_id)
	{
		Row row =new Row();
		//是否需要权限过滤0不需要1需要
		String is_need_permission ="0";
		//拥有相应管理权限的部门编号，格式： '1','2','3'...
		String fun_ids ="";
		String role_ids ="";
		DataSet ds =new DataSet();
		if(StringUtils.isEmptyOrNull(user_id))
		{
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
			return row;
		}
		String sql ="select * from mcn_users where id='"+user_id+"'";
		Row userRow =queryRow(sql);
		if(userRow == null)
		{
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
			return row;
		}
		String org_id =userRow.getString("org_id","");
		String username =userRow.getString("username","");
		//query sm_staff
		sql ="select * from sm_staff where bureau_no='"+org_id+"' and staff_name='"+username+"' ";
		Row staffRow =queryRow(sql);
		if(staffRow == null)
		{
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
			return row;
		}
		String staff_no =staffRow.getString("staff_no","");
		if(StringUtils.isEmptyOrNull(staff_no))
		{
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
			return row;
		}
		sql ="select * from sm_staff_app_role where staff_no='"+staff_no+"'";
		ds =queryDataSet(sql);
		if(ds == null || ds.size() == 0)
		{
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
			return row;
		}
		else
		{
			//有app角色权限设置，需要找出用户拥有哪些部门的管理权限
			is_need_permission ="1";
			for(int i=0;i<ds.size();i++)
			{
				Row temp =(Row)ds.get(i);
				String role_id =temp.getString("role_id","");
				if(!StringUtils.isEmptyOrNull(role_id))
				{
					if(role_ids.length()>0)
					{
						role_ids +=",";
					}
					role_ids +="'"+role_id+"'";
				}
			}
			//query fun_ids 
			if(role_ids.length()>0)
			{
				sql =" select distinct fun_id from sm_app_role_fun where role_id in ("+role_ids+") ";
				DataSet funDs =queryDataSet(sql);
				if(funDs != null && funDs.size()>0)
				{
					for(int i=0;i<funDs.size();i++)
					{
						Row funRow =(Row)funDs.get(i);
						String fun_id=funRow.getString("fun_id","");
						if(!StringUtils.isEmptyOrNull(fun_id))
						{
							if(fun_ids.length()>0)
							{
								fun_ids +=",";
							}
							fun_ids +="'"+fun_id+"'";
						}
					}
				}
			}
			row.put("is_need_permission", is_need_permission);
			row.put("fun_ids", fun_ids);
			row.put("role_ids", role_ids);
		}
		return row;
	}
}
