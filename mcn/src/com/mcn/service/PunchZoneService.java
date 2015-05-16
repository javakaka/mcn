package com.mcn.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.GeographyUtil;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("mcnPunchZoneService")
public class PunchZoneService extends Service{

	/**
	 * 分页查询企业的区域设置
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws ParseException 
	 */
	public Page queryPageForCompany(Pageable pageable,String org_id) {
		Page page =null;
		String sql = "select * from mcn_punch_map where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and org_id='"+org_id+"'  ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_punch_map where 1=1 ";
		countSql +=" and org_id='"+org_id+"' ";
		countSql += restrictions;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) 
		{
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		DataSet dataSet = queryDataSet(sql);
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
		int id = getTableSequence("mcn_punch_map", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_map", row);
	}

	/**
	 * 根据id查找
	 * 
	 * @return Row
	 * @throws
	 */
	public Row find(String id) {
		Row row = new Row();
		sql = "select * from mcn_punch_map where id='" + id + "'";
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
		System.out.println("row====>>"+row);
		update("mcn_punch_map", row, " id='" + ID + "'");
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
			sql = "delete from mcn_punch_map where id in(" + id + ")";
			update(sql);
		}
	}
	
	/**
	 * 根据企业编号查询该企业的打卡区域检测位置列表
	 * @param org_id
	 * @return
	 */
	public DataSet queryOrgPunchMap(String org_id)
	{
		DataSet ds =new DataSet();
		String sql ="select * from mcn_punch_map where org_id='"+org_id+"' ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 根据企业编号查询该企业的启用状态的打卡区域检测位置列表
	 * @param org_id
	 * @return
	 */
	public DataSet queryOrgValidPunchMap(String org_id)
	{
		DataSet ds =new DataSet();
		String sql ="select * from mcn_punch_map where org_id='"+org_id+"' and state='1' ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public boolean isNeedZoneCheck(String org_id)
	{
		boolean bool =false;
		String sql ="select count(*) from mcn_punch_map where org_id='"+org_id+"' and state='1' ";
		int num =Integer.parseInt(queryField(sql));
		if(num>0)
		{
			bool =true;
		}
		return bool;
	}
	
	/**
	 * 
	 * @param org_id 企业编号
	 * @param longitude 被检测点的经度
	 * @param latitude 被检测点的纬度
	 * @return
	 */
	public boolean isValid(String org_id,double longitude,double latitude)
	{
		boolean bool =true;
		String sql ="select * from mcn_punch_map where org_id='"+org_id+"' and state='1' ";
//		double center_longitude =0;
//		double center_latitude =0;
		DataSet ds =queryDataSet(sql);
		double minus =0;
		for(int i=0;i<ds.size();i++)
		{
			Row temp =(Row)ds.get(i);
			//圆心的经度、纬度、半径
			double center_longitude =Double.parseDouble(temp.getString("longitude","0"));
			double center_latitude =Double.parseDouble(temp.getString("latitude","0"));
			double radius =Double.parseDouble(temp.getString("radius","0"));
			if(center_longitude >0 && center_latitude>0 && radius>0)
			{
				minus =GeographyUtil.getLongDistance(center_longitude, center_latitude, longitude, latitude);
				if(minus <= radius)
				{
					bool =true;
					break;
				}
			}
			
		}
		return bool;
	}
	
	
	
}