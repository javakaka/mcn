package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;

@Component("mcnPunchRuleHistoryService")
public class PunchRuleHistoryService extends Service{

	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		String depart_id =row.getString("depart_id",null);
		sql = "select a.*, b.site_name as depart_name  from mcn_punch_rule_history a left join sm_site b on a.depart_id=b.site_no where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		if(depart_id == null || depart_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		sql +=" and a.depart_id='"+depart_id+"' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_punch_rule_history where 1=1 ";
		countSql +=" and org_id='"+org_id+"'";
		countSql +=" and depart_id='"+depart_id+"' ";
		countSql += restrictions;
//		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		System.out.println("sqllll---->>"+sql);
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
		int id = getTableSequence("mcn_punch_rule_history", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_rule_history", row);
	}

	public void insert(Row row) {
		int id = getTableSequence("mcn_punch_rule_history", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_rule_history", row);
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
		sql = "select * from mcn_punch_rule_history where id='" + id + "'";
		row = queryRow(sql);
		return row;
	}
	/**
	 * 查询当月是否已经有一条备份数据
	 * @param depart_id
	 * @param year
	 * @param month
	 * @return
	 */
	public Row findByDepartId(String depart_id,String year,String month) {
		Row row = new Row();
		sql = "select * from mcn_punch_rule_history where depart_id='" + depart_id + "' and bak_year='"+year+"' and bak_month='"+month+"'";
		System.out.println("sys====>>"+sql);
		row = queryRow(sql);
		return row;
	}
	public Row findByDepartId(String depart_id) {
		Row row = new Row();
		sql = "select * from mcn_punch_rule_history where depart_id='" + depart_id + "' ";
		System.out.println("sys====>>"+sql);
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update(Row row) {
		String ID=row.getString("ID","");
		row.put("ID", ID);
		System.out.println("row====>>"+row);
		updateWithoutFilterEmptyString("mcn_punch_rule_history", row, " id='" + ID + "'");
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
			sql = "delete from mcn_punch_rule_history where id in(" + id + ")";
			update(sql);
		}
	}
	
}