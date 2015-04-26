package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnWorkOvertimeService")
public class WorkOvertimeService extends Service{

	/**
	 * 分页查询加班申请单
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		String depart_id =row.getString("depart_id",null);
		String status =row.getString("status",null);
		String start_time =row.getString("start_time",null);
		String end_time =row.getString("end_time",null);
		sql = " select * from ( select a.*, b.name as user_name,c.site_name  "
				+ "from mcn_work_overtime a left join mcn_users b on a.user_id=b.id "
		+" left join sm_site c on a.depart_id=c.site_no "
		+ " ORDER BY a.create_time desc ) as tab "
		+ "where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and org_id='"+org_id+"'  ";
		if(! StringUtils.isEmptyOrNull(depart_id))
		{
			sql +=" and depart_id='"+depart_id+"' ";
		}
		if(! StringUtils.isEmptyOrNull(status))
		{
			sql +=" and status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(start_time))
		{
			sql +=" and start_time <='"+start_time+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_time))
		{
			sql +=" and end_time >= '"+end_time+"' ";
		}
		
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = " select count(*) from ( select a.*, b.name as user_name,c.site_name  "
				+ "from mcn_work_overtime a left join mcn_users b on a.user_id=b.id "
				+" left join sm_site c on a.depart_id=c.site_no ) as tab "
				+ "where 1=1 ";
		countSql +=" and org_id='"+org_id+"'  ";
		if(! StringUtils.isEmptyOrNull(depart_id))
		{
			countSql +=" and depart_id='"+depart_id+"' ";
		}
		if(! StringUtils.isEmptyOrNull(status))
		{
			countSql +=" and status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(start_time))
		{
			countSql +=" and start_time <='"+start_time+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_time))
		{
			countSql +=" and end_time >= '"+end_time+"' ";
		}
		countSql += restrictions;
//		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) 
		{
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		System.out.println("sql---->>"+sql);
		dataSet = queryDataSet(sql);
		page = new Page(dataSet, total, pageable);
		return page;
	}

	/**
	 * 保存
	 * 
	 * @Title: save
	 * @return void
	 */
	public void save() {
		Row row = new Row();
		String ORG_ID=getRow().getString("org_id","");
		String DEPART_ID=getRow().getString("DEPART_ID","");
		String AM_START=getRow().getString("AM_START","");
		String AM_END=getRow().getString("AM_END","");
		String PM_START=getRow().getString("PM_START","");
		String PM_END=getRow().getString("PM_END","");
		row.put("ORG_ID", ORG_ID);
		row.put("DEPART_ID", DEPART_ID);
		row.put("AM_START", AM_START);
		row.put("AM_END", AM_END);
		row.put("PM_START", PM_START);
		row.put("PM_END", PM_END);
		int id = getTableSequence("mcn_work_overtime", "id", 1);
		row.put("ID", id);
		insert("mcn_work_overtime", row);
	}

	/**
	 * 根据id查找
	 * 
	 * @return Row
	 * @throws
	 */
	public Row find() {
		Row row = new Row();
		String id = getRow().getString("id");
		sql = "select a.*,b.name as user_name,c.site_name from mcn_work_overtime a "
				+" left join mcn_users b on a.user_id=b.id "
				+" left join sm_site c on a.depart_id=c.site_no "
				+ "where a.id='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String ID=getRow().getString("ID","");
		String DEPART_ID=getRow().getString("DEPART_ID","");
		String AM_START=getRow().getString("AM_START","");
		String AM_END=getRow().getString("AM_END","");
		String PM_START=getRow().getString("PM_START","");
		String PM_END=getRow().getString("PM_END","");
		row.put("DEPART_ID", DEPART_ID);
		row.put("AM_START", AM_START);
		row.put("AM_END", AM_END);
		row.put("PM_START", PM_START);
		row.put("PM_END", PM_END);
		row.put("ID", ID);
		update("mcn_work_overtime", row, " id='" + ID + "'");
	}
	
	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update(Row row) {
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("mcn_work_overtime", row, " id='" + id + "'");
	}

	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	public void delete(Long... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from mcn_work_overtime where id in(" + id + ")";
			update(sql);
		}
	}
	
	/************************************* mobile api ************************************************/
	//手机请假
	public int mobileAdd(Row row)
	{
		int id =getTableSequence("mcn_work_overtime", "id", 1);
		row.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		row.put("create_time",curTime );
		row.put("year", curTime.substring(0,4));
		row.put("month", curTime.substring(5, 7));
		row.put("status", 1);
		insert("mcn_work_overtime", row);
		return  id;
	}
	
	public void check_up_log(Row checkRow,String org_id,String user_id) {
		int id =getTableSequence("check_up_log", "id", 1);
		checkRow.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		checkRow.put("create_time",curTime );
		String sql = "select id from mcn_users WHERE org_id='"+org_id+"' and depart_id in(SELECT depart_id from mcn_users WHERE org_id='"+org_id+"' and id='"+user_id+"') and manager_id='是'";
		String up_id = queryField(sql);;
		checkRow.put("up_check_id",up_id);
		insert("check_up_log", checkRow);
	}
	
	public void insert(Row row) {
		int id =getTableSequence("mcn_work_overtime", "id", 1);
		row.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		row.put("create_time",curTime );
		insert("mcn_work_overtime", row);
	}
	
	public boolean isExistedWorkOverTime(String user_id) {
		boolean bool =false;
		String curTime =DateUtil.getCurrentDateTime();
		String sql ="select count(*) from mcn_work_overtime where user_id='"+user_id+"' and start_time<='"+curTime+"' and end_time>='"+curTime+"'";
		int num =Integer.parseInt(queryField(sql));
		if(num  > 0)
		{
			bool =true;
		}
		return bool;
	}
	
	/**
	 * 加班时间是否和已有的羁绊记录冲突
	 * @param user_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public boolean isExistedWorkOverTime(String user_id,String start_time,String end_time) {
		boolean bool =false;
		String sql ="select count(*) from mcn_work_overtime where user_id='"+user_id+"' and ((start_time>='"+start_time+"' and end_time>='"+end_time+"') or (start_time<='"+start_time+"' and end_time>='"+end_time+"') or (start_time<='"+start_time+"' and end_time<='"+end_time+"') or ( end_time<='"+start_time+"')) ";
		System.out.println("sql --->>"+sql);
		int num =Integer.parseInt(queryField(sql));
		if(num  > 0)
		{
			bool =true;
		}
		return bool;
	}
	
	public Row getCurrentWorkOverTimeRecord(String user_id) {
		Row row =null;
		String curTime =DateUtil.getCurrentDateTime();
		String sql ="select * from mcn_work_overtime where user_id='"+user_id+"' and start_time<='"+curTime+"' and end_time>='"+curTime+"'";
		row =queryRow(sql);
		return row;
	}
	
	
	//查询自己的列表
	public DataSet queryPageByUserId(String user_id,String page,String page_size,String status)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql ="select * from mcn_work_overtime where user_id='"+user_id+"'  order by create_time desc limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet queryLeaveTypeList(String org_id,String user_id,String page,String page_size,String leave_type){
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql = "select b.name as name, a.status as status,a.modify_time as time from mcn_work_overtime a,mcn_users b WHERE a.user_id=b.id and a.org_id='"+org_id+"' and a.user_id='"+user_id+"' and a.leave_type='"+leave_type+"' LIMIT "+iStart+","+page_size;
		dataSet = queryDataSet(sql);
		return dataSet;
	}
	
	//查询属下的请假列表
	public DataSet queryFollowerLeaveList(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		/*
		sql ="select a.id,name,leave_type,start_date, end_date, reason,status, audit_objection from mcn_work_overtime a left join mcn_users b on a.user_id=b.id  where user_id "
		+" in ( select id from mcn_users where depart_id in (select depart_id from mcn_users where id='"+user_id+"') and id !='"+user_id+"')  limit "+iStart+" , "+page_size;
		*/
		sql = "SELECT b.sum_day as sum_day,check_type,a.id as check_id,b.id,c.name,b.leave_type,b.start_date, b.end_date, b.reason,a.check_content as audit_objection from check_up_log a ,mcn_work_overtime b,mcn_users c WHERE a.leave_id=b.id and b.user_id=c.id and a.up_check_id='"+user_id+"' and b.status='1' limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	//查询已审批过的请假列表
	public DataSet queryFollowerLeaveList2(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		/*
		sql ="select a.id,name,leave_type,start_date, end_date, reason,status, audit_objection from mcn_work_overtime a left join mcn_users b on a.user_id=b.id  where user_id "
		+" in ( select id from mcn_users where depart_id in (select depart_id from mcn_users where id='"+user_id+"') and id !='"+user_id+"')  limit "+iStart+" , "+page_size;
		*/
		sql = "SELECT a.check_type as check_type,b.sum_day as sum_day,check_type,a.id as check_id,b.id,c.name,b.leave_type,b.start_date, b.end_date, b.reason,a.check_content as audit_objection from check_up_log a ,mcn_work_overtime b,mcn_users c WHERE a.leave_id=b.id and b.user_id=c.id and a.up_check_id='"+user_id+"' and b.status not in(1) limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	

	public void auditLeave2(String check_id,String status,String audit_objection, String modify_time)
	{
		String sql = "UPDATE check_up_log SET check_type='"+status+"',check_content='"+audit_objection+"',update_time='"+modify_time+"' WHERE id='"+check_id+"'";
		update(sql);
		/*
		String id=row.getString("id");
		update("mcn_work_overtime", row, " id='"+id+"'");
		*/
	}
	public void auditLeave(Row row)
	{
		String id=row.getString("id");
		update("mcn_work_overtime", row, " id='"+id+"'");
	}
	//转交给上级时执行的审核操作
	public void auditCheck(Row checkRow)
	{
		int id =getTableSequence("check_up_log", "id", 1);
		checkRow.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		checkRow.put("create_time",curTime );
		insert("check_up_log", checkRow);
		String leave_id = checkRow.getString("leave_id");
		String up_check_id = checkRow.getString("check_id");
		String sql="update check_up_log set check_type='2' where leave_id='"+leave_id+"' and up_check_id='"+up_check_id+"'";
		update(sql);
	}
	/************************************* mobile api ************************************************/
	
	//根据企业ID查询所有管理用户
	public DataSet allUsersList(String org_id) {
		String sql = "SELECT id,name from mcn_users WHERE org_id='"+org_id+"' and manager_id='是'";
		DataSet da = queryDataSet(sql);
		return da;
	}
	
}