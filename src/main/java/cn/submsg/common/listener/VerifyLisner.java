package cn.submsg.common.listener;

import cn.submsg.member.bo.MemberVerify;

public interface VerifyLisner {
    /**
     * 成功
     * @param code
     * @param memberVerify
     */
	public void success(String code,MemberVerify memberVerify);
	/**
	 * 失败
	 * @param code
	 * @param memberVerify
	 */
	public void fail(String code,MemberVerify memberVerify);
}
