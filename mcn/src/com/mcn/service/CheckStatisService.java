package com.mcn.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("checkStatisService")
public class CheckStatisService extends Service{
	//查询公司总出勤
	@SuppressWarnings("null")
	public Row checkStatisCompany(String org_id,String time,String site_nos) throws ParseException {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = null;
		Row row = new Row();
		String sSql ="select id from mcn_users where 1=1 ";
		if(! StringUtils.isEmptyOrNull(site_nos))
		{
			sSql +=" and depart_id in ("+site_nos+") ";
		}
		dataSet  = queryDataSet(sSql);
		double allFunchDay = 0;//出勤总天数
		double allLeaveDay = 0;//假期总记录
		double allAddDay = 0; //加班总记录
		/*
		for(int i=0;i<dataSet.size();i++){
			Row r2 = new Row();
			r2=(Row) dataSet.get(i);
			allFunchDay += getPunchDay(org_id,r2.getString("id"),year,month);
			allLeaveDay += getLeaveDay(org_id,r2.getString("id"),year,month);
			allAddDay += getAddLeaveDay(org_id,r2.getString("id"),year,month);
		}
		*/
		String sql = "SELECT SUM(day_status) as all_day from mcn_punch_log "
				+ "WHERE org_id='"+org_id+"' and punch_time like '"+year+"_"+month+"%' ";
		if(! StringUtils.isEmptyOrNull(site_nos))
		{
			sql +=" and user_id in ( select id from mcn_users where depart_id in ("+site_nos+") ) ";
		}
		String all_day2 = queryField(sql);
		if(all_day2 != null){
			allFunchDay = Double.parseDouble(all_day2)/2;
		}
		
		String sql2 = "select sum(sum_day) from mcn_leave_log "
				+ "WHERE org_id='"+org_id+"' and status='2' and leave_type in(1,2,3,5,6,7) "
						+ "and start_date like '"+year+"_"+month+"%' ";
		if(! StringUtils.isEmptyOrNull(site_nos))
		{
			sql2 +=" and user_id in ( select id from mcn_users where depart_id in ("+site_nos+") ) ";
		}
		String allLeaveDay2 = queryField(sql2);
		if(allLeaveDay2 != null){
			allLeaveDay = Double.parseDouble(allLeaveDay2);
		}
		String sql3 = "select sum(sum_day) from mcn_leave_log "
				+ "WHERE org_id='"+org_id+"' and status='2' "
				+ "and leave_type in(4) and start_date like '"+year+"_"+month+"%' ";
		if(! StringUtils.isEmptyOrNull(site_nos))
		{
			sql3 +=" and user_id in ( select id from mcn_users where depart_id in ("+site_nos+") ) ";
		}
		String allAddDay2 = queryField(sql3);
		if(allAddDay2 != null){
			allAddDay = Double.parseDouble(allAddDay2);
		}
		
		System.out.println("出勤总天数=="+allFunchDay);
		System.out.println("假期总天数=="+allLeaveDay);
		System.out.println("加班总天数=="+allAddDay);
		sql = "select BUREAU_NAME as company_name from sm_bureau WHERE BUREAU_NO='"+org_id+"' ";
		String company_name = queryField(sql);
		row.put("company_name", company_name);
		row.put("work_day", allFunchDay);
		row.put("leave_day", allLeaveDay);
		row.put("add_day", allAddDay);
		row.put("all_day", allAddDay+allLeaveDay+allFunchDay);
		System.out.println("row=="+row.toString());
		return row;
	}
	
	//查询所有部门总出勤列表
	public DataSet checkStatisDepart(String org_id,String time) throws ParseException {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		DataSet data = null;
		DataSet data2 = null;
		sql = "select SITE_NO as dipart_id,SITE_NAME AS dipart_name from sm_site where BUREAU_NO='"+org_id+"'";
		data  = queryDataSet(sql);
		for(int i=0;i<data.size();i++) {
			Row row = new Row();
			row = (Row) data.get(i);
			String depart_id = row.getString("dipart_id");
			double allFunchDay = 0;//出勤总天数
			double allLeaveDay = 0;//假期总记录
			double allAddDay = 0; //加班总记录
			
			String sql = "SELECT SUM(day_status) as all_day from mcn_punch_log WHERE org_id='"+org_id+"' and user_id in(select id from mcn_users WHERE depart_id='"+depart_id+"') and punch_time like '"+year+"_"+month+"%'";
			String all_day2 = queryField(sql);
			if(all_day2 != null){
				allFunchDay = Double.parseDouble(all_day2)/2;
			}
			
			String sql2 = "select sum(sum_day) from mcn_leave_log WHERE org_id='"+org_id+"' and user_id in(select id from mcn_users WHERE depart_id='"+depart_id+"') and status='2' and leave_type in(1,2,3,5,6,7) and start_date like '"+year+"_"+month+"%'";
			String allLeaveDay2 = queryField(sql2);
			if(allLeaveDay2 != null){
				allLeaveDay = Double.parseDouble(allLeaveDay2);
			}
			
			String sql3 = "select sum(sum_day) from mcn_leave_log WHERE org_id='"+org_id+"' and user_id in(select id from mcn_users WHERE depart_id='"+depart_id+"') and status='2' and leave_type in(4) and start_date like '"+year+"_"+month+"%'";
			String allAddDay2 = queryField(sql3);
			if(allAddDay2 != null){
				allAddDay = Double.parseDouble(allAddDay2);
			}
			
			/*
			sql = "select id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"'";
			data2 = queryDataSet(sql);
			double allFunchDay = 0;//出勤总天数
			double allLeaveDay = 0;//假期总记录
			double allAddDay = 0; //加班总记录
			for(int j=0;j<data2.size();j++){
				Row row2 = new Row();
				row2 = (Row) data2.get(j);
				String user_id = row2.getString("id");
				allFunchDay += getPunchDay(org_id,user_id,year,month);
				allLeaveDay += getLeaveDay(org_id,user_id,year,month);
				allAddDay += getAddLeaveDay(org_id,user_id,year,month);
			}
			*/
			System.out.println("出勤总天数=="+allFunchDay);
			System.out.println("假期总天数=="+allLeaveDay);
			System.out.println("加班总天数=="+allAddDay);
			row.put("work_day", allFunchDay);
			row.put("leave_day", allLeaveDay);
			row.put("add_day", allAddDay);
			row.put("all_day", allAddDay+allLeaveDay+allFunchDay);
			dataSet.add(row);
		}
		System.out.println("dataSet===="+dataSet.toString());
		return dataSet;
	}
	
	//根据部门ID查询部门某月总出勤
	@SuppressWarnings("unchecked")
	public DataSet checkStatisPerson(String org_id , String depart_id , String time) throws ParseException {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		
		DataSet data2 = null;
		sql = "select id,name as user_name from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"'";
		data2 = queryDataSet(sql);
		for(int i=0;i<data2.size();i++ ) {
			Row row = new Row();
			row = (Row) data2.get(i);
			String user_id=row.getString("id");

			double allFunchDay = 0;//出勤总天数
			double allLeaveDay = 0;//假期总记录
			double allAddDay = 0; //加班总记录
			
			String sql = "SELECT SUM(day_status) as all_day from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_time like '"+year+"_"+month+"%'";
			String all_day2 = queryField(sql);
			if(all_day2 != null){
				allFunchDay = Double.parseDouble(all_day2)/2;
			}
			
			String sql2 = "select sum(sum_day) from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and status='2' and leave_type in(1,2,3,5,6,7) and start_date like '"+year+"_"+month+"%'";
			String allLeaveDay2 = queryField(sql2);
			if(allLeaveDay2 != null){
				allLeaveDay = Double.parseDouble(allLeaveDay2);
			}
			
			String sql3 = "select sum(sum_day) from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and status='2' and leave_type in(4) and start_date like '"+year+"_"+month+"%'";
			String allAddDay2 = queryField(sql3);
			if(allAddDay2 != null){
				allAddDay = Double.parseDouble(allAddDay2);
			}
			row.put("work_day", allFunchDay);
			row.put("leave_day", allLeaveDay);
			row.put("add_day", allAddDay);
			row.put("all_day", allAddDay+allLeaveDay+allFunchDay);
			dataSet.add(row);
		}
		System.out.println("dataSet=="+dataSet.toString());
		return dataSet;
	}
	
	//根据员工ID查询打卡详细信息
	public DataSet checkStatisPersonDetail(String org_id,String user_id,String time) {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		sql = "select img_path,punch_type,punch_time,place_name from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and "+
			  "punch_type in (1,2,3,4,5,6) and punch_time like '"+year+"_"+month+"%'";
		dataSet = queryDataSet(sql);
		System.out.println("dataSet=="+dataSet.toString());
		return dataSet;
	}
	
	//根据员工ID查询签到详细信息
	public DataSet checkStatisPersonDetail2(String org_id,String user_id,String time) {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		sql = "select img_path,punch_type,punch_time,place_name from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and "+
			  "punch_type in (7) and punch_time like '"+year+"-"+month+"%' order by punch_time desc ";
		dataSet = queryDataSet(sql);
		System.out.println("dataSet=="+dataSet.toString());
		return dataSet;
	}
	
	//根据员工ID查询员工某月出勤天数
	public double getPunchDay(String org_id,String user_id,String year,String month) {
		double sum = 0;
		/*
		for(int j = 0;j <= 31;j++){
			String sql = null;;
			if(j<9){
				  sql = "select COUNT(id) from mcn_punch_log where org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='1' and punch_time like '"+year+"%0"+month+"%"+(j+1)+"%'";
			}else{
				  sql = "select COUNT(id) from mcn_punch_log where org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='1' and punch_time like '"+year+"%"+month+"%"+(j+1)+"%'";	
			}
		String s = queryField(sql);
		if(s == null || s.equals("0")){
			//System.out.println("通过"+j);
			continue;
		}else{
			//System.out.println("失败"+j);
			if(j<9){
			sql = "select COUNT(id) from mcn_punch_log where org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='4' and punch_time like '"+year+"%"+month+"%0"+(j+1)+"%'";
			}else{
			sql = "select COUNT(id) from mcn_punch_log where org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='4' and punch_time like '"+year+"%"+month+"%"+(j+1)+"%'";
			}
			String s2 = queryField(sql);
			if(s2 == null || s2.equals("0")){
				continue;
			}else{
				sum++;
			}
		}
		}
		*/
		String sql = "SELECT SUM(day_status) as all_day from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_time like '"+year+"_"+month+"%'";
		String all_day2 = queryField(sql);
		if(all_day2 != null){
			sum = Double.parseDouble(all_day2)/2;
		}
		return sum;
	}
	
	//根据员工ID查询员工某月请假记录天数
	public double getLeaveDay(String org_id,String user_id,String year,String month) throws ParseException {
		java.util.Date beginDate; 
		java.util.Date endDate; 
		double sum = 0;
		sql = "select sum_day from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and status='2' and leave_type in(1,2,3,5,7) and start_date like '"+year+"_"+month+"%'";
		DataSet dataSet = queryDataSet(sql);
		for(int i=0;i<dataSet.size();i++){
			Row row = new Row();
			row = (Row) dataSet.get(i);
			/*
			String beginDateStr = row.getString("start_date");;
			String endDateStr = row.getString("end_date");;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			 beginDate = format.parse(beginDateStr); 
			 endDate= format.parse(endDateStr);
			 int day=(int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000)); 
			 //System.out.println("相隔的天数="+day); 
			  * *
			  */
			double day = Double.parseDouble(row.getString("sum_day"));
			System.out.println("========day=========="+day);
			 sum += day;
		}
		System.out.println("========day====sum=========="+sum);
		return sum;
	}
	
	//根据员工ID查询员工某月加班记录天数
	public double getAddLeaveDay(String org_id,String user_id,String year,String month) throws ParseException {
		java.util.Date beginDate; 
		java.util.Date endDate; 
		double sum = 0;
		sql = "select sum_day from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and status='2' and leave_type in(4) and start_date like '"+year+"_"+month+"%'";
		DataSet dataSet = queryDataSet(sql);
		for(int i=0;i<dataSet.size();i++){
			Row row = new Row();
			row = (Row) dataSet.get(i);
			/*
			String beginDateStr = row.getString("start_date");;
			String endDateStr = row.getString("end_date");;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			 beginDate = format.parse(beginDateStr); 
			 endDate= format.parse(endDateStr);
			 int day=(int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000)); 
			// System.out.println("相隔的天数="+day); 
			 */
			 double day = Double.parseDouble(row.getString("sum_day"));
			 sum += day;
		}
		
		return sum;
	}
	
	//请假入口查询
	@SuppressWarnings("unchecked")
	public DataSet userIdAllLeave(String org_id , String user_id) throws ParseException {
		java.util.Date beginDate; 
		java.util.Date endDate; 
		DataSet dataSet = new DataSet();
		String sql = "select year,sick,exchange from mcn_leave_setting WHERE org_id='"+org_id+"'";
		String sql2 = "select * from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and leave_type='1'";
		String sql3 = "select * from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and leave_type='2'";
		String sql4 = "select * from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and leave_type='3'";
		Row row = queryRow(sql);
		String year = "20";
		String sick = "20";
		String exchange = "20";
		if(row != null){
			year = row.getString("year");
			sick= row.getString("sick");
			exchange = row.getString("exchange");
		}
		DataSet data2 = queryDataSet(sql2);
		DataSet data3 = queryDataSet(sql3);
		DataSet data4 = queryDataSet(sql4);
		double year_day = 0; 	//申请年假天数
		double sick_day = 0;	//申请病假天数
		double exchange_day = 0;	//申请调休天数
		for(int i=0;i<data2.size();i++) {
			Row row2 = new Row();
			row2 = (Row) data2.get(i);
			/*
			String beginDateStr = row2.getString("start_date");;
			String endDateStr = row2.getString("end_date");;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			 beginDate = format.parse(beginDateStr); 
			 endDate= format.parse(endDateStr);
			 Long day=((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
			 */
			 double day = Double.parseDouble(row2.getString("sum_day"));
			 year_day+= day;
		}
		for(int i=0;i<data3.size();i++) {
			Row row3 = new Row();
			row3 = (Row) data2.get(i);
			/*
			String beginDateStr = row3.getString("start_date");;
			String endDateStr = row3.getString("end_date");;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			 beginDate = format.parse(beginDateStr); 
			 endDate= format.parse(endDateStr);
			 Long day=((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
			 */
			 double day = Double.parseDouble(row3.getString("sum_day"));
			 sick_day += day;
				}
		for(int i=0;i<data4.size();i++) {
			Row row4 = new Row();
			row4 = (Row) data2.get(i);
			/*
			String beginDateStr = row4.getString("start_date");;
			String endDateStr = row4.getString("end_date");;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			 beginDate = format.parse(beginDateStr); 
			 endDate= format.parse(endDateStr);
			 Long day=((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
			 */
			double day = Double.parseDouble(row4.getString("sum_day"));
			 exchange_day += day;
		}
			Row row5 = new Row();
			row5.put("type_name", "年假");	//类别名称
			row5.put("all_day", year);	//总计年假天数
			String n_year_day =  String.valueOf((Double.parseDouble(year)-year_day));
			row5.put("no_day", n_year_day);	//未使用年假天数
			row5.put("y_day", year_day);	//已申请的天数
			dataSet.add(row5);
			Row row6 = new Row();
			row6.put("type_name", "病假");	//类别名称
			row6.put("all_day", sick);	//总计病假天数
			String n_sick_day =  String.valueOf((Double.parseDouble(sick)-sick_day));
			row6.put("no_day", n_sick_day);	//未使用病假天数
			row6.put("y_day", sick_day);	//已申请的天数
			dataSet.add(row6);
			Row row7 = new Row();
			row7.put("type_name", "调休");	//类别名称
			row7.put("all_day", exchange);	//总计调休天数
			String n_exchange_day =  String.valueOf((Double.parseDouble(exchange)-exchange_day));
			row7.put("no_day", n_exchange_day);	//未使用调休天数
			row7.put("y_day", exchange_day);	//已申请的天数
			dataSet.add(row7);
			System.out.println("dataSet="+dataSet);
			 
		return dataSet;
	}
	
	//企业看板查询
	@SuppressWarnings("unchecked")
	public DataSet companyModel(String org_id,String time,String site_nos) throws ParseException {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		String day = time.substring(8, 10);
		System.out.println("month=="+day);
		DataSet dataSet = new DataSet();
		DataSet data = null;
		String sql = "select SITE_NO as depart_id,SITE_NAME AS depart_name from sm_site "
				+ "where BUREAU_NO='"+org_id+"' ";
		if(StringUtils.isEmptyOrNull(site_nos))
		{
			sql +=" site_no in ("+site_nos+") ";
		}
		data  = queryDataSet(sql);
		for(int i=0;i<data.size();i++) {
			DataSet dataSet2 = new DataSet();
			Row row = new Row();
			row = (Row) data.get(i);
			String depart_id = row.getString("depart_id");
			sql = "select id,name as user_name from mcn_users "
					+ "WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"'";
			DataSet data2 = queryDataSet(sql);
			for(int j=0;j<data2.size();j++)
			{ 
				Row row2 = new Row();
				row2 = (Row) data2.get(j);
				String user_id = row2.getString("id");
				sql = "select `status`,sum_day,leave_type,reason,start_date,end_date,reason "
						+ "from mcn_leave_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' "
						+ "and `status`='2'  and ((start_date like '"+time+"%' or end_date like '"+time+"%') "
						+ "or (UNIX_TIMESTAMP('"+time+"%') between UNIX_TIMESTAMP(start_date) "
						+ "and UNIX_TIMESTAMP(end_date))) LIMIT 0,1";
					Row row4 = new Row();
					Row row3 = queryRow(sql);
					if(row3 != null)
					{
						row4.put("depart_id", row.getString("depart_id"));//部门ID
						row4.put("depart_name", row.getString("depart_name"));//部门名称
						row4.put("user_name", row2.getString("user_name"));//员工姓名
						row4.put("leave_day", row3.getString("sum_day"));//请假天数
						row4.put("leave_type", row3.getString("leave_type"));//请假类别
						row4.put("content", row3.getString("reason"));//请假事由
						dataSet2.add(row4);
					}
					else
					{
						row4.put("depart_id", row.getString("depart_id"));//部门ID
						row4.put("depart_name", row.getString("depart_name"));//部门名称
						row4.put("user_name", row2.getString("user_name"));//员工姓名
						row4.put("leave_day", "0");//请假天数
						row4.put("leave_type", "");//请假类别
						row4.put("content", "");//请假事由
						dataSet2.add(row4);
					}
			}
			System.out.println("dataSet2="+dataSet2);
			dataSet.add(dataSet2);
		}
		System.out.println("dataSet=通过"+dataSet);
		return dataSet;
	}
	//查询企业部门名称
	public DataSet companyModelName(String org_id,String site_nos) throws ParseException {
		DataSet data = null;
		String sql = "select SITE_NO as depart_id,SITE_NAME AS depart_name from sm_site "
				+ "where BUREAU_NO='"+org_id+"' ";
		if(! StringUtils.isEmptyOrNull(site_nos))
		{
			sql +=" and site_no in ("+site_nos+") ";
		}
		data  = queryDataSet(sql);
		return data;
	}
	
	//查询企业LOGO入口
	public Row queryCompany(String org_id) {
		String sql = "select BUREAU_NAME as company_name,AREA_CODE as img_url from sm_bureau where BUREAU_NO='"+org_id+"'";
		Row row = queryRow(sql);
		return row;
	}
}
