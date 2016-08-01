package cn.submsg.member.dao;

import com.sr178.common.jdbc.SqlParameter;

import cn.submsg.common.dao.SubMsgBaseDao;
import cn.submsg.member.bo.MemberInvoice;

public class MemberInvoiceDao extends SubMsgBaseDao<MemberInvoice> {

	
	public boolean updateCommonInvoice(int id,int userId,String title, String lastname,
			String firstname, String provice, String city, String area, String address, String mob){
		String sql = "update "+super.getTable()+" set invoice_type=1,invoice_name=?,first_name=?,second_name=?,provice=?,city=?,area=?,address=?,phone=? where id=? and user_id=? limit 1";
		return super.getJdbc().update(sql,
				SqlParameter.Instance().withString(title).withString(firstname).withString(lastname).withString(provice)
						.withString(city).withString(area).withString(address).withString(mob).withInt(id)
						.withInt(userId)) > 0;
	}
	
	
	
	public boolean updateSpecialInvoice(int id,int userId, String title, String lastname,
			String firstname, String provice, String city, String area, String address, String mob,String s_address,
			String s_mob, String s_bank, String s_account, String s_taxcode){
		String sql = "update " + super.getTable()
				+ " set invoice_type=2,invoice_name=?,first_name=?,second_name=?,provice=?,city=?,area=?,address=?,phone=?,com_address=?,com_phone=?,com_bank_name=?,com_bank_account=?,taxpayer_tag=? where id=? and user_id=? limit 1";
		return super.getJdbc().update(sql,
				SqlParameter.Instance().withString(title).withString(firstname).withString(lastname).withString(provice)
						.withString(city).withString(area).withString(address).withString(mob).withString(s_address).withString(s_mob).withString(s_bank).withString(s_account).withString(s_taxcode).withInt(id)
						.withInt(userId)) > 0;
	}
}
