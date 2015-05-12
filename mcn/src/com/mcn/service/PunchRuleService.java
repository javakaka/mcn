package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnPunchRuleService")
public class PunchRuleService extends Service{

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
		sql = "select a.*, b.site_name as depart_name  from mcn_punch_rule a left join sm_site b on a.depart_id=b.site_no where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_punch_rule where 1=1 ";
		countSql +=" and org_id='"+org_id+"'";
		countSql += restrictions;
//		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
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
		int id = getTableSequence("mcn_punch_rule", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_rule", row);
	}

	public void insert(Row row) {
		int id = getTableSequence("mcn_punch_rule", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_rule", row);
	}
	
	public int update(Row row) {
		int rowNum =0;
		String id = row.getString("id");
		rowNum =update("mcn_punch_rule", row, " id='"+id+"'");
		return rowNum;
	}
	public int updateWithEmptyFields(Row row) {
		int rowNum =0;
		String id = row.getString("id");
		rowNum =updateWithoutFilterEmptyString("mcn_punch_rule", row, " id='"+id+"'");
		return rowNum;
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
		sql = "select * from mcn_punch_rule where id='" + id + "'";
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
		String PUNCH_NUM=getRow().getString("PUNCH_NUM","");
		row.put("DEPART_ID", DEPART_ID);
		row.put("AM_START", AM_START);
		row.put("AM_END", AM_END);
		row.put("PM_START", PM_START);
		row.put("PM_END", PM_END);
		row.put("ID", ID);
		update("mcn_punch_rule", row, " id='" + ID + "'");
	}
	
	
	/**
	 * 查询一条部门打卡时间数据
	 * 
	 * @return void
	 */
	public Row queryOneDepartPunchTime(String org_id) {
		String sql ="select * from mcn_punch_rule where org_id='"+org_id+"' limit 0,1";
		Row row =queryRow(sql);
		return row;
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
			sql = "delete from mcn_punch_rule where id in(" + id + ")";
			update(sql);
		}
	}
	
	/********************************************* mobile api ***************************************************************/
	public Row queryDepartTimes(String user_id)
	{
		Row row =new Row();
		sql ="select * from mcn_punch_rule  where DEPART_ID=(select depart_id from mcn_users where id='"+user_id+"') ";
		row =queryRow(sql);
		return row;
	}
	/********************************************* mobile api ***************************************************************/

	//通知公告列表
	public DataSet messageList(String org_id){
		DataSet data = null;
		sql = "select * from mcn_message where org_id='"+org_id+"'";
		data = queryDataSet(sql);
		return data;
	}
	//通知公告修改
	public void messageEdit(Row row){
		update("mcn_message", row, " id='" +row.getString("id") + "'");
	}
	
	//通知公告删除
	public void messageDelete(Long... ids){
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			System.out.println("id====="+id);
			sql = "delete from mcn_message where id in(" + id + ")";
			update(sql);
		}
	}
	
	//通知公告查看
	public Row messageView(String id){
		Row row = null;
		sql = "select * from mcn_message where id='"+id+"'";
		row = queryRow(sql);
		return row;
	}
	//通知公告添加
	public void messageAdd(Row row){
	int id = getTableSequence("mcn_message", "id", 1);
	row.put("id", id);
	row.put("create_time", DateUtil.getCurrentDate());
	insert("mcn_message", row);
	}
	
	/**
	 * 查询所有的打卡规则设置记录，切换打卡时间时需要用到
	 * @return
	 */
	public DataSet queryAllPunchTimeRules()
	{
		DataSet ds =new DataSet();
		String sql ="select * from mcn_punch_rule ";
		ds =queryDataSet(sql);
		return ds;
	}
}