package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnMessageReadLogService")
public class MessageReadLogService extends Service{

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
		sql = "select a.*, b.name  from mcn_message_read_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
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
			sql +=" and a.end_date < '"+end_date+"' ";
		}
		sql += "ORDER BY a.id DESC ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_message_read_log a left join mcn_users b  on a.user_id=b.id  where 1=1 ";
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
			countSql +=" and a.end_date < '"+end_date+"' ";
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
	public void save(Row row) {
		int id = getTableSequence("mcn_message_read_log", "id", 1);
		row.put("ID", id);
		row.put("read_num", 1);
		row.put("create_time", DateUtil.getCurrentDateTime());
		insert("mcn_message_read_log", row);
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
		sql = "select * from mcn_message_read_log where id='" + id + "'";
		row = queryRow(sql);
		return row;
	}
	
	/**
	 * 根据id查找
	 * 
	 * @return Row
	 * @throws
	 */
	public Row findByUserId(String user_id) {
		Row row =null;
		String id = getRow().getString("id");
		String sql = "select * from mcn_message_read_log where user_id='" + user_id + "'";
		row =queryRow(sql);
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
		update("mcn_message_read_log", row, " id='" + ID + "'");
	}
	
	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void updateReadNum(String user_id) {
		String sql ="update mcn_message_read_log set read_num =read_num+1 where user_id='"+user_id+"'";
		update(sql);
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
			sql = "delete from mcn_message_read_log where id in(" + id + ")";
			update(sql);
		}
	}
	
	/************************************* mobile api ************************************************/
}