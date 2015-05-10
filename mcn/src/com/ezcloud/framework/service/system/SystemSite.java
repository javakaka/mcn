package com.ezcloud.framework.service.system;

import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

/**
 * 区域
 * @author JianBoTong
 *
 */
@Component("frameworkSystemSiteService")
public class SystemSite  extends Service{

	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage() 
	{
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		sql = "select a.site_no, a.site_name ,a.state ,a.up_site_no ,b.site_name as up_site_name from sm_site a left join sm_site b on a.up_site_no=b.site_no where 1=1 ";
		if (org_id != null && org_id.replace(" ", "").length() >0){
			sql +="  and a.bureau_no='"+org_id+"'";
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from sm_site where 1=1 ";
		if (org_id != null && org_id.replace(" ", "").length() >0){
			countSql +="  and bureau_no='"+org_id+"'";
		}
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
	
	public Page queryPageForTree() 
	{
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id");
		String up_id =row.getString("up_id");
		sql = "select a.bureau_no,a.site_no, a.site_name ,a.up_site_no ,a.rela_phone,a.notes,a.state,a.addr,b.site_name as up_site_name "
				+ "from sm_site a left join sm_site b on a.up_site_no=b.site_no where 1=1 ";
		if (org_id != null && org_id.replace(" ", "").length() >0)
		{
			sql +="  and a.bureau_no='"+org_id+"'";
		}
		if (up_id != null && up_id.replace(" ", "").length() >0 && !up_id.replace(" ", "").equals("0"))
		{
			sql +="  and a.up_site_no='"+up_id+"'";
		}
		System.out.println("===========>>"+sql);
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from sm_site where 1=1 ";
		if (org_id != null && org_id.replace(" ", "").length() >0){
			countSql +="  and bureau_no='"+org_id+"'";
		}
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
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public void save() throws Exception {
		Row row = new Row();
		String BUREAU_NAME=getRow().getString("BUREAU_NAME","");
		String UP_BUREAU_NO=getRow().getString("UP_BUREAU_NO","");
		String AREA_CODE=getRow().getString("AREA_CODE","");
		String LINKS=getRow().getString("LINKS","");
		String NOTES=getRow().getString("NOTES","");
		row.put("BUREAU_NAME", BUREAU_NAME);
		row.put("UP_BUREAU_NO", UP_BUREAU_NO);
		row.put("AREA_CODE", AREA_CODE);
		row.put("LINKS", LINKS);
		
		int BUREAU_NO = getTableSequence("sm_site", "bureau_no", 10000);
		row.put("BUREAU_NO", BUREAU_NO);
		
		//NOTES 字段为企业登陆 url,拼接token,token的值是bureau_no的AES加密字符串
		String token= AesUtil.encode(String.valueOf(BUREAU_NO));
		token =URLEncoder.encode(token);
		NOTES =NOTES +"?token="+token;
		row.put("NOTES", NOTES);
		insert("sm_site", row);
		this.getRow().put("BUREAU_NO", BUREAU_NO);
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
		sql = "select * from sm_site where site_no='" + id + "'";
		row = queryRow(sql);
		return row;
	}
	public Row find(String id) {
		Row row = new Row();
		sql = "select * from sm_site where site_no='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String BUREAU_NO=getRow().getString("BUREAU_NO","");
		String BUREAU_NAME=getRow().getString("BUREAU_NAME","");
		String UP_BUREAU_NO=getRow().getString("UP_BUREAU_NO","");
		String AREA_CODE=getRow().getString("AREA_CODE","");
		String LINKS=getRow().getString("LINKS","");
		String NOTES=getRow().getString("NOTES","");
		Row row = new Row();
		row.put("BUREAU_NAME", BUREAU_NAME);
		row.put("UP_BUREAU_NO", UP_BUREAU_NO);
		row.put("AREA_CODE", AREA_CODE);
		row.put("LINKS", LINKS);
		row.put("NOTES", NOTES);
		update("sm_site", row, "BUREAU_NO='" + BUREAU_NO + "'");
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
			sql = "delete from sm_site where bureau_no in(" + id + ")";
			update(sql);
			//delete staff_role
			sql ="delete from sm_staff_role where staff_no in (select staff_no from sm_staff where bureau_no in("+id+"))  ";
			update(sql);
			//delete staff
			sql ="delete from sm_staff where bureau_no in("+id+") ";
			update(sql);
			
			//delete mcn_org_modules
			sql ="delete from mcn_org_modules where org_id in ("+id+")";
			update(sql);
		}
	}
	
	
	public void deleteOrgSite(Long... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from sm_site where site_no in(" + id + ")";
			update(sql);
//			//delete staff_role
//			sql ="delete from sm_staff_role where staff_no in (select staff_no from sm_staff where bureau_no in("+id+"))  ";
//			update(sql);
//			//delete staff
//			sql ="delete from sm_staff where bureau_no in("+id+") ";
//			update(sql);
//			
//			//delete mcn_org_modules
//			sql ="delete from mcn_org_modules where org_id in ("+id+")";
//			update(sql);
		}
	}
	
	public DataSet queryOrgSite(String org_id)
	{
		DataSet ds=new DataSet();
		sql ="select * from sm_site  where bureau_no='"+org_id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet queryOrgSiteWhereNotRuled(String org_id)
	{
		DataSet ds=new DataSet();
		sql ="select * from sm_site  where bureau_no='"+org_id+"' and site_no not in (select depart_id from mcn_punch_rule where org_id='"+org_id+"' );";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet queryOrgSiteAsFun(String org_id)
	{
		DataSet ds=new DataSet();
		sql ="select site_no as fun_id,up_site_no as up_fun_id ,site_name as fun_name from sm_site  where bureau_no='"+org_id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	
	//将所有功能目录按等级从上到下排序
	@SuppressWarnings("unchecked")
	public DataSet getSortedFuns(DataSet ds)
	{
		DataSet sortDs =new DataSet();
		if(ds == null){
			return sortDs;
		}
		String f_id=null;
		String p_id=null;
		Row row =null;
		for(int i=0;i<ds.size();i++)
		{
			row =(Row)ds.get(i);
			f_id =row.getString("fun_id",null);
			p_id =row.getString("up_fun_id",null);
			if(p_id == null || p_id.replace(" ","").length()==0)
			{
				sortDs.add(row);
				if(isHaveChildNodes(f_id, ds))
				{
					pushSortChildren(f_id, ds, sortDs);
				}
			}
		}
		return sortDs;
	}

	//判断某个节点有子节点
	public static boolean isHaveChildNodes(String fun_id,DataSet ds)
	{
		boolean bool=false;
		if(ds == null || ds.size() ==0 )
			return bool;
		String p_id=null;
		Row funRow=null;
		for(int i=0; i<ds.size(); i++)
		{
			funRow=(Row)ds.get(i);
			p_id =funRow.getString("up_fun_id",null);
			if(p_id != null && p_id.equalsIgnoreCase(fun_id)){
				bool =true;
				break;
			}
		}
		return bool;
	}
	
	@SuppressWarnings("unchecked")
	public void pushSortChildren(String fun_id,DataSet ds,DataSet sortDs)
	{
		DataSet childDs =getChildrenNodes(fun_id, ds);
		String f_id=null;
		Row row =null;
		if(childDs != null && childDs.size() >0)
		{
			for(int i=0;i<childDs.size();i++)
			{
				row =(Row)childDs.get(i);
				sortDs.add(row);
				f_id =row.getString("fun_id",null);
				if(isHaveChildNodes(f_id, ds))
				{
					pushSortChildren(f_id, ds, sortDs);
				}
			}
		}
	}
	
	//取子节点列表
	@SuppressWarnings("unchecked")
	public static DataSet getChildrenNodes(String fun_id, DataSet ds)
	{
		DataSet childrenDataSet =new DataSet();
		if(ds == null || ds.size() ==0)
			return childrenDataSet;
		String p_id=null;
//			String f_id =null;
		Row funRow=null;
		for(int i=0; i< ds.size(); i++)
		{
			funRow=(Row)ds.get(i);
			p_id =funRow.getString("up_fun_id",null);
			if(p_id != null && p_id.equals(fun_id))
			{
				childrenDataSet.add(funRow);
			}
		}
		return childrenDataSet;
	}
	
	
	public String  isHaveUpSite(String site_no)
	{
		Row row =null;
		row =this.find(site_no);
		String up_id =row.getString("up_site_no","");
		if(StringUtils.isEmptyOrNull(up_id))
		{
			up_id =null;
		}
		return up_id;
	}
	
	public String queryBureauNo(String id)
	{
		String bureau_no =null;
		sql ="select bureau_no from sm_site  where site_no='"+id+"'";
		bureau_no =queryField(sql);
		return bureau_no;
	}
	
	public void insertOrgSite(Row row)
	{
		String id =String.valueOf(getTableSequenceOfVarcharField("sm_site", "site_no", 1));
		row.put("site_no", id);
		insert("sm_site",row);
	}
	
	public void saveOrgSite(Row row)
	{
		String site_no =row.getString("site_no",null);
		update("sm_site",row, " site_no='"+site_no+"'");
	}
	
	/**
	 * stop child site  ,loop
	 * @param row
	 */
	public void stopChildrenOrgSite(String site_no)
	{
		DataSet ds =queryAllSiteBySiteNo(site_no);
		if(ds != null && ds.size()>0)
		{
			for(int i=0;i<ds.size();i++)
			{
				Row temp =(Row)ds.get(i);
				String s_no =temp.getString("site_no","");
				String sql ="update sm_site set state='0' where site_no='"+s_no+"' ";
				update(sql);
			}
		}
		
	}
	/**
	 * 根据指定的部门编号查询部门以及其递归子部门
	 * @param site_no
	 */
	public DataSet queryAllSiteBySiteNo(String site_no)
	{
		DataSet ds =queryAllSites();
		DataSet filterDs =filterSitesBySiteNo(site_no,ds);
		
		return filterDs;
	}
	
	public DataSet queryAllSites()
	{
		DataSet ds =new DataSet();
		String sql ="select * from sm_site ";
		ds =queryDataSet(sql);
		
		return ds;
	}
	
	public DataSet filterSitesBySiteNo(String site_no,DataSet all_ds)
	{
		DataSet filter_ds =new DataSet();
		Row temp =null;
		if(all_ds != null && all_ds.size() > 0)
		{
			for(int i=0;i<all_ds.size();i++)
			{
				temp =(Row)all_ds.get(i);
				String t_site_no =temp.getString("site_no","");
				if(!StringUtils.isEmptyOrNull(t_site_no) && site_no.equals(t_site_no))
				{
					filter_ds.add(temp);
					break;
				}
				else
				{
					continue;
				}
			}
			//children sites
			getChildSite(site_no,all_ds,filter_ds);
		}
		return filter_ds;
	}
	
	public void getChildSite(String site_no,DataSet all_ds,DataSet filter_ds)
	{
		Row row =null;
		Row temp =null;
		if(all_ds != null && all_ds.size() > 0)
		{
			for(int i=0;i<all_ds.size();i++)
			{
				temp =(Row)all_ds.get(i);
				String up_site_no =temp.getString("up_site_no",null);
				if(!StringUtils.isEmptyOrNull(up_site_no))
				{
					if(up_site_no.equals(site_no))
					{
						filter_ds.add(temp);
						String next_site_no=temp.getString("site_no","");
						getChildSite(next_site_no,all_ds,filter_ds);
					}
				}
			}
		}
	}
	//根据给定的部门编号，取出部门名称，如：1,2,3
	public String queryOrgSiteNameByIds(String ids)
	{
		String sql ="select * from sm_site where site_no in ("+ids+")";
		DataSet ds =queryDataSet(sql);
		String names ="";
		String site_name ="";
		if(ds != null)
		{
			for(int i=0;i<ds.size();i++)
			{
				Row row =(Row)ds.get(i);
				site_name =row.getString("site_name","");
				if(names.length()>0)
				{
					if(! StringUtils.isEmptyOrNull(site_name))
					{
						names +=","+site_name;
					}
				}
				else
				{
					if(! StringUtils.isEmptyOrNull(site_name))
					{
						names +=site_name;
					}
				}
			}
		}
		return names;
	}
}
