package cn.submsg.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr178.common.jdbc.bean.SqlParamBean;

import cn.submsg.member.bo.Doc;
import cn.submsg.member.bo.DocReply;
import cn.submsg.member.dao.DocDao;
import cn.submsg.member.dao.DocReplyDao;

public class DocService {
    @Autowired
	private DocDao docDao;
	@Autowired
	private DocReplyDao docReplyDao;
	
	/**
	 * 获取文档内容
	 * @param docId
	 * @return
	 */
	public Doc getDocContent(String docId){
		return docDao.get(new SqlParamBean("doc_id", docId));
	}
	
	/**
	 * 获取文档列表
	 * @param type
	 * @return
	 */
	public List<Doc> getDocListByType(int type){
		return docDao.getList("order by order_num desc",new SqlParamBean("type", type));
	}
	
	/**
	 * 获取某个文档的回复列表
	 * @param docKeyId
	 * @return
	 */
	public List<DocReply> getDocReplyList(int docKeyId){
		return docReplyDao.getList("order by created_time desc", new SqlParamBean("doc_key_id", docKeyId));
	}
}
