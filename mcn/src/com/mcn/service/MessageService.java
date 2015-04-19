package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("mcnMessageService")
public class MessageService extends Service{

	public Row queryMessage(String token){
		sql = "SELECT * from mcn_message WHERE org_id='"+token+"' ORDER BY id DESC LIMIT 0,1";
		return queryRow(sql);
	}
	
	public String queryMessageDetail(String id){
		sql = "SELECT content from mcn_message WHERE id='"+id+"' ";
		return queryField(sql);
	}
	
	public Row queryMessageById(String id){
		sql = "SELECT * from mcn_message WHERE id='"+id+"' ";
		return queryRow(sql);
	}
	
	public String updateMessageTotalNum(String id){
		sql = "update mcn_message set read_total_num=read_total_num+1 WHERE id='"+id+"' ";
		return queryField(sql);
	}
	
	public DataSet queryOrgMessageListByUserId(String depart_id,String token,int page,int page_size){
		int iStart =(page-1)*page_size;
		DataSet ds =null;
		sql = "select id,message_name,create_time from mcn_message where org_id='"+token+"' "
				+ "and message_qz like'%"+depart_id+"%' order by create_time desc limit "+iStart+", "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
}