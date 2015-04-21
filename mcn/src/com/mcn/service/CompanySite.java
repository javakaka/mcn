package com.mcn.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.ExcelUtil;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.Row;

@Component("companySiteService")
public class CompanySite  extends Service{

	public void deteleSite(String org_id)
	{
		String sSql ="delete from sm_site where bureau_no='"+org_id+"'";
		update(sSql);
	}

	
	public OVO parseExcel(String id)
	{
		OVO ovo =new OVO();
		boolean b=false;
		//增量解析方式，每次解析时只解析最新上传的部门人员excel文件
		String sSql ="select b.FILE_PATH from file_attach_control a,file_attach_upload b "+
				" where a.DEAL_CODE='"+id+"' and a.DEAL_TYPE='company_user' and a.TYPE='user_excel' and a.SUB_TYPE='user_excel'"+
				" and a.CONTROL_ID=b.CONTROL_ID order by a.UPLOAD_DATE desc limit 0,1";
		Row row =queryRow(sSql);
		if(row ==null)
			return new OVO(-1,"导入数据文件不存在，请先上传数据文件","");
		String filePath =row.getString("file_path",null);
		if(filePath == null || filePath.replace(" ", "").length() == 0)
		{
			return new OVO(-1,"文件已丢失，请重新上传数据文件","");
		}
		List<Map<String,Object>> list =null;
		try {
			list = ExcelUtil.parseExcel(filePath);
			if(list == null || list.size()<2)
			{
				ovo =new OVO(-1,"文件格式错误，请参考文件模版","");
			}
			else
			{
				ovo =new OVO(0,"解析成功","");
				try {
					ovo.set("sheet_list", list);
				} catch (JException e) {
					e.printStackTrace();
					ovo =new OVO(-1,"设置数据出错","");
				}
			}
//			parseExcelData(list, id);
			b=true;
		} catch (FileNotFoundException e) {
			ovo =new OVO(-1,"文件路径错误，请参考文件模版","");
		} catch (IOException e) {
			ovo =new OVO(-1,"文件解析出错，请参考文件模版","");
		}
		return ovo;
	}
	
	/**
	 * 解析excel数据
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean parseExcelData(List<Map<String,Object>> list ,String org_id)
	{
		boolean b=false;
		if(list == null || list.size()==0)
		{
			return b;
		}
		Map<String , Object>map =null;
		List<Object> sheetData=null;
		List<Object> rowData =null;
		DataSet tempDepartDs =new DataSet();
		Row tempRow =null;
		map =list.get(0);
		sheetData =(List<Object>)map.get("data");
		String dname=null;//
		String upname=null;
		//部门数据
		//第一行是标题,从第二行开始解析
		for(int i=1; i<sheetData.size(); i++)
		{
			rowData = (List<Object>)sheetData.get(i);
			tempRow =new Row();
			dname =(String)rowData.get(0);
			upname=(String)rowData.get(1);
			tempRow.put("name", dname);
			tempRow.put("upname", upname);
			tempRow.put("org_id", org_id);
			tempDepartDs.add(tempRow);
		}
		System.out.println("通过2"+tempDepartDs);
		System.out.println("通过3"+tempDepartDs.size());
		insertDepartment(tempDepartDs);
		System.out.println("通过4");
		//人员数据
		DataSet tempUserDs =new DataSet();
		map =list.get(1);
		sheetData =(List<Object>)map.get("data");
		for(int i=1; i<sheetData.size(); i++)
		{
			rowData = (List<Object>)sheetData.get(i);
			tempRow =new Row();
			String name =(String)rowData.get(0);
			String password=(String)rowData.get(1);
			String depart_name=(String)rowData.get(2);
			String telephone=(String)rowData.get(3);
			String phone=(String)rowData.get(4);
			String email=(String)rowData.get(5);
			String sex=(String)rowData.get(6);
			String pos_name=(String)rowData.get(7);
			String is_manager=(String)rowData.get(8);
			String remark=(String)rowData.get(9);
			String depart_id =null;
			if(depart_name == null || depart_name.replace(" ", "").length() == 0)
			{
				depart_id ="";
			}
			else
			{
				depart_id =	findDepartmentId(depart_name,tempDepartDs);
			}
			if(StringUtils.isEmptyOrNull(phone))
			{
				phone ="";
			}
			if(StringUtils.isEmptyOrNull(email))
			{
				email ="";
			}
			String user_id =String.valueOf(getTableSequence("mcn_users", "id", 1));
			String username= "100"+user_id;
			tempRow.put("id", user_id);
			tempRow.put("org_id", org_id);
			tempRow.put("name", name==null?"":name);
			if(StringUtils.isEmptyOrNull(password))
			{
				password ="123456";
			}
			tempRow.put("password", password);
			tempRow.put("username", username);
			tempRow.put("depart_id", depart_id);
			tempRow.put("telephone",telephone ==null?"":telephone);
			tempRow.put("phone",phone);
			tempRow.put("email",email);
			tempRow.put("sex", sex==null?"":sex);
			tempRow.put("position", pos_name==null?"":pos_name);
			tempRow.put("manager_id", is_manager==null?"否":is_manager);
			tempRow.put("remark", remark==null?"":remark);
			System.out.println("tempRow="+tempRow);
			insert("mcn_users",tempRow);
			//保存到sm_staff 
			Row staffRow =new Row();
			String staff_no =String.valueOf(getTableSequence("sm_staff", "staff_no", 10000));
			staffRow.put("staff_no", staff_no);
			staffRow.put("bureau_no", org_id);
			staffRow.put("site_no", depart_id);
			staffRow.put("staff_name", username);
			staffRow.put("password", Md5Util.Md5(password));
			staffRow.put("real_name", username);
			insert("sm_staff",staffRow);
		}
		return b;
	}
	
	/**
	 * 获取sheet数据行数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getExcelSheetRowNum(Map<String , Object>map)
	{
		int num =0;
		//人员数据
		List<Object> sheetData=null;
		sheetData =(List<Object>)map.get("data");
		num =sheetData.size();
		return num;
	}
	
	@SuppressWarnings("null")
	public boolean insertDepartment(DataSet ds)
	{
		System.out.println("dataset"+ds.size());
		boolean result =true;
		if(ds == null || ds.size() ==0)
		{
			return result;
		}
		//top department
		for(int i=0; i< ds.size(); i++)
		{
			
			Row temp =(Row)ds.get(i);
			String dname =temp.getString("name",null);
			String upname =temp.getString("upname",null);
			String org_id=temp.getString("org_id",null);
			Assert.notNull(org_id);
			System.out.println("dname"+dname);
			if(dname == null || dname.replace(" ", "").length() == 0)
			{
				System.out.println("dname2"+dname);
				result =false;
				break;
			}
			System.out.println("upname"+upname);
			if(upname == null || upname.replace(" ", "").length() == 0)
			{
				System.out.println("uoname2"+upname);
//				String site_no =String.valueOf(getTableSequence("sm_site", "site_no", 1));
				String site_no =String.valueOf(getTableSequenceOfStringToInteger("sm_site", "site_no", 1));
				Row siteRow =new Row();
				siteRow.put("site_no",site_no );
				siteRow.put("bureau_no",org_id );
				siteRow.put("site_name",dname );
				System.out.println("site=="+siteRow);
				insert("sm_site", siteRow);
				temp.put("site_no", site_no);
				ds.set(i, temp);
				insertChildDepartment(dname,site_no,ds);
			}
		}
		return result;
	}
	
	public void insertChildDepartment(String fname,String fsite_no, DataSet ds)
	{
		if (ds == null)
			return;
		for(int i=0; i< ds.size(); i++)
		{
			
			Row temp =(Row)ds.get(i);
			String dname =temp.getString("name",null);
			String upname =temp.getString("upname",null);
			String org_id=temp.getString("org_id",null);
			Assert.notNull(org_id);
			if(dname == null || dname.replace(" ", "").length() == 0)
			{
				break;
			}
			if(upname != null && upname.replace(" ", "").length() >  0 && upname.equals(fname))
			{
				String site_no =String.valueOf(getTableSequenceOfStringToInteger("sm_site", "site_no", 1));
				Row siteRow =new Row();
				siteRow.put("site_no",site_no );
				siteRow.put("bureau_no",org_id );
				siteRow.put("site_name",dname );
				siteRow.put("up_site_no",fsite_no );
				insert("sm_site", siteRow);
				temp.put("site_no", site_no);
				ds.set(i, temp);
				insertChildDepartment(dname,site_no,ds);
			}
		}
	}
	
	//查找人员所属部门的编号
	public String findDepartmentId(String dname,DataSet ds)
	{
		String site_no =null;
		if(ds == null || ds.size()== 0)
			return site_no;
		for(int i =0; i< ds.size(); i++)
		{
			Row temp =(Row)ds.get(i);
			String name =temp.getString("name",null);
			if(dname.equals(name))
			{
				site_no =temp.getString("site_no",null);
				break;
			}
		}
		return site_no;
	}
	
	public Row findById(String id)
	{
		Row row =new Row();
		String sql ="select * from sm_site where site_no='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public boolean isDepartStoped(String id)
	{
		boolean bool =false;
		String sql ="select state from sm_site where site_no='"+id+"' ";
		String state =queryField(sql);
		if(StringUtils.isEmptyOrNull(state))
		{
			state ="0";
		}
		if(state.equals("0"))
		{
			bool =true;
		}
		return bool;
	}
}