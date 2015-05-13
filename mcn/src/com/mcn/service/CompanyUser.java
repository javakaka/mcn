package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

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
	
	/**
	 * 查询所有启用状态的用户，用于每日统计漏打卡
	 * @return
	 */
	public DataSet queryAllRunningUsers()
	{
		DataSet ds =new DataSet();
		String cur_date =DateUtil.getCurrentDate();
		String sql ="select a.* from mcn_users a ,sm_site b ,sm_bureau c "
		+" where a.depart_id=b.site_no  "
		+" and a.org_id=c.bureau_no  "
		+" and a.status='1' "
		+" and b.state='1' "
		+" and c.status='1' "
		+" and '"+cur_date+"'>=c.begin_date "
		+" and '"+cur_date+"'<=c.end_date ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public void synUserFromMcnUsersToSmStaff(String org_id)
	{
		String sql ="select * from mcn_users where org_id='"+org_id+"' ";
		DataSet ds =new DataSet();
		ds =queryDataSet(sql);
		for(int i=0;i<ds.size();i++)
		{
			Row row =(Row)ds.get(i);
			String orgg_id =row.getString("org_id","");
			String depart_id =row.getString("depart_id","");
			String user_name =row.getString("username","");
			String password =row.getString("password","");
			String real_name =row.getString("name","");
			String md5_pwd =Md5Util.Md5(password);
			String sql2 ="select count(*) from sm_staff where bureau_no='"+org_id+"' and staff_name='"+user_name+"' ";
			int count =Integer.parseInt(queryField(sql2));
			if(count<=0)
			{
				int staff_no =getTableSequence("sm_staff", "staff_no", 10000);
				String staffSql="insert into sm_staff (staff_no,bureau_no,site_no,staff_name,password,real_name) " +
						"values ('"+staff_no+"','"+orgg_id+"','"+depart_id+"','"+user_name+"','"+md5_pwd+"','"+real_name+"')";
				System.out.println("sqll====>>"+staffSql);
				update(staffSql);
			}
			else
			{
				System.out.println("orgg_id:"+orgg_id+"   user_name:"+user_name+"  已经存在，不用同步");
			}
		}
	}
}
