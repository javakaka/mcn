package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnLeaveService")
public class LeaveService extends Service{

	/**
	 * 分页查询打卡规则
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		String status =row.getString("status",null);
		String start_date =row.getString("start_date",null);
		String end_date =row.getString("end_date",null);
		sql = "select a.*, b.name  from mcn_leave_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"'  ";
		if(! StringUtils.isEmptyOrNull(status))
		{
			sql +=" and a.status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			sql +=" and a.start_date >='"+start_date+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			sql +=" and a.end_date <= '"+end_date+"' ";
		}
		sql += "ORDER BY a.id DESC ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_leave_log a left join mcn_users b  on a.user_id=b.id  where 1=1 ";
		countSql +=" and a.org_id='"+org_id+"' ";
		if(! StringUtils.isEmptyOrNull(status))
		{
			countSql +=" and a.status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			countSql +=" and a.start_date >='"+start_date+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			countSql +=" and a.end_date <= '"+end_date+"' ";
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
		System.out.println("url========"+sql);
		dataSet = queryDataSet(sql);
		DataSet data = new DataSet();
		if(dataSet.size()>0){
		for(int i=0;i<dataSet.size();i++){
			Row row = new Row();
			row = (Row) dataSet.get(i);
			String leave_id = row.getString("id");
			String sql2 = "SELECT b.name as status_name from check_up_log a,mcn_users b WHERE a.up_check_id=b.id and a.leave_id='"+leave_id+"'";
			DataSet dataSet2 = queryDataSet(sql2);
			String statu_name = "";
			if(dataSet2.size()>0){
				for(int j=0;j<dataSet2.size();j++){
					Row row2 = new Row();
					row2 = (Row) dataSet2.get(j);
					if(j==0){
					statu_name += row2.getString("status_name");
					}else{
					statu_name += ","+row2.getString("status_name");
					}
				}
			}
			row.put("statu_name", statu_name);
			data.add(row);
		}
		}
		page = new Page(data, total, pageable);
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
		int id = getTableSequence("mcn_leave_log", "id", 1);
		row.put("ID", id);
		insert("mcn_leave_log", row);
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
		sql = "select * from mcn_leave_log where id='" + id + "'";
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
		update("mcn_leave_log", row, " id='" + ID + "'");
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
			sql = "delete from mcn_leave_log where id in(" + id + ")";
			update(sql);
		}
	}
	
	/************************************* mobile api ************************************************/
	//手机请假
	public int mobileAdd(Row row)
	{
		int id =getTableSequence("mcn_leave_log", "id", 1);
		row.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		row.put("create_time",curTime );
		row.put("year", curTime.substring(0,4));
		row.put("month", curTime.substring(5, 7));
		row.put("status", 1);
		insert("mcn_leave_log", row);
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
	
	public void insert_check_up_log(Row checkRow) {
		int id =getTableSequence("check_up_log", "id", 1);
		checkRow.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		checkRow.put("create_time",curTime );
		insert("check_up_log", checkRow);
	}
	
	
	//查询自己的请假列表
	public DataSet querySelfLeaveList(String user_id,String page,String page_size,String status)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql ="select id,sum_day,leave_type,start_date, end_date, reason, audit_objection from mcn_leave_log where user_id='"+user_id+"' and status='"+status+"' limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		System.out.println("dada====="+ds.toString());
		return ds;
	}
	
	public DataSet queryLeaveTypeList(String org_id,String user_id,String page,String page_size,String leave_type){
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql = "select b.name as name, a.status as status,a.modify_time as time from mcn_leave_log a,mcn_users b WHERE a.user_id=b.id and a.org_id='"+org_id+"' and a.user_id='"+user_id+"' and a.leave_type='"+leave_type+"' LIMIT "+iStart+","+page_size;
		dataSet = queryDataSet(sql);
		return dataSet;
	}
	
	//查询属下的请假列表
	public DataSet queryFollowerLeaveList(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		/*
		sql ="select a.id,name,leave_type,start_date, end_date, reason,status, audit_objection from mcn_leave_log a left join mcn_users b on a.user_id=b.id  where user_id "
		+" in ( select id from mcn_users where depart_id in (select depart_id from mcn_users where id='"+user_id+"') and id !='"+user_id+"')  limit "+iStart+" , "+page_size;
		*/
		sql = "SELECT b.sum_day as sum_day,check_type,a.id as check_id,b.id,b.status,c.name,"
				+ "b.leave_type,b.start_date, b.end_date, b.reason,a.check_content as audit_objection "
				+ "from check_up_log a ,mcn_leave_log b,mcn_users c "
				+ "WHERE a.leave_id=b.id and b.user_id=c.id and a.up_check_id='"+user_id+"' "
				+ "and b.status='1' limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	//查询已审批过的请假列表
	public DataSet queryFollowerLeaveList2(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		/*
		sql ="select a.id,name,leave_type,start_date, end_date, reason,status, audit_objection from mcn_leave_log a left join mcn_users b on a.user_id=b.id  where user_id "
		+" in ( select id from mcn_users where depart_id in (select depart_id from mcn_users where id='"+user_id+"') and id !='"+user_id+"')  limit "+iStart+" , "+page_size;
		*/
		sql = "SELECT a.check_type as check_type,b.sum_day as sum_day,check_type,a.id as check_id,b.id,c.name,b.leave_type,b.start_date, b.end_date, b.reason,a.check_content as audit_objection from check_up_log a ,mcn_leave_log b,mcn_users c WHERE a.leave_id=b.id and b.user_id=c.id and a.up_check_id='"+user_id+"' and b.status not in(1) limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	

	public void auditLeave2(String check_id,String status,String audit_objection, String modify_time)
	{
		String sql = "UPDATE check_up_log SET check_type='"+status+"',check_content='"+audit_objection+"',update_time='"+modify_time+"' WHERE id='"+check_id+"'";
		update(sql);
		/*
		String id=row.getString("id");
		update("mcn_leave_log", row, " id='"+id+"'");
		*/
	}
	public void auditLeave(Row row)
	{
		String id=row.getString("id");
		update("mcn_leave_log", row, " id='"+id+"'");
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
	
	//根据请假记录levae_ID查询审核人
	public DataSet queryLeaveId(String leave_id) {
		sql = "SELECT b.name as audit_name,a.check_type as audit_type,a.create_time as audit_time from check_up_log a,mcn_users b where a.up_check_id=b.id and leave_id='"+leave_id+"'";
		DataSet da = queryDataSet(sql);
		return da;
	}
	
	//请假规则说明
	public Row queryLeaveGZ(String org_id) {
		sql = "SELECT content from leave_gz WHERE org_id='"+org_id+"'";
		Row da = queryRow(sql);
		return da;
	}
	
	//审批规则说明
	public Row queryLeaveSPGZ(String org_id) {
		sql = "SELECT content from leave_spgz WHERE org_id='"+org_id+"'";
		Row da = queryRow(sql);
		return da;
	}
	
	//请假规则查询
	public String leaveGZ(String org_id) {
		sql = "SELECT content from leave_gz WHERE org_id='"+org_id+"' LIMIT 0,1";
		return queryField(sql);
	}
	
	//审批规则查询
	public String leaveSPGZ(String org_id) {
		sql = "SELECT content from leave_spgz WHERE org_id='"+org_id+"' LIMIT 0,1";
		return queryField(sql);
	}
	
	//请假规则添加
	public void leaveGZAdd(String org_id,String content) {
		sql = "SELECT content from leave_gz WHERE org_id='"+org_id+"' LIMIT 0,1";
		String sf = queryField(sql);
		String curTime =DateUtil.getCurrentDate();
		if(sf == null){
			Row row = new Row(); 
			int id =getTableSequence("leave_gz", "id", 1);
			row.put("id", id);
			row.put("org_id", org_id);
			row.put("content", content);
			row.put("create_time", curTime);
			insert("leave_gz", row);
		}else{
			String sql2="update leave_gz set content='"+content+"',update_time='"+curTime+"' where org_id='"+org_id+"'";
			update(sql2);
		}
	}
	
	//审批规则添加
	public void leaveSPGZAdd(String org_id,String content) {
		sql = "SELECT content from leave_spgz WHERE org_id='"+org_id+"' LIMIT 0,1";
		String sf = queryField(sql);
		String curTime =DateUtil.getCurrentDate();
		if(sf == null){
			Row row = new Row(); 
			int id =getTableSequence("leave_spgz", "id", 1);
			row.put("id", id);
			row.put("org_id", org_id);
			row.put("content", content);
			row.put("create_time", curTime);
			insert("leave_spgz", row);
		}else{
			String sql2="update leave_spgz set content='"+content+"',update_time='"+curTime+"' where org_id='"+org_id+"'";
			update(sql2);
		}
	}
	
	
	public DataSet queryLeaveListByType(String id,String type,int page,int page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(page-1)*page_size;
		String sql =" select * from mcn_leave_log where leave_type='"+type+"' and user_id='"+id+"' "
				+ "order by create_time desc limit "+iStart+" ,"+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
}