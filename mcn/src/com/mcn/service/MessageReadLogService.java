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
	public Page queryPageForCompany(Pageable pageable) {
		Page page = null;
		String org_id =row.getString("org_id","");
		String m_id =row.getString("m_id","");
		sql = "select a.*, b.name  from mcn_message_read_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		if(!StringUtils.isEmptyOrNull(org_id))
		{
			sql +=" and b.org_id='"+org_id+"'  ";
		}
		if(!StringUtils.isEmptyOrNull(m_id))
		{
			sql +=" and a.m_id='"+m_id+"'  ";
		}
		sql += "ORDER BY a.id DESC ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_message_read_log a left join mcn_users b  on a.user_id=b.id  where 1=1 ";
		if(!StringUtils.isEmptyOrNull(org_id))
		{
			countSql +=" and b.org_id='"+org_id+"' ";
		}
		if(!StringUtils.isEmptyOrNull(m_id))
		{
			countSql +=" and a.m_id='"+m_id+"'  ";
		}
		countSql += restrictions;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) 
		{
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
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