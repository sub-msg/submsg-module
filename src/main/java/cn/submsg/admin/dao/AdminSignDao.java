package cn.submsg.admin.dao;

import com.google.common.base.Strings;
import com.sr178.common.jdbc.SqlParameter;
import com.sr178.common.jdbc.bean.IPage;

import cn.submsg.admin.bo.AdminSign;
import cn.submsg.common.dao.SubMsgBaseDao;

public class AdminSignDao extends SubMsgBaseDao<AdminSign> {
	/**
	 * 查询签名列表
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<AdminSign> getAdminSignList(String searchStr,int pageSize,int pageIndex){
		
		String sql = "select * from "+super.getTable();
		if(!Strings.isNullOrEmpty(searchStr)){
			sql = sql + " where sign_content like '%"+searchStr+"%'";
		}
		sql = sql + " order by sign_status asc,created_time desc";
		return this.getJdbc().getListPage(sql, AdminSign.class, null, pageSize, pageIndex);
	}
	/**
	 * 更新签名状态和签名号码
	 * @param id
	 * @param signNum
	 * @param signStatus
	 * @return
	 */
	public boolean updateSign(int id,String signNum,int signStatus){
		String sql = "update "+super.getTable()+" set sign_status = ? ,sign_num = ? where id=?";
		return this.getJdbc().update(sql, SqlParameter.Instance().withInt(signStatus).withString(signNum).withInt(id))>0;
	}
	
	
}
