package com.ezcloud.framework.service.system;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

/**
 * 全国省份城市信息维护
 * 
 * @author JianBoTong
 */
@Component("frameworkSystemZoneService")
public class SystemZoneService extends Service {

	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from sm_window where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from sm_window where 1=1 ";
		countSql += restrictions;
		countSql += orders;
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
		String WIN_TARGET = getRow().getString("WIN_TARGET", null);
		String WIN_DESC = getRow().getString("WIN_DESC", null);
		row.put("WIN_TARGET", WIN_TARGET);
		row.put("WIN_DESC", WIN_DESC);
		int WIN_ID = getTableSequence("sm_window", "win_id", 1);
		row.put("WIN_ID", WIN_ID);
		insert("sm_window", row);
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
		sql = "select * from sm_window where win_id='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String WIN_ID = getRow().getString("WIN_ID", null);
		String WIN_TARGET = getRow().getString("WIN_TARGET", null);
		String WIN_DESC = getRow().getString("WIN_DESC", null);
		Row row = new Row();
		row.put("WIN_ID", WIN_ID);
		row.put("WIN_TARGET", WIN_TARGET);
		row.put("WIN_DESC", WIN_DESC);
		update("sm_window", row, "WIN_ID='" + WIN_ID + "'");
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
			sql = "delete from sm_window where win_id in(" + id + ")";
			update(sql);
		}
	}

	/**
	 * 查询所有的城市和城市所辖区域
	 */
	public Row queryAllCityAndZone()
	{
		Row row =new Row();
		//city 
		sql ="select a.* from common_city a left join common_province b on a.provinceId =b.id and b.state='1' where a.state='1'";
		DataSet cityDataSet =queryDataSet(sql);
		//zone
		sql ="select a.* from common_city_zone a left join common_city b on a.cityId=b.id and b.state='1' where a.state='1'";
		DataSet zoneDataSet =queryDataSet(sql);
		row.put("city", cityDataSet);
		row.put("zone", zoneDataSet);
		return row;
	}
	
	/**
	 * 查询指定城市
	 */
	public Row findCityById(String id)
	{
		Row row =new Row();
		sql ="select * from common_city where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	/**
	 * 查询指定辖区
	 */
	public Row findZoneById(String id)
	{
		Row row =new Row();
		sql ="select * from common_city_zone where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	/**
	 * 根据城市id查询城市的 辖区
	 */
	public DataSet findZoneByCityId(String id)
	{
		DataSet ds =new DataSet();
		String sql ="select * from common_city_zone where cityId='"+id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 查询所有已开放的省份
	 */
	public DataSet findAllOpenedProvince()
	{
		DataSet ds =new DataSet();
		String sql ="select id,name from common_province where state='1' ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 查询所有已开放的城市
	 */
	public DataSet findAllOpenedCities()
	{
		DataSet ds =new DataSet();
		String sql ="select id,name,provinceId from common_city where state='1' ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 查询所有已开放的城市的区域
	 */
	public DataSet findAllOpenedZone()
	{
		DataSet ds =new DataSet();
		String sql ="select id,name,cityId from common_city_zone where state='1' ";
		ds =queryDataSet(sql);
		return ds;
	}

}