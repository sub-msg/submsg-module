package cn.submsg.member.dao;


import java.util.List;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bean.MsgTempBean;
import cn.submsg.member.bo.MemberMessageTemp;

public class MemberMessageTempDao extends SubMsgBaseDao<MemberMessageTemp> {

	private static final String TEMP_BEAN_CLUMS = "temp.temp_id,temp.temp_title,temp.temp_content,temp.temp_status,temp.updated_time,temp.created_time,sign.id as sign_id,sign.sign_content,sign.sign_status,sign.sign_num";
	
	
	public List<MsgTempBean> getMsgTempBeanList(int userId,int limit){
		String sql = "select "+TEMP_BEAN_CLUMS+" from member_message_temp temp left join member_message_sign sign on temp.sign_id=sign.id where temp.user_id=? order by temp.updated_time desc limit "+limit;
		return super.getJdbc().getList(sql, MsgTempBean.class, SqlParameter.Instance().withInt(userId));
	}
	
	
}
