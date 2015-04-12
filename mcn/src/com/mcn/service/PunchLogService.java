package com.mcn.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnPunchLogService")
public class PunchLogService extends Service{

	/**
	 * 分页查询打卡汇总记录
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws ParseException 
	 */
	public DataSet queryPageForCompanyDepart(String org_id,String time) throws ParseException {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		DataSet dataSet2 = null;
		sql ="SELECT SITE_NO AS depart_id,SITE_NAME as depart_name from sm_site WHERE BUREAU_NO='"+org_id+"'";
		dataSet2 = queryDataSet(sql);
		if(dataSet2.size() > 0) {
		for(int i=0;i<dataSet2.size();i++) {
			Row row0 = new Row();
			row0 = (Row) dataSet2.get(i);
			String depart_id = row0.getString("depart_id");
			String depart_name = row0.getString("depart_name");
			String sq = "select id as user_id,name as manager_name from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"'";
			DataSet dataSet3 = queryDataSet(sq);
			if(dataSet3.size() > 0) {
				for(int j=0;j<dataSet3.size();j++){
					Row row = new Row();
					Row row2 = new Row();
					row2 = (Row) dataSet3.get(j);
					String user_id = row2.getString("user_id");
					String manager_name = row2.getString("manager_name");
					row.put("depart_name", depart_name);
					row.put("user_id", user_id);
					row.put("manager_name", manager_name);
					String sq2 = "SELECT SUM(day_status) as all_day from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_time like '"+year+"_"+month+"%'";
					String all_day2 = queryField(sq2);
					double all_day = 0;
					if(all_day2 != null){
						all_day = Double.parseDouble(all_day2)/2;
					}
					row.put("all_day", all_day);
					String sq3 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='1' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String year_day = queryField(sq3);
					if(year_day == null){
						row.put("year_day", "0");
					}else{
					row.put("year_day", year_day);
					}
					String sq4 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='2' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String sick_day = queryField(sq4);
					if(sick_day == null){
						row.put("sick_day", "0");
					}else{
					row.put("sick_day", sick_day);
					}
					String sq5 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='3' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String tiao_day = queryField(sq5);
					if(tiao_day == null){
						row.put("tiao_day", "0");
					}else{
					row.put("tiao_day", tiao_day);
					}
					String sq6 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='4' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String add_day = queryField(sq6);
					if(add_day == null){
						row.put("add_day", "0");
					}else{
					row.put("add_day", add_day);
					}
					String sq7 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='5' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String shi_day = queryField(sq7);
					if(shi_day == null){
						row.put("shi_day", "0");
					}else{
					row.put("shi_day", shi_day);
					}
					String sq8 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='6' and user_id='"+user_id+"' and status='2' and year='"+year+"' and month='"+month+"'";
					String wai_day = queryField(sq8);
					if(wai_day == null){
						row.put("wai_day", "0");
					}else{
					row.put("wai_day", wai_day);
					}
					String sql5 = "SELECT * from mcn_punch_rule WHERE org_id='"+org_id+"' and DEPART_ID='"+depart_id+"' ORDER BY ID DESC LIMIT 0,1";
					Row row5 = queryRow(sql5);
					int xshi = 0;
					int dshi = 0;
					int cshi = 0;
					if(row5 != null){
						String am_start = row5.getString("AM_START");
						String pm_start = row5.getString("PM_START");
						String sql8 = "SELECT punch_time from mcn_punch_log a,mcn_users b WHERE a.user_id=b.id and a.user_id='"+user_id+"' and a.org_id='"+org_id+"' and a.punch_type='1' and a.punch_time like '"+year+"_"+month+"%'";
						String sql9 = "SELECT punch_time from mcn_punch_log a,mcn_users b WHERE a.user_id=b.id and a.user_id='"+user_id+"' and a.org_id='"+org_id+"' and a.punch_type='3' and a.punch_time like '"+year+"_"+month+"%'";
						DataSet data8 = queryDataSet(sql8);
						DataSet data9 = queryDataSet(sql9);
						if(data8.size() > 0){
						for(int f=0;f<data8.size();f++){
							Row row8 =new Row();
							row8 = (Row) data8.get(f);
							String punch_time = row8.getString("punch_time");
							String punch_time2 = punch_time.replace("年", "-");
							String punch_time3 = punch_time2.replace("月", "-");
							String start_time = punch_time3.replace("日", "");
							String end_time = start_time.substring(0,10)+" "+am_start+":00";
							System.out.println("time======???======"+start_time+"================"+end_time);
							java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
							java.util.Date  beginDate = format.parse(end_time); 
							java.util.Date endDate= format.parse(start_time);
							Long day=(endDate.getTime()-beginDate.getTime())/(1000*60); 
							System.out.println("相隔的分数="+day); 
							if(day > 0){
							if(day < 10){
								xshi++;
							}else if(10 <= day && day < 30){
								dshi++;
							}else if(day > 30){
								cshi++;
							}
							}
						}
					}
						if(data9.size() > 0){
						for(int e=0;e<data9.size();e++){
							Row row9 =new Row();
							row9 = (Row) data9.get(e);
							String punch_time = row9.getString("punch_time");
							String punch_time2 = punch_time.replace("年", "-");
							String punch_time3 = punch_time2.replace("月", "-");
							String start_time = punch_time3.replace("日", "");
							String end_time = start_time.substring(0,10)+" "+pm_start+":00";
							System.out.println("time======???======"+start_time+"================"+end_time);
							java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
							java.util.Date  beginDate = format.parse(end_time); 
							java.util.Date endDate= format.parse(start_time);
							Long day=(endDate.getTime()-beginDate.getTime())/(1000*60); 
							System.out.println("相隔的分数="+day); 
							if(day > 0){
							if(day < 10){
								xshi++;
							}else if(10 <= day && day < 30){
								dshi++;
							}else if(day > 30){
								cshi++;
							}
							}
						}
					}
					}
					row.put("xshi",xshi);
					row.put("dshi",dshi);
					row.put("cshi",cshi);
					dataSet.add(row);
				}
			}
		}
		}
		/*
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = new DataSet();
		DataSet dataSet2 = null;
		sql ="SELECT SITE_NO AS depart_id,SITE_NAME as depart_name from sm_site WHERE BUREAU_NO='"+org_id+"'";
		dataSet2 = queryDataSet(sql);
		if(dataSet2.size() > 0) {
		for(int i=0;i<dataSet2.size();i++) {
			Row row = new Row();
			Row row2 = new Row();
			row2 = (Row) dataSet2.get(i);
			String depart_id = row2.getString("depart_id");
			String depart_name = row2.getString("depart_name");
			row.put("depart_name", depart_name);
			String sq = "select name from mcn_users WHERE depart_id='"+depart_id+"' and manager_id='是'";
			String manager_name = queryField(sq);
			if(manager_name != null){
			row.put("manager_name", manager_name);
			}
			String sq2 = "select id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"'";
			DataSet data2 = queryDataSet(sq2);
			double allAddDay = 0; //加班总记录
			for(int j=0;j<data2.size();j++){
				Row row3 = new Row();
				row3 = (Row) data2.get(j);
				String user_id = row3.getString("id");
				allAddDay += getPunchDay(org_id,user_id,year,month);
			}
			row.put("all_day",allAddDay);
			String sq3 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='1' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String year_day = queryField(sq3);
			if(year_day == null){
				row.put("year_day", "0");
			}else{
			row.put("year_day", year_day);
			}
			String sq4 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='2' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String sick_day = queryField(sq4);
			if(sick_day == null){
				row.put("sick_day", "0");
			}else{
			row.put("sick_day", sick_day);
			}
			String sq5 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='3' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String tiao_day = queryField(sq5);
			if(tiao_day == null){
				row.put("tiao_day", "0");
			}else{
			row.put("tiao_day", tiao_day);
			}
			String sq6 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='4' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String add_day = queryField(sq6);
			if(add_day == null){
				row.put("add_day", "0");
			}else{
			row.put("add_day", add_day);
			}
			String sq7 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='5' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String shi_day = queryField(sq7);
			if(shi_day == null){
				row.put("shi_day", "0");
			}else{
			row.put("shi_day", shi_day);
			}
			String sq8 = "SELECT SUM(sum_day) as day FROM mcn_leave_log WHERE org_id='"+org_id+"' and leave_type='6' and user_id in(SELECT id from mcn_users WHERE org_id='"+org_id+"' and depart_id='"+depart_id+"' and manager_id='否') and status='2' and year='"+year+"' and month='"+month+"'";
			String wai_day = queryField(sq8);
			if(wai_day == null){
				row.put("wai_day", "0");
			}else{
			row.put("wai_day", wai_day);
			}
			String sql5 = "SELECT * from mcn_punch_rule WHERE org_id='"+org_id+"' ORDER BY ID DESC LIMIT 0,1";
			Row row5 = queryRow(sql5);
			int xshi = 0;
			int dshi = 0;
			int cshi = 0;
			if(row5 != null){
				String am_start = row5.getString("AM_START");
				String pm_start = row5.getString("PM_START");
				String sql8 = "SELECT punch_time from mcn_punch_log a,mcn_users b WHERE a.user_id=b.id and b.depart_id='"+depart_id+"' and a.org_id='"+org_id+"' and a.punch_type='1' and a.punch_time like '"+year+"_"+month+"%'";
				String sql9 = "SELECT punch_time from mcn_punch_log a,mcn_users b WHERE a.user_id=b.id and b.depart_id='"+depart_id+"' and a.org_id='"+org_id+"' and a.punch_type='3' and a.punch_time like '"+year+"_"+month+"%'";
				DataSet data8 = queryDataSet(sql8);
				DataSet data9 = queryDataSet(sql9);
				for(int f=0;f<data8.size();f++){
					Row row8 =new Row();
					row8 = (Row) data8.get(f);
					String punch_time = row8.getString("punch_time");
					String punch_time2 = punch_time.replace("年", "-");
					String punch_time3 = punch_time2.replace("月", "-");
					String start_time = punch_time3.replace("日", "");
					String end_time = start_time.substring(0,10)+" "+am_start+":00";
					System.out.println("time======???======"+start_time+"================"+end_time);
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					java.util.Date  beginDate = format.parse(end_time); 
					java.util.Date endDate= format.parse(start_time);
					Long day=(endDate.getTime()-beginDate.getTime())/(1000*60); 
					System.out.println("相隔的分数="+day); 
					if(day > 0){
					if(day < 10){
						xshi++;
					}else if(10 <= day && day < 30){
						dshi++;
					}else if(day > 30){
						cshi++;
					}
					}
				}
				
				for(int e=0;e<data9.size();e++){
					Row row9 =new Row();
					row9 = (Row) data9.get(e);
					String punch_time = row9.getString("punch_time");
					String punch_time2 = punch_time.replace("年", "-");
					String punch_time3 = punch_time2.replace("月", "-");
					String start_time = punch_time3.replace("日", "");
					String end_time = start_time.substring(0,10)+" "+pm_start+":00";
					System.out.println("time======???======"+start_time+"================"+end_time);
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					java.util.Date  beginDate = format.parse(end_time); 
					java.util.Date endDate= format.parse(start_time);
					Long day=(endDate.getTime()-beginDate.getTime())/(1000*60); 
					System.out.println("相隔的分数="+day); 
					if(day > 0){
					if(day < 10){
						xshi++;
					}else if(10 <= day && day < 30){
						dshi++;
					}else if(day > 30){
						cshi++;
					}
					}
				}
			}
			row.put("xshi",xshi);
			row.put("dshi",dshi);
			row.put("cshi",cshi);
			dataSet.add(row);
		}
	}
	*/
		return dataSet;
	}
	
	/**
	 * 查询迟到记录
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws ParseException 
	 */
	public DataSet selectMM(String org_id,String user_id,String type,String time) throws ParseException{
		DataSet dataSet = new DataSet();
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		sql = "SELECT a.*,b.name from mcn_punch_log a,mcn_users b WHERE a.user_id=b.id and user_id='"+user_id+"' and punch_type in (1,3) and punch_time like '%"+year+"_"+month+"%'";
		DataSet data = queryDataSet(sql);
		if(data.size()>0){
			for(int i=0;i<data.size();i++){
				Row row = new Row();
				row = (Row)data.get(i);
				String punch_time = row.getString("punch_time");
				String punch_type = row.getString("punch_type");
				String sql5 = "SELECT * from mcn_punch_rule WHERE org_id='"+org_id+"' and DEPART_ID in (select depart_id from mcn_users WHERE id='"+user_id+"') ORDER BY ID DESC LIMIT 0,1";
				Row row5 = queryRow(sql5);
				if(row5 != null){
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String am_start = row5.getString("AM_START");
					String pm_start = row5.getString("PM_START");
					String punch_time2 = punch_time.replace("年", "-");
					String punch_time3 = punch_time2.replace("月", "-");
					String start_time = punch_time3.replace("日", "");
					System.out.println("start==============================="+start_time);
					String end_time = "";
					if(punch_type.equals("1")){
					end_time = start_time.substring(0,10)+" "+am_start+":00";
					}
					if(punch_type.equals("3")){
					end_time = start_time.substring(0,10)+" "+pm_start+":00";
					}
					System.out.println("time======???======"+start_time+"================"+end_time);
					 
					java.util.Date  beginDate = format.parse(end_time); 
					java.util.Date endDate= format.parse(start_time);
					Long day=(endDate.getTime()-beginDate.getTime())/(1000*60); 
					System.out.println("相隔的分数="+day);
					row.put("punch_mm", day);
					if(day > 0){
						if(type.equals("0")){
							if(day < 10){
								dataSet.add(row);
								
							}
						}
						if(type.equals("1")){
							if(10 <= day && day < 30){
								dataSet.add(row);
							}
						}
						if(type.equals("2")){
							if(day > 30){
								dataSet.add(row);
							}
						}
						}
				}
			}
			}
		System.out.println("dataSet====="+dataSet.toString());
		return dataSet;
	}
	
	
	/**
	 * 分页查询打卡记录
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		String punch_type =row.getString("punch_type",null);
		String punch_result =row.getString("punch_result",null);
		String depart_id =row.getString("depart_id",null);
		String start_date =row.getString("start_date",null);
		String end_date =row.getString("end_date",null);
		sql = "select a.*, b.name   from mcn_punch_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		if(! StringUtils.isEmptyOrNull(punch_type))
		{
			sql +=" and a.punch_type='"+punch_type+"' ";
		}
		if(! StringUtils.isEmptyOrNull(punch_result))
		{
			sql +=" and a.punch_result='"+punch_result+"' ";
		}
		if(! StringUtils.isEmptyOrNull(depart_id))
		{
			sql +=" and a.user_id in (select id from mcn_users where depart_id='"+depart_id+"') ";
		}
		String start_time="";
		String end_time="";
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			start_time =start_date+" 00:00:00";
			sql +=" and a.punch_time >='"+start_time+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			end_time =end_date+" 00:00:00";
			sql +=" and a.punch_time <'"+end_time+"' ";
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_punch_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		countSql +=" and a.org_id='"+org_id+"'";
		if(! StringUtils.isEmptyOrNull(punch_type))
		{
			countSql +=" and a.punch_type='"+punch_type+"' ";
		}
		if(! StringUtils.isEmptyOrNull(punch_result))
		{
			countSql +=" and a.punch_result='"+punch_result+"' ";
		}
		if(! StringUtils.isEmptyOrNull(depart_id))
		{
			countSql +=" and a.user_id in (select id from mcn_users where depart_id='"+depart_id+"') ";
		}
		if(! StringUtils.isEmptyOrNull(start_date))
		{
			start_time =start_date+" 00:00:00";
			countSql +=" and a.punch_time >='"+start_time+"' ";
		}
		if(! StringUtils.isEmptyOrNull(end_date))
		{
			end_time =end_date+" 00:00:00";
			countSql +=" and a.punch_time <'"+end_time+"' ";
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
		System.out.println("puch list sql===>>"+sql);
		dataSet = queryDataSet(sql);
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	public DataSet queryPageForCompany2(String user_id,String time) {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		DataSet dataSet = null;
		sql = "select a.*, b.name   from mcn_punch_log a left join mcn_users b on a.user_id=b.id where user_id='"+user_id+"' and punch_time like '%"+year+"_"+month+"%'";
		dataSet = queryDataSet(sql);
		return dataSet;
	}
	
	public DataSet queryPageForCompany3(String user_id,String leave_type,String time) {
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		/*
		DataSet dataSet = null;
		sql = "select * from mcn_leave_log WHERE user_id='"+user_id+"' and leave_type='"+leave_type+"' ORDER BY id DESC";
		dataSet = queryDataSet(sql);
		return dataSet;
		*/
		DataSet data = new DataSet();
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		sql = "select a.*, b.name  from mcn_leave_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return data;
		}
		sql +=" and a.org_id='"+org_id+"'  and a.user_id='"+user_id+"' and a.leave_type='"+leave_type+"' and year='"+year+"' and month='"+month+"' ORDER BY a.id DESC ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_leave_log a left join mcn_users b  on a.user_id=b.id  where 1=1 ";
		countSql +=" and a.org_id='"+org_id+"'";
		countSql += restrictions;
//		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) 
		{
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		//sql += " limit " + startPos + " , " + pageable.getPageSize();
		System.out.println("url========"+sql);
		dataSet = queryDataSet(sql);
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
		//page = new Page(data, total, pageable);
		return data;
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
		int id = getTableSequence("mcn_punch_log", "id", 1);
		row.put("ID", id);
		insert("mcn_punch_log", row);
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
		sql = "select * from mcn_punch_log where id='" + id + "'";
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
		update("mcn_punch_log", row, " id='" + ID + "'");
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
			sql = "delete from mcn_punch_log where id in(" + id + ")";
			update(sql);
		}
	}
	
	public boolean checkBooleanPuncn(String org_id,String id,String time,String punch_type){
		String year = time.substring(0, 4);
		System.out.println("year=="+year);
		String month = time.substring(5, 7);
		System.out.println("month=="+month);
		String day = time.substring(8, 10);
		System.out.println("month=="+day);
		String sql = "select * from mcn_punch_log where org_id='"+org_id+"' and user_id='"+id+"' and punch_type='"+punch_type+"' and punch_time like '"+year+"_"+month+"_"+day+"%'";
		String s = queryField(sql);
		System.out.println("s-boolean==="+s);
		if(s == null){
			return true;
		}else{
		return false;
		}
	}
	
	/********************************************************** mobile api **********************************************************************************/
	public void mobilePunch(Row row)
	{
		String org_id = row.getString("org_id");
		String user_id = row.getString("user_id");
		String time = row.getString("punch_time").substring(0,10);
		String psql = "SELECT punchsz from mcn_leave_setting WHERE org_id='"+org_id+"' LIMIT 0,1";
		String punchsz = queryField(psql);
		if(punchsz.equals("1")){
		if(row.getString("punch_type").equals("2")){
			
			String sql = "SELECT * from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='1' and punch_time like '%"+time+"%'";
			Row row2 = queryRow(sql);
			if(row2 != null){
				row.put("day_status", "1");
			}
		}
		if(row.getString("punch_type").equals("4")){
			
			String sql = "SELECT * from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='3' and punch_time like '%"+time+"%'";
			Row row2 = queryRow(sql);
			if(row2 != null){
				row.put("day_status", "1");
			}
		}
		}else{
			if(row.getString("punch_type").equals("4")){
				
				String sql = "SELECT * from mcn_punch_log WHERE org_id='"+org_id+"' and user_id='"+user_id+"' and punch_type='1' and punch_time like '%"+time+"%'";
				Row row2 = queryRow(sql);
				if(row2 != null){
					row.put("day_status", "2");
				}
			}
		}
		int id =getTableSequence("mcn_punch_log", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		insert("mcn_punch_log", row);
		
	}
	
	public DataSet queryUserMonthLog(String user_id,String date,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql =" select * from mcn_punch_log where user_id='"+user_id+"' and punch_time like '%"+date+"%'  limit  "+iStart+"  , "+page_size ;
		ds =queryDataSet(sql);
		return ds;
	}
	/********************************************************** mobile api **********************************************************************************/

	//根据员工ID查询员工某月出勤天数
	public double getPunchDay(String org_id,String user_id,String year,String month) {
		double sum = 0;
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
		return sum;
	}
	
	public DataSet personQLoglist(String org_id,String time) {
		DataSet dataSet = new DataSet();
		String sql = "SELECT a.user_id,c.SITE_NAME as depart_name,b.`name` as user_name,a.punch_time as qd_time,a.place_name as qd_address from mcn_punch_log a,mcn_users b,sm_site c WHERE "+
				"a.user_id=b.id and b.depart_id=c.SITE_NO and a.org_id='"+org_id+"' and punch_type='7' and punch_time like '"+time+"%'";
		DataSet da = queryDataSet(sql);
		for(int i=0;i<da.size();i++){
			Row row = new Row();
			row = (Row) da.get(i);
			String user_id = row.getString("user_id");
			String qdtime = row.getString("qd_time").substring(0,10);
			System.out.println("qdtime===="+qdtime);
			String sql2 = "SELECT a.punch_time as qt_time,a.place_name as qt_address from mcn_punch_log a,mcn_users b,sm_site c WHERE "+
				"a.user_id=b.id and b.depart_id=c.SITE_NO and a.org_id='"+org_id+"' and punch_type='8' and a.user_id='"+user_id+"' and punch_time like '"+qdtime+"%' ORDER BY a.id DESC LIMIT 0,1";
			Row row2 = queryRow(sql2);
			if(row2 != null){
			row.put("qt_time", row2.getString("qt_time"));
			row.put("qt_address", row2.getString("qt_address"));
			}else{
				row.put("qt_time", "");
				row.put("qt_address", "");
			}
			dataSet.add(row);
		}
		System.out.println("data=========="+dataSet.toString());
		return dataSet;
	}
	
	public void queryUserPunchLog(String punch_status,String id){
		String sql= "update mcn_punch_log set punch_status='"+punch_status+"' WHERE id='"+id+"'";
		update(sql);
	}
	
	public String queryPunchSZ(String token){
		sql = "SELECT punchsz from mcn_leave_setting WHERE org_id='"+token+"'";
		return queryField(sql);
	}
	
	public Row queryMessage(String token){
		sql = "SELECT * from mcn_message WHERE org_id='"+token+"' ORDER BY id DESC LIMIT 0,1";
		return queryRow(sql);
	}
}