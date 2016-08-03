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

	
	/**
	 * 获取模板列表
	 * @param userId
	 * @param limit
	 * @return
	 */
	public MsgTempBean getMsgTempBean(int userId,String tempId){
		String sql = "select "+TEMP_BEAN_CLUMS+" from member_message_temp temp left join member_message_sign sign on temp.sign_id=sign.id where temp.user_id=? and temp.temp_id=? limit 1";
		return super.getJdbc().get(sql, MsgTempBean.class, SqlParameter.Instance().withInt(userId).withString(tempId));
	}
	/**
	 * 获取模板列表
	 * @param userId
	 * @param limit
	 * @return
	 */
	public List<MsgTempBean> getMsgTempBeanList(int userId,int limit){
		String sql = "select "+TEMP_BEAN_CLUMS+" from member_message_temp temp left join member_message_sign sign on temp.sign_id=sign.id where temp.user_id=? order by temp.updated_time desc limit "+limit;
		return super.getJdbc().getList(sql, MsgTempBean.class, SqlParameter.Instance().withInt(userId));
	}
	
	/**
	 * 获取分页模板列表
	 * @param userId
	 * @param isAll
	 * @param status
	 * @param pageSize
	 * @param pageIndex
	 * @param searchStr
	 * @return
	 */
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
	
	/**
	 * 更新模板标题
	 * @param userId
	 * @param tempId
	 * @param tempTitle
	 * @return
	 */
	public boolean updateTempTitle(int userId,String tempId,String tempTitle){
		String sql = "update "+super.getTable()+" set temp_title=? where temp_id=? and user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withString(tempTitle).withString(tempId).withInt(userId))>0;
	}
	/**
	 * 删除模板
	 * @param userId
	 * @param tempId
	 * @return
	 */
	public boolean deleteTempByTempId(int userId,String tempId){
		String sql = "delete from "+super.getTable()+" where temp_id=? and user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withString(tempId).withInt(userId))>0;
	}
	/**
	 * 更新模板信息
	 * @param tempId
	 * @param userId
	 * @param tempContent
	 * @param appId
	 * @param signId
	 * @return
	 */
	public boolean updateTemp(String tempId,int userId,String tempContent,int signId,int tempStatus){
		String sql = "update "+super.getTable()+" set temp_content=?,temp_status=?,sign_id=? where temp_id=? and user_id=? limit 1";
		return super.getJdbc().update(sql, SqlParameter.Instance().withString(tempContent).withInt(tempStatus).withInt(signId).withString(tempId).withInt(userId))>0;
	}
	
	
}
