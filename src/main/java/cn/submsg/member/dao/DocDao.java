package cn.submsg.member.dao;



import java.util.List;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.Doc;

public class DocDao extends SubMsgBaseDao<Doc> {

	public List<Doc> searchDoc(String str){
		String sql = "select id,doc_id,doc_title,type from "+super.getTable()+" where (doc_title like '%"+str+"%' or doc_contnet like '%"+str+"%') and level>0 limit 10";
		return super.getJdbc().getList(sql, Doc.class);
	}
 
}
