package cn.submsg.member.dao;


import java.util.List;

import com.google.common.base.Strings;
import com.sr178.common.jdbc.SqlParameter;
import com.sr178.common.jdbc.bean.IPage;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bean.MsgTempBean;
import cn.submsg.member.bo.MemberMessageTemp;

public class MemberMessageTempDao extends SubMsgBaseDao<MemberMessageTemp> {

	private static final String TEMP_BEAN_CLUMS = "temp.temp_id,temp.temp_title,temp.temp_content,temp.temp_status,temp.updated_time,temp.created_time,sign.id as sign_id,sign.sign_content,sign.sign_status,sign.sign_num";
	
	
	public List<MsgTempBean> getMsgTempBeanList(int userId,int limit){
		String sql = "select "+TEMP_BEAN_CLUMS+" from member_message_temp temp left join member_message_sign sign on temp.sign_id=sign.id where temp.user_id=? order by temp.updated_time desc limit "+limit;
		return super.getJdbc().getList(sql, MsgTempBean.class, SqlParameter.Instance().withInt(userId));
	}
	
	
	public IPage<MsgTempBean> getMsgTempBeanPageList(int userId,boolean isAll,int status,int pageSize,int pageIndex,String searchStr){
		String selectSql = "select "+TEMP_BEAN_CLUMS+" from member_message_temp temp left join member_message_sign sign on temp.sign_id=sign.id where temp.user_id=?";
		SqlParameter selectParameter = SqlParameter.Instance().withInt(userId);
		if(!isAll){
			selectSql = selectSql+" and temp.temp_status=? ";
			selectParameter.withInt(status);
		}
		if(!Strings.isNullOrEmpty(searchStr)){
			selectSql = selectSql + " and temp.temp_content like '%"+searchStr+"%'";
		}
		
		selectSql = selectSql +" order by temp.id desc";
		
		String countSql = "select count(*) from member_message_temp temp  where temp.user_id=? ";
		SqlParameter countParameter = SqlParameter.Instance().withInt(userId);
		if(!isAll){
			countSql = countSql+" and temp.temp_status=? ";
			countParameter.withInt(status);
		}
		if(!Strings.isNullOrEmpty(searchStr)){
			countSql = countSql + " and temp.temp_content like '%"+searchStr+"%'";
		}
		
		return super.getJdbc().getListPage(selectSql, MsgTempBean.class, selectParameter, pageSize, pageIndex, countSql, countParameter);
	}
	
	
}
