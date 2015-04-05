package net.ezshop.service;

import net.ezshop.Page;
import net.ezshop.Pageable;
import net.ezshop.entity.Deposit;
import net.ezshop.entity.Member;

/**
 * Service - 预存款
 */
public interface DepositService extends BaseService<Deposit, Long> {

	/**
	 * 查找预存款分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 预存款分页
	 */
	Page<Deposit> findPage(Member member, Pageable pageable);

}