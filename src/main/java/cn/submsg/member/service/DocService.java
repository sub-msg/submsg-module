package cn.submsg.member.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	public Map<String,List<Doc>> getDocListByType(int type){
		//一级标题
		List<Doc> firstTitle = docDao.getList("order by order_num", new SqlParamBean("type", type),new SqlParamBean("and", "level", 0));
		//二级标题  有内容的
		List<Doc> secondTitle = docDao.getList("order by order_num", new SqlParamBean("type", type),new SqlParamBean("and", "level", 1));
		TreeMap<String,List<Doc>> result = Maps.newTreeMap();
		for(Doc first:firstTitle){
			List<Doc> seconds = Lists.newArrayList();
			for(Doc second:secondTitle){
				if(second.getParentId()==first.getId()){
					seconds.add(second);
				}
			}
			result.put(first.getDocTitle(), seconds);
		}
		return result;
	}
	
	public List<Doc> getListByTypeAndLevel(int type,int level){
		return docDao.getList("order by order_num", new SqlParamBean("type", type),new SqlParamBean("and", "level", level));
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
