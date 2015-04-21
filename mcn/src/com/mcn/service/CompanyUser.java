package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("companyUserService")
public class CompanyUser extends Service{

	public void delete (String org_id)
	{
		String sSql ="delete from mcn_users where org_id='"+org_id+"'";
		update(sSql);
	}
	
	public DataSet selectAll(String org_id)
	{
		DataSet ds=new DataSet();
		String sSql="select * from mcn_users where org_id='"+org_id+"'";
		ds =queryDataSet(sSql);
		return ds;
	}
	
	/**
	 * mobile login 
	 * @return
	 */
	public Row login(String org_id ,String username)
	{
		Row row =null;
		String sSql =" select * from mcn_users where ( username='"+username+"'  or telephone='"+username+"' ) and org_id ='"+org_id+"'";
		row =queryRow(sSql);
		return row;
	}
	
	/**
	 * mobile login 
	 * @return
	 */
	public Row findById(String id)
	{
		Row row =null;
		String sSql =" select * from mcn_users where id='"+id+"'";
		row =queryRow(sSql);
		return row;
	}
	
	/**
	 * change password
	 * @param user_id
	 * @param oldPwd
	 * @param newPwd
	 * @return 
	 */
	public int changePassword(String user_id,String oldPwd,String newPwd)
	{
		int status =0;
		String sSql ="select * from mcn_users where id='"+user_id+"'";
		Row staff =queryRow(sSql);
		if(staff == null){
			status =1;// user not exist
		}
		else
		{
			String password =staff.getString("password","");
			if( !password.equals(oldPwd)){
				status =2;// oldpassword not correct
			}
			else 
			{
				sSql ="update mcn_users set password='"+newPwd+"' where id='"+user_id+"'";
				int rowNum = update(sSql);
				if(rowNum ==0 ){
					status =3;// update failure
				}
			}
		}
		return status;
	}
	
	public void update(Row row )
	{
		String id=row.getString("id","");
		update("mcn_users", row, " id='"+id+"'");
	}
	
	/**
	 * 根据部门编号查询部门的默认审批人
	 * @param depart_id
	 * @return
	 */
	public String queryDefaultAuditUserId(String depart_id)
	{
		String id="";
		String sql ="select id from mcn_users where depart_id='"+depart_id+"' and default_manager='1' ";
		id =queryField(sql);
		return id;
	}
	
	/**
	 * 根据部门编号查询具有审核权限的人员列表
	 * @param depart_id
	 * @return
	 */
	public DataSet queryManagersByDepartID(String depart_id)
	{
		DataSet ds =null;
		String sql ="select * from mcn_users where depart_id='"+depart_id+"' and manager_id='是' ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 根据企业编号，查询此企业下面已有的人员总数
	 * @param org_id
	 * @return
	 */
	public int queryUserTotalNum(String org_id)
	{
		int num=0;
		String sql ="select count(*) from mcn_users where org_id='"+org_id+"' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
	
}
