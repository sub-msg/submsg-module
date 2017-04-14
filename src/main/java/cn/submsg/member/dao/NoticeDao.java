package cn.submsg.member.dao;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.Notice;

public class NoticeDao extends SubMsgBaseDao<Notice> {

	public Notice getLastNotice(){
		String sql = "select * from "+ super.getTable()+" order by id desc limit 1";
		return super.getJdbc().get(sql, Notice.class,null);
	}
}
