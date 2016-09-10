package cn.submsg.member.dao;


import java.util.Date;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MemberMessageSign;

public class MemberMessageSignDao extends SubMsgBaseDao<MemberMessageSign> {
     /**
      * 更新签名状态
      * @param signContent
      * @param signNum
      * @param signStatus
      * @return
      */
	 public boolean updateSignStatus(String signContent,String signNum,int signStatus,int sendType){
		 String sql = "update "+super.getTable()+" set sign_status = ? ,sign_num = ?,updated_time = ?,send_type=? where sign_content = ?";
		 return this.getJdbc().update(sql, SqlParameter.Instance().withInt(signStatus).withString(signNum).withObject(new Date()).withInt(sendType).withString(signContent))>0;
	 }
}
