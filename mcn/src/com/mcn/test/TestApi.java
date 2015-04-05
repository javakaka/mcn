package com.mcn.test;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.NetUtil;
public class TestApi {
//	String url ="http://211.154.151.145:8080/mcn/api/action/changePassword.do";
	
	// login 
	public static void login()
	{
		String url ="http://localhost:8080/mcn/api/action/login.do";
		//String url ="http://211.154.151.145:8080/mcn/api/action/login.do";
		IVO ivo =new IVO();
		   try {
//			ivo.set("username", "1001");
			ivo.set("username", "13629900136");
			ivo.set("password", "123456");
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.print("ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// change password
	public static void changePwd()
	{
		String url ="http://localhost:8080/mcn/api/action/changePassword.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("user_id", "1");
			ivo.set("oldPwd", "123456");
			ivo.set("newPwd", "000000");
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// room list
	public static void getRoomList()
	{
//		String url ="http://localhost:8080/mcn/api/room/list.do";
		String url ="http://211.154.151.145:8080/mcn/api/room/list.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("page", "1");
			ivo.set("pageSize", "10");
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// room  status
	public static void getRoomStatus()
	{
		String url ="http://localhost:8080/mcn/api/room/room_status.do";
//			String url ="http://211.154.151.145:8080/mcn/api/room/room_status.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("id", "1");
			ivo.set("date", DateUtil.getCurrentDate());
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// book room 
	public static void bookRoom()
	{
		String url ="http://localhost:8080/mcn/api/room/book_room.do";
//		String url ="http://211.154.151.145:8080/mcn/api/room/room_status.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("user_id", "1");
			ivo.set("room_id", "1");
			ivo.set("start_time", "2014-09-14 10:00:00");
			ivo.set("end_time", "2014-09-14 11:00:00");
			ivo.set("date", DateUtil.getCurrentDate());
			ivo.set("use_for", "use");
			ivo.set("meeters", "meeters");
			ivo.set("remark", "remark");
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	
	// 查询人员的预定记录
	public static void queryUserBookList()
	{
//		String url ="http://localhost:8080/mcn/api/room/userbooklist.do";
		String url ="http://211.154.151.145:8080/mcn/api/room/userbooklist.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("id", "1");
			ivo.set("token", "MTAwMDQ=");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	
	// 请假
		public static void mobileleaveAdd()
		{
			String url ="http://localhost:8080/mcn/api/leave/add.do";
			//	String url ="http://116.31.92.48:8080/mcn/api/leave/add.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "66");
				ivo.set("token", "MTAwMDc=");
				ivo.set("leave_type","6");
				ivo.set("start_date","2014-12-02");
				ivo.set("end_date","2014-12-04");
				ivo.set("sum_day", "3");
				ivo.set("reason", "3天");
				
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 查询自己的请假列表
		public static void querySelfLeaveList()
		{
			//String url ="http://localhost:8080/mcn/api/leave/selfList.do";
			String url ="http://116.31.92.48:8080/mcn/api/leave/selfList.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "66");
				ivo.set("token", "MTAwMDc=");
				ivo.set("page","1");
				ivo.set("page_size","10");
				ivo.set("status","1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 分页查询属下的请假列表
		public static void queryFollowerLeaveList()
		{
		//	String url ="http://localhost:8080/mcn/api/leave/followerList.do";
			String url ="http://116.31.92.48:8080/mcn/api/leave/followerList.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "66");
				ivo.set("token", "MTAwMDc=");
				ivo.set("page","1");
				ivo.set("page_size","10");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 审核请假
		public static void auditLeave()
		{
			String url ="http://localhost:8080/mcn/api/leave/audit.do";
			//String url ="http://211.154.151.145:8080/mcn/api/leave/audit.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "66");
				ivo.set("check_id", "13");
				ivo.set("token", "MTAwMDQ=");
				ivo.set("log_id","13");
				ivo.set("status","0");
				ivo.set("audit_objection","同意请假2");
				ivo.set("up_id","25");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		
		// 查询企业的部门和人员
		public static void queryCompanySitesAndUsers()
		{
			String url ="http://localhost:8080/mcn/api/company/all.do";
			//String url ="http://211.154.151.145:8080/mcn/api/company/all.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "MTAwMDQ=");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//查询打卡时间
		public static void queryPunchTimes()
		{
//			String url ="http://localhost:8080/mcn/api/punch/times.do";
			String url ="http://116.31.92.48:8080/mcn/api/punch/times.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "MTAwMDc=");
				ivo.set("id", "66");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		
		//打卡
		public static void punch()
		{
			String url ="http://localhost:8080/mcn/api/punch/add.do";
		//	String url ="http://211.154.151.145:8080/mcn/api/punch/add.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "MTAwMDc=");
				ivo.set("id", "66");
				ivo.set("punch_type", "4");
				ivo.set("punch_time", "2014-12-16 16:00:00");
				ivo.set("LONGITUDE", "1");
				ivo.set("LATITUDE ", "1");
				ivo.set("PLACE_NAME", "罗湖");
				String pictrue = Base64Util.GetImageStr("D:/tip.jpg");
				ivo.set("picture", Base64Util.encode(pictrue.getBytes()));
//				ivo.set("picture_name", "1001");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//根据人员编号和月份查询打卡记录
		public static void queryUserMonthPunchLog()
		{
			String url ="http://localhost:8080/mcn/api/punch/user_month_log.do";
			//String url ="http://211.154.151.145:8080/mcn/api/punch/user_month_log.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "MTAwMDQ=");
				ivo.set("id", "1");
				ivo.set("date", "2014-10");
				ivo.set("page", "1");
				ivo.set("page_size", "10");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
			
		//统计查询公司总出勤
		public static void checkStatisCompany() {
			//String url ="http://localhost:8080/mcn/api/checkstatis/company.do";
			String url ="http://116.31.92.48:8080/mcn/api/checkstatis/company.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "MTAwMDc=");
				ivo.set("date", "2014-12");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//统计查询部门总出勤
		public static void checkStatisDepart() {
			String url ="http://localhost:8080/mcn/api/checkstatis/depart.do";
			//String url ="http://116.31.92.48:8080/mcn/api/checkstatis/depart.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				   ivo.set("date", "2014-11");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//统计查询员工总出勤
		public static void checkStatisPerson() {
			String url ="http://localhost:8080/mcn/api/checkstatis/person.do";
			//String url ="http://116.31.92.48:8080/mcn/api/checkstatis/person.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				   ivo.set("depart_id", "23");
				   ivo.set("date", "2014-11");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//统计该员工该月打卡详细情况
		public static void checkStatisPersonDetail() {
			//String url ="http://localhost:8080/mcn/api/checkstatis/persondetail.do";
			String url ="http://116.31.92.48:8080/mcn/api/checkstatis/persondetail.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDQ=");
				   ivo.set("user_id", "1");
				   ivo.set("date", "2014-11");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//统计该员工该月签到详细情况
		public static void checkStatisPersonDetail2() {
			//String url ="http://localhost:8080/mcn/api/checkstatis/personQDdetail.do";
			String url ="http://116.31.92.48:8080/mcn/api/checkstatis/personQDdetail.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				   ivo.set("user_id", "66");
				   ivo.set("date", "2014-11");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//请假查询入口
		public static void userIdAllLeave() {
			String url ="http://localhost:8080/mcn/api/checkstatis/leaveUserId.do";
			//String url ="http://116.31.92.48:8080/mcn/api/checkstatis/leaveUserId.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				   ivo.set("user_id", "66");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//企业看板查询
		public static void companyModel() {
			//String url ="http://localhost:8080/mcn/api/checkstatis/companymodel.do";
			String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				   ivo.set("date", "2014-12-02");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		public static void queryLeaveTypeList() {
			//	String url ="http://localhost:8080/mcn/api/leave/leaveTypeList.do";
				String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
				IVO ivo =new IVO();
				   try {
					   ivo.set("token", "MTAwMDQ=");
					   ivo.set("user_id", "1");
					   ivo.set("leave_type", "1");
					   ivo.set("page", "1");
					   ivo.set("page_size", "10");
					String json =  VOConvert.ivoToJson(ivo);
					System.out.println("\n ivo to json ====>>"+json);
					String res =NetUtil.getNetResponse(url, json,"UTF-8");
					System.out.println("========================================\n");
					System.out.print(res);
				   } catch (JException e) {
						e.printStackTrace();
					}
			}
		
		//查询企业LOGO入口
		public static void queryCompany() {
		//	String url ="http://localhost:8080/mcn/api/checkstatis/companylogo.do";
			String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companylogo.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDQ=");
				   
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		//根据企业ID查询所有管理用户
		public static void allUsersList() {
			String url ="http://localhost:8080/mcn/api/leave/allUsersList.do";
		//	String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDQ=");
				   
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//根据请假记录levae_ID查询审核人
		public static void queryLeaveId() {
			String url ="http://localhost:8080/mcn/api/leave/leaveId.do";
		//	String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDQ=");
				   ivo.set("id", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		//请假规则说明
		public static void leavegz() {
			String url ="http://localhost:8080/mcn/api/leave/leavegz.do";
		//	String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//审批规则说明
		public static void leavespgz() {
			String url ="http://localhost:8080/mcn/api/leave/leavespgz.do";
		//	String url ="http://116.31.92.48:8080/mcn/api/checkstatis/companymodel.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//审批规则说明
		public static void punchsz() {
			//String url ="http://localhost:8080/mcn/api/punch/punchsz.do";
			String url ="http://116.31.92.48:8080/mcn/api/punch/punchsz.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//通知公告
		public static void message() {
		//	String url ="http://localhost:8080/mcn/api/punch/message.do";
			String url ="http://116.31.92.48:8080/mcn/api/punch/message.do";
			IVO ivo =new IVO();
			   try {
				   ivo.set("token", "MTAwMDc=");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 分页查询已审批过的请假列表
		public static void statusList()
		{
		//	String url ="http://localhost:8080/mcn/api/leave/statusList.do";
			String url ="http://116.31.92.48:8080/mcn/api/leave/statusList.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "66");
				ivo.set("token", "MTAwMDc=");
				ivo.set("page","1");
				ivo.set("page_size","10");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		public static void selectB()
		{
			//String url ="http://localhost:8080/mcn/system/fun/BBSJ.do";
			String url ="http://116.31.92.48:8080/mcn/system/fun/BBSJ.do";
			try{
				String res =NetUtil.getNetResponse(url ,"","UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
	 public static void main(String[] args) {
		 //String token = nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D;
		 //登陆
//		 login();
		 //修改密码
//		 changePwd();
		// room list
//		getRoomList();
		 //会议室状态
//		getRoomStatus();
		 //预定会议室
//		 bookRoom();
		// 查询人员的预定记录
//		queryUserBookList();
		// 请假
//		mobileleaveAdd();
		// 查询自己的请假列表
//		querySelfLeaveList();
		// 分页查询属下的请假列表
//		 queryFollowerLeaveList();
//		 审核请假
//		 auditLeave();
//		 查询企业的部门和人员
//		 queryCompanySitesAndUsers();
//		查询打卡时间
//		queryPunchTimes();
//		打卡
//		punch();
//		根据人员编号和月份查询打卡记录
//		queryUserMonthPunchLog();
		 
		 //统计查询公司总出勤
//		 checkStatisCompany();
		//统计查询该公司所有部门总出勤
//		 checkStatisDepart();
		 //统计查询该部门所有员工总出勤
//		 checkStatisPerson();
		//统计该员工该月打卡详细情况
//		 checkStatisPersonDetail();
		//统计该员工该月签到详细情况
//		 checkStatisPersonDetail2();
		 //请假查询入口
//		 userIdAllLeave();
		 //企业看板查询
//	 	companyModel();
		 //根据请假类别查询自己的请假记录
		// queryLeaveTypeList();
		//查询企业LOGO入口
//		 queryCompany();
		//根据企业ID查询所有管理用户
	//		allUsersList();
	//根据请假记录levae_ID查询审核人
//		queryLeaveId();
		//请假规则说明
//		leavegz();
		//审批规则说明
//		leavespgz();
		 //查询打卡设置
//	 	 punchsz();
		 //查询通知公告
//		 message();
		 //查询已审批过的记录
	//	 statusList();
		 //查询客户端版本更新接口
		 selectB();
	 }
	 }
