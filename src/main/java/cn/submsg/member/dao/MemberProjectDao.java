package cn.submsg.member.dao;

import java.util.Date;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MemberProject;

public class MemberProjectDao extends SubMsgBaseDao<MemberProject> {

	
	public boolean update(int userId,int appId,String projectName,String whiteIp,int maxSendNumDaily,int status){
		String sql = "update "+super.getTable()+" set project_name=?,max_send_num_daily=?,white_ip=?,status=?,updated_time=? where id=? and user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withString(projectName).withInt(maxSendNumDaily)
				.withString(whiteIp).withInt(status).withObject(new Date()).withInt(appId).withInt(userId)) > 0;
	}
	
	
	public boolean updateAppKey(int userId,int appId,String projecKey){
		String sql = "update "+super.getTable()+" set project_key=?,updated_time=? where id=? and user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withString(projecKey).withObject(new Date())
				.withInt(appId).withInt(userId)) > 0;
	}
}
